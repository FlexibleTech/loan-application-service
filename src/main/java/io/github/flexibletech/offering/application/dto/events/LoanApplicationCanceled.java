package io.github.flexibletech.offering.application.dto.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoanApplicationCanceled extends IntegrationEvent {
    private String loanApplicationId;
}
