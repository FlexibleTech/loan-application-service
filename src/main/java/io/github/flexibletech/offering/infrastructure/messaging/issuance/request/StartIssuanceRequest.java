package io.github.flexibletech.offering.infrastructure.messaging.issuance.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StartIssuanceRequest {
    private String applicationId;
    private BigDecimal amount;
    private int period;
    private String clientId;
}
