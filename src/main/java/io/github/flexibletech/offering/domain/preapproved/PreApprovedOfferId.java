package io.github.flexibletech.offering.domain.preapproved;

import io.github.flexibletech.offering.domain.ValueObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PreApprovedOfferId implements ValueObject {
    private String id;

    @Override
    public String toString() {
        return this.id;
    }

}
