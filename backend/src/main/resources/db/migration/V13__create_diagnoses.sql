-- =============================================================
-- V13__create_diagnoses.sql
-- Crea la tabla de diagnósticos asociados a una atención médica.
-- Una atención puede tener uno o muchos diagnósticos (1:N).
-- Cada diagnóstico referencia un código CIE-10 del catálogo.
-- =============================================================

CREATE TABLE diagnoses (
    id                    BIGSERIAL               PRIMARY KEY,
    medical_attendance_id BIGINT                  NOT NULL,
    cie10_code_id         BIGINT                  NOT NULL,
    description           TEXT,
    status                diagnosis_status_enum   NOT NULL,
    created_at            TIMESTAMPTZ             NOT NULL DEFAULT now(),

    CONSTRAINT fk_diagnoses_medical_attendances FOREIGN KEY (medical_attendance_id) REFERENCES medical_attendances(id)  ON DELETE RESTRICT,
    CONSTRAINT fk_diagnoses_cie10_codes         FOREIGN KEY (cie10_code_id)         REFERENCES cie10_codes(id)          ON DELETE RESTRICT
);