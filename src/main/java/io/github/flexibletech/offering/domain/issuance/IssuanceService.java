package io.github.flexibletech.offering.domain.issuance;

import io.github.flexibletech.offering.domain.LoanApplication;

public interface IssuanceService {

    void startIssuance(LoanApplication loanApplication);

}
