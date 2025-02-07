package ru.t1.java.service_1.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.java.library.dto.TransactionStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * DTO for {@link ru.t1.java.service_1.model.Transaction}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class TransactionDto {
    @NotNull
    @JsonProperty("transaction_id")
    private String transactionId;
    @NotNull
    @JsonProperty("account_id")
    private String accountId;
    @JsonProperty("amount")
    private BigDecimal amount;
    @JsonProperty("transaction_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime transactionTime;
    @JsonProperty("timestamp")
    private Timestamp timestamp;
    @JsonProperty("transaction_status")
    private TransactionStatus transactionStatus;
}