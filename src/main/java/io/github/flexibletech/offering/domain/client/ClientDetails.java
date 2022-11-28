package io.github.flexibletech.offering.domain.client;

import io.github.flexibletech.offering.domain.Amount;
import io.github.flexibletech.offering.domain.ValueObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@AllArgsConstructor
public class ClientDetails implements ValueObject {
    private final ClientId clientId;
    private final String name;
    private final String middleName;
    private final String surName;
    private final String passportSeries;
    private final String passportNumber;
    private final LocalDate passportIssueDate;
    private final String passportDepartment;
    private final String passportDepartmentCode;
    private final Client.MaritalStatus maritalStatus;
    private final String workPlaceTitle;
    private final String workPlaceInn;
    private final String workPlaceFullAddress;
    private final String fullRegistrationAddress;
    private final String phoneNumber;
    private final String email;
    private final Amount income;
    private final Amount spouseIncome;
    private final Client.Category category;
    private final LocalDate birthDate;

    boolean isMaritalStatusCodeNotNull() {
        return Objects.nonNull(this.maritalStatus);
    }

    boolean isFullRegistrationAddressNotNull() {
        return Objects.nonNull(this.fullRegistrationAddress);
    }

    boolean isPhoneNumberNotNull() {
        return Objects.nonNull(this.phoneNumber);
    }

    boolean isEmailNotNull() {
        return Objects.nonNull(this.email);
    }

    boolean isIncomeNotNull() {
        return Objects.nonNull(this.income);
    }

    boolean isSpouseIncomeNotNull() {
        return Objects.nonNull(this.spouseIncome);
    }

    boolean isCategoryNotNull() {
        return Objects.nonNull(this.category);
    }

    boolean isBirthDateNotNull() {
        return Objects.nonNull(this.birthDate);
    }

}
