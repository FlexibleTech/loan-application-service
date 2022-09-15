package io.github.flexibletech.offering.domain.risk;

import io.github.flexibletech.offering.domain.common.ValueObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RiskDecisionId implements ValueObject {
    private String id;

    @Override
    public String toString() {
        return this.id;
    }

}
