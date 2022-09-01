package io.github.flexibletech.offering.infrastructure.messaging.risk.response;

import io.github.flexibletech.offering.application.LoanApplicationService;
import io.github.flexibletech.offering.domain.risk.RiskDecision;
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
        loanApplicationService.addRiskDecisionToLoanApplication(riskResponse.getApplicationId(), toRiskDecision(riskResponse));
    }

    private RiskDecision toRiskDecision(RiskResponse response) {
        return RiskDecision.newRiskDecision(
                response.getId(),
                response.getStatus(),
                response.getPayrollSalary(),
                response.getPayrollLastSalaryDate(),
                response.getMaxConditionsAmount(),
                response.getMaxConditionsPeriod());
    }

}
