package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.t1.java.demo.aop.annotations.HandlingResult;
import ru.t1.java.demo.aop.annotations.Track;
import ru.t1.java.demo.aop.annotations.LogException;
import ru.t1.java.demo.dto.ClientDto;
import ru.t1.java.demo.exception.ClientException;
import ru.t1.java.demo.service.ClientService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping(value = "/client")
public class ClientController {
    private final ClientService clientService;

    @LogException
    @Track
    @GetMapping(value = "/something")
    @HandlingResult
    public void doSomething() throws IOException, InterruptedException {
//        try {
//            clientService.parseJson();
        Thread.sleep(3000L);
        throw new ClientException();
//        } catch (Exception e) {
//            log.info("Catching exception from ClientController");
//            throw new ClientException();
//        }
    }

    @GetMapping(value = "/get/{id}")
    public ClientDto getClientById(@PathVariable("id") long id) {
        return clientService.getClientById(id);
    }

}
