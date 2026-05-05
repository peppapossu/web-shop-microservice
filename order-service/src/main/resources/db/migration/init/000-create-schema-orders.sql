--liquibase formatted sql

--changeset ks:001-create-schema-orders
CREATE SCHEMA IF NOT EXISTS orders;