--liquibase formatted sql

-- changeset  nithin:202304092147

CREATE TABLE transactions (
  txn_hash VARCHAR(255) NOT NULL,
   created_at TIMESTAMP WITHOUT TIME ZONE,
   updated_at TIMESTAMP WITHOUT TIME ZONE,
   version INTEGER,
   from_address VARCHAR(255) NOT NULL,
   to_address VARCHAR(255),
   txn_value DECIMAL NOT NULL,
   gas DECIMAL NOT NULL,
   gas_price DECIMAL NOT NULL,
   timestamp DECIMAL NOT NULL,
   input_data VARCHAR,
   block_hash VARCHAR(255) NOT NULL,
   CONSTRAINT pk_transactions PRIMARY KEY (txn_hash)
);

CREATE INDEX from_address_idx ON transactions(from_address, timestamp);
CREATE INDEX to_address_idx ON transactions(to_address, timestamp);