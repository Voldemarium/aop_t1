package ru.t1.java.demo.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.annotations.LogDataSourceError;
import ru.t1.java.demo.aop.annotations.LogExecution;
import ru.t1.java.demo.mapper.ClientMapper;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.dto.ClientDto;
import ru.t1.java.demo.repository.ClientRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository repository;
    private final ClientMapper mapper;

    @LogExecution
    @LogDataSourceError
    public ClientDto getClientById(Long id) {
        Client entity = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        return mapper.toClientDto(entity);
    }

    @LogDataSourceError
    public Long getIdByClientId(String clientId ) {
        return repository.findIdByClientId(clientId).orElseThrow(EntityNotFoundException::new);
    }

    @LogDataSourceError
    public String getClientIdById(Long id) {
        return repository.findClientIdById(id).orElseThrow(EntityNotFoundException::new);
    }
}
