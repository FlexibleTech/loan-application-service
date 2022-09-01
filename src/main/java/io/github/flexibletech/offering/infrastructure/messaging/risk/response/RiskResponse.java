package io.github.flexibletech.offering.infrastructure.messaging.risk.response;

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
public class RiskResponse {
    private String applicationId;
    private String id;
    private BigDecimal payrollSalary;
    private LocalDate payrollLastSalaryDate;
    private BigDecimal maxConditionsAmount;
    private Integer maxConditionsPeriod;
    private String status;
}
