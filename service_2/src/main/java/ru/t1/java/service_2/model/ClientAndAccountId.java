package ru.t1.java.service_2.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientAndAccountId {
    @JsonProperty("client_id")
    private String clientId;
    @JsonProperty("account_id")
    private String accountId;
}
