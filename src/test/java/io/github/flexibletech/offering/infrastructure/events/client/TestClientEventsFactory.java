package io.github.flexibletech.offering.infrastructure.events.client;

import io.github.flexibletech.offering.TestValues;

import java.util.List;

public class TestClientEventsFactory {
    private TestClientEventsFactory() {
    }

    private static final String MARRIED_MARITAL_STATUS_CODE = "14";
    private static final String STANDARD_CLIENT_CATEGORY = "55";

    @SuppressWarnings("ConstantConditions")
    public static ClientCreatedEvent newClientCreatedEvent() {
        return new ClientCreatedEvent(
                TestValues.CLIENT_ID,
                TestValues.NAME,
                TestValues.MIDDLE_NAME,
                TestValues.SUR_NAME,
                List.of(newPassport()),
                MARRIED_MARITAL_STATUS_CODE,
                List.of(new ClientCreatedEvent.Address(
                        ClientCreatedEvent.Address.REGISTRATION_ADDRESS,
                        TestValues.CLIENT_FULL_REGISTRATION_ADDRESS)),
                newWorkPlace(),
                TestValues.CLIENT_PHONE_NUMBER,
                TestValues.CLIENT_EMAIL,
                TestValues.CLIENT_INCOME.getValue(),
                TestValues.CLIENT_SPOUSE_INCOME.getValue(),
                STANDARD_CLIENT_CATEGORY,
                TestValues.CLIENT_BIRTH_DATE);
    }

    private static ClientCreatedEvent.Organization newWorkPlace() {
        return new ClientCreatedEvent.Organization(
                TestValues.ORGANIZATION_TITLE,
                TestValues.ORGANIZATION_INN,
                new ClientCreatedEvent.Address(
                        ClientCreatedEvent.Address.WORK_ADDRESS,
                        TestValues.ORGANIZATION_FULL_ADDRESS));
    }

    private static ClientCreatedEvent.Document newPassport() {
        return new ClientCreatedEvent.Document(
                ClientCreatedEvent.Document.PASSPORT_DOCUMENT_TYPE,
                TestValues.PASSPORT_SERIES,
                TestValues.PASSPORT_NUMBER,
                TestValues.PASSPORT_ISSUE_DATE,
                TestValues.PASSPORT_DEPARTMENT,
                TestValues.PASSPORT_DEPARTMENT_CODE);
    }
}
