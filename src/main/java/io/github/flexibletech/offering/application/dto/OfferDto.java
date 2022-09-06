package io.github.flexibletech.offering.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "Рассчитанное предложение")
public class OfferDto {
    @Schema(description = "Процентная ставка по кредиту", example = "15", accessMode = Schema.AccessMode.READ_ONLY)
    private Double rate;
    @Schema(description = "Итоговая сумма", example = "500000", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal amount;
    @Schema(description = "Итоговый период", example = "60", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer period;
    @Schema(description = "Усредненная сумма ежемесячного платежа", example = "29963.837491", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal averageMonthlyPayment;
    @Schema(description = "Дата первого платежа", example = "2022-01-02", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate firstPaymentDate;
    @Schema(description = "Дата последнего платежа", example = "2022-01-12", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDate lastPaymentDate;
    @Schema(description = "Сумма единовременного платежа по страховке", example = "15000", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal singleInsurancePayment;
    @Schema(description = "Страховая премия", example = "20000", accessMode = Schema.AccessMode.READ_ONLY)
    private BigDecimal insurancePremium;
}
