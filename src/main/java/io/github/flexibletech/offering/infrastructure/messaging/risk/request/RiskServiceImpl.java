package io.github.flexibletech.offering.infrastructure.messaging.risk.request;

import io.github.flexibletech.offering.domain.Amount;
import io.github.flexibletech.offering.domain.LoanApplication;
import io.github.flexibletech.offering.domain.client.Client;
import io.github.flexibletech.offering.domain.client.Organization;
import io.github.flexibletech.offering.domain.client.Passport;
import io.github.flexibletech.offering.domain.risk.RiskService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class RiskServiceImpl implements RiskService {
    private final StreamBridge streamBridge;

    private static final String RISK_REQUEST_DESTINATION = "riskDestination";

    public RiskServiceImpl(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public void requestRiskDecision(LoanApplication loanApplication) {
        streamBridge.send(RISK_REQUEST_DESTINATION, MessageBuilder.withPayload(toRiskRequest(loanApplication)).build());
    }

    private RiskRequest toRiskRequest(LoanApplication loanApplication) {
        var client = loanApplication.getClient();
        var conditions = loanApplication.getConditions();

        var category = RiskRequest.Client.Category.valueOf(client.getCategory().name()).getCode();
        var maritalStatus = RiskRequest.Client.MaritalStatus.valueOf(client.getMaritalStatus().name()).getCode();

        return new RiskRequest(
                loanApplication.getId(),
                toClient(client, category, maritalStatus),
                conditions.getAmount().getValue(),
                conditions.getPeriod());
    }

    private RiskRequest.Organization toOrganization(Organization organization) {
        return new RiskRequest.Organization(
                organization.getTitle(),
                organization.getInn(),
                organization.getFullAddress());
    }

    private RiskRequest.Client toClient(Client client, int category, int maritalStatus) {
        var personNameDetails = client.getPersonNameDetails();
        return new RiskRequest.Client(
                client.getId(),
                personNameDetails.getName(),
                personNameDetails.getMiddleName(),
                personNameDetails.getSurName(),
                toPassport(client.getPassport()),
                maritalStatus,
                toOrganization(client.getWorkPlace()),
                client.getFullRegistrationAddress(),
                client.getPhoneNumber(),
                client.getEmail(),
                client.getIncome().getValue(),
                getAmountValue(client.getSpouseIncome()),
                category,
                client.getBirthDate());
    }

    private RiskRequest.Passport toPassport(Passport passport) {
        return new RiskRequest.Passport(
                passport.getSeries(),
                passport.getNumber(),
                passport.getIssueDate(),
                passport.getDepartment(),
                passport.getDepartmentCode());
    }

    private BigDecimal getAmountValue(Amount amount) {
        return Optional.ofNullable(amount)
                .map(Amount::getValue)
                .orElse(null);
    }

}
