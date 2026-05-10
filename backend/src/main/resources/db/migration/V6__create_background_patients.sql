-- =============================================================
-- V6__create_patient_background.sql
-- Crea la tabla de antecedentes clínicos del paciente.
-- Relación 1:1 con patients — un registro por paciente,
-- actualizable conforme evoluciona su historial.
-- =============================================================

CREATE TABLE patient_background (
    id              BIGSERIAL       PRIMARY KEY,
    patient_id      BIGINT          NOT NULL UNIQUE,
    allergies       TEXT,
    clinical        TEXT,
    gynecological   TEXT,
    traumatological TEXT,
    surgical        TEXT,
    pharmacological TEXT,
    updated_at      TIMESTAMPTZ     NOT NULL DEFAULT now(),
    updated_by      BIGINT          NOT NULL,

    CONSTRAINT fk_patient_background_patients   FOREIGN KEY (patient_id)  REFERENCES patients(id)  ON DELETE RESTRICT,
    CONSTRAINT fk_patient_background_users      FOREIGN KEY (updated_by)  REFERENCES users(id)     ON DELETE RESTRICT
);