package io.github.flexibletech.offering.application.client;

import io.github.flexibletech.offering.domain.client.Client;
import io.github.flexibletech.offering.domain.client.ClientDetails;
import io.github.flexibletech.offering.domain.client.ClientId;
import io.github.flexibletech.offering.domain.client.ClientRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void createNewClient(ClientDetails clientDetails) {
        log.info("Client {} creation...", clientDetails.getClientId().toString());
        var client = Client.newBuilder()
                .withId(clientDetails.getClientId())
                .withPersonNameDetails( //Create person name details
                        clientDetails.getName(),
                        clientDetails.getMiddleName(),
                        clientDetails.getSurName())
                .withPassport( //Create passport
                        clientDetails.getPassportSeries(),
                        clientDetails.getPassportNumber(),
                        clientDetails.getPassportIssueDate(),
                        clientDetails.getPassportDepartment(),
                        clientDetails.getPassportDepartmentCode())
                .withMaritalStatus(clientDetails.getMaritalStatus())
                .withWorkplace( //Create work place
                        clientDetails.getWorkPlaceTitle(),
                        clientDetails.getWorkPlaceInn(),
                        clientDetails.getWorkPlaceFullAddress())
                .withFullRegistrationAddress(clientDetails.getFullRegistrationAddress())
                .withPhoneNumber(clientDetails.getPhoneNumber())
                .withEmail(clientDetails.getEmail())
                .withIncome(clientDetails.getIncome())
                .withSpouseIncome(clientDetails.getSpouseIncome())
                .withCategory(clientDetails.getCategory())
                .withBirthDate(clientDetails.getBirthDate())
                .build();

        clientRepository.save(client);
        log.info("Client {} has been created", clientDetails.getClientId().toString());
    }

    public void updateClient(String clientId, ClientDetails clientDetails) {
        log.info("Client {} updating...", clientDetails);
        clientRepository.findById(new ClientId(clientId))
                .ifPresentOrElse(
                        client -> {
                            client.update(clientDetails);
                            log.info("Client {} has been updated", client);
                        },
                        () -> log.info("Client {} is not found", clientId));
    }

}
