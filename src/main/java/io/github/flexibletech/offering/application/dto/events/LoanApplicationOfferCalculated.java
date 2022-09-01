package io.github.flexibletech.offering.application.dto.events;

import io.github.flexibletech.offering.application.dto.OfferDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LoanApplicationOfferCalculated extends IntegrationEvent {
    private String loanApplicationId;
    private OfferDto offer;
}
