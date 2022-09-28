package io.github.flexibletech.offering.application.loanapplication;

class ProcessConstants {

    private ProcessConstants() {
    }

    //Process
    static final String LOAN_APPLICATION_PROCESS = "loanApplicationProcess";

    //Variables
    static final String LOAN_APPLICATION_ID = "loanApplicationId";
    static final String INCOME_CONFIRMATION_TYPE = "incomeConfirmationType";
    static final String STATUS = "status";
    static final String INSURANCE = "insurance";

    //Messages
    static final String RISK_DECISION_RECEIVED = "riskDecisionReceived";
    static final String LOAN_APPLICATION_COMPLETED = "loanApplicationCompleted";

    //User tasks
    static final String CHOSE_CONDITIONS_TASK = "choseConditionsTask";
    static final String SING_DOCUMENTS_TASK = "signDocumentsTask";

    //Delegates
    static final String REQUEST_RISK_DECISION_TASK = "requestRiskDecisionTask";
    static final String DEFINE_INCOME_CONFIRMATION_TYPE_TASK = "defineIncomeConfirmationTypeTask";
    static final String WAIT_FOR_DOCUMENT_PACKAGE_SIGNATURE_TASK = "waitForDocumentPackageSignatureTask";
    static final String CALCULATE_OFFER_TASK = "calculateOfferTask";
    static final String PRINT_FORM_TASK = "printFormTask";
    static final String PRINT_CONDITIONS_TASK = "printConditionsTask";
    static final String PRINT_INSURANCE_TASK = "printInsuranceTask";
    static final String START_ISSUANCE_TASK = "startIssuanceTask";
    static final String CANCEL_LOAN_APPLICATION_TASK = "cancelLoanApplicationTask";
}
