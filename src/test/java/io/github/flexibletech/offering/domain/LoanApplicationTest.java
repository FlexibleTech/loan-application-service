package io.github.flexibletech.offering.domain;

import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.domain.client.Client;
import io.github.flexibletech.offering.domain.document.Document;
import io.github.flexibletech.offering.domain.factory.TestClientFactory;
import io.github.flexibletech.offering.domain.factory.TestLoanApplicationFactory;
import io.github.flexibletech.offering.domain.factory.TestPreApprovedOfferFactory;
import io.github.flexibletech.offering.domain.factory.TestRiskDecisionFactory;
import io.github.flexibletech.offering.domain.risk.ConditionsRestrictions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class LoanApplicationTest {

    @Test
    public void shouldCreateNewLoanApplicationWithCommonLoanProgram() {
        var loanApplication = LoanApplication.newLoanApplication(
                TestClientFactory.newStandardMarriedClient(),
                null,
                TestLoanApplicationFactory.newConditionsWithoutInsurance());

        //Assert LoanApplication
        Assertions.assertNotNull(loanApplication);
        Assertions.assertEquals(loanApplication.getLoanProgram(), LoanApplication.LoanProgram.COMMON);
        Assertions.assertEquals(loanApplication.getIncomeConfirmationType(), LoanApplication.IncomeConfirmationType.TWO_NDFL);
        Assertions.assertEquals(loanApplication.getStatus(), LoanApplication.Status.NEW);
        Assertions.assertNull(loanApplication.getCompletedAt());

        //Assert Client
        var client = loanApplication.getClient();
        Assertions.assertNotNull(client);
        Assertions.assertEquals(client.getMaritalStatus(), Client.MaritalStatus.MARRIED);
        Assertions.assertEquals(client.getFullRegistrationAddress(), TestValues.CLIENT_FULL_REGISTRATION_ADDRESS);
        Assertions.assertEquals(client.getPhoneNumber(), TestValues.CLIENT_PHONE_NUMBER);
        Assertions.assertEquals(client.getEmail(), TestValues.CLIENT_EMAIL);

        //Assert incomes
        Assertions.assertEquals(client.getIncome(), TestValues.CLIENT_INCOME);
        Assertions.assertEquals(client.getSpouseIncome(), TestValues.CLIENT_SPOUSE_INCOME);

        //Assert PersonNameDetails
        var personNameDetails = client.getPersonNameDetails();
        Assertions.assertEquals(personNameDetails.getName(), TestValues.NAME);
        Assertions.assertEquals(personNameDetails.getMiddleName(), TestValues.MIDDLE_NAME);
        Assertions.assertEquals(personNameDetails.getSurName(), TestValues.SUR_NAME);

        //Assert WorkPlace
        var workPlace = client.getWorkPlace();
        Assertions.assertEquals(workPlace.getTitle(), TestValues.ORGANIZATION_TITLE);
        Assertions.assertEquals(workPlace.getInn(), TestValues.ORGANIZATION_INN);
        Assertions.assertEquals(workPlace.getFullAddress(), TestValues.ORGANIZATION_FULL_ADDRESS);

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
                TestLoanApplicationFactory.newConditionsWithoutInsurance());

        Assertions.assertEquals(loanApplication.getLoanProgram(), LoanApplication.LoanProgram.PREAPPROVED);
    }

    @Test
    public void shouldCreateNewLoanApplicationWithEmployeeLoanProgram() {
        var loanApplication = LoanApplication.newLoanApplication(
                TestClientFactory.newEmployeeUnmarriedClient(),
                TestPreApprovedOfferFactory.newPreApprovedOffer(),
                TestLoanApplicationFactory.newConditionsWithoutInsurance());

        Assertions.assertEquals(loanApplication.getLoanProgram(), LoanApplication.LoanProgram.EMPLOYEE);
        var client = loanApplication.getClient();

        Assertions.assertEquals(client.getMaritalStatus(), Client.MaritalStatus.UNMARRIED);
        Assertions.assertNull(client.getSpouseIncome());
    }

    @Test
    public void shouldCreateNewLoanApplicationWithSpecialLoanProgram() {
        var loanApplication = LoanApplication.newLoanApplication(
                TestClientFactory.newPremiumClient(),
                TestPreApprovedOfferFactory.newPreApprovedOffer(),
                TestLoanApplicationFactory.newConditionsWithoutInsurance());

        Assertions.assertEquals(loanApplication.getLoanProgram(), LoanApplication.LoanProgram.SPECIAL);
    }

    @Test
    public void shouldThrowExceptionIfSpouseIncomeSpecifiedForUnmarriedClient() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> LoanApplication.newLoanApplication(TestClientFactory.newUnmarriedClientWithSpouseIncome(),
                        null,
                        TestLoanApplicationFactory.newConditionsWithoutInsurance()),
                () -> String.format("Unable to specify spouse income for unmarried client %s", TestValues.CLIENT_ID));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void shouldBuildNewClient() {
        var client = Client.newBuilder()
                .withId(TestValues.CLIENT_ID)
                .withPersonNameDetails(TestValues.NAME, TestValues.MIDDLE_NAME, TestValues.SUR_NAME)
                .withPassport(TestValues.PASSPORT_SERIES, TestValues.PASSPORT_NUMBER, TestValues.PASSPORT_ISSUE_DATE,
                        TestValues.PASSPORT_DEPARTMENT, TestValues.PASSPORT_DEPARTMENT_CODE)
                .withMaritalStatus(Client.MaritalStatus.MARRIED.name())
                .withWorkplace(TestValues.ORGANIZATION_TITLE, TestValues.ORGANIZATION_INN, TestValues.ORGANIZATION_FULL_ADDRESS)
                .withFullRegistrationAddress(TestValues.CLIENT_FULL_REGISTRATION_ADDRESS)
                .withPhoneNumber(TestValues.CLIENT_PHONE_NUMBER)
                .withEmail(TestValues.CLIENT_EMAIL)
                .withIncome(TestValues.CLIENT_INCOME.getValue())
                .withSpouseIncome(TestValues.CLIENT_SPOUSE_INCOME.getValue())
                .withCategory(Client.Category.STANDARD.name())
                .withBirthDate(TestValues.CLIENT_BIRTH_DATE)
                .build();

        Assertions.assertNotNull(client);
        Assertions.assertEquals(client.getMaritalStatus(), Client.MaritalStatus.MARRIED);
        Assertions.assertEquals(client.getFullRegistrationAddress(), TestValues.CLIENT_FULL_REGISTRATION_ADDRESS);
        Assertions.assertEquals(client.getPhoneNumber(), TestValues.CLIENT_PHONE_NUMBER);
        Assertions.assertEquals(client.getEmail(), TestValues.CLIENT_EMAIL);

        //Assert incomes
        Assertions.assertEquals(client.getIncome(), TestValues.CLIENT_INCOME);
        Assertions.assertEquals(client.getSpouseIncome(), TestValues.CLIENT_SPOUSE_INCOME);

        //Assert PersonNameDetails
        var personNameDetails = client.getPersonNameDetails();
        Assertions.assertEquals(personNameDetails.getName(), TestValues.NAME);
        Assertions.assertEquals(personNameDetails.getMiddleName(), TestValues.MIDDLE_NAME);
        Assertions.assertEquals(personNameDetails.getSurName(), TestValues.SUR_NAME);

        //Assert WorkPlace
        var workPlace = client.getWorkPlace();
        Assertions.assertEquals(workPlace.getTitle(), TestValues.ORGANIZATION_TITLE);
        Assertions.assertEquals(workPlace.getInn(), TestValues.ORGANIZATION_INN);
        Assertions.assertEquals(workPlace.getFullAddress(), TestValues.ORGANIZATION_FULL_ADDRESS);
    }

    @Test
    public void shouldAddApprovedRiskDecision() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplication();

        loanApplication.addRiskDecision(TestRiskDecisionFactory.newApprovedRiskDecision());

        Assertions.assertEquals(loanApplication.getStatus(), LoanApplication.Status.APPROVED);

        //Assert RiskDecision
        var riskDecision = loanApplication.getRiskDecision();
        Assertions.assertNotNull(riskDecision);
        Assertions.assertTrue(riskDecision.isApproved());
        Assertions.assertEquals(riskDecision.getId(), TestValues.RISK_DECISION_ID);

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
    public void shouldAddApprovedRiskDecisionWithLimitedConditionsRestrictionsAmount() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplication();

        loanApplication.addRiskDecision(TestRiskDecisionFactory.newApprovedRiskDecisionWithLargeConditionsRestrictionsAmount());

        //Assert RiskDecision
        var riskDecision = loanApplication.getRiskDecision();
        //Assert ConditionsRestrictions
        var conditionsRestrictions = riskDecision.getConditionsRestrictions();
        Assertions.assertEquals(conditionsRestrictions.getMaxAmount(), ConditionsRestrictions.AMOUNT_LIMIT);
    }

    @Test
    public void shouldAddDeclinedRiskDecision() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplication();

        loanApplication.addRiskDecision(TestRiskDecisionFactory.newDeclinedRiskDecision());

        Assertions.assertEquals(loanApplication.getStatus(), LoanApplication.Status.DECLINED);

        //Assert RiskDecision
        var riskDecision = loanApplication.getRiskDecision();
        Assertions.assertFalse(riskDecision.isApproved());
    }

    @Test
    public void shouldDefinePayrollIncomeConfirmationTypeForEmployeeClient() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision(
                TestClientFactory.newEmployeeUnmarriedClient());

        var incomeConfirmationType = loanApplication.defineIncomeConfirmationType(null);

        Assertions.assertEquals(incomeConfirmationType, LoanApplication.IncomeConfirmationType.PAYROLL);
    }

    @Test
    public void shouldDefineTwoNdflIncomeConfirmationTypeForEmployeeClient() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecisionAndNotActualPayroll();

        var incomeConfirmationType = loanApplication.defineIncomeConfirmationType(null);

        Assertions.assertEquals(incomeConfirmationType, LoanApplication.IncomeConfirmationType.TWO_NDFL);
    }

    @Test
    public void shouldDefineNoneIncomeConfirmationTypeForPremiumClient() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision(
                TestClientFactory.newPremiumClient());

        var incomeConfirmationType = loanApplication.defineIncomeConfirmationType(null);

        Assertions.assertEquals(incomeConfirmationType, LoanApplication.IncomeConfirmationType.NONE);
    }

    @Test
    public void shouldDefineTwoNdflIncomeConfirmationTypeForStandardClientWithPreApprovedOffer() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision(
                TestClientFactory.newStandardMarriedClient());

        var incomeConfirmationType = loanApplication.defineIncomeConfirmationType(
                TestPreApprovedOfferFactory.newPreApprovedOfferWithSmallMaxOfferAmount());

        Assertions.assertEquals(incomeConfirmationType, LoanApplication.IncomeConfirmationType.TWO_NDFL);
    }

    @Test
    public void shouldChoseNewConditionsWithoutInsurance() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision(
                TestClientFactory.newStandardMarriedClient());

        loanApplication.choseConditions(
                TestValues.NEW_CHOSEN_CONDITIONS_AMOUNT,
                TestValues.NEW_CHOSEN_CONDITIONS_PERIOD,
                false);

        var newConditions = loanApplication.getConditions();
        Assertions.assertNotNull(newConditions);
        Assertions.assertFalse(newConditions.isInsurance());
        Assertions.assertEquals(newConditions.getAmount(), TestValues.NEW_CHOSEN_CONDITIONS_AMOUNT);
        Assertions.assertEquals(newConditions.getPeriod(), TestValues.NEW_CHOSEN_CONDITIONS_PERIOD);
    }

    @Test
    public void shouldNotChoseNewConditions() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision(
                TestClientFactory.newStandardMarriedClient());

        loanApplication.choseConditions(null, null, null);

        var newConditions = loanApplication.getConditions();
        Assertions.assertNotNull(newConditions);
        Assertions.assertFalse(newConditions.isInsurance());
        Assertions.assertEquals(newConditions.getAmount(), TestValues.CONDITIONS_AMOUNT);
        Assertions.assertEquals(newConditions.getPeriod(), TestValues.CONDITIONS_PERIOD);
    }

    @Test
    public void shouldChoseNewConditionsWithInsurance() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision(
                TestClientFactory.newStandardMarriedClient());

        loanApplication.choseConditions(null, null, true);

        var newConditions = loanApplication.getConditions();
        Assertions.assertEquals(newConditions.getAmount(), TestValues.ADJUSTED_CONDITIONS_AMOUNT);
    }

    @Test
    public void shouldAddFormDocument() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision(
                TestClientFactory.newStandardMarriedClient());

        loanApplication.addDocument(TestValues.FORM_DOCUMENT_ID, Document.Type.FORM);

        var agreementDocument = loanApplication.getDocumentByType(Document.Type.FORM);
        Assertions.assertEquals(agreementDocument.getType(), Document.Type.FORM);
        Assertions.assertEquals(agreementDocument.getId(), TestValues.FORM_DOCUMENT_ID);
    }

    @Test
    public void shouldAddConditionsDocument() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision(
                TestClientFactory.newStandardMarriedClient());

        loanApplication.addDocument(TestValues.CONDITIONS_DOCUMENT_ID, Document.Type.CONDITIONS);

        var agreementDocument = loanApplication.getDocumentByType(Document.Type.CONDITIONS);
        Assertions.assertEquals(agreementDocument.getType(), Document.Type.CONDITIONS);
        Assertions.assertEquals(agreementDocument.getId(), TestValues.CONDITIONS_DOCUMENT_ID);
    }

    @Test
    public void shouldAddInsuranceDocument() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision(
                TestClientFactory.newStandardMarriedClient());

        loanApplication.addDocument(TestValues.INSURANCE_DOCUMENT_ID, Document.Type.INSURANCE);

        var agreementDocument = loanApplication.getDocumentByType(Document.Type.INSURANCE);
        Assertions.assertEquals(agreementDocument.getType(), Document.Type.INSURANCE);
        Assertions.assertEquals(agreementDocument.getId(), TestValues.INSURANCE_DOCUMENT_ID);
    }

    @Test
    public void shouldCompleteLoanApplication() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision(
                TestClientFactory.newStandardMarriedClient());

        loanApplication.complete(TestValues.ISSUANCE_ID);

        Assertions.assertEquals(loanApplication.getStatus(), LoanApplication.Status.COMPLETED);
        Assertions.assertEquals(loanApplication.getCompletedAt().toLocalDate(), LocalDate.now());
        Assertions.assertEquals(loanApplication.getIssuanceId(), TestValues.ISSUANCE_ID);
    }

    @Test
    public void shouldCalculateOfferWithoutInsurance() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithRiskDecision(
                TestClientFactory.newStandardMarriedClient());

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
    public void shouldMarkDocumentsAsSigned() {
        var loanApplication = TestLoanApplicationFactory.newLoanApplicationWithDocumentPackage();

        loanApplication.markDocumentsAsSigned();

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
