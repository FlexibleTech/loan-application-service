package io.github.flexibletech.offering.infrastructure.persistence;

import io.github.flexibletech.offering.infrastructure.config.ApplicationConfig;
import io.github.flexibletech.offering.infrastructure.config.AsyncApiConfig;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("it")
@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {ApplicationConfig.class, AsyncApiConfig.class})
)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class AbstractPersistenceTest {
}