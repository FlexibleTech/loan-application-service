package io.github.flexibletech.offering.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.flexibletech.offering.domain.common.ValueObject;
import io.github.flexibletech.offering.domain.preapproved.PreApprovedOffer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Conditions implements ValueObject {
    private Amount amount;
    private int period;
    private boolean insurance;

    //Минимальная сумма, доступная для выбора премиальному клиенту.
    @Transient
    private static final Amount MAX_PREMIUM_ALLOWABLE_AMOUNT = Amount.fromValue(BigDecimal.valueOf(1_000000));
    @Transient
    //Коэффициент для расчета страховки.
    private static final double INSURANCE_COEFFICIENT = 0.00284;
    @Transient
    //Коэффициент для расчета ежемесячного платежа.
    private static final double MONTHLY_PAYMENT_RATE_COEFFICIENT = 1200;
    @Transient
    //Коэффициент для расчета страховой премии.
    private static final double INSURANCE_PREMIUM_COEFFICIENT = 0.042;
    @Transient
    //Ставка единовременного платежа по страховке
    static final double SINGLE_INSURANCE_RATE = 15;

    /**
     * Проверка - доступна ли выбранная сумма без подтверждения дохода.
     *
     * @param preApprovedOffer Предодобренное предложение.
     * @return true/false
     */
    @JsonIgnore
    boolean isChosenAmountAvailableWithoutConfirmation(PreApprovedOffer preApprovedOffer) {
        if (Objects.nonNull(preApprovedOffer))
            return this.amount.greaterOrEquals(preApprovedOffer.getMinAmount())
                    && this.amount.lessOrEquals(preApprovedOffer.getMaxAmount());
        return false;
    }

    @JsonIgnore
    boolean isChosenAmountLessThanMaxPremiumAllowableValue() {
        return this.amount.less(MAX_PREMIUM_ALLOWABLE_AMOUNT);
    }

    /**
     * Корректировка суммы условий если выбрана страховка. Сумма корректируется на единоразовую выплату по страховке.
     *
     * @return Новые условия.
     */
    Conditions adjustAmountIfInsuranceChosen() {
        if (isInsurance()) {
            var newAmount = calculateSingleInsurancePayment()
                    .add(this.amount);

            return new Conditions(newAmount, this.period, this.insurance);
        }
        return this;
    }

    //Расчет единовременного платежа по страховке
    Amount calculateSingleInsurancePayment() {
        return this.amount.divide(SINGLE_INSURANCE_RATE / 100)
                .multiply(this.period)
                .multiply(INSURANCE_COEFFICIENT);
    }

    /**
     * Формула для расчета ежемесячного платежа O*(Ps/(1 - (1 + Ps))^-(Pp-1)
     * O - остаток по кредиту
     * Ps - ставка
     * Pp - период до окончания срока кредита
     *
     * @return Ежемесячный платеж
     */
    Amount calculateMonthlyPayment() {
        var degree = -(this.period - 1);
        //Расчет ставки
        var monthlyRate = LoanApplication.LOAN_RATE / MONTHLY_PAYMENT_RATE_COEFFICIENT;
        return this.amount.multiply(monthlyRate / (1 - Math.pow(1 + monthlyRate, degree)));
    }

    //Расчет даты первого платежа
    LocalDate calculateFirstPaymentDate() {
        return LocalDate.now().plusMonths(1);
    }

    //Расчет даты последнего платежа
    LocalDate calculateLastPaymentDate() {
        return LocalDate.now().plusMonths(this.period + 1);
    }

    Conditions newConditions(Amount amount, Integer period, Boolean insurance) {
        return new Conditions(Optional.ofNullable(amount).orElse(this.amount),
                Optional.ofNullable(period).orElse(this.period),
                Optional.ofNullable(insurance).orElse(this.insurance));
    }

    //Расчет страховой премии
    Amount calculateInsurancePremium() {
        return this.amount.multiply(INSURANCE_PREMIUM_COEFFICIENT);
    }

}
