package ru.t1.java.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for {@link ru.t1.java.demo.model.Client}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSourceErrorLogDto {
    @JsonProperty("stack_trace")
    private String stackTrace;
    @JsonProperty("message")
    private String message;
    @JsonProperty("methodSignature")
    private String methodSignature;

}