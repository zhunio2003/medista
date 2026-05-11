-- =============================================================
-- V8__create_health_establishments.sql
-- Crea el catálogo de establecimientos de salud destino
-- de las referencias médicas generadas por el sistema.
-- =============================================================

CREATE TABLE health_establishments (
    id          BIGSERIAL       PRIMARY KEY,
    name        VARCHAR(255)    NOT NULL UNIQUE,
    entity_type VARCHAR(100),
    is_active   BOOLEAN         NOT NULL DEFAULT true
);