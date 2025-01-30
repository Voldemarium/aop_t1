package ru.t1.java.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.t1.java.demo.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    @Query("select id from Client c where c.clientId = ?1")
    Long findIdByClientId(@NonNull String clientId);

    @Query("select clientId from Client c where c.id = ?1")
    String findClientIdById(@NonNull Long id);
}