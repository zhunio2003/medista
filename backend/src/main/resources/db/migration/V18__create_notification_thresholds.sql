-- =============================================================
-- V18__create_notification_thresholds.sql
-- Crea la tabla de umbrales configurables que activan alertas
-- automáticas. Cada umbral es auditable — se registra quién
-- lo modificó y cuándo (auditado también por Hibernate Envers).
-- =============================================================

CREATE TABLE notification_thresholds (
    id             BIGSERIAL               PRIMARY KEY,
    threshold_type threshold_type_enum     NOT NULL UNIQUE,
    value          INTEGER                 NOT NULL,
    period_days    INTEGER                 NOT NULL,
    is_active      BOOLEAN                 NOT NULL DEFAULT true,
    updated_at     TIMESTAMPTZ             NOT NULL DEFAULT now(),
    updated_by     BIGINT                  NOT NULL,

    CONSTRAINT fk_notification_thresholds_users     FOREIGN KEY (updated_by)    REFERENCES users(id)    ON DELETE RESTRICT
);