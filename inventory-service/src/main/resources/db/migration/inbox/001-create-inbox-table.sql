-- liquibase formatted sql

-- changeset ks:001-create-inbox-table

CREATE TABLE inbox (
                       idempotent_key UUID PRIMARY KEY,
                       status TEXT NOT NULL,
                       saved_result BYTEA,
                       created_at timestamptz DEFAULT CURRENT_TIMESTAMP
);