-- =============================================================
-- V1__create_extensions.sql
-- Habilita las extensiones de PostgreSQL requeridas por MEDISTA.
-- Debe ejecutarse antes que cualquier otra migración.
-- =============================================================

-- pgcrypto: cifrado simétrico a nivel de campo para datos clínicos
-- y personales sensibles (LOPDP — datos de salud categoría especial)
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- pg_trgm: indexación por trigramas para búsqueda difusa en el
-- catálogo CIE-10 (+14.000 entradas) desde el formulario de atención
CREATE EXTENSION IF NOT EXISTS pg_trgm;