package io.github.flexibletech.offering.infrastructure.persistence;

import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.domain.loanapplication.LoanApplicationId;
import io.github.flexibletech.offering.domain.loanapplication.LoanApplicationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

public class LoanApplicationRepositoryIT extends AbstractPersistenceTest {
    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Test
    @Transactional
    @Sql("/sql/insert_loan_application.sql")
    public void shouldFindLoanApplicationById() {
        var loanApplication = loanApplicationRepository.findById(LoanApplicationId.fromValue(TestValues.LOAN_APPLICATION_ID))
                .orElse(null);

        Assertions.assertNotNull(loanApplication);
    }
}
