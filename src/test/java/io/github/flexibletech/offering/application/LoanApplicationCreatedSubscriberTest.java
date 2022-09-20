package io.github.flexibletech.offering.application;

import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.application.client.ClientCreated;
import io.github.flexibletech.offering.application.client.ClientUpdated;
import io.github.flexibletech.offering.application.client.LoanApplicationCreatedSubscriber;
import io.github.flexibletech.offering.domain.client.Client;
import io.github.flexibletech.offering.domain.client.ClientId;
import io.github.flexibletech.offering.domain.client.ClientRepository;
import io.github.flexibletech.offering.domain.factory.TestClientFactory;
import io.github.flexibletech.offering.domain.factory.TestLoanApplicationFactory;
import io.github.flexibletech.offering.infrastructure.mapper.DomainObjectMapperImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("ConstantConditions")
public class LoanApplicationCreatedSubscriberTest extends AbstractApplicationServiceTest {
    @Spy
    private DomainObjectMapper domainObjectMapper = new DomainObjectMapperImpl(newModelMapper());
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private EventPublisher eventPublisher;
    @InjectMocks
    private LoanApplicationCreatedSubscriber loanApplicationCreatedSubscriber;

    @Captor
    private ArgumentCaptor<? extends IntegrationEvent> eventCaptor;

    @Test
    public void shouldCreateNewClient() {
        Mockito.when(clientRepository.findById(ArgumentMatchers.any(ClientId.class))).thenReturn(Optional.empty());
        Mockito.when(clientRepository.save(ArgumentMatchers.any(Client.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.doNothing().when(eventPublisher).publish(eventCaptor.capture());

        loanApplicationCreatedSubscriber.onDomainEvent(
                TestLoanApplicationFactory.newLoanApplicationCreatedDomainEvent());

        Mockito.verify(clientRepository, Mockito.times(1)).save(ArgumentMatchers.any(Client.class));

        var clientCreated = (ClientCreated) eventCaptor.getValue();
        Assertions.assertNotNull(clientCreated);

        Assertions.assertEquals(clientCreated.getId(), TestValues.CLIENT_ID);
        Assertions.assertEquals(clientCreated.getMaritalStatus(), Client.MaritalStatus.MARRIED.name());
        Assertions.assertEquals(clientCreated.getFullRegistrationAddress(), TestValues.CLIENT_FULL_REGISTRATION_ADDRESS);
        Assertions.assertEquals(clientCreated.getPhoneNumber(), TestValues.CLIENT_PHONE_NUMBER);
        Assertions.assertEquals(clientCreated.getEmail(), TestValues.CLIENT_EMAIL);
        Assertions.assertEquals(clientCreated.getName(), TestValues.NAME);
        Assertions.assertEquals(clientCreated.getMiddleName(), TestValues.MIDDLE_NAME);
        Assertions.assertEquals(clientCreated.getSurName(), TestValues.SUR_NAME);

        //Assert incomes
        Assertions.assertEquals(clientCreated.getIncome(), TestValues.CLIENT_INCOME.getValue());
        Assertions.assertEquals(clientCreated.getSpouseIncome(), TestValues.CLIENT_SPOUSE_INCOME.getValue());

        //Assert WorkPlace
        var workPlace = clientCreated.getWorkPlace();
        Assertions.assertEquals(workPlace.getTitle(), TestValues.ORGANIZATION_TITLE);
        Assertions.assertEquals(workPlace.getInn(), TestValues.ORGANIZATION_INN);
        Assertions.assertEquals(workPlace.getFullAddress(), TestValues.ORGANIZATION_FULL_ADDRESS);
    }

    @Test
    public void shouldUpdateClient() {
        Mockito.when(clientRepository.findById(ArgumentMatchers.any(ClientId.class)))
                .thenReturn(Optional.of(TestClientFactory.newStandardMarriedClient()));
        Mockito.when(clientRepository.save(ArgumentMatchers.any(Client.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.doNothing().when(eventPublisher).publish(eventCaptor.capture());

        loanApplicationCreatedSubscriber.onDomainEvent(
                TestLoanApplicationFactory.newLoanApplicationCreatedDomainEvent());

        Mockito.verify(clientRepository, Mockito.times(1)).save(ArgumentMatchers.any(Client.class));

        var clientUpdated = (ClientUpdated) eventCaptor.getValue();
        Assertions.assertNotNull(clientUpdated);

        Assertions.assertEquals(clientUpdated.getId(), TestValues.CLIENT_ID);
        Assertions.assertEquals(clientUpdated.getMaritalStatus(), Client.MaritalStatus.MARRIED.name());
        Assertions.assertEquals(clientUpdated.getFullRegistrationAddress(), TestValues.CLIENT_FULL_REGISTRATION_ADDRESS);
        Assertions.assertEquals(clientUpdated.getPhoneNumber(), TestValues.CLIENT_PHONE_NUMBER);
        Assertions.assertEquals(clientUpdated.getEmail(), TestValues.CLIENT_EMAIL);
        Assertions.assertEquals(clientUpdated.getName(), TestValues.NAME);
        Assertions.assertEquals(clientUpdated.getMiddleName(), TestValues.MIDDLE_NAME);
        Assertions.assertEquals(clientUpdated.getSurName(), TestValues.SUR_NAME);

        //Assert incomes
        Assertions.assertEquals(clientUpdated.getIncome(), TestValues.CLIENT_INCOME.getValue());
        Assertions.assertEquals(clientUpdated.getSpouseIncome(), TestValues.CLIENT_SPOUSE_INCOME.getValue());

        //Assert WorkPlace
        var workPlace = clientUpdated.getWorkPlace();
        Assertions.assertEquals(workPlace.getTitle(), TestValues.ORGANIZATION_TITLE);
        Assertions.assertEquals(workPlace.getInn(), TestValues.ORGANIZATION_INN);
        Assertions.assertEquals(workPlace.getFullAddress(), TestValues.ORGANIZATION_FULL_ADDRESS);
    }

}
