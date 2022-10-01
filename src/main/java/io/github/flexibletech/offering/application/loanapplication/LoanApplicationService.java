package io.github.flexibletech.offering.application.loanapplication;

import io.github.flexibletech.camunda.tools.delegate.Delegate;
import io.github.flexibletech.camunda.tools.delegate.Delegates;
import io.github.flexibletech.camunda.tools.process.start.StartProcess;
import io.github.flexibletech.camunda.tools.process.values.ProcessKeyValue;
import io.github.flexibletech.camunda.tools.process.values.ProcessValue;
import io.github.flexibletech.camunda.tools.process.values.ProcessValues;
import io.github.flexibletech.camunda.tools.process.variables.ProcessVariable;
import io.github.flexibletech.camunda.tools.task.receive.ReceiveTask;
import io.github.flexibletech.camunda.tools.task.user.UserTask;
import io.github.flexibletech.offering.application.DomainObjectMapper;
import io.github.flexibletech.offering.application.EventPublisher;
import io.github.flexibletech.offering.application.client.ClientNotFoundException;
import io.github.flexibletech.offering.application.loanapplication.dto.ChoseConditionsRequest;
import io.github.flexibletech.offering.application.loanapplication.dto.LoanApplicationDto;
import io.github.flexibletech.offering.application.loanapplication.dto.StartNewLoanApplicationRequest;
import io.github.flexibletech.offering.application.loanapplication.dto.events.LoanApplicationCanceled;
import io.github.flexibletech.offering.application.loanapplication.dto.events.LoanApplicationCompleted;
import io.github.flexibletech.offering.application.loanapplication.dto.events.LoanApplicationCreated;
import io.github.flexibletech.offering.application.loanapplication.dto.events.LoanApplicationDeclined;
import io.github.flexibletech.offering.application.loanapplication.dto.events.LoanApplicationOfferCalculated;
import io.github.flexibletech.offering.domain.client.Client;
import io.github.flexibletech.offering.domain.client.ClientId;
import io.github.flexibletech.offering.domain.client.ClientRepository;
import io.github.flexibletech.offering.domain.loanapplication.LoanApplication;
import io.github.flexibletech.offering.domain.loanapplication.LoanApplicationId;
import io.github.flexibletech.offering.domain.loanapplication.LoanApplicationRepository;
import io.github.flexibletech.offering.domain.loanapplication.document.Document;
import io.github.flexibletech.offering.domain.loanapplication.document.DocumentStorage;
import io.github.flexibletech.offering.domain.loanapplication.document.PrintService;
import io.github.flexibletech.offering.domain.loanapplication.issuance.IssuanceService;
import io.github.flexibletech.offering.domain.loanapplication.risk.RiskDecision;
import io.github.flexibletech.offering.domain.loanapplication.risk.RiskService;
import io.github.flexibletech.offering.domain.preapproved.PreApprovedOfferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class LoanApplicationService {
    private final DomainObjectMapper domainObjectMapper;
    private final LoanApplicationRepository loanApplicationRepository;
    private final PreApprovedOfferRepository preApprovedOfferRepository;
    private final EventPublisher eventPublisher;
    private final PrintService printService;
    private final DocumentStorage documentStorage;
    private final RiskService riskService;
    private final IssuanceService issuanceService;
    private final ClientRepository clientRepository;

    @Autowired
    public LoanApplicationService(DomainObjectMapper domainObjectMapper, LoanApplicationRepository loanApplicationRepository,
                                  PreApprovedOfferRepository preApprovedOfferRepository, EventPublisher eventPublisher,
                                  PrintService printService, DocumentStorage documentStorage, RiskService riskService, IssuanceService issuanceService,
                                  ClientRepository clientRepository) {
        this.domainObjectMapper = domainObjectMapper;
        this.loanApplicationRepository = loanApplicationRepository;
        this.preApprovedOfferRepository = preApprovedOfferRepository;
        this.eventPublisher = eventPublisher;
        this.printService = printService;
        this.documentStorage = documentStorage;
        this.riskService = riskService;
        this.issuanceService = issuanceService;
        this.clientRepository = clientRepository;
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_ADMIN')")
    @StartProcess(processKey = ProcessConstants.LOAN_APPLICATION_PROCESS,
            businessKeyName = ProcessConstants.LOAN_APPLICATION_ID,
            businessKeyValue = "getId()")
    public LoanApplicationDto startNewLoanApplication(StartNewLoanApplicationRequest request) {
        log.info("Starting loan application...");
        var clientId = new ClientId(request.getClientId());

        var preApprovedOffer = preApprovedOfferRepository.findForClient(clientId);
        var client = clientOfId(clientId);

        var loanApplication = LoanApplication.newLoanApplication(client, preApprovedOffer,
                request.getAmount(), request.getPeriod(), request.getInsurance());
        var savedLoanApplication = loanApplicationRepository.save(loanApplication);

        var loanApplicationCreatedEvent = domainObjectMapper.map(savedLoanApplication, LoanApplicationCreated.class);
        eventPublisher.publish(loanApplicationCreatedEvent);

        log.info("Loan application {} has been started", savedLoanApplication.getId());

        return domainObjectMapper.map(savedLoanApplication, LoanApplicationDto.class);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Delegate(beanName = ProcessConstants.REQUEST_RISK_DECISION_TASK, key = ProcessConstants.LOAN_APPLICATION_ID)
    public void requestRiskDecisionForLoanApplication(@ProcessKeyValue String loanApplicationId) {
        log.info("Requesting risk decision for loan application {}...", loanApplicationId);
        var loanApplication = loanApplicationOfId(loanApplicationId);
        var client = clientOfId(loanApplication.getClientId());

        riskService.requestRiskDecision(loanApplication, client);
        log.info("Risk decision has been requested for loan application {}", loanApplicationId);
    }

    @Transactional
    @ReceiveTask(definitionKey = ProcessConstants.RISK_DECISION_RECEIVED,
            variables = @ProcessVariable(name = ProcessConstants.STATUS, value = "getStatus()"))
    public LoanApplicationDto acceptRiskDecisionToLoanApplication(@ProcessKeyValue String loanApplicationId, RiskDecision riskDecision) {
        log.info("Adding risk decision to loan application {}...", loanApplicationId);

        var loanApplication = loanApplicationOfId(loanApplicationId);
        loanApplication.acceptRiskDecision(riskDecision);

        if (loanApplication.isDeclined())
            eventPublisher.publish(new LoanApplicationDeclined(loanApplicationId));

        log.info("Risk decision has been added to loan application {}", loanApplicationId);
        return domainObjectMapper.map(loanApplication, LoanApplicationDto.class);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Delegate(beanName = ProcessConstants.DEFINE_INCOME_CONFIRMATION_TYPE_TASK, key = ProcessConstants.LOAN_APPLICATION_ID,
            variables = @ProcessVariable(name = ProcessConstants.INCOME_CONFIRMATION_TYPE, value = "toString()"))
    public String defineIncomeConfirmationTypeForLoanApplication(@ProcessKeyValue String loanApplicationId) {
        log.info("Definition of income confirmation type for loan application {}", loanApplicationId);

        var loanApplication = loanApplicationOfId(loanApplicationId);
        var preApprovedOffer = preApprovedOfferRepository.findForClient(loanApplication.getClientId());
        var client = clientOfId(loanApplication.getClientId());

        var incomeConfirmationType = loanApplication.defineIncomeConfirmationType(preApprovedOffer, client);

        log.info("Income confirmation type has been defined for loan application {}", loanApplicationId);
        return incomeConfirmationType.name();
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_ADMIN')")
    @UserTask(definitionKey = ProcessConstants.CHOSE_CONDITIONS_TASK,
            variables = @ProcessVariable(name = ProcessConstants.INSURANCE, value = "getInsurance()"))
    public ChoseConditionsRequest choseConditionsForLoanApplication(@ProcessKeyValue String loanApplicationId, ChoseConditionsRequest conditions) {
        log.info("Choosing conditions for loan application {}...", loanApplicationId);

        var loanApplication = loanApplicationOfId(loanApplicationId);
        loanApplication.choseConditions(
                conditions.getAmount(),
                conditions.getPeriod(),
                conditions.getInsurance());

        log.info("Conditions has been chosen for loan application {}", loanApplicationId);

        return domainObjectMapper.map(loanApplication.getConditions(), ChoseConditionsRequest.class);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Delegate(beanName = ProcessConstants.CALCULATE_OFFER_TASK, key = ProcessConstants.LOAN_APPLICATION_ID)
    public void calculateOfferForLoanApplication(@ProcessKeyValue String loanApplicationId) {
        log.info("Offer calculation for loan application {}...", loanApplicationId);

        var loanApplication = loanApplicationOfId(loanApplicationId);
        loanApplication.calculateOffer();

        var loanApplicationOfferCalculated = domainObjectMapper.map(loanApplication, LoanApplicationOfferCalculated.class);

        eventPublisher.publish(loanApplicationOfferCalculated);
        loanApplicationRepository.save(loanApplication);

        log.info("Offer has been calculated for loan application {}", loanApplicationId);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Delegates(values = {
            @Delegate(beanName = ProcessConstants.PRINT_FORM_TASK, key = ProcessConstants.LOAN_APPLICATION_ID),
            @Delegate(beanName = ProcessConstants.PRINT_CONDITIONS_TASK, key = ProcessConstants.LOAN_APPLICATION_ID),
            @Delegate(beanName = ProcessConstants.PRINT_INSURANCE_TASK, key = ProcessConstants.LOAN_APPLICATION_ID)
    })
    public void printDocumentForLoanApplication(@ProcessKeyValue String loanApplicationId,
                                                @ProcessValues(values = {
                                                        @ProcessValue(value = "FORM", type = Document.Type.class, delegate = ProcessConstants.PRINT_FORM_TASK),
                                                        @ProcessValue(value = "CONDITIONS", type = Document.Type.class, delegate = ProcessConstants.PRINT_CONDITIONS_TASK),
                                                        @ProcessValue(value = "INSURANCE", type = Document.Type.class, delegate = ProcessConstants.PRINT_INSURANCE_TASK)
                                                }) Document.Type documentType) {
        log.info("Printing document with type {} for loan application {}...", documentType, loanApplicationId);

        var loanApplication = loanApplicationOfId(loanApplicationId);
        var client = clientOfId(loanApplication.getClientId());

        byte[] content = printService.print(loanApplication, documentType, client);
        var documentName = loanApplication.makeDocumentName(documentType);

        var documentId = documentStorage.place(documentName, content);

        loanApplication.addDocument(documentId, documentType);

        log.info("Document with type {} has been printed for loan application {}", documentType, loanApplicationId);
    }

    @Transactional
    @Delegate(beanName = ProcessConstants.WAIT_FOR_DOCUMENT_PACKAGE_SIGNATURE_TASK, key = ProcessConstants.LOAN_APPLICATION_ID)
    public void waitForDocumentPackageSignatureOfLoanApplication(@ProcessKeyValue String loanApplicationId) {
        log.info("Transfer of the loan application {} pending signature", loanApplicationId);

        var loanApplication = loanApplicationOfId(loanApplicationId);
        loanApplication.waitForDocumentPackageSignature();

        log.info("loan application {} has been transferred to pending signature", loanApplicationId);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_ADMIN')")
    @UserTask(definitionKey = ProcessConstants.SING_DOCUMENTS_TASK)
    public void signDocumentPackageForLoanApplication(@ProcessKeyValue String loanApplicationId) {
        log.info("Signing document package of loan application {}...", loanApplicationId);

        var loanApplication = loanApplicationOfId(loanApplicationId);
        loanApplication.signDocumentPackage();

        log.info("Document package of loan application {} has been signed", loanApplicationId);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Delegate(beanName = ProcessConstants.START_ISSUANCE_TASK, key = ProcessConstants.LOAN_APPLICATION_ID)
    public void startIssuanceForLoanApplication(@ProcessKeyValue String loanApplicationId) {
        log.info("Starting issuance for loan application {}...", loanApplicationId);

        var loanApplication = loanApplicationOfId(loanApplicationId);

        issuanceService.startIssuance(loanApplication);

        log.info("Issuance has been started for loan application {}", loanApplicationId);
    }

    @Transactional
    @ReceiveTask(definitionKey = ProcessConstants.LOAN_APPLICATION_COMPLETED)
    public void completeLoanApplication(@ProcessKeyValue String loanApplicationId, String issuanceId) {
        log.info("Completion loan application {}...", loanApplicationId);

        var loanApplication = loanApplicationOfId(loanApplicationId);
        loanApplication.complete(issuanceId);

        eventPublisher.publish(new LoanApplicationCompleted(loanApplicationId, issuanceId));

        log.info("Loan application {} has been completed", loanApplicationId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Delegate(beanName = ProcessConstants.CANCEL_LOAN_APPLICATION_TASK, key = ProcessConstants.LOAN_APPLICATION_ID)
    public void cancelLoanApplication(@ProcessKeyValue String loanApplicationId) {
        log.info("Cancellation of loan application {}...", loanApplicationId);

        var loanApplication = loanApplicationOfId(loanApplicationId);
        loanApplication.cancel();

        eventPublisher.publish(new LoanApplicationCanceled(loanApplicationId));

        log.info("Loan application {} has been canceled", loanApplicationId);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('ROLE_CLIENT') or hasRole('ROLE_ADMIN')")
    public LoanApplicationDto findLoanApplicationById(String loanApplicationId) {
        return loanApplicationRepository.findById(LoanApplicationId.fromValue(loanApplicationId))
                .map(loanApplication -> domainObjectMapper.map(loanApplication, LoanApplicationDto.class))
                .orElseThrow(() -> new LoanApplicationNotFoundException(
                        String.format("Loan application with id %s is not found", loanApplicationId)));
    }

    private LoanApplication loanApplicationOfId(String loanApplicationId) {
        return loanApplicationRepository.findById(LoanApplicationId.fromValue(loanApplicationId))
                .orElseThrow(() -> new LoanApplicationNotFoundException(
                        String.format("Loan application with id %s is not found", loanApplicationId)));
    }

    private Client clientOfId(ClientId clientId) {
        return clientRepository.findById(clientId)
                .orElseThrow(() -> new ClientNotFoundException(
                        String.format("Client with id %s is not found", clientId)));
    }

}
