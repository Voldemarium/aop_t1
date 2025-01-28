package ru.t1.java.demo.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.annotations.LogExecution;
import ru.t1.java.demo.dto.ClientDto;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.repository.ClientRepository;
import ru.t1.java.demo.util.ClientMapper;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService {
    private final ClientRepository repository;
    private final Map<Long, Client> cache;

    @PostConstruct
    void init() {
        getClientById(1L);
    }

    @LogExecution
    public ClientDto getClientById(Long id) {
        log.debug("Call method getClient with id {}", id);
        ClientDto clientDto;

        if (cache.containsKey(id)) {
            return ClientMapper.toDto(cache.get(id));
        }

        Client entity = repository.findById(id).orElseThrow();
        clientDto = ClientMapper.toDto(entity);
        cache.put(id, entity);
        return clientDto;
    }

}
