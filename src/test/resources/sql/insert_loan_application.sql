insert into loan_applications(id, status, client_id, conditions, document_package,
                              offer, income_confirmation_type, completed_at,
                              loan_program, risk_decision, issuance_id, created_at, updated_at)
values ('LOANAPP22082500001', 'APPROVED', '20056671',
        '{"amount": {"value": 500000.000000}, "period": 20, "insurance": false}', '[]', null, 'TWO_NDFL', null,
        'COMMON',
        '{"id": "RSK202200911", "status": "APPROVED", "payroll": {"salary": {"value": 90000.000000}, "lastSalaryDate": [2022, 8, 23]}, "conditionsRestrictions": {"maxAmount": {"value": 1000000.000000}, "maxPeriod": 60}}',
        null, '2022-08-31 13:25:39.677556', '2022-08-31 13:25:39.677556');