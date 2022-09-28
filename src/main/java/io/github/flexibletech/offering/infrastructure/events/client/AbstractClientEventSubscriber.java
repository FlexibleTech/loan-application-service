package io.github.flexibletech.offering.infrastructure.events.client;

import io.github.flexibletech.offering.application.client.ClientService;
import io.github.flexibletech.offering.domain.Amount;
import io.github.flexibletech.offering.domain.client.Client;
import io.github.flexibletech.offering.domain.client.ClientDetails;
import io.github.flexibletech.offering.domain.client.ClientId;

import java.util.Optional;
import java.util.function.Function;

public abstract class AbstractClientEventSubscriber {
    protected final ClientService clientService;

    private static final String MARRIED_MARITAL_STATUS_CODE = "14";
    private static final String UNMARRIED_MARITAL_STATUS_CODE = "15";

    private static final String STANDARD_CLIENT_CATEGORY = "55";
    private static final String PAYROLL_CLIENT_CATEGORY = "71";
    private static final String PREMIUM_CLIENT_CATEGORY = "34";

    protected AbstractClientEventSubscriber(ClientService clientService) {
        this.clientService = clientService;
    }

    protected ClientDetails createClientDetailsFrom(AbstractClientEvent event) {
        var passport = getPassport(event);
        var workPlace = event.getWorkPlace();
        var registrationAddress = getRegistrationAddress(event);

        return new ClientDetails(new ClientId(event.getClientId()),
                event.getName(),
                event.getMiddleName(),
                event.getSurName(),
                getProperty(passport, ClientCreatedEvent.Document::getSeries),
                getProperty(passport, ClientCreatedEvent.Document::getNumber),
                getProperty(passport, ClientCreatedEvent.Document::getIssueDate),
                getProperty(passport, ClientCreatedEvent.Document::getIssuedBy),
                getProperty(passport, ClientCreatedEvent.Document::getIssuedByCode),
                defineMaritalStatus(event.getMaritalStatus()),
                workPlace.getTitle(),
                workPlace.getInn(),
                workPlace.getAddress().getStringValue(),
                registrationAddress,
                event.getPhoneNumber(),
                event.getEmail(),
                Amount.fromValue(event.getIncome()),
                Amount.fromValue(event.getSpouseIncome()),
                defineCategory(event.getCategory()),
                event.getBirthDate());
    }

    private ClientCreatedEvent.Document getPassport(AbstractClientEvent event) {
        return event.getDocuments()
                .stream()
                .filter(document -> document.getType().equals(ClientCreatedEvent.Document.PASSPORT_DOCUMENT_TYPE))
                .findAny()
                .orElse(null);
    }

    private String getRegistrationAddress(AbstractClientEvent event) {
        return event.getAddresses()
                .stream()
                .filter(address -> address.getType().equals(ClientCreatedEvent.Address.REGISTRATION_ADDRESS))
                .map(ClientCreatedEvent.Address::getStringValue)
                .findAny()
                .orElse(null);
    }

    private <T, U> U getProperty(T object, Function<T, U> supplier) {
        return Optional.ofNullable(object)
                .map(supplier)
                .orElse(null);
    }

    private Client.MaritalStatus defineMaritalStatus(String maritalStatus) {
        switch (maritalStatus) {
            case MARRIED_MARITAL_STATUS_CODE:
                return Client.MaritalStatus.MARRIED;
            case UNMARRIED_MARITAL_STATUS_CODE:
            default:
                return Client.MaritalStatus.UNMARRIED;
        }
    }

    private Client.Category defineCategory(String category) {
        switch (category) {
            case PAYROLL_CLIENT_CATEGORY:
                return Client.Category.PAYROLL;
            case PREMIUM_CLIENT_CATEGORY:
                return Client.Category.PREMIUM;
            case STANDARD_CLIENT_CATEGORY:
            default:
                return Client.Category.STANDARD;
        }
    }

}
