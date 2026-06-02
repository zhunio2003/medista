-- =============================================================================
-- V25__add_patients_trgm_indexes.sql
-- Agrega índices GIN con pg_trgm para búsqueda difusa por nombre
-- de paciente. Complementa el índice btree creado en V22.
-- =============================================================================
CREATE INDEX idx_patients_first_name_trgm
    ON patients USING GIN (first_name gin_trgm_ops);
CREATE INDEX idx_patients_last_name_trgm
    ON patients USING GIN (last_name gin_trgm_ops);