package io.github.flexibletech.offering.infrastructure;

import io.github.flexibletech.offering.application.dto.LoanApplicationDto;
import io.github.flexibletech.offering.domain.LoanApplication;
import io.github.flexibletech.offering.domain.document.Document;
import io.minio.MinioClient;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
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
import java.util.Set;
import java.util.stream.Collectors;

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

        Converter<Set<Document>, Set<String>> converter = new AbstractConverter<>() {
            @Override
            protected Set<String> convert(Set<Document> source) {
                return source.stream()
                        .map(Document::getId)
                        .collect(Collectors.toSet());
            }
        };

        mapper.typeMap(LoanApplication.class, LoanApplicationDto.class)
                .addMappings(m -> m.using(converter).map(LoanApplication::getDocumentPackage,
                        LoanApplicationDto::setDocumentPackage));

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
