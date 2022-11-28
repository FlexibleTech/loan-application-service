package io.github.flexibletech.offering.application;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class IntegrationEvent {
    @JsonIgnore
    public static final Integer VERSION = 1;
    @Schema(description = "Время события", example = "2022-11-25T13:47:37.817450",
            required = true, accessMode = Schema.AccessMode.READ_ONLY)
    private final LocalDateTime occurredOn = LocalDateTime.now();
}
