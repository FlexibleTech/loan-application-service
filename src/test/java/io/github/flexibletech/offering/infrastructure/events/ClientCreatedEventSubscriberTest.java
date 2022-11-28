package io.github.flexibletech.offering.infrastructure.events;

import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.application.client.ClientService;
import io.github.flexibletech.offering.domain.client.Client;
import io.github.flexibletech.offering.domain.client.ClientDetails;
import io.github.flexibletech.offering.infrastructure.events.client.ClientCreatedEventSubscriber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class ClientCreatedEventSubscriberTest {
    @Mock
    private ClientService clientService;
    @Spy
    private ModelMapper mapper;

    @InjectMocks
    private ClientCreatedEventSubscriber clientCreatedEventSubscriber;

    @Captor
    private ArgumentCaptor<ClientDetails> clientDetailsArgumentCaptor;

    @Test
    public void shouldReceiveClientCreatedEvent() {
        Mockito.doNothing().when(clientService).createNewClient(clientDetailsArgumentCaptor.capture());

        clientCreatedEventSubscriber.accept(TestClientEventsFactory.newClientCreatedEvent());

        var clientDetails = clientDetailsArgumentCaptor.getValue();
        Assertions.assertNotNull(clientDetails);
        Assertions.assertEquals(clientDetails.getClientId().toString(), TestValues.CLIENT_ID);
        Assertions.assertEquals(clientDetails.getCategory(), Client.Category.STANDARD);
        Assertions.assertEquals(clientDetails.getMaritalStatus(), Client.MaritalStatus.MARRIED);
        Assertions.assertEquals(clientDetails.getFullRegistrationAddress(), TestValues.CLIENT_FULL_REGISTRATION_ADDRESS);
        Assertions.assertEquals(clientDetails.getPhoneNumber(), TestValues.CLIENT_PHONE_NUMBER);
        Assertions.assertEquals(clientDetails.getEmail(), TestValues.CLIENT_EMAIL);
        Assertions.assertEquals(clientDetails.getIncome(), TestValues.CLIENT_INCOME);
        Assertions.assertEquals(clientDetails.getSpouseIncome(), TestValues.CLIENT_SPOUSE_INCOME);
        Assertions.assertEquals(clientDetails.getName(), TestValues.NAME);
        Assertions.assertEquals(clientDetails.getMiddleName(), TestValues.MIDDLE_NAME);
        Assertions.assertEquals(clientDetails.getSurName(), TestValues.SUR_NAME);
        Assertions.assertEquals(clientDetails.getWorkPlaceTitle(), TestValues.ORGANIZATION_TITLE);
        Assertions.assertEquals(clientDetails.getWorkPlaceInn(), TestValues.ORGANIZATION_INN);
        Assertions.assertEquals(clientDetails.getWorkPlaceFullAddress(), TestValues.ORGANIZATION_FULL_ADDRESS);
    }

}
