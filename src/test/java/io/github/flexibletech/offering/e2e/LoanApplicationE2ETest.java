package io.github.flexibletech.offering.e2e;

import io.github.flexibletech.offering.AbstractIntegrationTest;
import io.github.flexibletech.offering.ResourceUtil;
import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.application.TestApplicationObjectsFactory;
import io.github.flexibletech.offering.application.loanapplication.dto.LoanApplicationDto;
import io.github.flexibletech.offering.domain.client.Client;
import io.github.flexibletech.offering.domain.client.ClientId;
import io.github.flexibletech.offering.domain.client.ClientRepository;
import io.github.flexibletech.offering.domain.factory.TestClientFactory;
import io.github.flexibletech.offering.domain.factory.TestPreApprovedOfferFactory;
import io.github.flexibletech.offering.domain.loanapplication.LoanApplication;
import io.github.flexibletech.offering.domain.loanapplication.LoanApplicationId;
import io.github.flexibletech.offering.domain.loanapplication.LoanApplicationRepository;
import io.github.flexibletech.offering.domain.loanapplication.document.Document;
import io.github.flexibletech.offering.domain.loanapplication.document.DocumentStorage;
import io.github.flexibletech.offering.domain.loanapplication.document.PrintService;
import io.github.flexibletech.offering.domain.loanapplication.risk.RiskDecision;
import io.github.flexibletech.offering.domain.preapproved.PreApprovedOfferRepository;
import io.github.flexibletech.offering.infrastructure.messaging.issuance.response.IssuanceResponse;
import io.github.flexibletech.offering.infrastructure.messaging.risk.response.RiskResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.http.MediaType;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class LoanApplicationE2ETest extends AbstractIntegrationTest {
    @MockBean
    private PreApprovedOfferRepository preApprovedOfferRepository;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private PrintService printService;

    @MockBean
    private DocumentStorage documentStorage;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InputDestination inputDestination;

    @Value("${spring.cloud.stream.bindings.riskResponseListener-in-0.destination}")
    private String riskDestination;
    @Value("${spring.cloud.stream.bindings.issuanceResponseListener-in-0.destination}")
    private String issuanceDestination;

    @Test
    @WithMockUser(roles = "CLIENT")
    public void shouldGoThroughFullLoanApplicationProcess() throws Exception {
        Mockito.when(preApprovedOfferRepository.findForClient(ArgumentMatchers.any(ClientId.class)))
                .thenReturn(TestPreApprovedOfferFactory.newPreApprovedOffer());
        Mockito.when(printService.print(ArgumentMatchers.any(LoanApplication.class),
                        ArgumentMatchers.any(Document.Type.class),
                        ArgumentMatchers.any(Client.class)))
                .thenReturn(ResourceUtil.getByteArray("classpath:files/test.pdf"));
        Mockito.when(documentStorage.place(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(clientRepository.findById(ArgumentMatchers.any(ClientId.class)))
                .thenReturn(Optional.of(TestClientFactory.newStandardMarriedClient()));

        var loanApplicationDto = startLoanApplicationProcess();

        sendRiskDecisionResponse(loanApplicationDto.getId());
        Thread.sleep(1000);

        choseConditions(loanApplicationDto.getId());
        Thread.sleep(1000);

        signDocuments(loanApplicationDto.getId());
        Thread.sleep(100);

        sendIssuanceResponse(loanApplicationDto.getId());
        Thread.sleep(1000);

        var loanApplication = loanApplicationRepository.findById(LoanApplicationId.fromValue(loanApplicationDto.getId()))
                .orElse(null);
        Assertions.assertNotNull(loanApplication);
        Assertions.assertTrue(loanApplication.isCompleted());
    }

    //Process steps
    private LoanApplicationDto startLoanApplicationProcess() throws Exception {
        var actualResponse = mockMvc.perform(MockMvcRequestBuilders.post("/v1/loan-applications")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TestApplicationObjectsFactory.newStartNewLoanApplicationRequest()))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readValue(actualResponse, LoanApplicationDto.class);
    }

    private void sendRiskDecisionResponse(String loanApplicationId) {
        inputDestination.send(MessageBuilder.withPayload(newRiskResponse(loanApplicationId)).build(),
                riskDestination);
    }

    private void choseConditions(String loanApplicationId) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(
                                "/v1/loan-applications/{id}/conditions", loanApplicationId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TestApplicationObjectsFactory.newChoseConditionsRequest()))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private void signDocuments(String loanApplicationId) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(
                                "/v1/loan-applications/{id}/documents/sign", loanApplicationId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    private void sendIssuanceResponse(String loanApplicationId) {
        inputDestination.send(MessageBuilder.withPayload(newIssuanceResponse(loanApplicationId)).build(),
                issuanceDestination);
    }

    @SuppressWarnings("ConstantConditions")
    private RiskResponse newRiskResponse(String loanApplicationId) {
        return new RiskResponse(
                loanApplicationId,
                TestValues.RISK_DECISION_ID,
                TestValues.PAYROLL_SALARY.getValue(),
                TestValues.PAYROLL_LAST_SALARY_DATE,
                TestValues.CONDITIONS_RESTRICTIONS_MAX_AMOUNT.getValue(),
                TestValues.CONDITIONS_RESTRICTIONS_MAX_PERIOD,
                RiskDecision.Status.APPROVED.name());
    }

    private IssuanceResponse newIssuanceResponse(String loanApplicationId) {
        return new IssuanceResponse(loanApplicationId, TestValues.ISSUANCE_ID);
    }

}
