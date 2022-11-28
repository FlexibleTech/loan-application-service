package io.github.flexibletech.offering.infrastructure.config;

import io.github.flexibletech.offering.web.security.JwtAuthenticationConverter;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@OpenAPIDefinition(info = @Info(title = "Loan Application Service", version = "v1"))
@SecurityScheme(name = "auth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class WebConfig {
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public WebConfig(JwtAuthenticationConverter jwtAuthenticationConverter) {
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeRequests(expressionInterceptUrlRegistry
                        -> expressionInterceptUrlRegistry.anyRequest().authenticated())
                .oauth2ResourceServer(resourceServerConfigurer ->
                        resourceServerConfigurer.jwt(jwtConfigurer ->
                                jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                )
                .build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/actuator/**",
                "/v3/api-docs/**",
                "/springwolf/**",
                "/camunda/**",
                "/swagger-resources/**",
                "/swagger-ui/**");
    }

}
