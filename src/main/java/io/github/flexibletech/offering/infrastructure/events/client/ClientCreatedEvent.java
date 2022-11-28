package io.github.flexibletech.offering.infrastructure.events.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientCreatedEvent extends AbstractClientEvent {

    public ClientCreatedEvent(String clientId, String name, String middleName, String surName,
                              List<Document> documents, String maritalStatus,
                              List<Address> addresses, Organization workPlace,
                              String phoneNumber, String email, BigDecimal income, BigDecimal spouseIncome,
                              String category, LocalDate birthDate) {
        super(clientId, name, middleName, surName, documents, maritalStatus,
                addresses, workPlace, phoneNumber, email, income,
                spouseIncome, category, birthDate);
    }

}
