package io.github.flexibletech.offering.application.loanapplication.dto.events;

import io.github.flexibletech.offering.application.IntegrationEvent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Событие - заявка на кредит создана")
public class LoanApplicationCreated extends IntegrationEvent {
    @Schema(description = "Идентификатор заявки на кредит", example = "LOANAPP2022000001",
            required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String loanApplicationId;
    @Schema(description = "Идентификатор клиента", example = "20056671",
            required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String clientId;
    @Schema(description = "Запрошенная сумма кредита", example = "100000",
            required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal amount;
    @Schema(description = "Запрошенный период кредита в месяцах", required = true,
            example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer period;
    @Schema(description = "Страховка", example = "true", required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private Boolean insurance;
    @Schema(description = "Программа кредитования", example = "COMMON", required = true,
            allowableValues = {"COMMON", "PREAPPROVED", "PAYROLL_CLIENT", "SPECIAL"},
            accessMode = Schema.AccessMode.READ_ONLY)
    private String loanProgram;
}
