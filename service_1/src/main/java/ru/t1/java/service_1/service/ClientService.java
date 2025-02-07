package ru.t1.java.service_1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.service_1.aop.annotations.LogDataSourceError;
import ru.t1.java.service_1.aop.annotations.LogExecution;
import ru.t1.java.service_1.mapper.ClientMapper;
import ru.t1.java.service_1.model.Client;
import ru.t1.java.service_1.model.dto.ClientDto;
import ru.t1.java.service_1.repository.ClientRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository repository;
    private final ClientMapper mapper;

    @LogExecution
    public ClientDto getClientById(Long id) {
        Client entity = repository.findById(id).orElseThrow();
        return mapper.toClientDto(entity);
    }

    @LogDataSourceError
    public Long getIdByClientId(String clientId ) {
        return repository.findIdByClientId(clientId).orElseThrow();
    }

    @LogDataSourceError
    public String getClientIdById(Long id) {
        return repository.findClientIdById(id).orElseThrow();
    }
}
