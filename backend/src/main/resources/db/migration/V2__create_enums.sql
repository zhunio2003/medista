-- =============================================================
-- V2__create_enums.sql
-- Define los 13 tipos ENUM nativos de PostgreSQL utilizados
-- por las tablas del sistema. Debe ejecutarse antes que
-- cualquier migración que cree tablas.
-- =============================================================

-- -------------------------------------------------------------
-- Usuarios y seguridad
-- -------------------------------------------------------------

-- Roles del sistema — determina el control de acceso en toda la aplicación
CREATE TYPE role_enum AS ENUM ('MEDICO', 'DECANO', 'ADMINISTRADOR', 'ESTUDIANTE');

-- -------------------------------------------------------------
-- Dominio clínico — pacientes
-- -------------------------------------------------------------

-- Género del paciente — controla la visualización condicional de la sección obstétrica
CREATE TYPE gender_enum AS ENUM ('MALE', 'FEMALE');

CREATE TYPE marital_status_enum AS ENUM ('SINGLE', 'MARRIED', 'WIDOWED', 'DIVORCED', 'FREE_UNION');

CREATE TYPE blood_type_enum AS ENUM ('A_POSITIVE', 'A_NEGATIVE', 'B_POSITIVE', 'B_NEGATIVE', 'AB_POSITIVE', 'AB_NEGATIVE', 'O_POSITIVE', 'O_NEGATIVE');

-- -------------------------------------------------------------
-- Dominio clínico — atención médica
-- -------------------------------------------------------------

CREATE TYPE diagnosis_status_enum AS ENUM ('PRESUMPTIVE', 'DEFINITIVE');

-- 9 sistemas corporales evaluados en el examen físico
CREATE TYPE body_system_enum AS ENUM ('SKIN_AND_ANNEXES', 'HEAD', 'NECK', 'THORAX', 'HEART', 'ABDOMEN', 'INGUINAL', 'UPPER_LIMBS', 'LOWER_LIMBS');

CREATE TYPE referral_reason_enum AS ENUM ('LIMITED_RESOLUTION', 'LACK_OF_PROFESSIONAL', 'OTHER');

-- -------------------------------------------------------------
-- Sistema e infraestructura
-- -------------------------------------------------------------

CREATE TYPE audit_action_enum AS ENUM ('LOGIN', 'LOGOUT', 'VIEW', 'CREATE', 'UPDATE', 'DELETE', 'EXPORT', 'PRINT');

CREATE TYPE backup_trigger_enum AS ENUM ('AUTOMATIC', 'MANUAL');
CREATE TYPE backup_status_enum AS ENUM ('SUCCESS', 'FAILED');

-- -------------------------------------------------------------
-- Notificaciones y reportes
-- -------------------------------------------------------------

CREATE TYPE notification_type_enum AS ENUM ('CLINICAL', 'EPIDEMIOLOGICAL', 'INSTITUTIONAL');
CREATE TYPE threshold_type_enum AS ENUM ('PATIENT_VISITS', 'EPIDEMIOLOGICAL', 'INSTITUTIONAL');
CREATE TYPE report_frequency_enum AS ENUM ('WEEKLY', 'MONTHLY', 'BIANNUAL');