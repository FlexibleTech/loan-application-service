package io.github.flexibletech.offering.domain;

import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.domain.client.Client;
import io.github.flexibletech.offering.domain.client.ClientId;
import io.github.flexibletech.offering.domain.factory.TestClientFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClientTest {

    @Test
    public void shouldBuildNewClient() {
        var client = Client.newBuilder()
                .withId(new ClientId(TestValues.CLIENT_ID))
                .withPersonNameDetails(TestValues.NAME, TestValues.MIDDLE_NAME, TestValues.SUR_NAME)
                .withPassport(TestValues.PASSPORT_SERIES, TestValues.PASSPORT_NUMBER, TestValues.PASSPORT_ISSUE_DATE,
                        TestValues.PASSPORT_DEPARTMENT, TestValues.PASSPORT_DEPARTMENT_CODE)
                .withMaritalStatus(Client.MaritalStatus.MARRIED)
                .withWorkplace(TestValues.ORGANIZATION_TITLE, TestValues.ORGANIZATION_INN, TestValues.ORGANIZATION_FULL_ADDRESS)
                .withFullRegistrationAddress(TestValues.CLIENT_FULL_REGISTRATION_ADDRESS)
                .withPhoneNumber(TestValues.CLIENT_PHONE_NUMBER)
                .withEmail(TestValues.CLIENT_EMAIL)
                .withIncome(TestValues.CLIENT_INCOME)
                .withSpouseIncome(TestValues.CLIENT_SPOUSE_INCOME)
                .withCategory(Client.Category.STANDARD)
                .withBirthDate(TestValues.CLIENT_BIRTH_DATE)
                .build();

        assertNewClient(client);
    }

    private void assertNewClient(Client client) {
        Assertions.assertNotNull(client);
        Assertions.assertEquals(client.getCategory(), Client.Category.STANDARD);
        Assertions.assertEquals(client.getId().toString(), TestValues.CLIENT_ID);
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
    public void shouldCheckSpouseIncomeForUnmarriedClient() {
        var client = TestClientFactory.newUnmarriedClientWithSpouseIncome();

        Assertions.assertTrue(client.hasSpouseIncomeAndUnmarried());
    }

    @Test
    public void shouldUpdateClient() {
        var client = TestClientFactory.newStandardMarriedClient();

        client.update(TestClientFactory.newClientDetailsForUpdate());

        assertUpdatedClient(client);
    }

    private void assertUpdatedClient(Client client) {
        Assertions.assertEquals(client.getId().toString(), TestValues.CLIENT_ID);
        Assertions.assertEquals(client.getCategory(), Client.Category.PREMIUM);
        Assertions.assertEquals(client.getMaritalStatus(), Client.MaritalStatus.UNMARRIED);
        Assertions.assertEquals(client.getFullRegistrationAddress(), TestValues.CLIENT_FULL_REGISTRATION_ADDRESS);
        Assertions.assertEquals(client.getPhoneNumber(), TestValues.CLIENT_PHONE_NUMBER);
        Assertions.assertEquals(client.getEmail(), TestValues.CLIENT_EMAIL);

        //Assert incomes
        Assertions.assertEquals(client.getIncome(), TestValues.CLIENT_NEW_INCOME);
        Assertions.assertEquals(client.getSpouseIncome(), TestValues.CLIENT_SPOUSE_INCOME);

        //Assert PersonNameDetails
        var personNameDetails = client.getPersonNameDetails();
        Assertions.assertEquals(personNameDetails.getName(), TestValues.NEW_NAME);
        Assertions.assertEquals(personNameDetails.getMiddleName(), TestValues.MIDDLE_NAME);
        Assertions.assertEquals(personNameDetails.getSurName(), TestValues.SUR_NAME);

        //Assert WorkPlace
        var workPlace = client.getWorkPlace();
        Assertions.assertEquals(workPlace.getTitle(), TestValues.ORGANIZATION_TITLE);
        Assertions.assertEquals(workPlace.getInn(), TestValues.ORGANIZATION_NEW_INN);
        Assertions.assertEquals(workPlace.getFullAddress(), TestValues.ORGANIZATION_FULL_ADDRESS);
    }

}
