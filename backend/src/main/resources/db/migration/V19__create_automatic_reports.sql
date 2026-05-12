-- =============================================================
-- V19__create_automatic_reports.sql
-- Crea la tabla de configuración de reportes programados.
-- Define qué reportes se generan automáticamente, con qué
-- frecuencia y a qué destinatarios se envían.
-- =============================================================

CREATE TABLE automatic_reports (
    id          BIGSERIAL               PRIMARY KEY,
    report_type VARCHAR(100)            NOT NULL,
    frequency   report_frequency_enum   NOT NULL,
    filters     JSONB,
    recipients  JSONB                   NOT NULL,
    is_active   BOOLEAN                 NOT NULL DEFAULT true,
    updated_by  BIGINT                  NOT NULL,
    updated_at  TIMESTAMPTZ             NOT NULL DEFAULT now(),

    CONSTRAINT fk_automatic_reports_users   FOREIGN KEY (updated_by)    REFERENCES users(id)    ON DELETE RESTRICT
);