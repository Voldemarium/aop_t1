CREATE TABLE account (
   id BIGSERIAL,
   client_id BIGINT,
   account_type VARCHAR,
   balance NUMERIC(19,2),
   CONSTRAINT pk_account PRIMARY KEY (id)
);

ALTER TABLE account ADD CONSTRAINT FK_ACCOUNT_ON_CLIENT FOREIGN KEY (client_id) REFERENCES client (id);