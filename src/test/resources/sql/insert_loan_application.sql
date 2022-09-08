insert into loan_applications(id, status, client, conditions, document_package,
                              offer, income_confirmation_type, created_at, updated_at, completed_at,
                              loan_program, risk_decision, issuance_id)
values ('LOANAPP22082500001', 'APPROVED',
        '{"id": "20056671", "email": "test@mail.ru", "income": {"value": 100000.000000}, "category": "STANDARD", "passport": {"number": "190134", "series": "4401", "issueDate": [2000, 9, 2], "department": "ОВД КИТАЙ-ГОРОД 1 РУВД ЦАО Г. МОСКВЫ", "departmentCode": "772-001"}, "birthDate": [1986, 1, 22], "workPlace": {"inn": "7842349892", "title": "ООО \"В Контакте\"", "fullAddress": "191024, ГОРОД САНКТ-ПЕТЕРБУРГ, УЛИЦА ХЕРСОНСКАЯ, ДОМ 12-14, ЛИТЕР А, ПОМЕЩЕНИЕ 1Н"}, "phoneNumber": "79851325677", "spouseIncome": null, "maritalStatus": "UNMARRIED", "personNameDetails": {"name": "Тест", "surName": "Тестов", "middleName": "Тестович"}, "fullRegistrationAddress": "125009, город Москва, Тверская ул. 10 с1"}',
        '{"amount": {"value": 500000.000000}, "period": 20, "insurance": false}', '[]', null, 'TWO_NDFL',
        '2022-08-31 13:25:39.677556', '2022-08-31 13:25:39.677556',
        null, 'COMMON',
        '{"id": "RSK202200911", "status": "APPROVED", "payroll": {"salary": {"value": 90000.000000}, "lastSalaryDate": [2022, 8, 23]}, "conditionsRestrictions": {"maxAmount": {"value": 1000000.000000}, "maxPeriod": 60}}',
        null);