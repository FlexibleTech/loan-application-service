package io.github.flexibletech.offering.domain.factory;

import io.github.flexibletech.offering.TestValues;
import io.github.flexibletech.offering.domain.risk.ConditionsRestrictions;
import io.github.flexibletech.offering.domain.risk.Payroll;
import io.github.flexibletech.offering.domain.risk.RiskDecision;

public class TestRiskDecisionFactory {
    private TestRiskDecisionFactory() {
    }

    public static RiskDecision newApprovedRiskDecision() {
        return new RiskDecision(
                TestValues.RISK_DECISION_ID,
                RiskDecision.Status.APPROVED,
                newPayroll(),
                newConditionsRestrictions());
    }

    public static RiskDecision newApprovedRiskDecisionWithNotActualPayroll() {
        return new RiskDecision(
                TestValues.RISK_DECISION_ID,
                RiskDecision.Status.APPROVED,
                newNotActualPayroll(),
                newConditionsRestrictions());
    }

    public static RiskDecision newDeclinedRiskDecision() {
        return new RiskDecision(
                TestValues.RISK_DECISION_ID,
                RiskDecision.Status.DECLINED,
                newPayroll(),
                newConditionsRestrictions());
    }

    private static Payroll newPayroll() {
        return new Payroll(
                TestValues.PAYROLL_SALARY,
                TestValues.PAYROLL_LAST_SALARY_DATE);
    }

    private static Payroll newNotActualPayroll() {
        return new Payroll(
                TestValues.PAYROLL_SALARY_LESS_THAN_INCOME,
                TestValues.PAYROLL_LAST_SALARY_DATE_LESS_THAN_CURRENT_DATE);
    }

    private static ConditionsRestrictions newConditionsRestrictions() {
        return new ConditionsRestrictions(
                TestValues.CONDITIONS_RESTRICTIONS_MAX_AMOUNT,
                TestValues.CONDITIONS_RESTRICTIONS_MAX_PERIOD);
    }
}
