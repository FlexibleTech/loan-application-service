package io.github.flexibletech.offering.application;

import io.github.flexibletech.offering.application.dto.events.IntegrationEvent;

public interface EventPublisher {

    <T extends IntegrationEvent> void publish(T event);

}
