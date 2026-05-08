-- =============================================================
-- V4__create_users.sql
-- Crea la tabla de cuentas de usuario del sistema.
-- Gestiona exclusivamente autenticación y autorización —
-- ningún dato clínico se almacena aquí.
-- =============================================================

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role role_enum NOT NULL,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);