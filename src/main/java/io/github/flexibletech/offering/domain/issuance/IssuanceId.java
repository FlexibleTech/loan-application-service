package io.github.flexibletech.offering.domain.issuance;

import io.github.flexibletech.offering.domain.common.ValueObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IssuanceId implements ValueObject {
    private String id;

    @Override
    public String toString() {
        return this.id;
    }

}
