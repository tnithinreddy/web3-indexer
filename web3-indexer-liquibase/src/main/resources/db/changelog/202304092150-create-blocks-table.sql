--liquibase formatted sql

-- changeset  nithin:202304092150

CREATE TABLE blocks (
  block_hash VARCHAR(255) NOT NULL,
   created_at TIMESTAMP WITHOUT TIME ZONE,
   updated_at TIMESTAMP WITHOUT TIME ZONE,
   version INTEGER,
   timestamp DECIMAL NOT NULL,
   transactions_count INTEGER NOT NULL,
   block_number DECIMAL NOT NULL,
   status VARCHAR(255) NOT NULL,
   CONSTRAINT pk_blocks PRIMARY KEY (block_hash)
);