package io.github.flexibletech.offering.infrastructure.persistence;

import io.github.flexibletech.offering.domain.LoanApplicationRepository;
import io.github.flexibletech.offering.domain.factory.TestLoanApplicationFactory;
import io.github.flexibletech.offering.infrastructure.ApplicationConfig;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

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
    public void shouldFindLoanApplicationById() {
        var loanApplication = loanApplicationRepository.save(TestLoanApplicationFactory.newLoanApplicationWithoutId());

        var foundLoanApplication = loanApplicationRepository.findById(loanApplication.getId()).orElse(null);

        Assertions.assertNotNull(foundLoanApplication);
        Assertions.assertTrue(StringUtils.isNoneBlank(foundLoanApplication.getId()));
        Assertions.assertEquals(foundLoanApplication.getCreatedAt().toLocalDate(), LocalDate.now());
        Assertions.assertEquals(foundLoanApplication.getUpdatedAt().toLocalDate(), LocalDate.now());
    }
}
