package io.github.flexibletech.offering.application.client;

import io.github.flexibletech.offering.application.IntegrationEvent;
import io.github.flexibletech.offering.application.loanapplication.dto.OrganizationDto;
import io.github.flexibletech.offering.application.loanapplication.dto.PassportDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ClientUpdated extends IntegrationEvent {
    private String id;
    private String name;
    private String middleName;
    private String surName;
    private PassportDto passport;
    private String maritalStatus;
    private OrganizationDto workPlace;
    private String fullRegistrationAddress;
    private String phoneNumber;
    private String email;
    private BigDecimal income;
    private BigDecimal spouseIncome;
    private String category;
    private LocalDate birthDate;
}
