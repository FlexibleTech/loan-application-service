package io.github.flexibletech.offering.application.dto.events;

import io.github.flexibletech.offering.application.dto.ConditionsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoanApplicationCreated extends IntegrationEvent {
    private String loanApplicationId;
    private String clientId;
    private ConditionsDto conditions;
    private String loanProgram;
}
