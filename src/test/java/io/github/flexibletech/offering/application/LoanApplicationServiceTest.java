package io.github.flexibletech.offering.application;

import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.application.dto.LoanApplicationDto;
import io.github.flexibletech.offering.application.dto.events.IntegrationEvent;
import io.github.flexibletech.offering.application.dto.events.LoanApplicationCanceled;
import io.github.flexibletech.offering.application.dto.events.LoanApplicationCompleted;
import io.github.flexibletech.offering.application.dto.events.LoanApplicationCreated;
import io.github.flexibletech.offering.application.dto.events.LoanApplicationOfferCalculated;
import io.github.flexibletech.offering.domain.LoanApplication;
import io.github.flexibletech.offering.domain.LoanApplicationRepository;
import io.github.flexibletech.offering.domain.document.Document;
import io.github.flexibletech.offering.domain.document.DocumentStorage;
import io.github.flexibletech.offering.domain.document.PrintService;
import io.github.flexibletech.offering.domain.factory.TestClientFactory;
import io.github.flexibletech.offering.domain.factory.TestLoanApplicationFactory;
import io.github.flexibletech.offering.domain.factory.TestRiskDecisionFactory;
import io.github.flexibletech.offering.domain.issuance.IssuanceService;
import io.github.flexibletech.offering.domain.preapproved.PreApprovedOfferRepository;
import io.github.flexibletech.offering.domain.risk.RiskService;
import io.github.flexibletech.offering.infrastructure.mapper.DomainObjectMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("ConstantConditions")
public class LoanApplicationServiceTest {
    @Spy
    private DomainObjectMapper domainObjectMapper = new DomainObjectMapperImpl(newModelMapper());
    @Mock
    private LoanApplicationRepository loanApplicationRepository;
    @Mock
    private PreApprovedOfferRepository preApprovedOfferRepository;
    @Mock
    private EventPublisher eventPublisher;
    @Mock
    private IssuanceService issuanceService;
    @Mock
    private RiskService riskService;
    @Mock
    private PrintService printService;
    @Mock
    private DocumentStorage documentStorage;

    @InjectMocks
    private LoanApplicationService loanApplicationService;

    @Captor
    private ArgumentCaptor<LoanApplication> loanApplicationArgumentCaptor;
    @Captor
    private ArgumentCaptor<? extends IntegrationEvent> eventCaptor;

    private ModelMapper newModelMapper() {
        var mapper = new ModelMapper();
        mapper.getConfiguration().setAmbiguityIgnored(true);

        Converter<Set<Document>, Set<String>> converter = new AbstractConverter<>() {
            @Override
            protected Set<String> convert(Set<Document> source) {
                return source.stream()
                        .map(Document::getId)
                        .collect(Collectors.toSet());
            }
        };

        mapper.typeMap(LoanApplication.class, LoanApplicationDto.class)
                .addMappings(m -> m.using(converter).map(LoanApplication::getDocumentPackage,
                        LoanApplicationDto::setDocumentPackage));

        return mapper;
    }

    @Test
    public void shouldStartNewLoanApplication() {
        Mockito.when(loanApplicationRepository.save(ArgumentMatchers.any(LoanApplication.class)))
                .thenReturn(TestLoanApplicationFactory.newLoanApplication());
        Mockito.doNothing().when(eventPublisher).publish(eventCaptor.capture());
        Mockito.when(preApprovedOfferRepository.findForClient(TestValues.CLIENT_ID)).thenReturn(null);

        var loanApplicationDto = loanApplicationService.startNewLoanApplication(
                TestApplicationObjectsFactory.newStartNewLoanApplicationRequest());

        //Assert loan application
        Assertions.assertNotNull(loanApplicationDto);
        Assertions.assertEquals(loanApplicationDto.getId(), TestValues.LOAN_APPLICATION_ID);
        Assertions.assertEquals(loanApplicationDto.getStatus(), LoanApplication.Status.NEW.name());

        //Assert application event
        var loanApplicationCreatedEvent = (LoanApplicationCreated) eventCaptor.getValue();
        Assertions.assertEquals(loanApplicationCreatedEvent.getLoanApplicationId(), TestValues.LOAN_APPLICATION_ID);
        Assertions.assertEquals(loanApplicationCreatedEvent.getLoanProgram(), LoanApplication.LoanProgram.COMMON.name());
        Assertions.assertEquals(loanApplicationCreatedEvent.getClientId(), TestValues.CLIENT_ID);

        //Assert conditions
        var conditions = loanApplicationCreatedEvent.getConditions();
        Assertions.assertNotNull(conditions);
        Assertions.assertEquals(conditions.getAmount(), TestValues.CONDITIONS_AMOUNT.getValue());
        Assertions.assertEquals(conditions.getPeriod(), TestValues.CONDITIONS_PERIOD);
        Assertions.assertFalse(conditions.getInsurance());
    }

    @Test
    public void shouldAddRiskDecisionToLoanApplication() {
        Mockito.when(loanApplicationRepository.save(ArgumentMatchers.any(LoanApplication.class)))
                .thenReturn(TestLoanApplicationFactory.newLoanApplication());
        Mockito.when(loanApplicationRepository.findById(TestValues.LOAN_APPLICATION_ID))
                .thenReturn(Optional.of(TestLoanApplicationFactory.newLoanApplication()));

        var status = loanApplicationService.addRiskDecisionToLoanApplication(TestValues.LOAN_APPLICATION_ID,
                TestRiskDecisionFactory.newApprovedRiskDecision());

        Assertions.assertEquals(status, LoanApplication.Status.APPROVED.name());
    }

    @Test
    public void shouldRequestRiskDecisionForLoanApplication() {
        Mockito.when(loanApplicationRepository.findById(TestValues.LOAN_APPLICATION_ID))
                .thenReturn(Optional.of(TestLoanApplicationFactory.newLoanApplication()));
        Mockito.doNothing().when(riskService).requestRiskDecision(loanApplicationArgumentCaptor.capture());

        loanApplicationService.requestRiskDecisionForLoanApplication(TestValues.LOAN_APPLICATION_ID);

        Assertions.assertNotNull(loanApplicationArgumentCaptor.getValue());
        Mockito.verify(riskService, Mockito.times(1)).requestRiskDecision(ArgumentMatchers.any(LoanApplication.class));
    }

    @Test
    public void shouldDefineIncomeConfirmationTypeForLoanApplication() {
        Mockito.when(loanApplicationRepository.findById(TestValues.LOAN_APPLICATION_ID))
                .thenReturn(Optional.of(TestLoanApplicationFactory.newLoanApplicationWithRiskDecision(
                        TestClientFactory.newPremiumClient())));
        Mockito.when(preApprovedOfferRepository.findForClient(Mockito.eq(TestValues.CLIENT_ID))).thenReturn(null);

        var incomeConfirmationType = loanApplicationService.defineIncomeConfirmationTypeForLoanApplication(
                TestValues.LOAN_APPLICATION_ID);

        Assertions.assertEquals(incomeConfirmationType, LoanApplication.IncomeConfirmationType.NONE.name());
    }

    @Test
    public void shouldChoseConditionsForLoanApplication() {
        Mockito.when(loanApplicationRepository.findById(TestValues.LOAN_APPLICATION_ID))
                .thenReturn(Optional.of(TestLoanApplicationFactory.newLoanApplication()));

        var conditions = loanApplicationService.choseConditionsForLoanApplication(
                TestValues.LOAN_APPLICATION_ID, TestApplicationObjectsFactory.newConditionsDto());

        Assertions.assertNotNull(conditions);
        Assertions.assertFalse(conditions.getInsurance());
        Assertions.assertEquals(conditions.getAmount(), TestValues.CONDITIONS_AMOUNT.getValue());
        Assertions.assertEquals(conditions.getPeriod(), TestValues.CONDITIONS_PERIOD);
    }

    @Test
    public void shouldCalculateOfferForLoanApplication() {
        Mockito.when(loanApplicationRepository.save(ArgumentMatchers.any(LoanApplication.class)))
                .thenReturn(TestLoanApplicationFactory.newLoanApplication());
        Mockito.when(loanApplicationRepository.findById(TestValues.LOAN_APPLICATION_ID))
                .thenReturn(Optional.of(TestLoanApplicationFactory.newLoanApplication()));
        Mockito.doNothing().when(eventPublisher).publish(eventCaptor.capture());

        loanApplicationService.calculateOfferForLoanApplication(TestValues.LOAN_APPLICATION_ID);

        var loanApplicationOfferCalculated = (LoanApplicationOfferCalculated) eventCaptor.getValue();
        Assertions.assertEquals(loanApplicationOfferCalculated.getLoanApplicationId(), TestValues.LOAN_APPLICATION_ID);

        //Assert offer
        var offer = loanApplicationOfferCalculated.getOffer();
        Assertions.assertEquals(offer.getPeriod(), TestValues.CONDITIONS_PERIOD);
        Assertions.assertEquals(offer.getAmount(), TestValues.CONDITIONS_AMOUNT.getValue());
        Assertions.assertEquals(offer.getAverageMonthlyPayment(), TestValues.OFFER_AVERAGE_MONTHLY_PAYMENT.getValue());
        Assertions.assertEquals(offer.getFirstPaymentDate(), TestValues.OFFER_FIRST_PAYMENT_DATE);
        Assertions.assertEquals(offer.getLastPaymentDate(), TestValues.OFFER_LAST_PAYMENT_DATE);
    }

    @Test
    public void shouldPrintFormForLoanApplication() {
        Mockito.when(loanApplicationRepository.save(ArgumentMatchers.any(LoanApplication.class)))
                .thenReturn(TestLoanApplicationFactory.newLoanApplication());
        Mockito.when(loanApplicationRepository.findById(TestValues.LOAN_APPLICATION_ID))
                .thenReturn(Optional.of(TestLoanApplicationFactory.newLoanApplication()));
        Mockito.when(printService.print(ArgumentMatchers.any(LoanApplication.class), Mockito.eq(Document.Type.FORM)))
                .thenReturn(new byte[]{});
        Mockito.when(documentStorage.place(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .thenReturn(TestValues.FORM_DOCUMENT_ID);

        loanApplicationService.printDocumentForLoanApplication(TestValues.LOAN_APPLICATION_ID, Document.Type.FORM);

        Mockito.verify(printService, Mockito.times(1))
                .print(ArgumentMatchers.any(LoanApplication.class), Mockito.eq(Document.Type.FORM));
        Mockito.verify(documentStorage, Mockito.times(1)).place(ArgumentMatchers.anyString(), ArgumentMatchers.any());
    }

    @Test
    public void shouldStartIssuanceForLoanApplication() {
        Mockito.when(loanApplicationRepository.findById(TestValues.LOAN_APPLICATION_ID))
                .thenReturn(Optional.of(TestLoanApplicationFactory.newLoanApplication()));
        Mockito.doNothing().when(issuanceService).startIssuance(loanApplicationArgumentCaptor.capture());

        loanApplicationService.startIssuanceForLoanApplication(TestValues.LOAN_APPLICATION_ID);

        Assertions.assertNotNull(loanApplicationArgumentCaptor.getValue());
        Mockito.verify(issuanceService, Mockito.times(1)).startIssuance(ArgumentMatchers.any(LoanApplication.class));
    }

    @Test
    public void shouldCancelLoanApplication() {
        Mockito.when(loanApplicationRepository.save(ArgumentMatchers.any(LoanApplication.class)))
                .thenReturn(TestLoanApplicationFactory.newLoanApplication());
        Mockito.doNothing().when(eventPublisher).publish(eventCaptor.capture());
        Mockito.when(loanApplicationRepository.findById(TestValues.LOAN_APPLICATION_ID))
                .thenReturn(Optional.of(TestLoanApplicationFactory.newLoanApplicationWithOffer()));

        var status = loanApplicationService.cancelLoanApplication(TestValues.LOAN_APPLICATION_ID);

        Assertions.assertEquals(status, LoanApplication.Status.CANCEL.name());

        var loanApplicationCanceled = (LoanApplicationCanceled) eventCaptor.getValue();
        Assertions.assertEquals(loanApplicationCanceled.getLoanApplicationId(), TestValues.LOAN_APPLICATION_ID);
    }

    @Test
    public void shouldCompleteLoanApplication() {
        Mockito.when(loanApplicationRepository.save(ArgumentMatchers.any(LoanApplication.class)))
                .thenReturn(TestLoanApplicationFactory.newLoanApplication());
        Mockito.when(loanApplicationRepository.findById(TestValues.LOAN_APPLICATION_ID))
                .thenReturn(Optional.of(TestLoanApplicationFactory.newLoanApplication()));
        Mockito.doNothing().when(eventPublisher).publish(eventCaptor.capture());

        loanApplicationService.completeLoanApplication(TestValues.LOAN_APPLICATION_ID, TestValues.ISSUANCE_ID);

        var loanApplicationCompleted = (LoanApplicationCompleted) eventCaptor.getValue();
        Assertions.assertEquals(loanApplicationCompleted.getLoanApplicationId(), TestValues.LOAN_APPLICATION_ID);
    }

    @Test
    public void shouldFindLoanApplicationById() {
        Mockito.when(loanApplicationRepository.findById(TestValues.LOAN_APPLICATION_ID))
                .thenReturn(Optional.of(TestLoanApplicationFactory.newLoanApplicationWithOffer()));

        var loanApplicationDto = loanApplicationService.findLoanApplicationById(TestValues.LOAN_APPLICATION_ID);

        Assertions.assertNotNull(loanApplicationDto);
        Assertions.assertEquals(loanApplicationDto.getId(), TestValues.LOAN_APPLICATION_ID);
        Assertions.assertEquals(loanApplicationDto.getStatus(), LoanApplication.Status.APPROVED.name());

        var offer = loanApplicationDto.getOffer();
        Assertions.assertNotNull(offer);
        Assertions.assertEquals(offer.getPeriod(), TestValues.CONDITIONS_PERIOD);
        Assertions.assertEquals(offer.getInsurancePremium(), TestValues.OFFER_INSURANCE_PREMIUM.getValue());
        Assertions.assertEquals(offer.getSingleInsurancePayment(), TestValues.OFFER_SINGLE_INSURANCE_PAYMENT.getValue());
        Assertions.assertEquals(offer.getAmount(), TestValues.CONDITIONS_AMOUNT.getValue());
        Assertions.assertEquals(offer.getAverageMonthlyPayment(), TestValues.OFFER_AVERAGE_MONTHLY_PAYMENT.getValue());
        Assertions.assertEquals(offer.getFirstPaymentDate(), TestValues.OFFER_FIRST_PAYMENT_DATE);
        Assertions.assertEquals(offer.getLastPaymentDate(), TestValues.OFFER_LAST_PAYMENT_DATE);

        var conditionsRestrictions = loanApplicationDto.getConditionsRestrictions();
        Assertions.assertNotNull(conditionsRestrictions);
        Assertions.assertEquals(conditionsRestrictions.getMaxAmount(), TestValues.CONDITIONS_RESTRICTIONS_MAX_AMOUNT.getValue());
        Assertions.assertEquals(conditionsRestrictions.getMaxPeriod(), TestValues.CONDITIONS_RESTRICTIONS_MAX_PERIOD);

        Assertions.assertEquals(loanApplicationDto.getDocumentPackage(),
                Set.of(TestValues.FORM_DOCUMENT_ID, TestValues.CONDITIONS_DOCUMENT_ID));
    }

}
