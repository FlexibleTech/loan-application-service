package io.github.flexibletech.offering.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(description = "Документ")
public class DocumentDto {
    @Schema(description = "Идентификатор документа в хранилище", implementation = DocumentDto.class, required = true,
            example = "1662982189_LOANAPP22082500001_FORM.pdf", accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    @Schema(description = "Тип документа", implementation = DocumentDto.class, required = true,
            example = "FROM", allowableValues = {"FROM", "CONDITIONS", "INSURANCE"},
            accessMode = Schema.AccessMode.READ_ONLY)
    private String type;
}
