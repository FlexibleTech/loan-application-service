package io.github.flexibletech.offering.application.dto.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class IntegrationEvent {
    @JsonIgnore
    public static final Integer VERSION = 1;
    private final LocalDateTime occurredOn = LocalDateTime.now();
}
