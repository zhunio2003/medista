-- =============================================================
-- V16__create_referral_diagnoses.sql
-- Crea la tabla de diagnósticos comunicados en una referencia
-- médica. Tabla separada de diagnoses por diseño (ADR-004) —
-- evita FKs mutuamente excluyentes en una misma fila.
-- Una referencia comunica uno o más diagnósticos (1:N).
-- =============================================================

CREATE TABLE referral_diagnoses (
    id            BIGSERIAL               PRIMARY KEY,
    referral_id   BIGINT                  NOT NULL,
    cie10_code_id BIGINT                  NOT NULL,
    description   TEXT,
    status        diagnosis_status_enum   NOT NULL,
    created_at    TIMESTAMPTZ             NOT NULL DEFAULT now(),

    CONSTRAINT fk_referral_diagnoses_medical_referrals  FOREIGN KEY (referral_id)   REFERENCES medical_referrals(id)    ON DELETE RESTRICT,
    CONSTRAINT fk_referral_diagnoses_cie10_codes        FOREIGN KEY (cie10_code_id) REFERENCES cie10_codes(id)          ON DELETE RESTRICT
);