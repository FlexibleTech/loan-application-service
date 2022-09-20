package io.github.flexibletech.offering.application.client;

import io.github.flexibletech.offering.application.DomainObjectMapper;
import io.github.flexibletech.offering.application.EventPublisher;
import io.github.flexibletech.offering.domain.client.ClientRepository;
import io.github.flexibletech.offering.domain.loanapplication.LoanApplicationCreated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class LoanApplicationCreatedSubscriber {
    private final ClientRepository clientRepository;
    public final EventPublisher eventPublisher;
    private final DomainObjectMapper domainObjectMapper;

    @Autowired
    public LoanApplicationCreatedSubscriber(ClientRepository clientRepository, EventPublisher eventPublisher,
                                            DomainObjectMapper domainObjectMapper) {
        this.clientRepository = clientRepository;
        this.eventPublisher = eventPublisher;
        this.domainObjectMapper = domainObjectMapper;
    }

    @EventListener
    @Transactional
    public void onDomainEvent(LoanApplicationCreated loanApplicationCreatedDomainEvent) {
        var newClient = loanApplicationCreatedDomainEvent.getClient();
        var optionalClient = clientRepository.findById(newClient.getId());

        if (optionalClient.isEmpty()) {
            clientRepository.save(newClient);
            eventPublisher.publish(domainObjectMapper.map(newClient, ClientCreated.class));

            log.info("New client {} has been created", newClient.getId().toString());
        } else {
            var oldClient = optionalClient.get();
            oldClient.update(newClient.createClientDetails());

            clientRepository.save(oldClient);
            eventPublisher.publish(domainObjectMapper.map(newClient, ClientUpdated.class));

            log.info("Client {} has been updated", newClient.getId().toString());
        }
    }

}
