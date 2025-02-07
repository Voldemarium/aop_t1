package ru.t1.java.service_2.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ObjectFromRecord {
    private long messageTimestamp;
    private long transactionTimestamp;
    private String transactionId;

}