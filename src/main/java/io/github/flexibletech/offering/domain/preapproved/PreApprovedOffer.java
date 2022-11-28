package io.github.flexibletech.offering.domain.preapproved;

import io.github.flexibletech.offering.domain.Amount;
import io.github.flexibletech.offering.domain.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PreApprovedOffer implements Entity {
    private PreApprovedOfferId id;
    private Amount minAmount;
    private Amount maxAmount;
    private String clientId;

    public static PreApprovedOffer newPreApprovedOffer(String id, BigDecimal minAmount,
                                                       BigDecimal maxAmount, String clientId) {
        return new PreApprovedOffer(
                new PreApprovedOfferId(id),
                Amount.fromValue(minAmount),
                Amount.fromValue(maxAmount),
                clientId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        PreApprovedOffer that = (PreApprovedOffer) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
