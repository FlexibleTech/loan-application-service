package io.github.flexibletech.offering.infrastructure.messaging.risk.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RiskRequest {
    private String applicationId;
    private Client client;
    private BigDecimal amount;
    private Integer period;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Client {
        private String id;
        private String name;
        private String middleName;
        private String surName;
        private Passport passport;
        private Integer maritalStatus;
        private Organization workPlace;
        private String fullRegistrationAddress;
        private String phoneNumber;
        private String email;
        private BigDecimal income;
        private BigDecimal spouseIncome;
        private Integer category;
        private LocalDate birthDate;

        @RequiredArgsConstructor
        public enum MaritalStatus {
            MARRIED("MARRIED", 1),
            UNMARRIED("UNMARRIED", 2);

            private final String value;
            @Getter
            private final Integer code;
        }

        @RequiredArgsConstructor
        public enum Category {
            STANDARD("STANDARD", 60),
            PAYROLL("PAYROLL", 89),
            PREMIUM("PREMIUM", 77);

            private final String value;
            @Getter
            private final Integer code;
        }
    }

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Passport {
        private String series;
        private String number;
        private LocalDate issueDate;
        private String department;
        private String departmentCode;
    }

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Organization {
        private String title;
        private String inn;
        private String fullAddress;
    }
}
