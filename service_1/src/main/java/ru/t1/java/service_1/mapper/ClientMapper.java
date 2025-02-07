package ru.t1.java.service_1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.t1.java.service_1.model.Client;
import ru.t1.java.service_1.model.dto.ClientDto;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientMapper {
    Client toEntity(ClientDto clientDto);

    List<ClientDto> toClientDto(List<Client> client);

    ClientDto toClientDto(Client client);

    List<Client> toEntity(List<ClientDto> clientDto);
}