package io.github.flexibletech.offering.domain.loanapplication;

import io.github.flexibletech.offering.domain.DomainEvent;
import io.github.flexibletech.offering.domain.client.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoanApplicationCreated implements DomainEvent {
    private Client client;
}
