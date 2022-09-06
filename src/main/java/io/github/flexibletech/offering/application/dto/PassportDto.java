package io.github.flexibletech.offering.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "Паспорт клиента")
public class PassportDto {
    @NotEmpty(message = "Client passport series can't be null or empty")
    @Pattern(regexp = "[\\d]{4}", message = "Invalid passport series format")
    @Schema(description = "Серия", required = true, example = "4490")
    private String series;
    @NotEmpty(message = "Client passport number can't be null or empty")
    @Schema(description = "Номер", required = true, example = "180123")
    @Pattern(regexp = "[\\d]{6}", message = "Invalid passport number format")
    private String number;
    @NotNull(message = "Client passport issueDate can't be null")
    @PastOrPresent(message = "Invalid client passport issue date")
    @Schema(description = "Дата выдачи паспорта", required = true, example = "2000-01-11")
    private LocalDate issueDate;
    @NotEmpty(message = "Client passport department can't be null or empty")
    @Schema(description = "Кем выдан паспорт", required = true, example = "ОВД КИТАЙ-ГОРОД 1 РУВД ЦАО Г. МОСКВЫ")
    private String department;
    @NotEmpty(message = "Client passport department code can't be null or empty")
    @Schema(description = "Код подразделения", required = true, example = "772-001")
    private String departmentCode;
}
