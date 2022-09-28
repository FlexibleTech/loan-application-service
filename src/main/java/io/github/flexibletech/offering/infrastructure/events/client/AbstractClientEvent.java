package io.github.flexibletech.offering.infrastructure.events.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
public abstract class AbstractClientEvent {
    @NotEmpty(message = "ClientId can't be null or empty")
    protected String clientId;
    @NotEmpty(message = "Client name can't be null or empty")
    protected String name;
    protected String middleName;
    @NotEmpty(message = "Client surname can't be null or empty")
    protected String surName;
    @NotEmpty(message = "Client documents can't be null or empty")
    protected List<@Valid Document> documents;
    @NotEmpty(message = "Client marital status can't be null or empty")
    protected String maritalStatus;
    @NotEmpty(message = "Client addresses status can't be null or empty")
    protected List<@Valid Address> addresses;
    @Valid
    @NotNull(message = "Client work place can't be null")
    protected Organization workPlace;
    @NotEmpty(message = "Client phone number can't be null or empty")
    @Pattern(regexp = "^7\\d{10}$", message = "Invalid phone number format")
    protected String phoneNumber;
    protected String email;
    @NotNull(message = "Client income can't be null")
    @Positive(message = "Client income must be greater than 0")
    protected BigDecimal income;
    @Positive(message = "Client spouse income must be greater than 0")
    protected BigDecimal spouseIncome;
    @NotEmpty(message = "Client category can't be null or empty")
    protected String category;
    @Past(message = "Invalid client birth date")
    @NotNull(message = "Client birthDate can't be null")
    protected LocalDate birthDate;

    public AbstractClientEvent(String clientId, String name, String middleName, String surName, List<@Valid Document> documents, String maritalStatus, List<@Valid Address> addresses, Organization workPlace, String phoneNumber, String email, BigDecimal income, BigDecimal spouseIncome, String category, LocalDate birthDate) {
        this.clientId = clientId;
        this.name = name;
        this.middleName = middleName;
        this.surName = surName;
        this.documents = documents;
        this.maritalStatus = maritalStatus;
        this.addresses = addresses;
        this.workPlace = workPlace;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.income = income;
        this.spouseIncome = spouseIncome;
        this.category = category;
        this.birthDate = birthDate;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address {
        @JsonIgnore
        public static final String REGISTRATION_ADDRESS = "REGISTRATION";
        @JsonIgnore
        public static final String WORK_ADDRESS = "WORK";

        @NotEmpty(message = "Address type can't be null or empty")
        private String type;
        @NotEmpty(message = "Address string value can't be null or empty")
        private String stringValue;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Document {
        @JsonIgnore
        public static final String PASSPORT_DOCUMENT_TYPE = "PASSPORT";

        @NotEmpty(message = "Document type can't be null or empty")
        private String type;
        @NotEmpty(message = "Document series can't be null or empty")
        private String series;
        @NotEmpty(message = "Document number can't be null or empty")
        private String number;
        @NotNull(message = "Client document issueDate can't be null")
        @PastOrPresent(message = "Invalid client passport issue date")
        private LocalDate issueDate;
        @NotEmpty(message = "Client document issuedBy can't be null or empty")
        private String issuedBy;
        @NotEmpty(message = "Client document issuedByCode can't be null or empty")
        private String issuedByCode;
    }

    @Data
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Organization {
        @NotEmpty(message = "Organization title can't be null or empty")
        private String title;
        @NotEmpty(message = "Organization inn can't be null or empty")
        private String inn;
        @Valid
        @NotNull(message = "Organization address can't be null")
        private ClientCreatedEvent.Address address;
    }
}
