package io.github.flexibletech.offering.infrastructure.events;

import io.github.flexibletech.offering.application.EventPublisher;
import io.github.flexibletech.offering.application.IntegrationEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class EventPublisherImpl implements EventPublisher {
    private static final String EVENT_DESTINATION = "eventDestination";
    private static final String EVENT_VERSION = "VERSION";
    private static final String EVENT_TYPE_HEADER = "TYPE";

    private final StreamBridge streamBridge;

    public EventPublisherImpl(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public <T extends IntegrationEvent> void publish(T event) {
        streamBridge.send(
                EVENT_DESTINATION,
                MessageBuilder.withPayload(event)
                        .setHeader(EVENT_TYPE_HEADER, event.getClass().getSimpleName())
                        .setHeader(EVENT_VERSION, IntegrationEvent.VERSION)
                        .build());
    }

}
