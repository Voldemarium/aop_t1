package ru.t1.java.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.t1.java.demo.model.Client;
import ru.t1.java.demo.model.dto.ClientDto;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientMapper {
    Client toEntity(ClientDto clientDto);

    List<ClientDto> toClientDto(List<Client> client);

    ClientDto toClientDto(Client client);

    List<Client> toEntity(List<ClientDto> clientDto);
}