package io.github.flexibletech.offering.application;

import io.github.flexibletech.offering.application.client.ClientService;
import io.github.flexibletech.offering.domain.client.Client;
import io.github.flexibletech.offering.domain.client.ClientRepository;
import io.github.flexibletech.offering.domain.factory.TestClientFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    public ClientService clientService;

    @Captor
    private ArgumentCaptor<Client> argumentCaptor;

    @Test
    public void shouldCreateNewClient() {
        Mockito.when(clientRepository.save(argumentCaptor.capture()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        clientService.createNewClient(TestClientFactory.newClientDetailsForCreate());

        var client = argumentCaptor.getValue();
        Assertions.assertNotNull(client);
    }

}
