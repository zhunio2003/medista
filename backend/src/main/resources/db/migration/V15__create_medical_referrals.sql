-- =============================================================
-- V15__create_medical_referrals.sql
-- Crea la tabla de referencias médicas a establecimientos externos.
-- Toda referencia debe originarse desde una atención médica
-- existente — relación 1:1 opcional con medical_attendances.
-- =============================================================

CREATE TABLE medical_referrals (
    id                      BIGSERIAL               PRIMARY KEY,
    medical_attendance_id   BIGINT                  NOT NULL UNIQUE,
    health_establishment_id BIGINT                  NOT NULL,
    service                 VARCHAR(150),
    speciality              VARCHAR(150)            NOT NULL,
    referral_date           DATE,
    referral_reason         referral_reason_enum    NOT NULL,
    clinical_summary        TEXT,
    referred_by             BIGINT                  NOT NULL,
    created_at              TIMESTAMPTZ             NOT NULL DEFAULT now(),
    is_active               BOOLEAN                 NOT NULL DEFAULT true,

    CONSTRAINT fk_medical_referrals_medical_attendances     FOREIGN KEY (medical_attendance_id)     REFERENCES medical_attendances(id)      ON DELETE RESTRICT,
    CONSTRAINT fk_medical_referrals_health_establishments   FOREIGN KEY (health_establishment_id)   REFERENCES health_establishments(id)    ON DELETE RESTRICT,
    CONSTRAINT fk_medical_referrals_users                   FOREIGN KEY (referred_by)               REFERENCES users(id)                    ON DELETE RESTRICT
);