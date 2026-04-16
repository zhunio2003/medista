# CU-M6-01 — Recibir Notificación

**Módulo:** M6 — Notificaciones Inteligentes  
**Versión:** 1.0  
**Fecha:** 16 Abril 2026

---

## Descripción

El sistema entrega notificaciones proactivas a cada rol según los eventos clínicos, epidemiológicos o institucionales que se producen en el sistema, a través de los canales correspondientes a cada destinatario.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M6-01 |
| **Nombre** | Recibir notificación |
| **Actores** | Médico, Decano, Estudiante |
| **Módulo** | M6 — Notificaciones Inteligentes |
| **Requisitos asociados** | RF-M6-01, RF-M6-02, RF-M6-03, RF-M6-04, RF-M6-05, RF-M6-06, RF-M6-07, RF-M6-08, RF-M6-09 |

---

## Precondiciones

1. El usuario tiene sesión activa en el sistema.
2. Se ha producido un evento en el sistema que genera una notificación para ese rol.

---

## Postcondiciones

1. La notificación llega al usuario por el canal correspondiente a su rol.
2. La notificación queda almacenada en el historial del usuario con estado "no leída".

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Sistema | Detecta un evento que genera una notificación: atención médica registrada, referencia generada, seguimiento pendiente, paciente frecuente, alerta epidemiológica o anomalía institucional. |
| 2 | Sistema | Determina el destinatario y el canal de entrega según el rol: bandeja interna y correo electrónico para todos los roles, notificación push móvil exclusivamente para estudiantes. |
| 3 | Sistema | Entrega la notificación al destinatario por los canales correspondientes y la almacena en su historial. |
| 4 | Usuario | Recibe y visualiza la notificación en el canal correspondiente. |

---

## Flujos Alternativos

### FA-01 — Fallo en entrega por push móvil (paso 3)

| Paso | Actor | Acción |
|---|---|---|
| 3a | Sistema | Detecta un fallo en la entrega de la notificación push al dispositivo móvil del estudiante. |
| 3b | Sistema | Registra el fallo y reintenta la entrega. |
| 3c | Sistema | La notificación queda disponible en la bandeja interna independientemente del resultado del reintento. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | Toda notificación se almacena en el historial del destinatario independientemente del resultado de la entrega por cada canal. |
| RN-02 | Las notificaciones dirigidas al Decano nunca exponen datos clínicos individuales de ningún estudiante. |
| RN-03 | Las notificaciones push son exclusivas del canal móvil para el rol Estudiante. |
| RN-04 | Los umbrales que disparan alertas automáticas son configurados por el Administrador del Sistema en el Módulo M8. |

---

*MEDISTA — Casos de Uso M6 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
