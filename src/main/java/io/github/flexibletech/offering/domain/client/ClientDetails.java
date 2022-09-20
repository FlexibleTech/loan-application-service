package io.github.flexibletech.offering.domain.client;

import io.github.flexibletech.offering.domain.Amount;
import io.github.flexibletech.offering.domain.ValueObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ClientDetails implements ValueObject {
    private final PersonNameDetails personNameDetails;
    private final Passport passport;
    private final Client.MaritalStatus maritalStatus;
    private final Organization workPlace;
    private final String fullRegistrationAddress;
    private final String phoneNumber;
    private final String email;
    private final Amount income;
    private final Amount spouseIncome;
    private final Client.Category category;
    private final LocalDate birthDate;
}
