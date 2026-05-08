-- =============================================================
-- V5__create_patients.sql
-- Crea la tabla de datos demográficos de los estudiantes(pacientes).
-- Entidad raíz del dominio clínico — ninguna atención médica
-- puede existir sin un registro de paciente previo.
-- =============================================================

CREATE TABLE patients (
    id              BIGSERIAL               PRIMARY KEY,
    user_id         BIGINT                  UNIQUE,
    career_id       BIGINT                  NOT NULL,
    cedula          VARCHAR(10)             NOT NULL UNIQUE,
    first_name      VARCHAR(100)            NOT NULL,
    last_name       VARCHAR(100)            NOT NULL,
    semester        VARCHAR(20)             NOT NULL,
    address         VARCHAR(255)            NOT NULL,
    neighborhood    VARCHAR(100),
    parish          VARCHAR(100),
    canton          VARCHAR(100)            NOT NULL,
    province        VARCHAR(100)            NOT NULL,
    phone           VARCHAR(20)             NOT NULL,
    birth_date      DATE                    NOT NULL,
    birth_place     VARCHAR(100),
    birth_country   VARCHAR(100)            NOT NULL DEFAULT 'Ecuador',
    gender          gender_enum             NOT NULL,
    marital_status  marital_status_enum     NOT NULL,
    blood_type      blood_type_enum         NOT NULL,
    is_active       BOOLEAN                 NOT NULL DEFAULT true,
    created_at      TIMESTAMPTZ             NOT NULL DEFAULT now(),
    updated_at      TIMESTAMPTZ             NOT NULL DEFAULT now(),

    CONSTRAINT fk_patients_users    FOREIGN KEY (user_id)   REFERENCES users(id)    ON DELETE RESTRICT,
    CONSTRAINT fk_patients_careers  FOREIGN KEY (career_id) REFERENCES careers(id)  ON DELETE RESTRICT
);