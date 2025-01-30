-- liquibase formatted sql

CREATE TABLE client (
   id BIGSERIAL,
   client_id VARCHAR NOT NULL,
   first_name VARCHAR NOT NULL,
   middle_name VARCHAR,
   last_name VARCHAR NOT NULL,
   CONSTRAINT pk_client PRIMARY KEY (id)
);

