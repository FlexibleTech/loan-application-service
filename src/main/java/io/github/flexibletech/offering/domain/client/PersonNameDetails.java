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
public class PersonNameDetails implements ValueObject {
    private String name;
    private String middleName;
    private String surName;

    PersonNameDetails update(String name, String middleName, String surName) {
        return new PersonNameDetails(
                Optional.ofNullable(name).orElse(this.name),
                Optional.ofNullable(middleName).orElse(this.middleName),
                Optional.ofNullable(surName).orElse(this.surName));
    }
}
