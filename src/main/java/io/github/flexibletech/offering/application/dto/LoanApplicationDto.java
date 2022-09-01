package io.github.flexibletech.offering.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
}
