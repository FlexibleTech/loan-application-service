package io.github.flexibletech.offering.infrastructure;

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

@Configuration
@EnableCaching
@EnableTransactionManagement
@EnableJpaAuditing(auditorAwareRef = "auditProvider")
@EnableFeignClients("io.github.flexibletech.offering.infrastructure.rest")
public class ApplicationConfig {

    @Bean
    public AuditorAware<String> auditProvider(@Value("${spring.application.name}") String application) {
        return () -> Optional.of(application);
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
