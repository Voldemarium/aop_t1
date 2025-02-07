package ru.t1.java.service_1.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.t1.java.service_1.model.DataSourceErrorLog;
import ru.t1.java.service_1.model.dto.DataSourceErrorLogDto;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface DataSourceErrorLogMapper {
    DataSourceErrorLog toEntity(DataSourceErrorLogDto errorLogDto);

    DataSourceErrorLogDto toDto(DataSourceErrorLog errorLog);

    List<DataSourceErrorLogDto> toDataSourceErrorLogDto(List<DataSourceErrorLog> dataSourceErrorLog);

    List<DataSourceErrorLog> toEntity(List<DataSourceErrorLogDto> dataSourceErrorLogDto);

}
