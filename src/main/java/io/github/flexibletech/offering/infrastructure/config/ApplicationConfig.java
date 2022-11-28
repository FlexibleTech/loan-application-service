package io.github.flexibletech.offering.infrastructure.config;

import com.asyncapi.v2.model.info.Info;
import com.asyncapi.v2.model.server.Server;
import io.github.stavshamir.springwolf.asyncapi.types.KafkaProducerData;
import io.github.stavshamir.springwolf.configuration.AsyncApiDocket;
import io.github.stavshamir.springwolf.configuration.EnableAsyncApi;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.minio.MinioClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Optional;
import java.util.function.Consumer;

@Configuration
@EnableCaching
@EnableAsyncApi
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditProvider")
@EnableFeignClients("io.github.flexibletech.offering.infrastructure.rest")
public class ApplicationConfig {

    @Bean
    public JvmThreadMetrics threadMetrics() {
        return new JvmThreadMetrics();
    }

    @Bean
    public MeterRegistry meterRegistry() {
        return new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);
    }

    @Bean
    public AuditorAware<String> auditProvider(@Value("${spring.application.name}") String application) {
        return () -> Optional.of(application);
    }

    @Bean
    public Consumer<String> forgetSubscriber() {
        return s -> {
        };
    }

    @Bean
    public ModelMapper mapper() {
        var mapper = new ModelMapper();
        mapper.getConfiguration().setAmbiguityIgnored(true);

        return mapper;
    }

    @Bean
    public MinioClient minioClient(@Value("${minio.url}") String url,
                                   @Value("${minio.access-key}") String accessKey,
                                   @Value("${minio.secret-key}") String secretKey) {
        return MinioClient.builder()
                .credentials(accessKey, secretKey)
                .endpoint(url)
                .build();
    }

}
