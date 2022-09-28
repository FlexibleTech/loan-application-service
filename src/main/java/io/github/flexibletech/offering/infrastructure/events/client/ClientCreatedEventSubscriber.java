package io.github.flexibletech.offering.infrastructure.events.client;

import io.github.flexibletech.offering.application.client.ClientService;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.function.Consumer;

@Component
public class ClientCreatedEventSubscriber extends AbstractClientEventSubscriber implements Consumer<ClientCreatedEvent> {

    protected ClientCreatedEventSubscriber(ClientService clientService) {
        super(clientService);
    }

    @Override
    public void accept(@Valid ClientCreatedEvent clientCreatedEvent) {
        var clientDetails = createClientDetailsFrom(clientCreatedEvent);
        clientService.createNewClient(clientDetails);
    }
}
