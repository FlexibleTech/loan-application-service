package io.github.flexibletech.offering.infrastructure.events.loanapplication;

import io.github.flexibletech.offering.application.EventPublisher;
import io.github.flexibletech.offering.application.IntegrationEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class EventPublisherImpl implements EventPublisher {
    private static final String EVENT_DESTINATION = "eventDestination";

    private final StreamBridge streamBridge;

    public EventPublisherImpl(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public <T extends IntegrationEvent> void publish(T event) {
        streamBridge.send(
                EVENT_DESTINATION,
                MessageBuilder.withPayload(event)
                        .setHeader(EventHeaders.EVENT_TYPE_HEADER, event.getClass().getSimpleName())
                        .setHeader(EventHeaders.EVENT_VERSION, IntegrationEvent.VERSION)
                        .build());
    }

}
