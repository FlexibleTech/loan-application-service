package io.github.flexibletech.offering.application.loanapplication.dto.events;

import io.github.flexibletech.offering.application.IntegrationEvent;
import io.github.flexibletech.offering.application.loanapplication.dto.OfferDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Событие - предложение по заявке на кредит рассчитано")
public class LoanApplicationOfferCalculated extends IntegrationEvent {
    @Schema(description = "Идентификатор заявки на кредит", example = "LOANAPP2022000001",
            required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String loanApplicationId;
    @Schema(description = "Рассчитанное предложение", required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private OfferDto offer;
}
