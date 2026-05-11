-- =============================================================
-- V9__create_medical_attendances.sql
-- Crea la tabla central de consultas médicas.
-- Cada registro representa una atención completa e inmutable —
-- las correcciones se gestionan en attendance_corrections.
-- =============================================================

CREATE TABLE medical_attendances (
    id                      BIGSERIAL       PRIMARY KEY,
    patient_id              BIGINT          NOT NULL,
    attended_by             BIGINT          NOT NULL,
    attendance_date         TIMESTAMPTZ     NOT NULL DEFAULT now(),
    reason_for_visit        TEXT            NOT NULL,
    current_illness         TEXT,
    treatment               TEXT,
    blood_pressure_systolic SMALLINT,
    blood_pressure_diastolic SMALLINT,
    weight_kg               NUMERIC(5,2),
    height_cm               NUMERIC(5,2),
    bmi                     NUMERIC(5,2),
    heart_rate              SMALLINT,
    respiratory_rate        SMALLINT,
    temperature_celsius     NUMERIC(4,1),
    oxygen_saturation       SMALLINT,
    glasgow_eye             SMALLINT,
    glasgow_verbal          SMALLINT,
    glasgow_motor           SMALLINT,
    glasgow_total           SMALLINT,
    capillary_refill        VARCHAR(20),
    pupillary_reflex        VARCHAR(50),
    is_active               BOOLEAN         NOT NULL DEFAULT true,
    created_at              TIMESTAMPTZ     NOT NULL DEFAULT now(),

    CONSTRAINT fk_medical_attendances_patients  FOREIGN KEY (patient_id)  REFERENCES patients(id)  ON DELETE RESTRICT,
    CONSTRAINT fk_medical_attendances_users     FOREIGN KEY (attended_by) REFERENCES users(id)     ON DELETE RESTRICT
);