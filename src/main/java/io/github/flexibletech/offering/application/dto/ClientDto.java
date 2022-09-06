package io.github.flexibletech.offering.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "Данные по клиенту", implementation = ClientDto.class)
public class ClientDto {
    @NotEmpty(message = "Client id can't be null or empty")
    @Schema(description = "Идентификатор клиента", required = true, example = "20056671")
    private String id;
    @NotEmpty(message = "Client name can't be null or empty")
    @Schema(description = "Имя клиента", required = true, example = "Иван")
    private String name;
    @Schema(description = "Отчество клиента", example = "Иванович")
    private String middleName;
    @NotEmpty(message = "Client surname can't be null or empty")
    @Schema(description = "Фамилия клиента", example = "Иванов")
    private String surName;
    @Valid
    @NotNull(message = "Client passport can't be null")
    @Schema(description = "Паспрорт клиента", required = true, implementation = PassportDto.class)
    private PassportDto passport;
    @NotEmpty(message = "Client marital status can't be null or empty")
    @Schema(description = "Семейный статус клиента", required = true, example = "MARRIED", allowableValues = {"MARRIED", "UNMARRIED"})
    private String maritalStatus;
    @Valid
    @NotNull(message = "Client work place can't be null")
    @Schema(description = "Организация - место работы клиента", required = true, implementation = OrganizationDto.class)
    private OrganizationDto workPlace;
    @NotEmpty(message = "Client full registration address status can't be null or empty")
    @Schema(description = "Полный адрес ресгистрации клиента", required = true, example = "125009, город Москва, Тверская ул. 10 с1")
    private String fullRegistrationAddress;
    @NotEmpty(message = "Client phone number can't be null or empty")
    @Pattern(regexp = "^7\\d{10}$", message = "Invalid phone number format")
    @Schema(description = "Номер телефона клиента (в формате 7xxxxxxxxxx)", required = true, example = "79151234457")
    private String phoneNumber;
    @Email(message = "Invalid client email format")
    @Schema(description = "Адрес почты клиента", example = "test@gmail.com")
    private String email;
    @NotNull(message = "Client income can't be null")
    @Positive(message = "Client income must be greater than 0")
    @Schema(description = "Ежемесячный доход клиента в рублях", required = true, example = "75000")
    private BigDecimal income;
    @Positive(message = "Client spouse income must be greater than 0")
    @Schema(description = "Ежемесячный доход супруга(ги) клиента в рублях", example = "75000")
    private BigDecimal spouseIncome;
    @NotEmpty(message = "Client category can't be null or empty")
    @Schema(description = "Категория клиента", example = "STANDARD", required = true, allowableValues = {"STANDARD", "PAYROLL", "PREMIUM"})
    private String category;
    @Past(message = "Invalid client birth date")
    @NotNull(message = "Client birthDate can't be null")
    @Schema(description = "Дата рождения клиента", example = "1999-03-22", required = true)
    private LocalDate birthDate;
}
