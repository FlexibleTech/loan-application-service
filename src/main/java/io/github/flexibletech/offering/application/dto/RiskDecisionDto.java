package io.github.flexibletech.offering.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RiskDecisionDto {
    private String id;
    private String status;
    private BigDecimal salary;
    private LocalDate lastSalaryDate;
    private BigDecimal maxAmount;
    private Integer maxPeriod;
}
