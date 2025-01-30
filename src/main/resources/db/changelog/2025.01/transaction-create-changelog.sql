CREATE TABLE transaction (
   id BIGSERIAL,
   transaction_id VARCHAR NOT NULL,
   account_id INTEGER NOT NULL,
   amount NUMERIC(19,2),
   transaction_time TIMESTAMP WITHOUT TIME ZONE,
   time_stamp TIMESTAMP WITHOUT TIME ZONE,
   transaction_status VARCHAR,
   CONSTRAINT pk_transaction PRIMARY KEY (id)
);

ALTER TABLE transaction ADD CONSTRAINT FK_TRANSACTION_ON_ACCOUNT FOREIGN KEY (account_id) REFERENCES account (id);