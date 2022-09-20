create table clients
(
    id                        varchar(60) primary key not null,
    person_name_details       jsonb                   not null,
    passport                  jsonb                   not null,
    marital_status            varchar(10)             not null,
    work_place                jsonb                   not null,
    full_registration_address text                    not null,
    phone_number              varchar(11)             not null,
    email                     text                    not null,
    income                    decimal(14, 6)          not null,
    spouse_income             decimal(14, 6)          null,
    category                  text                    not null,
    birth_date                date                    not null,
    created_at                timestamp               not null,
    updated_at                timestamp               not null
);