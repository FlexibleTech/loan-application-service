package io.github.flexibletech.offering.domain.risk;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.flexibletech.offering.domain.Amount;
import io.github.flexibletech.offering.domain.common.ValueObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payroll implements ValueObject {
    private Amount salary;
    private LocalDate lastSalaryDate;

    static Payroll newPayroll(BigDecimal salary, LocalDate lastSalaryDate) {
        return new Payroll(Amount.fromValue(salary), lastSalaryDate);
    }

    //Коэффициент сверки дохода.
    @Transient
    private static final double INCOME_RECONCILIATION_THRESHOLD_PERCENTAGE = 30;
    //Допустимая разница между текущей датой и датой последней зарплаты в месяцах.
    @Transient
    private static final int ALLOWABLE_PAYROLL_DIFFERENCE = 3;

    //Расчет порогового значения зарплаты клиента, которому мы можем доверять без справки 2-НДФЛ.
    public Amount calculateThresholdSalaryAmount() {
        return this.salary.calculatePercentage(INCOME_RECONCILIATION_THRESHOLD_PERCENTAGE);
    }

    /*
    Зарплата считается актуальной только если разница между текущей датой и датой последней
    зарплаты не превыщает допустимого значения.
     */
    @JsonIgnore
    public boolean isActual() {
        var difference = Period.between(this.lastSalaryDate, LocalDate.now());
        return difference.getMonths() < ALLOWABLE_PAYROLL_DIFFERENCE;
    }
}
