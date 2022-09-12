package io.github.flexibletech.offering.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "Ограничения условий")
public class ConditionsRestrictionsDto {
    @Schema(description = "Максимальная сумма", example = "500000", accessMode = Schema.AccessMode.READ_ONLY, required = true)
    private BigDecimal maxAmount;
    @Schema(description = "Максимальный период", example = "60", accessMode = Schema.AccessMode.READ_ONLY, required = true)
    private Integer maxPeriod;
}
