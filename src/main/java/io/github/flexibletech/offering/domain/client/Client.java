package io.github.flexibletech.offering.domain.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.flexibletech.offering.domain.Amount;
import io.github.flexibletech.offering.domain.common.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Client implements Entity {
    private String id;
    private PersonNameDetails personNameDetails;
    private Passport passport;
    private MaritalStatus maritalStatus;
    private Organization workPlace;
    private String fullRegistrationAddress;
    private String phoneNumber;
    private String email;
    private Amount income;
    private Amount spouseIncome;
    private Category category;
    private LocalDate birthDate;

    @JsonIgnore
    public boolean isMarried() {
        return this.maritalStatus == MaritalStatus.MARRIED;
    }

    @JsonIgnore
    public boolean isEmployee() {
        return this.category == Category.EMPLOYEE;
    }

    @JsonIgnore
    public boolean isPremium() {
        return this.category == Category.PREMIUM;
    }

    public static Builder newBuilder() {
        return new Client().new Builder();
    }

    @RequiredArgsConstructor
    public enum MaritalStatus {
        MARRIED("MARRIED"),
        UNMARRIED("UNMARRIED");

        private final String value;

        public static MaritalStatus fromValue(String value) {
            return Arrays.stream(values())
                    .filter(maritalStatus -> maritalStatus == MaritalStatus.valueOf(value))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Unknown marital status %s ", value)));
        }
    }

    @RequiredArgsConstructor
    public enum Category {
        STANDARD("STANDARD"),
        EMPLOYEE("EMPLOYEE"),
        PREMIUM("PREMIUM");

        private final String value;

        public static Category fromValue(String value) {
            return Arrays.stream(values())
                    .filter(category -> category == Category.valueOf(value))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Unknown category %s ", value)));
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class Builder {

        public Builder withId(String id) {
            Client.this.id = id;
            return this;
        }

        public Builder withPersonNameDetails(String name, String middleName, String surName) {
            Client.this.personNameDetails = new PersonNameDetails(name, middleName, surName);
            return this;
        }

        public Builder withPassport(String series,
                                    String number,
                                    LocalDate issueDate,
                                    String department,
                                    String departmentCode) {
            Client.this.passport = new Passport(series, number, issueDate, department, departmentCode);
            return this;
        }

        public Builder withWorkplace(String title,
                                     String inn,
                                     String fullAddress) {
            Client.this.workPlace = new Organization(title, inn, fullAddress);
            return this;
        }

        public Builder withMaritalStatus(String value) {
            Client.this.maritalStatus = MaritalStatus.fromValue(value);
            return this;
        }

        public Builder withCategory(String value) {
            Client.this.category = Category.fromValue(value);
            return this;
        }

        public Builder withPhoneNumber(String phoneNumber) {
            Client.this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder withEmail(String email) {
            Client.this.email = email;
            return this;
        }

        public Builder withIncome(BigDecimal income) {
            Client.this.income = Amount.fromValue(income);
            return this;
        }

        public Builder withFullRegistrationAddress(String fullRegistrationAddress) {
            Client.this.fullRegistrationAddress = fullRegistrationAddress;
            return this;
        }

        public Builder withSpouseIncome(BigDecimal spouseIncome) {
            Client.this.spouseIncome = Amount.fromValue(spouseIncome);
            return this;
        }

        public Builder withBirthDate(LocalDate birthDate) {
            Client.this.birthDate = birthDate;
            return this;
        }

        public Client build() {
            return Client.this;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
