package io.github.flexibletech.offering.infrastructure.persistence;

import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.domain.LoanApplicationRepository;
import io.github.flexibletech.offering.infrastructure.config.ApplicationConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("it")
@DataJpaTest(includeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = ApplicationConfig.class))
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LoanApplicationRepositoryIT {
    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Test
    @Transactional
    @Sql("/sql/insert_loan_application.sql")
    public void shouldFindLoanApplicationById() {
        var loanApplication = loanApplicationRepository.findById(TestValues.LOAN_APPLICATION_ID).orElse(null);

        Assertions.assertNotNull(loanApplication);
    }
}
