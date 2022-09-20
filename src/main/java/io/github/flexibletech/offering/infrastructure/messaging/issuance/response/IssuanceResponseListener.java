package io.github.flexibletech.offering.infrastructure.messaging.issuance.response;

import io.github.flexibletech.offering.application.loanapplication.LoanApplicationService;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class IssuanceResponseListener implements Consumer<IssuanceResponse> {
    private final LoanApplicationService loanApplicationService;

    public IssuanceResponseListener(LoanApplicationService loanApplicationService) {
        this.loanApplicationService = loanApplicationService;
    }

    @Override
    public void accept(IssuanceResponse issuanceResponse) {
        loanApplicationService.completeLoanApplication(issuanceResponse.getApplicationId(), issuanceResponse.getIssuanceId());
    }

}
