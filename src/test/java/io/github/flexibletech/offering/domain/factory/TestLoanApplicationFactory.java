package io.github.flexibletech.offering.domain.factory;

import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.domain.Conditions;
import io.github.flexibletech.offering.domain.LoanApplication;
import io.github.flexibletech.offering.domain.Offer;
import io.github.flexibletech.offering.domain.client.Client;
import io.github.flexibletech.offering.domain.document.Document;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestLoanApplicationFactory {
    private TestLoanApplicationFactory() {
    }

    public static LoanApplication newLoanApplicationWithoutId() {
        return LoanApplication.newLoanApplication(
                TestClientFactory.newStandardMarriedClient(),
                null,
                TestLoanApplicationFactory.newConditionsWithoutInsurance());
    }

    public static LoanApplication newLoanApplication() {
        var loanApplication = LoanApplication.newLoanApplication(
                TestClientFactory.newStandardMarriedClient(),
                null,
                TestLoanApplicationFactory.newConditionsWithoutInsurance());

        ReflectionTestUtils.setField(loanApplication, "id", TestValues.LOAN_APPLICATION_ID);
        return loanApplication;
    }

    public static LoanApplication newLoanApplicationWithRiskDecision(Client client) {
        var loanApplication = LoanApplication.newLoanApplication(
                client,
                null,
                TestLoanApplicationFactory.newConditionsWithoutInsurance());

        ReflectionTestUtils.setField(loanApplication, "riskDecision", TestRiskDecisionFactory.newApprovedRiskDecision());
        ReflectionTestUtils.setField(loanApplication, "id", TestValues.LOAN_APPLICATION_ID);

        return loanApplication;
    }

    public static LoanApplication newInsuredLoanApplicationWithRiskDecision() {
        var loanApplication = LoanApplication.newLoanApplication(
                TestClientFactory.newStandardMarriedClient(),
                null,
                TestLoanApplicationFactory.newConditionsWithInsurance());

        ReflectionTestUtils.setField(loanApplication, "riskDecision", TestRiskDecisionFactory.newApprovedRiskDecision());
        ReflectionTestUtils.setField(loanApplication, "id", TestValues.LOAN_APPLICATION_ID);

        return loanApplication;
    }

    public static LoanApplication newLoanApplicationWithRiskDecisionAndNotActualPayroll() {
        var loanApplication = LoanApplication.newLoanApplication(
                TestClientFactory.newEmployeeUnmarriedClient(),
                null,
                TestLoanApplicationFactory.newConditionsWithoutInsurance());

        ReflectionTestUtils.setField(loanApplication, "riskDecision",
                TestRiskDecisionFactory.newApprovedRiskDecisionWithNotActualPayroll());
        ReflectionTestUtils.setField(loanApplication, "id", TestValues.LOAN_APPLICATION_ID);

        return loanApplication;
    }

    public static LoanApplication newLoanApplicationWithDocumentPackage() {
        var loanApplication = LoanApplication.newLoanApplication(
                TestClientFactory.newStandardMarriedClient(),
                null,
                TestLoanApplicationFactory.newConditionsWithoutInsurance());

        ReflectionTestUtils.setField(loanApplication, "riskDecision",
                TestRiskDecisionFactory.newApprovedRiskDecisionWithNotActualPayroll());
        ReflectionTestUtils.setField(loanApplication, "id", TestValues.LOAN_APPLICATION_ID);

        Set<Document> documentPackage = new HashSet<>(
                Arrays.asList(
                        new Document(TestValues.FORM_DOCUMENT_ID, Document.Type.FORM, false),
                        new Document(TestValues.CONDITIONS_DOCUMENT_ID, Document.Type.CONDITIONS, false),
                        new Document(TestValues.INSURANCE_DOCUMENT_ID, Document.Type.INSURANCE, false)));
        ReflectionTestUtils.setField(loanApplication, "documentPackage", documentPackage);

        return loanApplication;
    }

    public static Conditions newConditionsWithoutInsurance() {
        return new Conditions(
                TestValues.CONDITIONS_AMOUNT,
                TestValues.CONDITIONS_PERIOD,
                false);
    }

    private static Conditions newConditionsWithInsurance() {
        return new Conditions(
                TestValues.CONDITIONS_AMOUNT,
                TestValues.CONDITIONS_PERIOD,
                true);
    }

    public static LoanApplication newLoanApplicationWithOffer() {
        var loanApplication = LoanApplication.newLoanApplication(
                TestClientFactory.newStandardMarriedClient(),
                null,
                TestLoanApplicationFactory.newConditionsWithInsurance());

        ReflectionTestUtils.setField(loanApplication, "riskDecision", TestRiskDecisionFactory.newApprovedRiskDecision());
        ReflectionTestUtils.setField(loanApplication, "status", LoanApplication.Status.APPROVED);
        ReflectionTestUtils.setField(loanApplication, "offer", newOffer());
        ReflectionTestUtils.setField(loanApplication, "id", TestValues.LOAN_APPLICATION_ID);

        return loanApplication;
    }

    private static Offer newOffer() {
        return Offer.newOfferWithInsurance(newConditionsWithInsurance());
    }

}
