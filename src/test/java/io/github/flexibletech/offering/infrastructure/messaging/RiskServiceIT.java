package io.github.flexibletech.offering.infrastructure.messaging;

import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.domain.factory.TestLoanApplicationFactory;
import io.github.flexibletech.offering.domain.risk.RiskService;
import io.github.flexibletech.offering.AbstractIntegrationTest;
import io.github.flexibletech.offering.infrastructure.messaging.risk.request.RiskRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;

@Import(TestChannelBinderConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RiskServiceIT extends AbstractIntegrationTest {
    @Autowired
    private RiskService riskService;

    @Autowired
    private OutputDestination outputDestination;

    @Test
    @SuppressWarnings("ConstantConditions")
    public void shouldSendRiskRequestToQueue() throws IOException {
        var loanApplication = TestLoanApplicationFactory.newLoanApplication();

        riskService.requestRiskDecision(loanApplication);

        var message = outputDestination.receive(3000);
        var riskRequest = objectMapper.readValue(message.getPayload(), RiskRequest.class);

        Assertions.assertNotNull(riskRequest);
        Assertions.assertEquals(riskRequest.getApplicationId(), TestValues.LOAN_APPLICATION_ID);
        Assertions.assertEquals(riskRequest.getAmount(), TestValues.CONDITIONS_AMOUNT.getValue());
        Assertions.assertEquals(riskRequest.getPeriod(), TestValues.CONDITIONS_PERIOD);

        var client = riskRequest.getClient();
        Assertions.assertEquals(client.getMaritalStatus(), RiskRequest.Client.MaritalStatus.MARRIED.getCode());
        Assertions.assertEquals(client.getFullRegistrationAddress(), TestValues.CLIENT_FULL_REGISTRATION_ADDRESS);
        Assertions.assertEquals(client.getPhoneNumber(), TestValues.CLIENT_PHONE_NUMBER);
        Assertions.assertEquals(client.getEmail(), TestValues.CLIENT_EMAIL);
        Assertions.assertEquals(client.getName(), TestValues.NAME);
        Assertions.assertEquals(client.getMiddleName(), TestValues.MIDDLE_NAME);
        Assertions.assertEquals(client.getSurName(), TestValues.SUR_NAME);
        Assertions.assertEquals(client.getIncome(), TestValues.CLIENT_INCOME.getValue());
        Assertions.assertEquals(client.getSpouseIncome(), TestValues.CLIENT_SPOUSE_INCOME.getValue());

        var passport = client.getPassport();
        Assertions.assertEquals(passport.getDepartment(), TestValues.PASSPORT_DEPARTMENT);
        Assertions.assertEquals(passport.getDepartmentCode(), TestValues.PASSPORT_DEPARTMENT_CODE);
        Assertions.assertEquals(passport.getIssueDate(), TestValues.PASSPORT_ISSUE_DATE);
        Assertions.assertEquals(passport.getSeries(), TestValues.PASSPORT_SERIES);
        Assertions.assertEquals(passport.getNumber(), TestValues.PASSPORT_NUMBER);

        var workPlace = client.getWorkPlace();
        Assertions.assertEquals(workPlace.getTitle(), TestValues.ORGANIZATION_TITLE);
        Assertions.assertEquals(workPlace.getInn(), TestValues.ORGANIZATION_INN);
        Assertions.assertEquals(workPlace.getFullAddress(), TestValues.ORGANIZATION_FULL_ADDRESS);
    }

}
