package io.github.flexibletech.offering.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Кредитная заявка", implementation = LoanApplicationDto.class)
public class LoanApplicationDto extends RepresentationModel<LoanApplicationDto> {
    @Schema(description = "Идентификатор кредитной заявки", implementation = LoanApplicationDto.class,
            example = "LOANAPP2022000001", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    @Schema(description = "Статус кредитной заявки", example = "NEW", accessMode = Schema.AccessMode.READ_ONLY,
            allowableValues = {"NEW", "PENDING_ISSUANCE", "APPROVED", "DECLINED", "CANCELED", "COMPLETED"})
    private String status;
    @Schema(description = "Рассчитанное предложение", implementation = OfferDto.class, example = "LOANAPP2022000001",
            accessMode = Schema.AccessMode.READ_ONLY)
    private OfferDto offer;
    @Schema(description = "Ограничения условий", implementation = ConditionsRestrictionsDto.class,
            accessMode = Schema.AccessMode.READ_ONLY)
    private ConditionsRestrictionsDto conditionsRestrictions;
    @Schema(description = "Пакет документов (список идентификаторов)", example = "e5dde60e-7225-49f8-a9eb-a31e2acd239f",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Set<String> documentPackage;
}
