-- liquibase formatted sql

-- changeset ks:000-create-table-items
-- preconditions onFail:MARK_RAN onError:HALT
-- precondition-not tableExists tableName:items

CREATE TABLE items
(
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(255)   NOT NULL,
    price    DECIMAL(19, 2) NOT NULL,
    quantity INT            NOT NULL,
    discount INT
);

ALTER TABLE items
    ADD CONSTRAINT chk_quantity_positive CHECK (quantity >= 0);