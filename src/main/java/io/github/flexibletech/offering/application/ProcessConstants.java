package io.github.flexibletech.offering.application;

import lombok.experimental.UtilityClass;

@UtilityClass
class ProcessConstants {
    //Process
    final String LOAN_APPLICATION_PROCESS = "loanApplicationProcess";

    //Variables
    final String LOAN_APPLICATION_ID = "loanApplicationId";
    final String INCOME_CONFIRMATION_TYPE = "incomeConfirmationType";
    final String STATUS = "status";
    final String INSURANCE = "insurance";

    //Messages
    final String RISK_DECISION_RECEIVED = "riskDecisionReceived";
    final String LOAN_APPLICATION_COMPLETED = "loanApplicationCompleted";

    //User tasks
    final String CHOSE_CONDITIONS_TASK = "choseConditionsTask";
    final String SING_DOCUMENTS_TASK = "signDocumentsTask";

    //Delegates
    final String REQUEST_RISK_DECISION_TASK = "requestRiskDecisionTask";
    final String DEFINE_INCOME_CONFIRMATION_TYPE_TASK = "defineIncomeConfirmationTypeTask";
    final String CALCULATE_OFFER_TASK = "calculateOfferTask";
    final String PRINT_FORM_TASK = "printFormTask";
    final String PRINT_CONDITIONS_TASK = "printConditionsTask";
    final String PRINT_INSURANCE_TASK = "printInsuranceTask";
    final String START_ISSUANCE_TASK = "startIssuanceTask";
    final String CANCEL_LOAN_APPLICATION_TASK = "cancelLoanApplicationTask";
}
