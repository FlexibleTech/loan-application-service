package io.github.flexibletech.offering.infrastructure.events.client;

import io.github.flexibletech.offering.application.client.ClientService;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.function.Consumer;

@Component
public class ClientUpdatedEventSubscriber extends AbstractClientEventSubscriber implements Consumer<ClientUpdatedEvent> {

    protected ClientUpdatedEventSubscriber(ClientService clientService) {
        super(clientService);
    }

    @Override
    public void accept(@Valid  ClientUpdatedEvent clientUpdatedEvent) {
        var clientDetails = createClientDetailsFrom(clientUpdatedEvent);
        clientService.updateClient(clientUpdatedEvent.getClientId(), clientDetails);
    }

}
