-- =============================================================
-- V7__create_cie10_codes.sql
-- Crea el catálogo de códigos diagnósticos CIE-10.
-- Más de 14.000 entradas cargadas via script de seed externo.
-- La columna description soporta búsqueda difusa mediante
-- índice GIN con pg_trgm (definido en V22).
-- =============================================================

CREATE TABLE cie10_codes (
    id          BIGSERIAL       PRIMARY KEY,
    code        VARCHAR(10)     NOT NULL UNIQUE,
    description VARCHAR(500)    NOT NULL,
    is_active   BOOLEAN         NOT NULL DEFAULT true
);