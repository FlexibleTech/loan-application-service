package io.github.flexibletech.offering.domain.loanapplication.risk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.flexibletech.offering.domain.Amount;
import io.github.flexibletech.offering.domain.ValueObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * Полученные от рисков ограничения условий кредита.
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConditionsRestrictions implements ValueObject {
    private Amount maxAmount;
    private int maxPeriod;

    //Максимально допустимая сумма, доступная для выбора. Ограничиваем ею условия даже если риски вернули большую сумму.
    @Transient
    public static final Amount AMOUNT_LIMIT = Amount.fromValue(BigDecimal.valueOf(3_000000));

    static ConditionsRestrictions newConditionsRestrictions(BigDecimal amount, int maxPeriod) {
        return new ConditionsRestrictions(Amount.fromValue(amount), maxPeriod);
    }

    @JsonIgnore
    boolean isMaxAmountGreaterThanLimit() {
        return this.maxAmount.greater(AMOUNT_LIMIT);
    }

    ConditionsRestrictions withLimitedAmount() {
        return new ConditionsRestrictions(AMOUNT_LIMIT, this.maxPeriod);
    }

}
