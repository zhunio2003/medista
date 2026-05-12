-- =============================================================
-- V14__create_attendance_corrections.sql
-- Crea la tabla de notas de corrección sobre atenciones médicas.
-- Las atenciones son inmutables — cualquier enmienda se registra
-- aquí como un nuevo registro acumulativo (1:N).
-- =============================================================

CREATE TABLE attendance_corrections (
    id                    BIGSERIAL   PRIMARY KEY,
    medical_attendance_id BIGINT      NOT NULL,
    note                  TEXT        NOT NULL,
    created_by            BIGINT      NOT NULL,
    created_at            TIMESTAMPTZ NOT NULL DEFAULT now(),

    CONSTRAINT fk_attendance_corrections_medical_attendances    FOREIGN KEY (medical_attendance_id) REFERENCES medical_attendances(id)  ON DELETE RESTRICT,
    CONSTRAINT fk_attendance_corrections_users                  FOREIGN KEY (created_by)            REFERENCES users(id)               ON DELETE RESTRICT
);