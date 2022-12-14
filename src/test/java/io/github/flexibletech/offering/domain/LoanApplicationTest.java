package io.github.flexibletech.offering.domain;

import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.domain.factory.TestClientFactory;
import io.github.flexibletech.offering.domain.factory.TestLoanApplicationFactory;
import io.github.flexibletech.offering.domain.factory.TestPreApprovedOfferFactory;
import io.github.flexibletech.offering.domain.factory.TestRiskDecisionFactory;
import io.github.flexibletech.offering.domain.loanapplication.LoanApplication;
import io.github.flexibletech.offering.domain.loanapplication.document.Document;
import io.github.flexibletech.offering.domain.loanapplication.risk.ConditionsRestrictions;
import io.github.flexibletech.offering.domain.loanapplication.risk.RiskDecision;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

@SuppressWarnings("ConstantConditions")
public class LoanApplicationTest {

    @Test
    public void shouldCreateNewLoanApplicationWithCommonLoanProgram() {
        var loanApplication = LoanApplication.newLoanApplication(
                TestClientFactory.newStandardMarriedClient(),
                null,
                TestValues.CONDITIONS_AMOUNT.getValue(),
                TestValues.CONDITIONS_PERIOD,
                false);

        //Assert LoanApplication
        Assertions.assertNotNull(loanApplication);
        Assertions.assertEquals(loanApplication.getLoanProgram(), LoanApplication.LoanProgram.COMMON);
        Assertions.assertEquals(loanApplication.getIncomeConfirmationType(), LoanApplication.IncomeConfirmationType.TWO_NDFL);
        Assertions.assertEquals(loanApplication.getStatus(), LoanApplication.Status.NEW);
        Assertions.assertNull(loanApplication.getCompletedAt());
        Assertions.assertEquals(loanApplication.getClientId().getId(), TestValues.CLIENT_ID);

        //Assert conditions
        var conditions = loanApplication.getConditions();
        Assertions.assertNotNull(conditions);
        Assertions.assertEquals(conditions.getAmount(), TestValues.CONDITIONS_AMOUNT);
        Assertions.assertEquals(conditions.getPeriod(), TestValues.CONDITIONS_PERIOD);
        Assertions.assertFalse(conditions.isInsurance());
    }

    @Test
    public void shouldCreateNewLoanApplicationWithPreApprovedLoanProgram() {
        var loanApplication = LoanApplication.newLoanApplication(
                TestClientFactory.newStandardMarriedClient(),
                TestPreApprovedOfferFactory.newPreApprovedOffer(),
                TestValues.CONDITIONS_AMOUNT.getValue(),
                TestValues.CONDITIONS_PERIOD,
                false);

        Assertions.assertEquals(loanApplication.getLoanProgram(), LoanApplication.LoanProgram.PREAPPROVED);
    }

    @Test
    public void shouldCreateNewLoanApplicationWithPayrollClientLoanProgram() {
        var loanApplication = LoanApplication.newLoanApplication(
                TestClientFactory.newPayrollUnmarriedClient(),
                TestPreApprovedOfferFactory.newPreApprovedOffer(),
                TestValues.CONDITIONS_AMOUNT.getValue(),
                TestValues.CONDITIONS_PERIOD,
                false);

        Assertions.assertEquals(loanApplication.getLoanProgram(), LoanApplication.LoanProgram.PAYROLL_CLIENT);
    }

    @Test
    public void shouldCreateNewLoanApplicationWithSpecialLoanProgram() {
        var loanApplication = LoanApplication.newLoanApplication(
                TestClientFactory.newPremiumClient(),
                TestPreApprovedOfferFactory.newPreApprovedOffer(),
                TestValues.CONDITIONS_AMOUNT.getValue(),
                TestValues.CONDITIONS_PERIOD,
                false);

        Assertions.assertEquals(loanApplication.getLoanProgram(), LoanApplication.LoanProgram.SPECIAL);
    }

    @Test
    public void shouldThrowExceptionIfSpouseIncomeSpecifiedForUnmarriedClient() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> LoanApplication.newLoanApplication(TestClientFactory.newUnmarriedClientWithSpouseIncome(),
                        null,
                        TestValues.CONDITIONS_AMOUNT.getValue(),
                        TestValues.CONDITIONS_PERIOD,
                        false),
                () -> String.format("Unable to specify spouse income for unmarried client %s", TestValues.CLIENT_ID));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void shouldCreateNewRiskDecisionWithoutLimitedAmount() {
        var riskDecision = RiskDecision.newRiskDecision(TestValues.RISK_DECISION_ID,
                RiskDecision.Status.APPROVED.name(),
                TestValues.PAYROLL_SALARY.getValue(),
                TestValues.PAYROLL_LAST_SALARY_DATE,
                TestValues.CONDITIONS_RESTRICTIONS_MAX_AMOUNT.getValue(),
                TestValues.CONDITIONS_RESTRICTIONS_MAX_PERIOD);

        Assertions.assertTrue(riskDecision.isApproved());
        Assertions.assertEquals(riskDecision.getId().toString(), TestValues.RISK_DECISION_ID);

        //Assert payroll
        var payroll = riskDecision.getPayroll();
        Assertions.assertNotNull(payroll);
        Assertions.assertEquals(payroll.getLastSalaryDate(), TestValues.PAYROLL_LAST_SALARY_DATE);
        Assertions.assertEquals(payroll.getSalary(), TestValues.PAYROLL_SALARY);

        //Assert conditionsRestrictions
        var conditionsRestrictions = riskDecision.getConditionsRestrictions();
        Assertions.assertNotNull(conditionsRestrictions);
        Assertions.assertEquals(conditionsRestrictions.getMaxAmount(), TestValues.CONDITIONS_RESTRICTIONS_MAX_AMOUNT);
        Assertions.assertEquals(conditionsRestrictions.getMaxPeriod(), TestValues.CONDITIONS_RESTRICTIONS_MAX_PERIOD);
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void shouldCreateNewRiskDecisionWithLimitedAmount() {
        var riskDecision = RiskDecision.newRiskDecision(TestValues.RISK_DECISION_ID,
                RiskDecision.Status.APPROVED.name(),
                TestValues.PAYROLL_SALARY.getValue(),
                TestValues.PAYROLL_LAST_SALARY_DATE,
                TestValues.CONDITIONS_RESTRICTIONS_LARGE_MAX_AMOUNT.getValue(),
                TestValues.CONDITIONS_RESTRICTIONS_MAX_PERIOD);

        Assertions.assertTrue(riskDecision.isApproved());
        Assertions.assertEquals(riskDecision.getId().toString(), TestValues.RISK_DECISION_ID);

        //Assert payroll
        var payroll = riskDecision.getPayroll();
        Assertions.assertNotNull(payroll);
        Assertions.assertEquals(payroll.getLastSalaryDate(), TestValues.PAYROLL_LAST_SALARY_DATE);
        Assertions.assertEquals(payroll.getSalary(), TestValues.PAYROLL_SALARY);

        //Assert conditionsRestrictions
        var conditionsRestrictions = riskDecision.getConditionsRestrictions();
        Assertions.assertNotNull(conditionsRestrictions);
        Assertions.assertEquals(conditionsRestrictions.getMaxAmount(), ConditionsRestrictions.AMOUNT_LIMIT);
        Assertions.assertEquals(conditionsRestrictions.getMaxPeriod(), TestValues.CONDITIONS_RESTRICTIONS_MAX_PERIOD);
    }

    @Test
    public void shouldAcceptApprovedRiskDecision() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplication();

        loanApplication.acceptRiskDecision(TestRiskDecisionFactory.newApprovedRiskDecision());

        Assertions.assertEquals(loanApplication.getStatus(), LoanApplication.Status.APPROVED);

        //Assert RiskDecision
        var riskDecision = loanApplication.getRiskDecision();
        Assertions.assertNotNull(riskDecision);
        Assertions.assertTrue(riskDecision.isApproved());
        Assertions.assertEquals(riskDecision.getId().toString(), TestValues.RISK_DECISION_ID);

        //Assert payroll
        var payroll = riskDecision.getPayroll();
        Assertions.assertNotNull(payroll);
        Assertions.assertEquals(payroll.getSalary(), TestValues.PAYROLL_SALARY);
        Assertions.assertEquals(payroll.getLastSalaryDate(), TestValues.PAYROLL_LAST_SALARY_DATE);

        //Assert ConditionsRestrictions
        var conditionsRestrictions = riskDecision.getConditionsRestrictions();
        Assertions.assertNotNull(conditionsRestrictions);
        Assertions.assertEquals(conditionsRestrictions.getMaxAmount(), TestValues.CONDITIONS_RESTRICTIONS_MAX_AMOUNT);
        Assertions.assertEquals(conditionsRestrictions.getMaxPeriod(), TestValues.CONDITIONS_RESTRICTIONS_MAX_PERIOD);
    }

    @Test
    public void shouldAcceptDeclinedRiskDecision() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplication();

        loanApplication.acceptRiskDecision(TestRiskDecisionFactory.newDeclinedRiskDecision());

        Assertions.assertEquals(loanApplication.getStatus(), LoanApplication.Status.DECLINED);

        //Assert RiskDecision
        var riskDecision = loanApplication.getRiskDecision();
        Assertions.assertFalse(riskDecision.isApproved());
    }

    @Test
    public void shouldDefineSalaryReceiptIncomeConfirmationTypeForPayrollClient() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision();

        var incomeConfirmationType = loanApplication.defineIncomeConfirmationType(null,
                TestClientFactory.newPayrollUnmarriedClient());

        Assertions.assertEquals(incomeConfirmationType, LoanApplication.IncomeConfirmationType.SALARY_RECEIPT);
    }

    @Test
    public void shouldDefineTwoNdflIncomeConfirmationTypeForPayrollClient() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecisionAndNotActualPayroll();

        var incomeConfirmationType = loanApplication.defineIncomeConfirmationType(null,
                TestClientFactory.newPayrollUnmarriedClient());

        Assertions.assertEquals(incomeConfirmationType, LoanApplication.IncomeConfirmationType.TWO_NDFL);
    }

    @Test
    public void shouldDefineTwoNdflIncomeConfirmationTypeForNullableClient() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision();

        var incomeConfirmationType = loanApplication.defineIncomeConfirmationType(null, null);

        Assertions.assertEquals(incomeConfirmationType, LoanApplication.IncomeConfirmationType.TWO_NDFL);
    }

    @Test
    public void shouldDefineNoneIncomeConfirmationTypeForPremiumClient() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision();

        var incomeConfirmationType = loanApplication.defineIncomeConfirmationType(null,
                TestClientFactory.newPremiumClient());

        Assertions.assertEquals(incomeConfirmationType, LoanApplication.IncomeConfirmationType.NONE);
    }

    @Test
    public void shouldDefineTwoNdflIncomeConfirmationTypeForStandardClientWithPreApprovedOffer() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision();

        var incomeConfirmationType = loanApplication.defineIncomeConfirmationType(
                TestPreApprovedOfferFactory.newPreApprovedOfferWithSmallMaxOfferAmount(), TestClientFactory.newStandardMarriedClient());

        Assertions.assertEquals(incomeConfirmationType, LoanApplication.IncomeConfirmationType.TWO_NDFL);
    }

    @Test
    public void shouldChoseNewConditionsWithoutInsurance() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision();

        loanApplication.choseConditions(
                TestValues.NEW_CHOSEN_CONDITIONS_AMOUNT,
                TestValues.NEW_CHOSEN_CONDITIONS_PERIOD,
                false);

        var newConditions = loanApplication.getConditions();
        Assertions.assertNotNull(newConditions);
        Assertions.assertFalse(newConditions.isInsurance());
        Assertions.assertEquals(newConditions.getAmount().getValue(), TestValues.NEW_CHOSEN_CONDITIONS_AMOUNT);
        Assertions.assertEquals(newConditions.getPeriod(), TestValues.NEW_CHOSEN_CONDITIONS_PERIOD);
    }

    @Test
    public void shouldNotChoseNewConditions() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision();

        loanApplication.choseConditions(null, null, null);

        var newConditions = loanApplication.getConditions();
        Assertions.assertNotNull(newConditions);
        Assertions.assertFalse(newConditions.isInsurance());
        Assertions.assertEquals(newConditions.getAmount(), TestValues.CONDITIONS_AMOUNT);
        Assertions.assertEquals(newConditions.getPeriod(), TestValues.CONDITIONS_PERIOD);
    }

    @Test
    public void shouldChoseNewConditionsWithInsurance() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision();

        loanApplication.choseConditions(null, null, true);

        var newConditions = loanApplication.getConditions();
        Assertions.assertEquals(newConditions.getAmount(), TestValues.ADJUSTED_CONDITIONS_AMOUNT);
    }

    @Test
    public void shouldAddFormDocument() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision();

        loanApplication.addDocument(TestValues.FORM_DOCUMENT_ID, Document.Type.FORM);

        var agreementDocument = loanApplication.getDocumentByType(Document.Type.FORM);
        Assertions.assertEquals(agreementDocument.getType(), Document.Type.FORM);
        Assertions.assertEquals(agreementDocument.getId(), TestValues.FORM_DOCUMENT_ID);
    }

    @Test
    public void shouldAddConditionsDocument() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision();

        loanApplication.addDocument(TestValues.CONDITIONS_DOCUMENT_ID, Document.Type.CONDITIONS);

        var agreementDocument = loanApplication.getDocumentByType(Document.Type.CONDITIONS);
        Assertions.assertEquals(agreementDocument.getType(), Document.Type.CONDITIONS);
        Assertions.assertEquals(agreementDocument.getId(), TestValues.CONDITIONS_DOCUMENT_ID);
    }

    @Test
    public void shouldAddInsuranceDocument() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision();

        loanApplication.addDocument(TestValues.INSURANCE_DOCUMENT_ID, Document.Type.INSURANCE);

        var agreementDocument = loanApplication.getDocumentByType(Document.Type.INSURANCE);
        Assertions.assertEquals(agreementDocument.getType(), Document.Type.INSURANCE);
        Assertions.assertEquals(agreementDocument.getId(), TestValues.INSURANCE_DOCUMENT_ID);
    }

    @Test
    public void shouldCompleteLoanApplication() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision();

        loanApplication.complete(TestValues.ISSUANCE_ID);

        Assertions.assertEquals(loanApplication.getStatus(), LoanApplication.Status.COMPLETED);
        Assertions.assertEquals(loanApplication.getCompletedAt().toLocalDate(), LocalDate.now());
        Assertions.assertEquals(loanApplication.getIssuanceId().toString(), TestValues.ISSUANCE_ID);
    }

    @Test
    public void shouldCalculateOfferWithoutInsurance() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision();

        loanApplication.calculateOffer();

        var offer = loanApplication.getOffer();
        Assertions.assertEquals(offer.getAmount(), TestValues.CONDITIONS_AMOUNT);
        Assertions.assertEquals(offer.getAverageMonthlyPayment(), TestValues.OFFER_AVERAGE_MONTHLY_PAYMENT);
        Assertions.assertEquals(offer.getFirstPaymentDate(), TestValues.OFFER_FIRST_PAYMENT_DATE);
        Assertions.assertEquals(offer.getLastPaymentDate(), TestValues.OFFER_LAST_PAYMENT_DATE);
        Assertions.assertEquals(offer.getPeriod(), TestValues.CONDITIONS_PERIOD);
    }

    @Test
    public void shouldCalculateOfferWithInsurance() {
        var loanApplication = TestLoanApplicationFactory.newInsuredLoanApplicationWithRiskDecision();

        loanApplication.calculateOffer();

        var offer = loanApplication.getOffer();
        Assertions.assertEquals(offer.getInsurancePremium(), TestValues.OFFER_INSURANCE_PREMIUM);
        Assertions.assertEquals(offer.getSingleInsurancePayment(), TestValues.OFFER_SINGLE_INSURANCE_PAYMENT);
        Assertions.assertEquals(offer.getAmount(), TestValues.CONDITIONS_AMOUNT);
        Assertions.assertEquals(offer.getAverageMonthlyPayment(), TestValues.OFFER_AVERAGE_MONTHLY_PAYMENT);
        Assertions.assertEquals(offer.getFirstPaymentDate(), TestValues.OFFER_FIRST_PAYMENT_DATE);
        Assertions.assertEquals(offer.getLastPaymentDate(), TestValues.OFFER_LAST_PAYMENT_DATE);
    }

    @Test
    public void shouldCancelLoanApplication() {
        var loanApplication = TestLoanApplicationFactory.newInsuredLoanApplicationWithRiskDecision();

        loanApplication.cancel();

        Assertions.assertEquals(loanApplication.getStatus(), LoanApplication.Status.CANCEL);
        Assertions.assertEquals(loanApplication.getCompletedAt().toLocalDate(), LocalDate.now());
    }

    @Test
    public void shouldSignDocumentPackage() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithDocumentPackage();

        loanApplication.signDocumentPackage();

        Assertions.assertTrue(loanApplication.isDocumentPackageSigned());
        Assertions.assertEquals(loanApplication.getStatus(), LoanApplication.Status.PENDING_ISSUANCE);
    }

    @Test
    public void shouldDefineEmptyDocumentPackageAsUnsigned() {
        var loanApplication = TestLoanApplicationFactory.newInsuredLoanApplicationWithRiskDecision();

        var documentPackageSigned = loanApplication.isDocumentPackageSigned();

        Assertions.assertFalse(documentPackageSigned);
    }

    @Test
    public void shouldMakeFormName() {
        var loanApplication = TestLoanApplicationFactory.newInsuredLoanApplicationWithRiskDecision();

        var documentName = loanApplication.makeDocumentName(Document.Type.FORM);

        Assertions.assertTrue(documentName.contains(TestValues.LOAN_APPLICATION_ID));
        Assertions.assertTrue(documentName.contains(Document.Type.FORM.name()));
        Assertions.assertTrue(documentName.contains(Document.FORMAT));
    }

}
