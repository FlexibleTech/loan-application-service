package io.github.flexibletech.offering.application;

import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.application.loanapplication.dto.ChoseConditionsRequest;
import io.github.flexibletech.offering.application.loanapplication.dto.ConditionsRestrictionsDto;
import io.github.flexibletech.offering.application.loanapplication.dto.DocumentDto;
import io.github.flexibletech.offering.application.loanapplication.dto.LoanApplicationDto;
import io.github.flexibletech.offering.application.loanapplication.dto.OfferDto;
import io.github.flexibletech.offering.application.loanapplication.dto.StartNewLoanApplicationRequest;
import io.github.flexibletech.offering.application.loanapplication.dto.events.LoanApplicationCompleted;
import io.github.flexibletech.offering.domain.loanapplication.LoanApplication;
import io.github.flexibletech.offering.domain.loanapplication.document.Document;

import java.util.Set;

@SuppressWarnings("ConstantConditions")
public class TestApplicationObjectsFactory {
    private TestApplicationObjectsFactory() {
    }

    public static StartNewLoanApplicationRequest newStartNewLoanApplicationRequest() {
        return new StartNewLoanApplicationRequest(
                TestValues.CLIENT_ID,
                TestValues.CONDITIONS_AMOUNT.getValue(),
                TestValues.CONDITIONS_PERIOD,
                false);
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
                Set.of(new DocumentDto(TestValues.FORM_DOCUMENT_ID, Document.Type.FORM.name()),
                        new DocumentDto(TestValues.CONDITIONS_DOCUMENT_ID, Document.Type.CONDITIONS.name())
                ));
    }

    private static ConditionsRestrictionsDto newConditionsRestrictionsDto() {
        return new ConditionsRestrictionsDto(TestValues.CONDITIONS_RESTRICTIONS_MAX_AMOUNT.getValue(),
                TestValues.CONDITIONS_RESTRICTIONS_MAX_PERIOD);
    }

    public static StartNewLoanApplicationRequest newInvalidStartNewLoanApplicationRequest() {
        return new StartNewLoanApplicationRequest(
                TestValues.CLIENT_ID,
                TestValues.CONDITIONS_AMOUNT.getValue(),
                null,
                false);
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

    public static ChoseConditionsRequest newConditionsDto() {
        return new ChoseConditionsRequest(
                TestValues.CONDITIONS_AMOUNT.getValue(),
                TestValues.CONDITIONS_PERIOD,
                false);
    }

    public static LoanApplicationCompleted newLoanApplicationCompleted() {
        return new LoanApplicationCompleted(TestValues.LOAN_APPLICATION_ID, TestValues.ISSUANCE_ID);
    }
}
