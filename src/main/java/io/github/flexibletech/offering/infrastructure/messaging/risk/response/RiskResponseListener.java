package io.github.flexibletech.offering.infrastructure.messaging.risk.response;

import io.github.flexibletech.offering.application.LoanApplicationService;
import io.github.flexibletech.offering.application.dto.RiskDecisionDto;
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
        loanApplicationService.acceptRiskDecisionToLoanApplication(riskResponse.getApplicationId(), toRiskDecisionDto(riskResponse));
    }

    private RiskDecisionDto toRiskDecisionDto(RiskResponse response) {
        return new RiskDecisionDto(
                response.getId(),
                response.getStatus(),
                response.getPayrollSalary(),
                response.getPayrollLastSalaryDate(),
                response.getMaxConditionsAmount(),
                response.getMaxConditionsPeriod());
    }

}
