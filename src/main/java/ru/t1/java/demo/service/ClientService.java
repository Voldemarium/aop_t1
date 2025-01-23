package ru.t1.java.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.annotations.LogDataSourceError;
import ru.t1.java.demo.model.dto.ClientDto;
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

    @LogDataSourceError
    public ClientDto getClientById(Long id) {
        if (cache.containsKey(id)) {
            return ClientMapper.toDto(cache.get(id));
        }
        Client entity = repository.findById(id).orElseThrow();
        cache.put(id, entity);
        return ClientMapper.toDto(entity);

    }
}
