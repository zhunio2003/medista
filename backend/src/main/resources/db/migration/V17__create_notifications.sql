-- =============================================================
-- V17__create_notifications.sql
-- Crea la tabla de notificaciones entregadas a los usuarios.
-- Cubre alertas clínicas, epidemiológicas e institucionales
-- según notification_type_enum.
-- =============================================================

CREATE TABLE notifications (
    id           BIGSERIAL               PRIMARY KEY,
    recipient_id BIGINT                  NOT NULL,
    title        VARCHAR(255)            NOT NULL,
    message      TEXT                    NOT NULL,
    type         notification_type_enum  NOT NULL,
    is_read      BOOLEAN                 NOT NULL DEFAULT false,
    sent_at      TIMESTAMPTZ,
    created_at   TIMESTAMPTZ             NOT NULL DEFAULT now(),

    CONSTRAINT fk_notifications_users   FOREIGN KEY (recipient_id)  REFERENCES users(id)    ON DELETE RESTRICT
);