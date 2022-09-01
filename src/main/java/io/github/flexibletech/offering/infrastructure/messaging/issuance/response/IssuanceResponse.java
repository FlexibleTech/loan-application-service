package io.github.flexibletech.offering.infrastructure.messaging.issuance.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssuanceResponse {
    private String applicationId;
    private String issuanceId;
}
