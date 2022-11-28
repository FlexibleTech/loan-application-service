package io.github.flexibletech.offering.infrastructure.messaging;

import io.github.flexibletech.offering.AbstractIntegrationTest;
import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.domain.factory.TestLoanApplicationFactory;
import io.github.flexibletech.offering.domain.loanapplication.issuance.IssuanceService;
import io.github.flexibletech.offering.infrastructure.messaging.issuance.request.StartIssuanceRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class IssuanceServiceIT extends AbstractIntegrationTest {
    @Autowired
    private IssuanceService issuanceService;

    @Autowired
    private OutputDestination outputDestination;

    @Test
    @SuppressWarnings("ConstantConditions")
    public void shouldSendStartIssuanceRequestToQueue() throws IOException {
        var loanApplication = TestLoanApplicationFactory.newLoanApplication();

        issuanceService.startIssuance(loanApplication);

        var message = outputDestination.receive(3000);
        var startIssuanceRequest = objectMapper.readValue(message.getPayload(), StartIssuanceRequest.class);

        Assertions.assertNotNull(startIssuanceRequest);
        Assertions.assertEquals(startIssuanceRequest.getApplicationId(), TestValues.LOAN_APPLICATION_ID);
        Assertions.assertEquals(startIssuanceRequest.getAmount(), TestValues.CONDITIONS_AMOUNT.getValue());
        Assertions.assertEquals(startIssuanceRequest.getPeriod(), TestValues.CONDITIONS_PERIOD);
        Assertions.assertEquals(startIssuanceRequest.getClientId(), TestValues.CLIENT_ID);
    }

}
