package io.github.flexibletech.offering.infrastructure.messaging.issuance.request;

import io.github.flexibletech.offering.domain.loanapplication.LoanApplication;
import io.github.flexibletech.offering.domain.loanapplication.issuance.IssuanceService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
public class IssuanceServiceImpl implements IssuanceService {
    private final StreamBridge streamBridge;

    private static final String ISSUANCE_REQUEST_DESTINATION = "issuanceDestination";

    public IssuanceServiceImpl(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void startIssuance(LoanApplication loanApplication) {
        var conditions = loanApplication.getConditions();

        streamBridge.send(
                ISSUANCE_REQUEST_DESTINATION,
                MessageBuilder.withPayload(
                                new StartIssuanceRequest(
                                        loanApplication.getId().toString(),
                                        conditions.getAmount().getValue(),
                                        conditions.getPeriod(),
                                        loanApplication.getClientId().toString()))
                        .build());
    }

}
