create sequence if not exists loan_application_seq
    start with 1
    increment by 1
    cycle
    maxvalue 99999
    cache 50;