package ru.t1.java.library.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class TransactionAcceptDto {
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("account_id")
    private String accountId;
    @JsonProperty("transaction_id")
    private String transactionId;
    @JsonProperty("timestamp")
    private Timestamp timestamp;
    @JsonProperty("amount")
    private BigDecimal transactionAmount;
    @JsonProperty("balance")
    private BigDecimal accountBalance;
}
