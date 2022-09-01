package io.github.flexibletech.offering.infrastructure.rest.preapprovedoffer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PreApprovedOfferResponse {
    private String id;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private String clientId;
}
