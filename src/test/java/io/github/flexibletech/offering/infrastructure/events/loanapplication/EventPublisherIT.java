package io.github.flexibletech.offering.infrastructure.events.loanapplication;

import io.github.flexibletech.offering.AbstractIntegrationTest;
import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.application.EventPublisher;
import io.github.flexibletech.offering.application.TestApplicationObjectsFactory;
import io.github.flexibletech.offering.application.loanapplication.dto.events.LoanApplicationCompleted;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EventPublisherIT extends AbstractIntegrationTest {
    @Autowired
    private EventPublisher eventPublisher;

    @Autowired
    private OutputDestination outputDestination;

    @Test
    public void shouldPublishLoanApplicationCompletedEvent() throws IOException {
        eventPublisher.publish(TestApplicationObjectsFactory.newLoanApplicationCompleted());

        var message = outputDestination.receive(3000);
        var loanApplicationCompletedEvent = objectMapper.readValue(message.getPayload(), LoanApplicationCompleted.class);

        Assertions.assertNotNull(loanApplicationCompletedEvent);
        Assertions.assertEquals(loanApplicationCompletedEvent.getLoanApplicationId(), TestValues.LOAN_APPLICATION_ID);
        Assertions.assertEquals(loanApplicationCompletedEvent.getIssuanceId(), TestValues.ISSUANCE_ID);
    }

}
