package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

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