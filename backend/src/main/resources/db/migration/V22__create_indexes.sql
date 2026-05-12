-- =============================================================
-- V22__create_indexes.sql
-- Crea los índices de búsqueda frecuente sobre las tablas del
-- sistema. Los índices UNIQUE ya están definidos como constraints
-- en sus respectivas tablas y no se repiten aquí.
-- =============================================================

-- users
CREATE INDEX idx_users_role
    ON users (role);

-- audit_logs
CREATE INDEX idx_audit_logs_user_id
    ON audit_logs (user_id);
CREATE INDEX idx_audit_logs_entity
    ON audit_logs (entity_type, entity_id);
CREATE INDEX idx_audit_logs_created_at
    ON audit_logs (created_at);

-- patients
CREATE INDEX idx_patients_name
    ON patients (last_name, first_name);
CREATE INDEX idx_patients_career_id
    ON patients (career_id);

-- medical_attendances
CREATE INDEX idx_medical_attendances_patient_id
    ON medical_attendances (patient_id);
CREATE INDEX idx_medical_attendances_attended_by
    ON medical_attendances (attended_by);
CREATE INDEX idx_medical_attendances_date
    ON medical_attendances (attendance_date);
CREATE INDEX idx_medical_attendances_is_active
    ON medical_attendances (is_active);

-- attendance_corrections
CREATE INDEX idx_attendance_corrections_attendance_id
    ON attendance_corrections (medical_attendance_id);

-- cie10_codes — índice GIN con pg_trgm para búsqueda difusa
CREATE INDEX idx_cie10_description_trgm
    ON cie10_codes USING GIN (description gin_trgm_ops);

-- notifications
CREATE INDEX idx_notifications_recipient_read
    ON notifications (recipient_id, is_read);