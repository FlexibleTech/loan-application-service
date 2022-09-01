package io.github.flexibletech.offering.domain.risk;

import io.github.flexibletech.offering.domain.LoanApplication;

public interface RiskService {

    void requestRiskDecision(LoanApplication loanApplication);

}
