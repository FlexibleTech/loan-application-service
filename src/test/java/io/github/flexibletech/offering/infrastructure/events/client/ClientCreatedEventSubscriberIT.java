package io.github.flexibletech.offering.infrastructure.events.client;

import io.github.flexibletech.offering.AbstractIntegrationTest;
import io.github.flexibletech.offering.application.client.ClientService;
import io.github.flexibletech.offering.domain.client.ClientDetails;
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

public class ClientCreatedEventSubscriberIT extends AbstractIntegrationTest {
    private static final String EVENT_HEADER = "type";
    private static final String EVENT_HEADER_VALUE = "ClientCreated";

    @Autowired
    private InputDestination inputDestination;

    @MockBean
    private ClientService clientService;

    @Value("${spring.cloud.stream.bindings.functionRouter-in-0.destination}")
    private String destination;

    @Captor
    private ArgumentCaptor<ClientDetails> clientDetailsArgumentCaptor;

    @Test
    public void shouldReceiveClientCreatedEvent() {
        Mockito.doNothing().when(clientService).createNewClient(clientDetailsArgumentCaptor.capture());

        inputDestination.send(MessageBuilder.withPayload(TestClientEventsFactory.newClientCreatedEvent())
                .setHeader(EVENT_HEADER, EVENT_HEADER_VALUE)
                .build(), destination);

        Awaitility.await()
                .atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    var clientDetails = clientDetailsArgumentCaptor.getValue();
                    Assertions.assertNotNull(clientDetails);
                });
    }


}
