package io.github.flexibletech.offering.domain.loanapplication.risk;

import io.github.flexibletech.offering.domain.client.Client;
import io.github.flexibletech.offering.domain.loanapplication.LoanApplication;

public interface RiskService {

    void requestRiskDecision(LoanApplication loanApplication, Client client);

}
