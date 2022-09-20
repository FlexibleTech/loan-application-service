package io.github.flexibletech.offering.domain.loanapplication;

import io.github.flexibletech.offering.domain.Amount;
import io.github.flexibletech.offering.domain.ValueObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Offer implements ValueObject {
    private double rate;
    private Amount amount;
    private Integer period;
    private Amount averageMonthlyPayment;
    private LocalDate firstPaymentDate;
    private LocalDate lastPaymentDate;
    private Amount singleInsurancePayment;
    private Amount insurancePremium;

    private Offer(double rate, Amount amount, Integer period, Amount averageMonthlyPayment,
                  LocalDate firstPaymentDate, LocalDate lastPaymentDate) {
        this.rate = rate;
        this.amount = amount;
        this.period = period;
        this.averageMonthlyPayment = averageMonthlyPayment;
        this.firstPaymentDate = firstPaymentDate;
        this.lastPaymentDate = lastPaymentDate;
    }

    public static Offer newOffer(Conditions conditions) {
        return new Offer(
                LoanApplication.LOAN_RATE,
                conditions.getAmount(),
                conditions.getPeriod(),
                conditions.calculateMonthlyPayment(),
                conditions.calculateFirstPaymentDate(),
                conditions.calculateLastPaymentDate());
    }

    public static Offer newOfferWithInsurance(Conditions conditions) {
        return new Offer(
                LoanApplication.LOAN_RATE,
                conditions.getAmount(),
                conditions.getPeriod(),
                conditions.calculateMonthlyPayment(),
                conditions.calculateFirstPaymentDate(),
                conditions.calculateLastPaymentDate(),
                conditions.calculateSingleInsurancePayment(),
                conditions.calculateInsurancePremium());
    }
}
