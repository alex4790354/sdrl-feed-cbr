--liquibase formatted sql
--changeset alexander.v:01_001

CREATE TABLE IF NOT EXISTS feed.currency
(
    id                  VARCHAR(10),
    frequency           VARCHAR(10) NOT NULL,
    name_rus            VARCHAR(250) NOT NULL,
    name_eng            VARCHAR(250) NOT NULL,
    nominal             BIGINT NOT NULL,
    parent_code         VARCHAR(10),
    creation_time       TIMESTAMP WITH TIME ZONE DEFAULT now(),
    update_time         TIMESTAMP WITH TIME ZONE DEFAULT now(),
    instr_name          VARCHAR(250),
    instr_name_eng      VARCHAR(250),
    CONSTRAINT PK_CURRENCY PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS feed.currency_rate
(
    id                  VARCHAR(10),
    effective_date      DATE,
    first_crncy         VARCHAR(3) NOT NULL,
    second_crncy        VARCHAR(3) NOT NULL,
    nominal             BIGINT NOT NULL,
    value               DECIMAL(14,4),
    creation_time       TIMESTAMP WITH TIME ZONE DEFAULT now(),
    update_time         TIMESTAMP WITH TIME ZONE DEFAULT now(),
    CONSTRAINT PK_CURRENCY_RATE PRIMARY KEY (ID),
    CONSTRAINT FK_currency_rate FOREIGN KEY (id) REFERENCES feed.currency (id)
);

