package io.github.flexibletech.offering.domain.client;

import io.github.flexibletech.offering.domain.ValueObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Organization implements ValueObject {
    private String title;
    private String inn;
    private String fullAddress;

    Organization update(String title, String inn, String fullAddress) {
        return new Organization(
                Optional.ofNullable(title).orElse(this.title),
                Optional.ofNullable(inn).orElse(this.inn),
                Optional.ofNullable(fullAddress).orElse(this.fullAddress));
    }

}
