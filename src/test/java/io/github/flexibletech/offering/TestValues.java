package io.github.flexibletech.offering;

import io.github.flexibletech.offering.domain.Amount;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TestValues {
    private TestValues() {
    }

    public static final String LOAN_APPLICATION_ID = "LOANAPP22082500001";

    //Client values
    public static final String CLIENT_ID = "20056671";
    public static final LocalDate CLIENT_BIRTH_DATE = LocalDate.parse("1986-01-22");
    public static final String CLIENT_FULL_REGISTRATION_ADDRESS = "125009, город Москва, Тверская ул. 10 с1";
    public static final String CLIENT_PHONE_NUMBER = "79851325677";
    public static final String CLIENT_EMAIL = "test@mail.ru";
    public static final Amount CLIENT_INCOME = Amount.fromValue(BigDecimal.valueOf(100_000));
    public static final Amount CLIENT_SPOUSE_INCOME = Amount.fromValue(BigDecimal.valueOf(70_000));
    //PersonNameDetails values
    public static final String NAME = "Тест";
    public static final String MIDDLE_NAME = "Тестович";
    public static final String SUR_NAME = "Тестов";
    //Passport values
    public static final String PASSPORT_SERIES = "4401";
    public static final String PASSPORT_NUMBER = "190134";
    public static final LocalDate PASSPORT_ISSUE_DATE = LocalDate.parse("2000-09-02");
    public static final String PASSPORT_DEPARTMENT = "ОВД КИТАЙ-ГОРОД 1 РУВД ЦАО Г. МОСКВЫ";
    public static final String PASSPORT_DEPARTMENT_CODE = "772-001";
    //Organization values
    public static final String ORGANIZATION_TITLE = "ООО \"В Контакте\"";
    public static final String ORGANIZATION_INN = "7842349892";
    public static final String ORGANIZATION_FULL_ADDRESS = "191024, ГОРОД САНКТ-ПЕТЕРБУРГ, УЛИЦА ХЕРСОНСКАЯ, ДОМ 12-14, ЛИТЕР А, ПОМЕЩЕНИЕ 1Н";

    //PreApprovedOffer values
    public static final Amount PRE_APPROVED_OFFER_MIN_AMOUNT = Amount.fromValue(BigDecimal.valueOf(50_000));
    public static final Amount PRE_APPROVED_OFFER_MAX_AMOUNT = Amount.fromValue(BigDecimal.valueOf(1_300000));
    public static final Amount PRE_APPROVED_OFFER_SMALL_MAX_AMOUNT = Amount.fromValue(BigDecimal.valueOf(100_000));
    public static final String PRE_APPROVED_OFFER_ID = "PO20220721001";

    //Conditions
    public static final Amount CONDITIONS_AMOUNT = Amount.fromValue(new BigDecimal(500_000));
    public static final int CONDITIONS_PERIOD = 20;
    public static final Amount NEW_CHOSEN_CONDITIONS_AMOUNT = Amount.fromValue(new BigDecimal(700_000));
    public static final int NEW_CHOSEN_CONDITIONS_PERIOD = 30;
    public static final Amount ADJUSTED_CONDITIONS_AMOUNT = Amount.fromValue(BigDecimal.valueOf(689333.333334));

    //RiskDecision
    public static final String RISK_DECISION_ID = "RSK202200911";
    //Payroll
    public static final Amount PAYROLL_SALARY = Amount.fromValue(BigDecimal.valueOf(90_000));
    public static final Amount PAYROLL_SALARY_LESS_THAN_INCOME = Amount.fromValue(BigDecimal.valueOf(60_000));
    public static final LocalDate PAYROLL_LAST_SALARY_DATE = LocalDate.now().minusDays(8);
    public static final LocalDate PAYROLL_LAST_SALARY_DATE_LESS_THAN_CURRENT_DATE = LocalDate.now().minusDays(5);
    //ConditionsRestrictions
    public static final Amount CONDITIONS_RESTRICTIONS_MAX_AMOUNT = Amount.fromValue(BigDecimal.valueOf(1_000000));
    public static final Amount CONDITIONS_RESTRICTIONS_LARGE_MAX_AMOUNT = Amount.fromValue(BigDecimal.valueOf(4_200000));
    public static final int CONDITIONS_RESTRICTIONS_MAX_PERIOD = 60;

    //Document
    public static final String FORM_DOCUMENT_ID = "e5dde60e-7225-49f8-a9eb-a31e2acd239f";
    public static final String CONDITIONS_DOCUMENT_ID = "8b748bc8-8011-4392-b478-d95bb83a0ffe";
    public static final String INSURANCE_DOCUMENT_ID = "3e417929-9c48-4fcd-9099-d9a93b643ef5";
    public static final String FILE_NAME = "test.pdf";

    //Issuance
    public static final String ISSUANCE_ID = "IS202200004";

    //Offer
    public static final Amount OFFER_AVERAGE_MONTHLY_PAYMENT = Amount.fromValue(BigDecimal.valueOf(29963.837491));
    public static final LocalDate OFFER_FIRST_PAYMENT_DATE = LocalDate.now().plusMonths(1);
    public static final LocalDate OFFER_LAST_PAYMENT_DATE = LocalDate.now().plusMonths(CONDITIONS_PERIOD + 1);
    public static final Amount OFFER_SINGLE_INSURANCE_PAYMENT = Amount.fromValue(BigDecimal.valueOf(189333.333334));
    public static final Amount OFFER_INSURANCE_PREMIUM = Amount.fromValue(BigDecimal.valueOf(21000));
}
