package io.github.flexibletech.offering.infrastructure.messaging.risk.response;

import io.github.flexibletech.offering.application.loanapplication.LoanApplicationService;
import io.github.flexibletech.offering.domain.loanapplication.risk.RiskDecision;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class RiskResponseListener implements Consumer<RiskResponse> {
    private final LoanApplicationService loanApplicationService;

    public RiskResponseListener(LoanApplicationService loanApplicationService) {
        this.loanApplicationService = loanApplicationService;
    }

    @Override
    public void accept(RiskResponse riskResponse) {
        loanApplicationService.acceptRiskDecisionToLoanApplication(riskResponse.getApplicationId(),
                RiskDecision.newRiskDecision(
                        riskResponse.getId(),
                        riskResponse.getStatus(),
                        riskResponse.getPayrollSalary(),
                        riskResponse.getPayrollLastSalaryDate(),
                        riskResponse.getMaxConditionsAmount(),
                        riskResponse.getMaxConditionsPeriod()));
    }


}
