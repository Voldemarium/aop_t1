package ru.t1.java.library.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class TransactionResultDto {
    @JsonProperty("account_id")
    private String accountId;
    @JsonProperty("transaction_id")
    private String transactionId;
    @JsonProperty("transaction_status")
    private TransactionStatus transactionStatus;
}
