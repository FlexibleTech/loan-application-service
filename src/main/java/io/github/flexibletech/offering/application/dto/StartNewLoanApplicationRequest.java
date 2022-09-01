package io.github.flexibletech.offering.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Запрос на запуск кредитной заявки", implementation = StartNewLoanApplicationRequest.class)
public class StartNewLoanApplicationRequest {
    @Valid
    @NotNull(message = "Client can't be null")
    @Schema(description = "Данные по клиенту", required = true, implementation = ClientDto.class)
    private ClientDto client;
    @Valid
    @NotNull(message = "Conditions can't be null")
    @Schema(description = "Выбранные условия кредита", required = true, implementation = ConditionsDto.class)
    private ConditionsDto conditions;
}
