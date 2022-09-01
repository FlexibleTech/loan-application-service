create table loan_applications
(
    id                       varchar(60) primary key not null,
    status                   varchar(30)             not null,
    client                   jsonb                   not null,
    conditions               jsonb                   not null,
    document_package         jsonb                   null,
    offer                    jsonb                   null,
    income_confirmation_type varchar(30)             not null,
    created_at               timestamp               not null,
    updated_at               timestamp               not null,
    completed_at             timestamp               null,
    loan_program             varchar(30)             not null,
    risk_decision            jsonb                   null,
    issuance_id              varchar(60)             null
);