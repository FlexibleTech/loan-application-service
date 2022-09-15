package io.github.flexibletech.offering.e2e;

import io.github.flexibletech.offering.AbstractIntegrationTest;
import io.github.flexibletech.offering.ResourceUtil;
import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.application.TestApplicationObjectsFactory;
import io.github.flexibletech.offering.application.dto.LoanApplicationDto;
import io.github.flexibletech.offering.domain.LoanApplication;
import io.github.flexibletech.offering.domain.LoanApplicationId;
import io.github.flexibletech.offering.domain.LoanApplicationRepository;
import io.github.flexibletech.offering.domain.client.ClientId;
import io.github.flexibletech.offering.domain.document.Document;
import io.github.flexibletech.offering.domain.document.DocumentStorage;
import io.github.flexibletech.offering.domain.document.PrintService;
import io.github.flexibletech.offering.domain.factory.TestPreApprovedOfferFactory;
import io.github.flexibletech.offering.domain.preapproved.PreApprovedOfferRepository;
import io.github.flexibletech.offering.domain.risk.RiskDecision;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@AutoConfigureMockMvc
public class LoanApplicationE2ETest extends AbstractIntegrationTest {
    @MockBean
    private PreApprovedOfferRepository preApprovedOfferRepository;

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
        Mockito.when(printService.print(ArgumentMatchers.any(LoanApplication.class), ArgumentMatchers.any(Document.Type.class)))
                .thenReturn(ResourceUtil.getByteArray("classpath:files/test.pdf"));
        Mockito.when(documentStorage.place(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        //Start loan application process
        var actualResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/loan-applications")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TestApplicationObjectsFactory.newStartNewLoanApplicationRequest()))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        var loanApplicationDto = objectMapper.readValue(actualResponse, LoanApplicationDto.class);

        //Send risk decision response
        inputDestination.send(MessageBuilder.withPayload(newRiskResponse(loanApplicationDto.getId())).build(),
                riskDestination);

        Thread.sleep(1000);

        //Chose conditions
        mockMvc.perform(MockMvcRequestBuilders.post(
                                "/api/v1/loan-applications/{id}/conditions", loanApplicationDto.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(TestApplicationObjectsFactory.newConditionsDto()))
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Thread.sleep(1000);

        //Sign documents
        mockMvc.perform(MockMvcRequestBuilders.post(
                                "/api/v1/loan-applications/{id}/documents/sign", loanApplicationDto.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andReturn()
                .getResponse()
                .getContentAsString();

        Thread.sleep(100);

        //Send issuance response
        inputDestination.send(MessageBuilder.withPayload(newIssuanceResponse(loanApplicationDto.getId())).build(),
                issuanceDestination);

        Thread.sleep(1000);

        var loanApplication = loanApplicationRepository.findById(LoanApplicationId.fromValue(loanApplicationDto.getId()))
                .orElse(null);
        Assertions.assertNotNull(loanApplication);
        Assertions.assertTrue(loanApplication.isCompleted());
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
