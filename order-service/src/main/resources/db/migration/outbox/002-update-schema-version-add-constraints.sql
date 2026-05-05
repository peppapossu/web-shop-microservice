--liquibase formatted sql

--changeset ks:002-update-schema-version-add-constraints

UPDATE outbox_order
SET schema_version = 1
WHERE schema_version IS NULL;

ALTER TABLE outbox_order
ALTER COLUMN schema_version SET NOT NULL;