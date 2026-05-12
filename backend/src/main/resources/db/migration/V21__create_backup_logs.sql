-- =============================================================
-- V21__create_backup_logs.sql
-- Crea la tabla de registro de ejecuciones de respaldo.
-- Cubre respaldos automáticos y manuales. requested_by es
-- nullable — los respaldos automáticos no tienen usuario asociado.
-- =============================================================

CREATE TABLE backup_logs (
    id              BIGSERIAL               PRIMARY KEY,
    executed_at     TIMESTAMPTZ             NOT NULL,
    triggered_by    backup_trigger_enum     NOT NULL,
    requested_by    BIGINT,
    file_name       VARCHAR(255)            NOT NULL,
    file_size_bytes BIGINT,
    status          backup_status_enum      NOT NULL,
    error_message   TEXT,

    CONSTRAINT fk_backup_logs_users     FOREIGN KEY (requested_by)  REFERENCES users(id)    ON DELETE RESTRICT
);