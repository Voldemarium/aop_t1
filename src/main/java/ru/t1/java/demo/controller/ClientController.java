package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.java.demo.aop.annotations.HandlingResult;
import ru.t1.java.demo.aop.annotations.Metric;
import ru.t1.java.demo.aop.annotations.Track;
import ru.t1.java.demo.aop.annotations.LogException;
import ru.t1.java.demo.kafka.KafkaClientProducer;
import ru.t1.java.demo.model.dto.ClientDto;
import ru.t1.java.demo.exception.ClientException;
import ru.t1.java.demo.service.ClientService;
import ru.t1.java.demo.service.impl.ClientServiceImpl;

import java.io.IOException;
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
