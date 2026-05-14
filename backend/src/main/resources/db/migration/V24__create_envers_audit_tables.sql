-- =============================================================================
-- V24__create_envers_audit_tables.sql
-- =============================================================================
--  Crea las tablas de auditoría histórica requeridas por Hibernate Envers para
--  todas las entidades anotadas con @Audited en el sistema. Cada tabla replica
--  las columnas de su entidad origen y agrega las columnas de control de Envers:
--  rev (revisión), revtype (tipo de operación: 0=INSERT, 1=UPDATE, 2=DELETE).
--
--  Estas tablas son inmutables por diseño — ningún proceso de la aplicación
--  elimina ni modifica sus registros. Satisfacen el requisito de trazabilidad
--  completa de cambios sobre datos clínicos exigido por la LOPDP y el
--  Acuerdo Ministerial MSP No. 00000125.
--
--  Todas las tablas _aud dependen de revinfo (V23), que debe existir previamente.
-- ----------------------------------------------------------------------------
-- Tablas creadas:
--   1. users_aud
--   2. patients_aud
--   3. patient_background_aud
--   4. medical_attendances_aud
--   5. diagnoses_aud
--   6. medical_referrals_aud
--   7. notification_thresholds_aud
-- =============================================================================


-- -----------------------------------------------------------------------------
-- 1. users_aud
-- Audita cambios de rol, estado de cuenta y datos personales de usuarios.
-- Trazabilidad requerida por LOPDP para cuentas con acceso a datos clínicos.
-- -----------------------------------------------------------------------------
CREATE TABLE users_aud (
    id             BIGINT        NOT NULL,
    rev            INTEGER       NOT NULL REFERENCES revinfo(rev),
    revtype        SMALLINT,
    first_name     VARCHAR(100),
    last_name      VARCHAR(100),
    email          VARCHAR(255),
    password_hash  VARCHAR(255),
    role           VARCHAR(50),
    is_active      BOOLEAN,
    created_at     TIMESTAMPTZ,
    updated_at     TIMESTAMPTZ,
    CONSTRAINT pk_users_aud PRIMARY KEY (id, rev)
);


-- -----------------------------------------------------------------------------
-- 2. patients_aud
-- Audita cambios en datos demográficos del paciente (estudiante).
-- La LOPDP exige trazabilidad completa sobre modificaciones de datos personales.
-- -----------------------------------------------------------------------------
CREATE TABLE patients_aud (
    id               BIGINT        NOT NULL,
    rev              INTEGER       NOT NULL REFERENCES revinfo(rev),
    revtype          SMALLINT,
    user_id          BIGINT,
    career_id        BIGINT,
    cedula           VARCHAR(10),
    first_name       VARCHAR(100),
    last_name        VARCHAR(100),
    semester         VARCHAR(20),
    address          VARCHAR(255),
    neighborhood     VARCHAR(100),
    parish           VARCHAR(100),
    canton           VARCHAR(100),
    province         VARCHAR(100),
    phone            VARCHAR(20),
    birth_date       DATE,
    birth_place      VARCHAR(100),
    birth_country    VARCHAR(100),
    gender           VARCHAR(20),
    marital_status   VARCHAR(30),
    blood_type       VARCHAR(10),
    is_active        BOOLEAN,
    created_at       TIMESTAMPTZ,
    updated_at       TIMESTAMPTZ,
    CONSTRAINT pk_patients_aud PRIMARY KEY (id, rev)
);


-- -----------------------------------------------------------------------------
-- 3. patient_background_aud
-- Audita la evolución de antecedentes clínicos del paciente a lo largo del tiempo.
-- Permite a la médico consultar cómo evolucionaron los antecedentes históricos.
-- -----------------------------------------------------------------------------
CREATE TABLE patient_background_aud (
    id               BIGINT    NOT NULL,
    rev              INTEGER   NOT NULL REFERENCES revinfo(rev),
    revtype          SMALLINT,
    patient_id       BIGINT,
    allergies        TEXT,
    clinical         TEXT,
    gynecological    TEXT,
    traumatological  TEXT,
    surgical         TEXT,
    pharmacological  TEXT,
    updated_at       TIMESTAMPTZ,
    updated_by       BIGINT,
    CONSTRAINT pk_patient_background_aud PRIMARY KEY (id, rev)
);


-- -----------------------------------------------------------------------------
-- 4. medical_attendances_aud
-- Segunda capa de garantía de integridad sobre atenciones médicas inmutables.
-- Aunque las atenciones no se modifican por regla de negocio, Envers actúa
-- como respaldo de integridad a nivel de infraestructura (ADR-003).
-- -----------------------------------------------------------------------------
CREATE TABLE medical_attendances_aud (
    id                       BIGINT          NOT NULL,
    rev                      INTEGER         NOT NULL REFERENCES revinfo(rev),
    revtype                  SMALLINT,
    patient_id               BIGINT,
    attended_by              BIGINT,
    attendance_date          TIMESTAMPTZ,
    reason_for_visit         TEXT,
    current_illness          TEXT,
    treatment                TEXT,
    blood_pressure_systolic  SMALLINT,
    blood_pressure_diastolic SMALLINT,
    weight_kg                NUMERIC(5,2),
    height_cm                NUMERIC(5,2),
    bmi                      NUMERIC(5,2),
    heart_rate               SMALLINT,
    respiratory_rate         SMALLINT,
    temperature_celsius      NUMERIC(4,1),
    oxygen_saturation        SMALLINT,
    glasgow_eye              SMALLINT,
    glasgow_verbal           SMALLINT,
    glasgow_motor            SMALLINT,
    glasgow_total            SMALLINT,
    capillary_refill         VARCHAR(20),
    pupillary_reflex         VARCHAR(50),
    is_active                BOOLEAN,
    created_at               TIMESTAMPTZ,
    CONSTRAINT pk_medical_attendances_aud PRIMARY KEY (id, rev)
);


-- -----------------------------------------------------------------------------
-- 5. diagnoses_aud
-- Audita diagnósticos clínicos — datos sensibles sujetos a retención indefinida
-- por normativa MSP. Permite reconstruir el historial diagnóstico completo.
-- -----------------------------------------------------------------------------
CREATE TABLE diagnoses_aud (
    id                   BIGINT      NOT NULL,
    rev                  INTEGER     NOT NULL REFERENCES revinfo(rev),
    revtype              SMALLINT,
    medical_attendance_id BIGINT,
    cie10_code_id        BIGINT,
    description          TEXT,
    status               VARCHAR(20),
    created_at           TIMESTAMPTZ,
    CONSTRAINT pk_diagnoses_aud PRIMARY KEY (id, rev)
);


-- -----------------------------------------------------------------------------
-- 6. medical_referrals_aud
-- Audita referencias médicas — actos clínicos formales cuyo cambio de estado
-- debe quedar registrado para trazabilidad del acto médico.
-- -----------------------------------------------------------------------------
CREATE TABLE medical_referrals_aud (
    id                      BIGINT        NOT NULL,
    rev                     INTEGER       NOT NULL REFERENCES revinfo(rev),
    revtype                 SMALLINT,
    medical_attendance_id   BIGINT,
    health_establishment_id BIGINT,
    service                 VARCHAR(150),
    specialty               VARCHAR(150),
    referral_date           DATE,
    referral_reason         VARCHAR(50),
    relevant_findings       TEXT,
    referred_by             BIGINT,
    is_active               BOOLEAN,
    created_at              TIMESTAMPTZ,
    CONSTRAINT pk_medical_referrals_aud PRIMARY KEY (id, rev)
);


-- -----------------------------------------------------------------------------
-- 7. notification_thresholds_aud
-- Audita cambios en umbrales de notificación — configuración crítica del sistema.
-- Permite saber quién modificó un umbral y cuándo, para auditoría administrativa.
-- -----------------------------------------------------------------------------
CREATE TABLE notification_thresholds_aud (
    id              BIGINT      NOT NULL,
    rev             INTEGER     NOT NULL REFERENCES revinfo(rev),
    revtype         SMALLINT,
    threshold_type  VARCHAR(50),
    value           INTEGER,
    period_days     INTEGER,
    is_active       BOOLEAN,
    updated_at      TIMESTAMPTZ,
    updated_by      BIGINT,
    CONSTRAINT pk_notification_thresholds_aud PRIMARY KEY (id, rev)
);