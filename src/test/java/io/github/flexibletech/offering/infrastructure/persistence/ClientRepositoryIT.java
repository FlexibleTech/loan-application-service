package io.github.flexibletech.offering.infrastructure.persistence;

import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.domain.client.ClientId;
import io.github.flexibletech.offering.domain.client.ClientRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

public class ClientRepositoryIT extends AbstractPersistenceTest {
    @Autowired
    private ClientRepository clientRepository;

    @Test
    @Transactional
    @Sql("/sql/insert_client.sql")
    public void shouldFindByClientId() {
        var client = clientRepository.findById(new ClientId(TestValues.CLIENT_ID));

        Assertions.assertTrue(client.isPresent());
    }

}
