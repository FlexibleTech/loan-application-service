package io.github.flexibletech.offering.domain.client;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, ClientId> {

    boolean existsById(@NotNull ClientId id);

}
