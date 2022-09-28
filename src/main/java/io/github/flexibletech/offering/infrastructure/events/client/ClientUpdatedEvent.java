package io.github.flexibletech.offering.infrastructure.events.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientUpdatedEvent extends AbstractClientEvent {

    public ClientUpdatedEvent(String clientId, String name, String middleName, String surName, List<Document> documents,
                              String maritalStatus, List<Address> addresses, Organization workPlace,
                              String phoneNumber, String email, BigDecimal income, BigDecimal spouseIncome,
                              String category, LocalDate birthDate) {
        super(clientId, name, middleName, surName, documents, maritalStatus, addresses,
                workPlace, phoneNumber, email, income, spouseIncome, category, birthDate);
    }
}
