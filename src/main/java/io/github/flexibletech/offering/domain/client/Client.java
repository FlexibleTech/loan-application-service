package io.github.flexibletech.offering.domain.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import io.github.flexibletech.offering.domain.Amount;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

@Getter
@Entity
@AllArgsConstructor
@Table(name = "clients")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Client extends AbstractAggregateRoot<Client> {
    @EmbeddedId
    private ClientId id;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @Basic(fetch = FetchType.LAZY)
    private PersonNameDetails personNameDetails;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @Basic(fetch = FetchType.LAZY)
    private Passport passport;
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    @Basic(fetch = FetchType.LAZY)
    private Organization workPlace;
    private String fullRegistrationAddress;
    private String phoneNumber;
    private String email;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "income"))
    private Amount income;
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "spouse_income"))
    private Amount spouseIncome;
    @Enumerated(EnumType.STRING)
    private Category category;
    private LocalDate birthDate;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @JsonIgnore
    public boolean isPayroll() {
        return this.category == Category.PAYROLL;
    }

    @JsonIgnore
    public boolean isPremium() {
        return this.category == Category.PREMIUM;
    }

    @JsonIgnore
    public boolean hasSpouseIncomeAndUnmarried() {
        return (this.maritalStatus == MaritalStatus.UNMARRIED && Objects.nonNull(this.spouseIncome));
    }

    public ClientDetails createClientDetails() {
        return new ClientDetails(
                this.personNameDetails,
                this.passport,
                this.maritalStatus,
                this.workPlace,
                this.fullRegistrationAddress,
                this.phoneNumber,
                this.email,
                this.income,
                this.spouseIncome,
                this.category,
                this.birthDate);
    }

    public void update(ClientDetails clientDetails) {
        this.personNameDetails = clientDetails.getPersonNameDetails();
        this.passport = clientDetails.getPassport();
        this.maritalStatus = clientDetails.getMaritalStatus();
        this.workPlace = clientDetails.getWorkPlace();
        this.fullRegistrationAddress = clientDetails.getFullRegistrationAddress();
        this.phoneNumber = clientDetails.getPhoneNumber();
        this.email = clientDetails.getEmail();
        this.income = clientDetails.getIncome();
        this.spouseIncome = clientDetails.getSpouseIncome();
        this.category = clientDetails.getCategory();
        this.birthDate = clientDetails.getBirthDate();
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
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Unknown marital status %s", value)));
        }
    }

    @RequiredArgsConstructor
    public enum Category {
        STANDARD("STANDARD"),
        PAYROLL("PAYROLL"),
        PREMIUM("PREMIUM");

        private final String value;

        public static Category fromValue(String value) {
            return Arrays.stream(values())
                    .filter(category -> category == Category.valueOf(value))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Unknown category %s", value)));
        }
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public class Builder {

        public Builder withId(String id) {
            Client.this.id = new ClientId(id);
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
