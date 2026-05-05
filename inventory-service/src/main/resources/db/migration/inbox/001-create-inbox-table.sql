-- liquibase formatted sql

-- changeset ks:001-create-inbox-table

CREATE TABLE inbox (
                       idempotent_key UUID PRIMARY KEY,
                       status VARCHAR(50) NOT NULL,
                       saved_result JSONB,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);