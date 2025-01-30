package ru.t1.java.demo.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.annotations.LogExecution;
import ru.t1.java.demo.mapper.ClientMapper;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.dto.ClientDto;
import ru.t1.java.demo.repository.ClientRepository;
//import ru.t1.java.demo.util.ClientMapper;


@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository repository;
    private final ClientMapper mapper;

    @PostConstruct
    void init() {
        getClientById(1L);
    }

    @LogExecution
    public ClientDto getClientById(Long id) {
        ClientDto clientDto;
        Client entity = repository.findById(id).get();
//        clientDto = ClientMapper.toDto(entity);
        clientDto = mapper.toClientDto(entity);
        return clientDto;
    }

    public Long getIdByClientId(String clientId ) {
        return repository.findIdByClientId(clientId );
    }

    public String getClientIdById(Long id) {
        return repository.findClientIdById(id);
    }
}
