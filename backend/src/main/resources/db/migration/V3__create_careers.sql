-- =============================================================
-- V3__create_careers.sql
-- Crea el catálogo de carreras académicas e inserta las 16
-- carreras activas del Instituto Superior Universitario TEC Azuay.
-- Debe existir antes de crear la tabla patients (FK careers.id).
-- =============================================================

CREATE TABLE careers (
  id BIGSERIAL PRIMARY KEY,
  name VARCHAR(150) NOT NULL UNIQUE,
  is_active BOOLEAN NOT NULL DEFAULT true
);

-- Seed: 16 carreras institucionales activas en el primer despliegue.

INSERT INTO careers (name, is_active) VALUES
    ('Tecnología Superior Universitaria en Desarrollo de Software', true),
    ('Tecnología Superior en Desarrollo de Software', true),
    ('Tecnología en Análisis de Sistemas', true),
    ('Tecnología Superior en Administración de Infraestructura y Plataformas Tecnológicas', true),
    ('Tecnología Superior en Big Data', true),
    ('Tecnología Superior en Ciberseguridad', true),
    ('Tecnología Superior en Electricidad', true),
    ('Tecnología Superior en Mantenimiento Eléctrico y Control Industrial', true),
    ('Tecnología Superior en Mecánica Industrial', true),
    ('Tecnología Superior en Contabilidad', true),
    ('Tecnología Superior en Asesoría Financiera', true),
    ('Tecnología Superior en Educación Inicial', true),
    ('Tecnología Superior en Gestión del Patrimonio Histórico Cultural', true),
    ('Tecnología Superior en Control de Incendios y Operaciones de Rescate', true),
    ('Producción y Realización Audiovisual', true),
    ('Entrenamiento Deportivo', true);