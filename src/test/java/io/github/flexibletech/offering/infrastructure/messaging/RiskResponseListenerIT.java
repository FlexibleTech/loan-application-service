package io.github.flexibletech.offering.infrastructure.messaging;

import io.github.flexibletech.offering.AbstractIntegrationTest;
import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.application.LoanApplicationService;
import io.github.flexibletech.offering.application.TestApplicationObjectsFactory;
import io.github.flexibletech.offering.domain.risk.RiskDecision;
import io.github.flexibletech.offering.infrastructure.messaging.risk.response.RiskResponse;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.messaging.support.MessageBuilder;

import java.util.concurrent.TimeUnit;

public class RiskResponseListenerIT extends AbstractIntegrationTest {
    @Autowired
    private InputDestination inputDestination;

    @MockBean
    private LoanApplicationService loanApplicationService;

    @Value("${spring.cloud.stream.bindings.riskResponseListener-in-0.destination}")
    private String destination;

    @Captor
    private ArgumentCaptor<RiskDecision> riskDecisionArgumentCaptor;

    @Test
    public void shouldReceiveRiskResponse() {
        Mockito.when(loanApplicationService.addRiskDecisionToLoanApplication(
                        Mockito.eq(TestValues.LOAN_APPLICATION_ID), riskDecisionArgumentCaptor.capture()))
                .thenReturn(TestApplicationObjectsFactory.newLoanApplicationDtoWithOffer());

        inputDestination.send(MessageBuilder.withPayload(newRiskResponse()).build(), destination);

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var riskDecision = riskDecisionArgumentCaptor.getValue();

                    Assertions.assertNotNull(riskDecision);
                    Assertions.assertTrue(riskDecision.isApproved());
                    Assertions.assertEquals(riskDecision.getId(), TestValues.RISK_DECISION_ID);

                    //Assert payroll
                    var payroll = riskDecision.getPayroll();

                    Assertions.assertNotNull(payroll);
                    Assertions.assertEquals(payroll.getSalary(), TestValues.PAYROLL_SALARY);
                    Assertions.assertEquals(payroll.getLastSalaryDate(), TestValues.PAYROLL_LAST_SALARY_DATE);

                    //Assert ConditionsRestrictions
                    var conditionsRestrictions = riskDecision.getConditionsRestrictions();

                    Assertions.assertNotNull(conditionsRestrictions);
                    Assertions.assertEquals(conditionsRestrictions.getMaxAmount(), TestValues.CONDITIONS_RESTRICTIONS_MAX_AMOUNT);
                    Assertions.assertEquals(conditionsRestrictions.getMaxPeriod(), TestValues.CONDITIONS_RESTRICTIONS_MAX_PERIOD);
                });
    }

    @SuppressWarnings("ConstantConditions")
    private RiskResponse newRiskResponse() {
        return new RiskResponse(
                TestValues.LOAN_APPLICATION_ID,
                TestValues.RISK_DECISION_ID,
                TestValues.PAYROLL_SALARY.getValue(),
                TestValues.PAYROLL_LAST_SALARY_DATE,
                TestValues.CONDITIONS_RESTRICTIONS_MAX_AMOUNT.getValue(),
                TestValues.CONDITIONS_RESTRICTIONS_MAX_PERIOD,
                RiskDecision.Status.APPROVED.name());
    }

}
