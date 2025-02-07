CREATE TABLE account (
   id BIGSERIAL,
   account_id VARCHAR NOT NULL UNIQUE,
   client_id BIGINT NOT NULL,
   account_type VARCHAR NOT NULL,
   balance NUMERIC(19,2),
   status VARCHAR,
   frozen_amount DECIMAL(19, 2),
   CONSTRAINT pk_account PRIMARY KEY (id)
);

ALTER TABLE account ADD CONSTRAINT FK_ACCOUNT_ON_CLIENT FOREIGN KEY (client_id) REFERENCES client (id);