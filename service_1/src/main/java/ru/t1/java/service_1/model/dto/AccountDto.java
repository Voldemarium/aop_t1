package ru.t1.java.service_1.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.java.service_1.model.AccountStatus;
import ru.t1.java.service_1.model.AccountType;

import java.math.BigDecimal;

/**
 * DTO for {@link ru.t1.java.service_1.model.Account}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class AccountDto  {
    @NotNull
    @JsonProperty("account_id")
    private String accountId;
    @NotNull
    @JsonProperty("client_id")
    private String client_id;
    @NotNull
    @JsonProperty("account_type")
    private AccountType accountType;
    @JsonProperty("balance")
    private BigDecimal balance;
    @JsonProperty("status")
    private AccountStatus status;
    @JsonProperty("frozen_amount")
    private BigDecimal frozenAmount;
}