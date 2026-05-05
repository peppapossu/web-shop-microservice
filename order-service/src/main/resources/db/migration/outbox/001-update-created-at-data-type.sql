--liquibase formatted sql

--changeset ks:001-update-created-at-data-type
ALTER TABLE outbox_order
ALTER COLUMN created_at TYPE TIMESTAMPTZ
USING created_at::timestamptz;

ALTER TABLE outbox_order
ALTER COLUMN created_at DROP DEFAULT;