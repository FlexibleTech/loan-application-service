package io.github.flexibletech.offering.application.loanapplication.dto.events;

import io.github.flexibletech.offering.application.IntegrationEvent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "Событие - заявка на кредит отклонена")
public class LoanApplicationDeclined extends IntegrationEvent {
    @Schema(description = "Идентификатор заявки на кредит", example = "LOANAPP2022000001",
            required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private String loanApplicationId;
}
