package io.github.flexibletech.offering.application.loanapplication.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Запрос на выбор условий кредита")
public class ChoseConditionsRequest {
    @Positive(message = "Conditions amount must be greater than 0")
    @NotNull(message = "Conditions amount can't be null")
    @Schema(description = "Сумма кредита в рублях", required = true, example = "500000")
    private BigDecimal amount;
    @Positive(message = "Conditions period must be greater than 0")
    @NotNull(message = "Conditions period can't be null")
    @Schema(description = "Период кредита в месяцах", required = true, example = "20")
    private Integer period;
    @NotNull(message = "Conditions insurance can't be null")
    @Schema(description = "Страховка кредита", required = true)
    private Boolean insurance;
}
