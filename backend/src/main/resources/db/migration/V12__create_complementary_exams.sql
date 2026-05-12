-- =============================================================
-- V12__create_complementary_exams.sql
-- Crea la tabla de exámenes complementarios adjuntos a una
-- atención médica. Almacena únicamente metadatos — los archivos
-- binarios residen en el sistema de ficheros del servidor.
-- Una atención puede tener cero o muchos exámenes (1:N).
-- =============================================================

CREATE TABLE complementary_exams (
    id                    BIGSERIAL       PRIMARY KEY,
    medical_attendance_id BIGINT          NOT NULL,
    description           TEXT,
    not_applicable        BOOLEAN         NOT NULL DEFAULT false,
    file_path             VARCHAR(500),
    file_name             VARCHAR(255),
    file_mime_type        VARCHAR(100),
    created_at            TIMESTAMPTZ     NOT NULL DEFAULT now(),

    CONSTRAINT fk_complementary_exams_medical_attendances   FOREIGN KEY (medical_attendance_id)  REFERENCES medical_attendances(id)  ON DELETE RESTRICT
);