package ru.t1.java.demo.mapper;

import org.mapstruct.*;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.model.dto.DataSourceErrorLogDto;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface DataSourceErrorLogMapper {
    DataSourceErrorLog toEntity(DataSourceErrorLogDto errorLogDto);

    DataSourceErrorLogDto toDto(DataSourceErrorLog errorLog);

    List<DataSourceErrorLogDto> toDataSourceErrorLogDto(List<DataSourceErrorLog> dataSourceErrorLog);

    List<DataSourceErrorLog> toEntity(List<DataSourceErrorLogDto> dataSourceErrorLogDto);

}
