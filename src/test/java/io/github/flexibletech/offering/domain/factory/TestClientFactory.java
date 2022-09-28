package io.github.flexibletech.offering.domain.factory;

import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.domain.client.Client;
import io.github.flexibletech.offering.domain.client.ClientDetails;
import io.github.flexibletech.offering.domain.client.ClientId;

public class TestClientFactory {
    private TestClientFactory() {
    }

    public static Client newStandardMarriedClient() {
        return Client.newBuilder()
                .withId(new ClientId(TestValues.CLIENT_ID))
                .withPersonNameDetails(TestValues.NAME, TestValues.MIDDLE_NAME, TestValues.SUR_NAME)
                .withPassport(TestValues.PASSPORT_SERIES, TestValues.PASSPORT_NUMBER, TestValues.PASSPORT_ISSUE_DATE,
                        TestValues.PASSPORT_DEPARTMENT, TestValues.PASSPORT_DEPARTMENT_CODE)
                .withWorkplace(TestValues.ORGANIZATION_TITLE, TestValues.ORGANIZATION_INN, TestValues.ORGANIZATION_FULL_ADDRESS)
                .withMaritalStatus(Client.MaritalStatus.MARRIED)
                .withCategory(Client.Category.STANDARD)
                .withEmail(TestValues.CLIENT_EMAIL)
                .withBirthDate(TestValues.CLIENT_BIRTH_DATE)
                .withIncome(TestValues.CLIENT_INCOME)
                .withSpouseIncome(TestValues.CLIENT_SPOUSE_INCOME)
                .withPhoneNumber(TestValues.CLIENT_PHONE_NUMBER)
                .withFullRegistrationAddress(TestValues.CLIENT_FULL_REGISTRATION_ADDRESS)
                .build();
    }

    public static Client newPayrollUnmarriedClient() {
        return Client.newBuilder()
                .withId(new ClientId(TestValues.CLIENT_ID))
                .withPersonNameDetails(TestValues.NAME, TestValues.MIDDLE_NAME, TestValues.SUR_NAME)
                .withPassport(TestValues.PASSPORT_SERIES, TestValues.PASSPORT_NUMBER, TestValues.PASSPORT_ISSUE_DATE,
                        TestValues.PASSPORT_DEPARTMENT, TestValues.PASSPORT_DEPARTMENT_CODE)
                .withWorkplace(TestValues.ORGANIZATION_TITLE, TestValues.ORGANIZATION_INN, TestValues.ORGANIZATION_FULL_ADDRESS)
                .withMaritalStatus(Client.MaritalStatus.UNMARRIED)
                .withCategory(Client.Category.PAYROLL)
                .withEmail(TestValues.CLIENT_EMAIL)
                .withBirthDate(TestValues.CLIENT_BIRTH_DATE)
                .withIncome(TestValues.CLIENT_INCOME)
                .withSpouseIncome(null)
                .withPhoneNumber(TestValues.CLIENT_PHONE_NUMBER)
                .withFullRegistrationAddress(TestValues.CLIENT_FULL_REGISTRATION_ADDRESS)
                .build();
    }

    public static Client newPremiumClient() {
        return Client.newBuilder()
                .withId(new ClientId(TestValues.CLIENT_ID))
                .withPersonNameDetails(TestValues.NAME, TestValues.MIDDLE_NAME, TestValues.SUR_NAME)
                .withPassport(TestValues.PASSPORT_SERIES, TestValues.PASSPORT_NUMBER, TestValues.PASSPORT_ISSUE_DATE,
                        TestValues.PASSPORT_DEPARTMENT, TestValues.PASSPORT_DEPARTMENT_CODE)
                .withWorkplace(TestValues.ORGANIZATION_TITLE, TestValues.ORGANIZATION_INN, TestValues.ORGANIZATION_FULL_ADDRESS)
                .withMaritalStatus(Client.MaritalStatus.UNMARRIED)
                .withCategory(Client.Category.PREMIUM)
                .withEmail(TestValues.CLIENT_EMAIL)
                .withBirthDate(TestValues.CLIENT_BIRTH_DATE)
                .withIncome(TestValues.CLIENT_INCOME)
                .withSpouseIncome(null)
                .withPhoneNumber(TestValues.CLIENT_PHONE_NUMBER)
                .withFullRegistrationAddress(TestValues.CLIENT_FULL_REGISTRATION_ADDRESS)
                .build();
    }

    public static Client newUnmarriedClientWithSpouseIncome() {
        return Client.newBuilder()
                .withId(new ClientId(TestValues.CLIENT_ID))
                .withPersonNameDetails(TestValues.NAME, TestValues.MIDDLE_NAME, TestValues.SUR_NAME)
                .withPassport(TestValues.PASSPORT_SERIES, TestValues.PASSPORT_NUMBER, TestValues.PASSPORT_ISSUE_DATE,
                        TestValues.PASSPORT_DEPARTMENT, TestValues.PASSPORT_DEPARTMENT_CODE)
                .withWorkplace(TestValues.ORGANIZATION_TITLE, TestValues.ORGANIZATION_INN, TestValues.ORGANIZATION_FULL_ADDRESS)
                .withMaritalStatus(Client.MaritalStatus.UNMARRIED)
                .withCategory(Client.Category.PREMIUM)
                .withEmail(TestValues.CLIENT_EMAIL)
                .withBirthDate(TestValues.CLIENT_BIRTH_DATE)
                .withIncome(TestValues.CLIENT_INCOME)
                .withSpouseIncome(TestValues.CLIENT_SPOUSE_INCOME)
                .withPhoneNumber(TestValues.CLIENT_PHONE_NUMBER)
                .withFullRegistrationAddress(TestValues.CLIENT_FULL_REGISTRATION_ADDRESS)
                .build();
    }

    public static ClientDetails newClientDetailsForCreate() {
        return new ClientDetails(
                new ClientId(TestValues.CLIENT_ID),
                TestValues.NAME,
                TestValues.MIDDLE_NAME,
                TestValues.SUR_NAME,
                TestValues.PASSPORT_SERIES,
                TestValues.PASSPORT_NUMBER,
                TestValues.PASSPORT_ISSUE_DATE,
                TestValues.PASSPORT_DEPARTMENT,
                TestValues.PASSPORT_DEPARTMENT_CODE,
                Client.MaritalStatus.MARRIED,
                TestValues.ORGANIZATION_TITLE,
                TestValues.ORGANIZATION_INN,
                TestValues.ORGANIZATION_FULL_ADDRESS,
                TestValues.CLIENT_FULL_REGISTRATION_ADDRESS,
                TestValues.CLIENT_PHONE_NUMBER,
                TestValues.CLIENT_EMAIL,
                TestValues.CLIENT_INCOME,
                TestValues.CLIENT_SPOUSE_INCOME,
                Client.Category.STANDARD,
                TestValues.CLIENT_BIRTH_DATE);
    }

    public static ClientDetails newClientDetailsForUpdate() {
        return new ClientDetails(
                new ClientId(TestValues.CLIENT_ID),
                TestValues.NEW_NAME,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                Client.MaritalStatus.UNMARRIED,
                null,
                TestValues.ORGANIZATION_NEW_INN,
                null,
                null,
                null,
                null,
                TestValues.CLIENT_NEW_INCOME,
                null,
                Client.Category.PREMIUM,
                null);
    }
}
