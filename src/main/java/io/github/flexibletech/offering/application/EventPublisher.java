package io.github.flexibletech.offering.application;

public interface EventPublisher {

    <T extends IntegrationEvent> void publish(T event);

}
