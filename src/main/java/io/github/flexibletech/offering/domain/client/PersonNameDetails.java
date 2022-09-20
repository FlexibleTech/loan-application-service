package io.github.flexibletech.offering.domain.client;

import io.github.flexibletech.offering.domain.ValueObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PersonNameDetails implements ValueObject {
    private String name;
    private String middleName;
    private String surName;
}
