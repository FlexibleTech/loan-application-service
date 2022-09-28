package io.github.flexibletech.offering.application.loanapplication.dto.events;

import io.github.flexibletech.offering.application.IntegrationEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoanApplicationCreated extends IntegrationEvent {
    private String loanApplicationId;
    private String clientId;
    private BigDecimal amount;
    private Integer period;
    private Boolean insurance;
    private String loanProgram;
}
