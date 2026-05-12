-- =============================================================
-- V20__create_audit_logs.sql
-- Crea la tabla de registro inmutable de acciones de seguridad.
-- Los registros nunca se eliminan — retención indefinida por LOPDP.
-- =============================================================

CREATE TABLE audit_logs (
    id          BIGSERIAL           PRIMARY KEY,
    user_id     BIGINT              NOT NULL,
    action      audit_action_enum   NOT NULL,
    entity_type VARCHAR(100),
    entity_id   BIGINT,
    ip_address  INET                NOT NULL,
    user_agent  TEXT,
    created_at  TIMESTAMPTZ         NOT NULL DEFAULT now(),

    CONSTRAINT fk_audit_logs_users  FOREIGN KEY (user_id)   REFERENCES users(id)    ON DELETE RESTRICT
);