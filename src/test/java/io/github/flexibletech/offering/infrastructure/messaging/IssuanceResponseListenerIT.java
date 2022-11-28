package io.github.flexibletech.offering.infrastructure.messaging;

import io.github.flexibletech.offering.AbstractIntegrationTest;
import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.application.loanapplication.LoanApplicationService;
import io.github.flexibletech.offering.infrastructure.messaging.issuance.response.IssuanceResponse;
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

public class IssuanceResponseListenerIT extends AbstractIntegrationTest {
    @Autowired
    private InputDestination inputDestination;

    @MockBean
    private LoanApplicationService loanApplicationService;

    @Value("${spring.cloud.stream.bindings.issuanceResponseListener-in-0.destination}")
    private String destination;

    @Captor
    private ArgumentCaptor<String> issuanceIdCaptor;

    @Test
    public void shouldReceiveIssuanceResponse() {
        Mockito.doNothing().when(loanApplicationService)
                .completeLoanApplication(Mockito.eq(TestValues.LOAN_APPLICATION_ID), issuanceIdCaptor.capture());

        inputDestination.send(MessageBuilder.withPayload(
                        new IssuanceResponse(TestValues.LOAN_APPLICATION_ID, TestValues.ISSUANCE_ID))
                .build(), destination);

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var issuanceId = issuanceIdCaptor.getValue();
                    Assertions.assertEquals(issuanceId, TestValues.ISSUANCE_ID);
                });
    }
}
