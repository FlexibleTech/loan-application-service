package io.github.flexibletech.offering.domain.risk;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.flexibletech.offering.domain.Amount;
import io.github.flexibletech.offering.domain.common.Entity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RiskDecision implements Entity {
    private String id;
    private Status status;
    private Payroll payroll;
    private ConditionsRestrictions conditionsRestrictions;

    @JsonIgnore
    public boolean isApproved() {
        return this.status == Status.APPROVED;
    }

    /**
     * Проверка - соответствует ли доход сумме, полученной из зарплатной ведомости.
     *
     * @param specifiedIncome   Указанный доход.
     * @return                  true/false
     */
    @JsonIgnore
    public boolean doesIncomeMatchSalary(Amount specifiedIncome) {
        if (!this.payroll.isActual()) return false;
        var incomeDifference = specifiedIncome.subtract(payroll.getSalary());
        return incomeDifference.less(payroll.calculateThresholdAmount());
    }

    //Ограничиваем сумму, полученную от рисков.
    public void limitConditionsRestrictionsAmount() {
        if (this.conditionsRestrictions.isMaxAmountGreaterThanLimit())
            this.conditionsRestrictions = this.conditionsRestrictions.withLimitedAmount();
    }

    @RequiredArgsConstructor
    public enum Status {
        APPROVED("APPROVED"),
        DECLINED("DECLINED");

        private final String value;

        @JsonCreator
        public static Status fromValue(String value) {
            return Arrays.stream(values())
                    .filter(status -> status == Status.valueOf(value))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Unknown status %s ", value)));
        }
    }

    public static RiskDecision newRiskDecision(String id, String status, BigDecimal payrollSalary,
                                               LocalDate payrollLastSalaryDate, BigDecimal conditionsMaxAmount, int conditionsMaxPeriod) {
        return new RiskDecision(
                id,
                Status.fromValue(status),
                Payroll.newPayroll(payrollSalary, payrollLastSalaryDate),
                ConditionsRestrictions.newConditionsRestrictions(conditionsMaxAmount, conditionsMaxPeriod));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass())
            return false;
        RiskDecision that = (RiskDecision) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
