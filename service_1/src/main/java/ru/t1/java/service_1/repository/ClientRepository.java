package ru.t1.java.service_1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.t1.java.service_1.model.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("select id from Client c where c.clientId = ?1")
    Optional<Long> findIdByClientId(@NonNull String clientId);

    @Query("select clientId from Client c where c.id = ?1")
    Optional<String> findClientIdById(@NonNull Long id);
}