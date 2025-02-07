package ru.t1.java.service_1.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.t1.java.service_1.aop.annotations.HandlingResult;
import ru.t1.java.service_1.aop.annotations.LogExecution;
import ru.t1.java.service_1.aop.annotations.Track;
import ru.t1.java.service_1.kafka.KafkaClientProducer;
import ru.t1.java.service_1.model.Client;
import ru.t1.java.service_1.model.dto.ClientDto;
import ru.t1.java.service_1.repository.ClientRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientServiceImpl implements ImplService<Client, ClientDto> {
    private final ClientRepository repository;
    private final KafkaClientProducer kafkaClientProducer;
    @Value("${t1.mock_client}")
    private String mock;

    @Override
    public void registerEvents(List<Client> clients) {
        clients = clients.stream().filter(client -> client.getClientId() != null &&
                client.getFirstName() != null &&
                client.getLastName() != null).toList();
        if (!clients.isEmpty()) {
            repository.saveAll(clients)          // сохраняем список в БД
                    .stream()
                    .map(Client::getClientId)
                    .forEach(kafkaClientProducer::send); // отправляем сообщения в Kafka с сохраненным Id клиента
        }
    }

    @Override
    @LogExecution
    @Track
    @HandlingResult
    public List<ClientDto> parseJson() {
        ObjectMapper mapper = new ObjectMapper();

        ClientDto[] clients;
        try {
            Path filePath = Paths.get(mock);
            String stringJson = Files.readString(filePath);
            clients = mapper.readValue(stringJson, ClientDto[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Arrays.asList(clients);
    }
}
