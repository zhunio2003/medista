-- =============================================================
-- V10__create_physical_exam_findings.sql
-- Crea la tabla de hallazgos del examen físico por sistema
-- corporal. Cada atención produce exactamente 9 registros —
-- uno por sistema corporal definido en body_system_enum.
-- =============================================================

CREATE TABLE physical_exam_findings (
    id                      BIGSERIAL           PRIMARY KEY,
    medical_attendance_id   BIGINT              NOT NULL,
    body_system             body_system_enum    NOT NULL,
    is_normal               BOOLEAN             NOT NULL,
    description             TEXT,

    CONSTRAINT fk_physical_exam_findings_medical_attendances    FOREIGN KEY (medical_attendance_id)     REFERENCES medical_attendances(id)  ON DELETE RESTRICT
);