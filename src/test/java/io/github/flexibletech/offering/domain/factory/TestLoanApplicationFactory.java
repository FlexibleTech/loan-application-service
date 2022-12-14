package io.github.flexibletech.offering.domain.factory;

import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.domain.loanapplication.Conditions;
import io.github.flexibletech.offering.domain.loanapplication.LoanApplication;
import io.github.flexibletech.offering.domain.loanapplication.LoanApplicationId;
import io.github.flexibletech.offering.domain.loanapplication.Offer;
import io.github.flexibletech.offering.domain.loanapplication.document.Document;

public class TestLoanApplicationFactory {
    private TestLoanApplicationFactory() {
    }

    public static LoanApplication newLoanApplication() {
        return LoanApplication.newBuilder()
                .withId(LoanApplicationId.fromValue(TestValues.LOAN_APPLICATION_ID))
                .withLoanProgram(LoanApplication.LoanProgram.COMMON)
                .withClient(TestValues.CLIENT_ID)
                .withConditions(TestLoanApplicationFactory.newConditionsWithoutInsurance())
                .build();
    }

    public static LoanApplication newLoanApplicationWithRiskDecision() {
        return LoanApplication.newBuilder()
                .withId(LoanApplicationId.fromValue(TestValues.LOAN_APPLICATION_ID))
                .withRiskDecision(TestRiskDecisionFactory.newApprovedRiskDecision())
                .withLoanProgram(LoanApplication.LoanProgram.COMMON)
                .withClient(TestValues.CLIENT_ID)
                .withConditions(TestLoanApplicationFactory.newConditionsWithoutInsurance())
                .build();
    }

    public static LoanApplication newInsuredLoanApplicationWithRiskDecision() {
        return LoanApplication.newBuilder()
                .withId(LoanApplicationId.fromValue(TestValues.LOAN_APPLICATION_ID))
                .withRiskDecision(TestRiskDecisionFactory.newApprovedRiskDecision())
                .withLoanProgram(LoanApplication.LoanProgram.COMMON)
                .withClient(TestValues.CLIENT_ID)
                .withConditions(TestLoanApplicationFactory.newConditionsWithInsurance())
                .build();
    }

    public static LoanApplication newLoanApplicationWithRiskDecisionAndNotActualPayroll() {
        return LoanApplication.newBuilder()
                .withId(LoanApplicationId.fromValue(TestValues.LOAN_APPLICATION_ID))
                .withRiskDecision(TestRiskDecisionFactory.newApprovedRiskDecisionWithNotActualPayroll())
                .withLoanProgram(LoanApplication.LoanProgram.COMMON)
                .withClient(TestValues.CLIENT_ID)
                .withConditions(TestLoanApplicationFactory.newConditionsWithoutInsurance())
                .build();
    }

    public static LoanApplication newLoanApplicationWithDocumentPackage() {
        return LoanApplication.newBuilder()
                .withId(LoanApplicationId.fromValue(TestValues.LOAN_APPLICATION_ID))
                .withRiskDecision(TestRiskDecisionFactory.newApprovedRiskDecisionWithNotActualPayroll())
                .withLoanProgram(LoanApplication.LoanProgram.COMMON)
                .withClient(TestValues.CLIENT_ID)
                .withConditions(TestLoanApplicationFactory.newConditionsWithoutInsurance())
                .withDocumentPackage(
                        new Document(TestValues.FORM_DOCUMENT_ID, Document.Type.FORM, false),
                        new Document(TestValues.CONDITIONS_DOCUMENT_ID, Document.Type.CONDITIONS, false),
                        new Document(TestValues.INSURANCE_DOCUMENT_ID, Document.Type.INSURANCE, false))
                .build();
    }

    public static LoanApplication newLoanApplicationWithOffer() {
        return LoanApplication.newBuilder()
                .withId(LoanApplicationId.fromValue(TestValues.LOAN_APPLICATION_ID))
                .withStatus(LoanApplication.Status.APPROVED)
                .withRiskDecision(TestRiskDecisionFactory.newApprovedRiskDecisionWithNotActualPayroll())
                .withLoanProgram(LoanApplication.LoanProgram.COMMON)
                .withClient(TestValues.CLIENT_ID)
                .withConditions(TestLoanApplicationFactory.newConditionsWithInsurance())
                .withDocumentPackage(
                        new Document(TestValues.FORM_DOCUMENT_ID, Document.Type.FORM, false),
                        new Document(TestValues.CONDITIONS_DOCUMENT_ID, Document.Type.CONDITIONS, false))
                .withOffer(newOffer())
                .build();
    }

    public static LoanApplication newLoanApplicationWithoutDocuments() {
        return LoanApplication.newBuilder()
                .withId(LoanApplicationId.fromValue(TestValues.LOAN_APPLICATION_ID))
                .withStatus(LoanApplication.Status.APPROVED)
                .withRiskDecision(TestRiskDecisionFactory.newApprovedRiskDecisionWithNotActualPayroll())
                .withLoanProgram(LoanApplication.LoanProgram.COMMON)
                .withClient(TestValues.CLIENT_ID)
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
