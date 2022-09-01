package io.github.flexibletech.offering.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "Данные по организации-месту работы клиента", implementation = OrganizationDto.class)
public class OrganizationDto {
    @NotEmpty(message = "Client work place title can't be null or empty")
    @Schema(description = "Наименование организации-места работы клиента", required = true, example = "ООО \"В Контакте\"")
    private String title;
    @NotEmpty(message = "Client work place inn can't be null or empty")
    @Schema(description = "ИНН организации-места работы клиента", required = true, example = "7842349892")
    private String inn;
    @NotEmpty(message = "Client work place full address can't be null or empty")
    @Schema(description = "Полныц адрес организации-места работы клиента", required = true, example = "191024, ГОРОД САНКТ-ПЕТЕРБУРГ, УЛИЦА ХЕРСОНСКАЯ, ДОМ 12-14, ЛИТЕР А, ПОМЕЩЕНИЕ 1Н")
    private String fullAddress;
}
