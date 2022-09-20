package io.github.flexibletech.offering.domain.loanapplication.issuance;

import io.github.flexibletech.offering.domain.loanapplication.LoanApplication;

public interface IssuanceService {

    void startIssuance(LoanApplication loanApplication);

}
