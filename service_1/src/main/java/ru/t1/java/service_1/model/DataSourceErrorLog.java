package ru.t1.java.service_1.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "data_source_error_log")
@NoArgsConstructor

public class DataSourceErrorLog extends AbstractEntity<Long> {
    @Column(name = "stack_trace")
    private String stackTrace;

    @Column(name = "message")
    private String message;

    @Column(name = "method_signature")
    private String methodSignature;

}