package io.github.flexibletech.offering.domain.factory;

import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.domain.Conditions;
import io.github.flexibletech.offering.domain.LoanApplication;
import io.github.flexibletech.offering.domain.Offer;
import io.github.flexibletech.offering.domain.client.Client;
import io.github.flexibletech.offering.domain.document.Document;

public class TestLoanApplicationFactory {
    private TestLoanApplicationFactory() {
    }

    public static LoanApplication newLoanApplicationWithoutId() {
        return LoanApplication.newBuilder()
                .withLoanProgram(LoanApplication.LoanProgram.COMMON)
                .withClient(TestClientFactory.newStandardMarriedClient())
                .withConditions(TestLoanApplicationFactory.newConditionsWithoutInsurance())
                .build();
    }

    public static LoanApplication newLoanApplication() {
        return LoanApplication.newBuilder()
                .withId(TestValues.LOAN_APPLICATION_ID)
                .withLoanProgram(LoanApplication.LoanProgram.COMMON)
                .withClient(TestClientFactory.newStandardMarriedClient())
                .withConditions(TestLoanApplicationFactory.newConditionsWithoutInsurance())
                .build();
    }

    public static LoanApplication newLoanApplicationWithRiskDecision(Client client) {
        return LoanApplication.newBuilder()
                .withId(TestValues.LOAN_APPLICATION_ID)
                .withRiskDecision(TestRiskDecisionFactory.newApprovedRiskDecision())
                .withLoanProgram(LoanApplication.LoanProgram.COMMON)
                .withClient(client)
                .withConditions(TestLoanApplicationFactory.newConditionsWithoutInsurance())
                .build();
    }

    public static LoanApplication newInsuredLoanApplicationWithRiskDecision() {
        return LoanApplication.newBuilder()
                .withId(TestValues.LOAN_APPLICATION_ID)
                .withRiskDecision(TestRiskDecisionFactory.newApprovedRiskDecision())
                .withLoanProgram(LoanApplication.LoanProgram.COMMON)
                .withClient(TestClientFactory.newStandardMarriedClient())
                .withConditions(TestLoanApplicationFactory.newConditionsWithInsurance())
                .build();
    }

    public static LoanApplication newLoanApplicationWithRiskDecisionAndNotActualPayroll() {
        return LoanApplication.newBuilder()
                .withId(TestValues.LOAN_APPLICATION_ID)
                .withRiskDecision(TestRiskDecisionFactory.newApprovedRiskDecisionWithNotActualPayroll())
                .withLoanProgram(LoanApplication.LoanProgram.COMMON)
                .withClient(TestClientFactory.newEmployeeUnmarriedClient())
                .withConditions(TestLoanApplicationFactory.newConditionsWithoutInsurance())
                .build();
    }

    public static LoanApplication newLoanApplicationWithDocumentPackage() {
        return LoanApplication.newBuilder()
                .withId(TestValues.LOAN_APPLICATION_ID)
                .withRiskDecision(TestRiskDecisionFactory.newApprovedRiskDecisionWithNotActualPayroll())
                .withLoanProgram(LoanApplication.LoanProgram.COMMON)
                .withClient(TestClientFactory.newStandardMarriedClient())
                .withConditions(TestLoanApplicationFactory.newConditionsWithoutInsurance())
                .withDocumentPackage(
                        new Document(TestValues.FORM_DOCUMENT_ID, Document.Type.FORM, false),
                        new Document(TestValues.CONDITIONS_DOCUMENT_ID, Document.Type.CONDITIONS, false),
                        new Document(TestValues.INSURANCE_DOCUMENT_ID, Document.Type.INSURANCE, false))
                .build();
    }

    public static LoanApplication newLoanApplicationWithOffer() {
        return LoanApplication.newBuilder()
                .withId(TestValues.LOAN_APPLICATION_ID)
                .withStatus(LoanApplication.Status.APPROVED)
                .withRiskDecision(TestRiskDecisionFactory.newApprovedRiskDecisionWithNotActualPayroll())
                .withLoanProgram(LoanApplication.LoanProgram.COMMON)
                .withClient(TestClientFactory.newStandardMarriedClient())
                .withConditions(TestLoanApplicationFactory.newConditionsWithInsurance())
                .withDocumentPackage(
                        new Document(TestValues.FORM_DOCUMENT_ID, Document.Type.FORM, false),
                        new Document(TestValues.CONDITIONS_DOCUMENT_ID, Document.Type.CONDITIONS, false))
                .withOffer(newOffer())
                .build();
    }

    public static LoanApplication newLoanApplicationWithoutDocuments() {
        return LoanApplication.newBuilder()
                .withId(TestValues.LOAN_APPLICATION_ID)
                .withStatus(LoanApplication.Status.APPROVED)
                .withRiskDecision(TestRiskDecisionFactory.newApprovedRiskDecisionWithNotActualPayroll())
                .withLoanProgram(LoanApplication.LoanProgram.COMMON)
                .withClient(TestClientFactory.newStandardMarriedClient())
                .withConditions(TestLoanApplicationFactory.newConditionsWithInsurance())
                .withOffer(newOffer())
                .build();
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

    private static Offer newOffer() {
        return Offer.newOfferWithInsurance(newConditionsWithInsurance());
    }

}
