package ru.t1.java.service_1.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.java.service_1.aop.annotations.HandlingResult;
import ru.t1.java.service_1.aop.annotations.LogException;
import ru.t1.java.service_1.aop.annotations.Metric;
import ru.t1.java.service_1.aop.annotations.Track;
import ru.t1.java.service_1.kafka.KafkaClientProducer;
import ru.t1.java.service_1.model.dto.ClientDto;
import ru.t1.java.service_1.service.ClientService;
import ru.t1.java.service_1.service.impl.ClientServiceImpl;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/client")
public class ClientController {
    private final ClientService clientService;
    private final ClientServiceImpl clientServiceImpl;
    private final KafkaClientProducer kafkaClientProducer;
    @Value("${t1.kafka.topic.client_registration}")
    private String topic;

    @LogException
    @Track
    @GetMapping(value = "/sendAllClient")
    @HandlingResult
    public void sendAllClients() {
        List<ClientDto> clientDtos = clientServiceImpl.parseJson();
        clientDtos.forEach(dto -> {
            kafkaClientProducer.sendTo(topic, UUID.randomUUID().toString().substring(0, 3), dto);
        });
    }

    @Metric(maxExecutionTime = 3)
    @GetMapping(value = "/get/{id}")
    public ClientDto getClientById(@PathVariable("id") long id) {
        return clientService.getClientById(id);
    }

}
