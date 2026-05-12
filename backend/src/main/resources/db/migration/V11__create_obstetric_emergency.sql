-- =============================================================
-- V11__create_obstetric_emergency.sql
-- Crea la tabla de datos obstétricos asociados a una atención
-- médica. Registro condicional — solo aplica para pacientes
-- de género FEMALE. Relación 1:1 opcional con medical_attendances.
-- =============================================================

CREATE TABLE obstetric_emergency (
    id                      BIGSERIAL   PRIMARY KEY,
    medical_attendance_id   BIGINT      NOT NULL UNIQUE,
    menarche                VARCHAR(20),
    menstrual_rhythm_r      VARCHAR(10),
    menstrual_rhythm_i      VARCHAR(10),
    cycles                  VARCHAR(20),
    fum                     DATE,
    ivsa                    VARCHAR(20),
    sexual_partners         SMALLINT,
    gapc                    VARCHAR(20),
    dysmenorrhea            BOOLEAN     NOT NULL DEFAULT false,
    mastodynia              BOOLEAN     NOT NULL DEFAULT false,
    is_pregnant             BOOLEAN     NOT NULL DEFAULT false,
    fpp                     DATE,
    gestational_age_weeks   SMALLINT,
    prenatal_controls       SMALLINT,
    immunizations           VARCHAR(255),
    notes                   TEXT,

    CONSTRAINT fk_obstetric_emergency_medical_attendances   FOREIGN KEY (medical_attendance_id)     REFERENCES medical_attendances(id)  ON DELETE RESTRICT
);