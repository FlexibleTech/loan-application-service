package io.github.flexibletech.offering.domain.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import io.github.flexibletech.offering.domain.AggregateRoot;
import io.github.flexibletech.offering.domain.Amount;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

@Getter
@Entity
@AllArgsConstructor
@Table(name = "clients")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Client extends AggregateRoot {
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

    public void update(ClientDetails clientDetails) {
        this.passport = this.passport.update(clientDetails.getPassportSeries(),
                clientDetails.getPassportNumber(),
                clientDetails.getPassportIssueDate(),
                clientDetails.getPassportDepartment(),
                clientDetails.getPassportDepartmentCode());
        this.workPlace = this.workPlace.update(clientDetails.getWorkPlaceTitle(),
                clientDetails.getWorkPlaceInn(),
                clientDetails.getWorkPlaceFullAddress());
        this.personNameDetails = this.personNameDetails.update(clientDetails.getName(),
                clientDetails.getMiddleName(),
                clientDetails.getSurName());

        if (clientDetails.isMaritalStatusCodeNotNull()) this.maritalStatus = clientDetails.getMaritalStatus();
        if (clientDetails.isFullRegistrationAddressNotNull())
            this.fullRegistrationAddress = clientDetails.getFullRegistrationAddress();
        if (clientDetails.isPhoneNumberNotNull()) this.phoneNumber = clientDetails.getPhoneNumber();
        if (clientDetails.isEmailNotNull()) this.email = clientDetails.getEmail();
        if (clientDetails.isIncomeNotNull()) this.income = clientDetails.getIncome();
        if (clientDetails.isSpouseIncomeNotNull()) this.spouseIncome = clientDetails.getSpouseIncome();
        if (clientDetails.isCategoryNotNull()) this.category = clientDetails.getCategory();
        if (clientDetails.isBirthDateNotNull()) this.birthDate = clientDetails.getBirthDate();
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

        public Builder withId(ClientId clientId) {
            Client.this.id = clientId;
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

        public Builder withMaritalStatus(MaritalStatus maritalStatus) {
            Client.this.maritalStatus = maritalStatus;
            return this;
        }

        public Builder withCategory(Category category) {
            Client.this.category = category;
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

        public Builder withIncome(Amount income) {
            Client.this.income = income;
            return this;
        }

        public Builder withFullRegistrationAddress(String fullRegistrationAddress) {
            Client.this.fullRegistrationAddress = fullRegistrationAddress;
            return this;
        }

        public Builder withSpouseIncome(Amount spouseIncome) {
            Client.this.spouseIncome = spouseIncome;
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
