package io.github.flexibletech.offering.infrastructure.config;

import com.asyncapi.v2.model.info.Info;
import com.asyncapi.v2.model.server.Server;
import io.github.flexibletech.offering.application.IntegrationEvent;
import io.github.flexibletech.offering.application.loanapplication.dto.events.LoanApplicationCanceled;
import io.github.flexibletech.offering.application.loanapplication.dto.events.LoanApplicationCompleted;
import io.github.flexibletech.offering.application.loanapplication.dto.events.LoanApplicationCreated;
import io.github.flexibletech.offering.application.loanapplication.dto.events.LoanApplicationDeclined;
import io.github.flexibletech.offering.application.loanapplication.dto.events.LoanApplicationOfferCalculated;
import io.github.flexibletech.offering.infrastructure.events.loanapplication.EventHeaders;
import io.github.stavshamir.springwolf.asyncapi.types.KafkaProducerData;
import io.github.stavshamir.springwolf.asyncapi.types.channel.operation.message.header.AsyncHeaders;
import io.github.stavshamir.springwolf.asyncapi.types.channel.operation.message.header.AsyncHeadersForCloudEventsBuilder;
import io.github.stavshamir.springwolf.configuration.AsyncApiDocket;
import io.github.stavshamir.springwolf.configuration.EnableAsyncApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableAsyncApi
public class AsyncApiConfig {
    @Value("${spring.cloud.stream.bindings.eventDestination.destination}")
    private String topicName;

    @Bean
    public AsyncApiDocket asyncApiDocket(@Value("${spring.cloud.stream.kafka.binder.brokers}") String servers) {
        var info = Info.builder()
                .version("1.0.0")
                .title("Loan Application Service")
                .build();

        var kafkaServer = Server.builder()
                .protocol("kafka")
                .url(servers)
                .build();

        return AsyncApiDocket.builder()
                .basePackage("io.github.flexibletech.offering.infrastructure.events")
                .info(info)
                .producers(List.of(loanApplicationCreatedEventProducer(),
                        loanApplicationCanceledEventProducer(),
                        loanApplicationCompletedEventProducer(),
                        loanApplicationDeclinedProducer(),
                        loanApplicationOfferCalculatedProducer())
                )
                .server("kafka", kafkaServer)
                .build();
    }

    private KafkaProducerData loanApplicationCreatedEventProducer() {
        return KafkaProducerData.kafkaProducerDataBuilder()
                .description("Подписаться на событие о создании заявки на кредит.")
                .topicName(topicName)
                .headers(eventsHeaders(LoanApplicationCreated.class.getSimpleName()))
                .payloadType(LoanApplicationCreated.class)
                .build();
    }

    private KafkaProducerData loanApplicationCanceledEventProducer() {
        return KafkaProducerData.kafkaProducerDataBuilder()
                .description("Подписаться на событие об отмене заявки на кредит.")
                .topicName(topicName)
                .headers(eventsHeaders(LoanApplicationCanceled.class.getSimpleName()))
                .payloadType(LoanApplicationCanceled.class)
                .build();
    }

    private KafkaProducerData loanApplicationCompletedEventProducer() {
        return KafkaProducerData.kafkaProducerDataBuilder()
                .description("Подписаться на событие о завершении заявки на кредит.")
                .topicName(topicName)
                .headers(eventsHeaders(LoanApplicationCompleted.class.getSimpleName()))
                .payloadType(LoanApplicationCompleted.class)
                .build();
    }

    private KafkaProducerData loanApplicationDeclinedProducer() {
        return KafkaProducerData.kafkaProducerDataBuilder()
                .description("Подписаться на событие об отклонении заявки на кредит.")
                .topicName(topicName)
                .headers(eventsHeaders(LoanApplicationDeclined.class.getSimpleName()))
                .payloadType(LoanApplicationDeclined.class)
                .build();
    }

    private KafkaProducerData loanApplicationOfferCalculatedProducer() {
        return KafkaProducerData.kafkaProducerDataBuilder()
                .description("Подписаться на событие об успешном расчете предложения по заявке на кредит.")
                .topicName(topicName)
                .headers(eventsHeaders(LoanApplicationOfferCalculated.class.getSimpleName()))
                .payloadType(LoanApplicationOfferCalculated.class)
                .build();
    }

    private static AsyncHeaders eventsHeaders(String typeName) {
        return new AsyncHeadersForCloudEventsBuilder("EventsHeaders")
                .withExtension(EventHeaders.EVENT_TYPE_HEADER,
                        List.of(LoanApplicationCreated.class.getSimpleName(),
                                LoanApplicationCanceled.class.getSimpleName(),
                                LoanApplicationCompleted.class.getSimpleName(),
                                LoanApplicationDeclined.class.getSimpleName(),
                                LoanApplicationOfferCalculated.class.getSimpleName()),
                        typeName,
                        "Тип события")
                .withExtension(EventHeaders.EVENT_VERSION,
                        List.of(IntegrationEvent.VERSION.toString()),
                        IntegrationEvent.VERSION.toString(),
                        "Версия события")
                .build();
    }
}
