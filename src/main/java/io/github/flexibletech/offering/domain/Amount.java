package io.github.flexibletech.offering.domain;

import io.github.flexibletech.offering.domain.common.ValueObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Amount implements ValueObject, Comparable<Amount> {
    private BigDecimal value;

    public static Amount fromValue(BigDecimal value) {
        if (Objects.isNull(value)) return null;
        return new Amount(value.setScale(6, RoundingMode.UP));
    }

    public void setValue(BigDecimal value) {
        this.value = value.setScale(6, RoundingMode.UP);
    }

    public boolean greater(Amount amount) {
        return compareTo(amount) > 0;
    }

    public boolean greaterOrEquals(Amount amount) {
        return compareTo(amount) >= 0;
    }

    public boolean lessOrEquals(Amount amount) {
        return compareTo(amount) <= 0;
    }

    public boolean less(Amount amount) {
        return compareTo(amount) < 0;
    }

    public Amount subtract(Amount amount) {
        if (amount.value.signum() != 1)
            throw new IllegalArgumentException(
                    String.format("Can't perform subtraction operation, value %f must be positive", value));
        return Amount.fromValue(value.subtract(amount.value));
    }

    public Amount calculatePercentage(double value) {
        if (value <= 0)
            throw new IllegalArgumentException(
                    String.format("Can't calculate percentage, value %f can't be greater than 100", value));
        return this.multiply(value / 100);
    }

    public Amount add(Amount amount) {
        return Amount.fromValue(this.value.add(amount.value));
    }

    public Amount multiply(int value) {
        if (value <= 0)
            throw new IllegalArgumentException(
                    String.format("Can't perform multiplication, value %d must be positive", value));
        return Amount.fromValue(this.value.multiply(BigDecimal.valueOf(value)));
    }

    public Amount multiply(double value) {
        if (value <= 0)
            throw new IllegalArgumentException(
                    String.format("Can't perform multiplication, value %f must be positive", value));
        return Amount.fromValue(this.value.multiply(BigDecimal.valueOf(value)));
    }

    public Amount divide(double value) {
        if (value <= 0)
            throw new IllegalArgumentException(
                    String.format("Can't perform division, value %f must be positive", value));
        return Amount.fromValue(this.value.divide(BigDecimal.valueOf(value), RoundingMode.FLOOR));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Amount amount = (Amount) o;
        return Objects.equals(value, amount.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(Amount o) {
        return this.value.compareTo(o.value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

}
