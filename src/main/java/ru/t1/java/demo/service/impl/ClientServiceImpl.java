package ru.t1.java.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.annotations.HandlingResult;
import ru.t1.java.demo.aop.annotations.LogExecution;
import ru.t1.java.demo.aop.annotations.Track;
import ru.t1.java.demo.kafka.KafkaClientProducer;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.dto.ClientDto;
import ru.t1.java.demo.repository.ClientRepository;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ImplService<Client, ClientDto> {
    private final ClientRepository repository;
    private final KafkaClientProducer kafkaClientProducer;

    @Override
    public void registerEvents(List<Client> clients) {
        repository.saveAll(clients)          // сохраняем список в БД
                .stream()
                .map(Client::getId)
                .forEach(kafkaClientProducer::send); // отправляем сообщения в Kafka с сохраненным Id клиента
    }

    @Override
    @LogExecution
    @Track
    @HandlingResult
    public List<ClientDto> parseJson() {
        ObjectMapper mapper = new ObjectMapper();

        ClientDto[] clients;
        try {
            clients = mapper.readValue(new File("src/main/resources/MOCK_CLIENT.json"), ClientDto[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Arrays.asList(clients);
    }
}
