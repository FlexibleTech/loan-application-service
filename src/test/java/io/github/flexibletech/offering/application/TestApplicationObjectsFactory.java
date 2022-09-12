package io.github.flexibletech.offering.application;

import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.application.dto.RiskDecisionDto;
import io.github.flexibletech.offering.application.dto.ClientDto;
import io.github.flexibletech.offering.application.dto.ConditionsDto;
import io.github.flexibletech.offering.application.dto.ConditionsRestrictionsDto;
import io.github.flexibletech.offering.application.dto.LoanApplicationDto;
import io.github.flexibletech.offering.application.dto.OfferDto;
import io.github.flexibletech.offering.application.dto.OrganizationDto;
import io.github.flexibletech.offering.application.dto.PassportDto;
import io.github.flexibletech.offering.application.dto.StartNewLoanApplicationRequest;
import io.github.flexibletech.offering.application.dto.events.LoanApplicationCompleted;
import io.github.flexibletech.offering.domain.LoanApplication;
import io.github.flexibletech.offering.domain.client.Client;
import io.github.flexibletech.offering.domain.risk.RiskDecision;

import java.util.Set;

@SuppressWarnings("ConstantConditions")
public class TestApplicationObjectsFactory {
    private TestApplicationObjectsFactory() {
    }

    public static StartNewLoanApplicationRequest newStartNewLoanApplicationRequest() {
        return new StartNewLoanApplicationRequest(newClientDto(), newConditionsDto());
    }

    public static ClientDto newClientDto() {
        return new ClientDto(
                TestValues.CLIENT_ID,
                TestValues.NAME,
                TestValues.MIDDLE_NAME,
                TestValues.SUR_NAME,
                newPassportDto(),
                Client.MaritalStatus.UNMARRIED.name(),
                newOrganizationDto(),
                TestValues.CLIENT_FULL_REGISTRATION_ADDRESS,
                TestValues.CLIENT_PHONE_NUMBER,
                TestValues.CLIENT_EMAIL,
                TestValues.CLIENT_INCOME.getValue(),
                null,
                Client.Category.STANDARD.name(),
                TestValues.CLIENT_BIRTH_DATE);
    }

    private static ClientDto newInvalidClientDto() {
        return new ClientDto(
                TestValues.CLIENT_ID,
                null,
                TestValues.MIDDLE_NAME,
                TestValues.SUR_NAME,
                newPassportDto(),
                Client.MaritalStatus.UNMARRIED.name(),
                newOrganizationDto(),
                TestValues.CLIENT_FULL_REGISTRATION_ADDRESS,
                TestValues.CLIENT_PHONE_NUMBER,
                TestValues.CLIENT_EMAIL,
                TestValues.CLIENT_INCOME.getValue(),
                null,
                Client.Category.STANDARD.name(),
                TestValues.CLIENT_BIRTH_DATE);
    }

    public static LoanApplicationDto newLoanApplicationDtoWithoutOffer() {
        return new LoanApplicationDto(
                TestValues.LOAN_APPLICATION_ID,
                LoanApplication.Status.NEW.name(),
                null,
                null,
                null);
    }

    public static LoanApplicationDto newLoanApplicationDtoWithOffer() {
        return new LoanApplicationDto(
                TestValues.LOAN_APPLICATION_ID,
                LoanApplication.Status.APPROVED.name(),
                newOfferDto(),
                newConditionsRestrictionsDto(),
                Set.of(TestValues.FORM_DOCUMENT_ID, TestValues.CONDITIONS_DOCUMENT_ID));
    }

    private static ConditionsRestrictionsDto newConditionsRestrictionsDto() {
        return new ConditionsRestrictionsDto(TestValues.CONDITIONS_RESTRICTIONS_MAX_AMOUNT.getValue(),
                TestValues.CONDITIONS_RESTRICTIONS_MAX_PERIOD);
    }

    public static StartNewLoanApplicationRequest newInvalidStartNewLoanApplicationRequest() {
        return new StartNewLoanApplicationRequest(newInvalidClientDto(), newConditionsDto());
    }

    private static OfferDto newOfferDto() {
        return new OfferDto(
                LoanApplication.LOAN_RATE,
                TestValues.CONDITIONS_AMOUNT.getValue(),
                TestValues.CONDITIONS_PERIOD,
                TestValues.OFFER_AVERAGE_MONTHLY_PAYMENT.getValue(),
                TestValues.OFFER_FIRST_PAYMENT_DATE,
                TestValues.OFFER_LAST_PAYMENT_DATE,
                TestValues.OFFER_SINGLE_INSURANCE_PAYMENT.getValue(),
                TestValues.OFFER_INSURANCE_PREMIUM.getValue());
    }

    private static PassportDto newPassportDto() {
        return new PassportDto(
                TestValues.PASSPORT_SERIES,
                TestValues.PASSPORT_NUMBER,
                TestValues.PASSPORT_ISSUE_DATE,
                TestValues.PASSPORT_DEPARTMENT,
                TestValues.PASSPORT_DEPARTMENT_CODE);
    }

    private static OrganizationDto newOrganizationDto() {
        return new OrganizationDto(
                TestValues.ORGANIZATION_TITLE,
                TestValues.ORGANIZATION_INN,
                TestValues.ORGANIZATION_FULL_ADDRESS);
    }

    public static ConditionsDto newConditionsDto() {
        return new ConditionsDto(
                TestValues.CONDITIONS_AMOUNT.getValue(),
                TestValues.CONDITIONS_PERIOD,
                false);
    }

    public static LoanApplicationCompleted newLoanApplicationCompleted() {
        return new LoanApplicationCompleted(TestValues.LOAN_APPLICATION_ID, TestValues.ISSUANCE_ID);
    }

    public static RiskDecisionDto newRiskDecisionDto() {
        return new RiskDecisionDto(
                TestValues.RISK_DECISION_ID,
                RiskDecision.Status.APPROVED.name(),
                TestValues.PAYROLL_SALARY.getValue(),
                TestValues.PAYROLL_LAST_SALARY_DATE,
                TestValues.CONDITIONS_RESTRICTIONS_MAX_AMOUNT.getValue(),
                TestValues.CONDITIONS_RESTRICTIONS_MAX_PERIOD);
    }
}
