package io.github.flexibletech.offering.domain.factory;

import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.domain.client.Client;
import io.github.flexibletech.offering.domain.client.Organization;
import io.github.flexibletech.offering.domain.client.Passport;
import io.github.flexibletech.offering.domain.client.PersonNameDetails;

public class TestClientFactory {
    private TestClientFactory() {
    }

    public static Client newStandardMarriedClient() {
        return new Client(
                TestValues.CLIENT_ID,
                newPersonNameDetails(),
                newPassport(),
                Client.MaritalStatus.MARRIED,
                newOrganization(),
                TestValues.CLIENT_FULL_REGISTRATION_ADDRESS,
                TestValues.CLIENT_PHONE_NUMBER,
                TestValues.CLIENT_EMAIL,
                TestValues.CLIENT_INCOME,
                TestValues.CLIENT_SPOUSE_INCOME,
                Client.Category.STANDARD,
                TestValues.CLIENT_BIRTH_DATE);
    }

    public static Client newEmployeeUnmarriedClient() {
        return new Client(
                TestValues.CLIENT_ID,
                newPersonNameDetails(),
                newPassport(),
                Client.MaritalStatus.UNMARRIED,
                newOrganization(),
                TestValues.CLIENT_FULL_REGISTRATION_ADDRESS,
                TestValues.CLIENT_PHONE_NUMBER,
                TestValues.CLIENT_EMAIL,
                TestValues.CLIENT_INCOME,
                null,
                Client.Category.EMPLOYEE,
                TestValues.CLIENT_BIRTH_DATE);
    }

    public static Client newPremiumClient() {
        return new Client(
                TestValues.CLIENT_ID,
                newPersonNameDetails(),
                newPassport(),
                Client.MaritalStatus.MARRIED,
                newOrganization(),
                TestValues.CLIENT_FULL_REGISTRATION_ADDRESS,
                TestValues.CLIENT_PHONE_NUMBER,
                TestValues.CLIENT_EMAIL,
                TestValues.CLIENT_INCOME,
                TestValues.CLIENT_SPOUSE_INCOME,
                Client.Category.PREMIUM,
                TestValues.CLIENT_BIRTH_DATE);
    }

    public static Client newUnmarriedClientWithSpouseIncome() {
        return new Client(
                TestValues.CLIENT_ID,
                newPersonNameDetails(),
                newPassport(),
                Client.MaritalStatus.UNMARRIED,
                newOrganization(),
                TestValues.CLIENT_FULL_REGISTRATION_ADDRESS,
                TestValues.CLIENT_PHONE_NUMBER,
                TestValues.CLIENT_EMAIL,
                TestValues.CLIENT_INCOME,
                TestValues.CLIENT_SPOUSE_INCOME,
                Client.Category.PREMIUM,
                TestValues.CLIENT_BIRTH_DATE);
    }

    private static PersonNameDetails newPersonNameDetails() {
        return new PersonNameDetails(
                TestValues.NAME,
                TestValues.MIDDLE_NAME,
                TestValues.SUR_NAME);
    }

    private static Organization newOrganization() {
        return new Organization(
                TestValues.ORGANIZATION_TITLE,
                TestValues.ORGANIZATION_INN,
                TestValues.ORGANIZATION_FULL_ADDRESS);
    }

    private static Passport newPassport() {
        return new Passport(
                TestValues.PASSPORT_SERIES,
                TestValues.PASSPORT_NUMBER,
                TestValues.PASSPORT_ISSUE_DATE,
                TestValues.PASSPORT_DEPARTMENT,
                TestValues.PASSPORT_DEPARTMENT_CODE);
    }

}
