package ru.t1.java.demo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.t1.java.demo.model.AccountType;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link ru.t1.java.demo.model.Account}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountDto implements Serializable {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("client_id")
    private Long client_id;
    @JsonProperty("accountType")
    private AccountType accountType;
    @JsonProperty("balance")
    private BigDecimal balance;
}