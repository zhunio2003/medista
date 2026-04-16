# CU-M6-02 — Consultar Historial de Notificaciones

**Módulo:** M6 — Notificaciones Inteligentes  
**Versión:** 1.0  
**Fecha:** 16 Abril 2026

---

## Descripción

Permite a cualquier usuario del sistema consultar el historial completo de sus notificaciones recibidas, con fecha, tipo y estado de lectura de cada una.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M6-02 |
| **Nombre** | Consultar historial de notificaciones |
| **Actores** | Médico, Decano, Estudiante |
| **Módulo** | M6 — Notificaciones Inteligentes |
| **Requisitos asociados** | RF-M6-11 |

---

## Precondiciones

1. El usuario tiene sesión activa en el sistema.
2. El usuario tiene al menos una notificación registrada en su historial.

---

## Postcondiciones

1. El usuario visualiza el listado de sus notificaciones con fecha, tipo y estado de lectura.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Usuario | Accede a la bandeja de notificaciones del sistema. |
| 2 | Sistema | Muestra el listado cronológico de todas las notificaciones del usuario, indicando para cada una: fecha, tipo de notificación y estado de lectura (leída / no leída). |
| 3 | Usuario | Consulta las notificaciones listadas. |
| 4 | Usuario | Selecciona una notificación para ver su detalle completo. |
| 5 | Sistema | Muestra el detalle completo de la notificación seleccionada. |

---

## Flujos Alternativos

### FA-01 — Sin notificaciones registradas (paso 2)

| Paso | Actor | Acción |
|---|---|---|
| 2a | Sistema | Detecta que el usuario no tiene notificaciones registradas. |
| 2b | Sistema | Informa al usuario que no tiene notificaciones registradas en su historial. |
| 2c | — | El caso de uso termina. |

### FA-02 — Usuario marca notificación como leída (paso 3)

| Paso | Actor | Acción |
|---|---|---|
| 3a | Usuario | Decide marcar una o más notificaciones como leídas. *[extend: Marcar notificación como leída]* |
| 3b | — | El flujo continúa desde el paso 3. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | Cada usuario solo puede consultar sus propias notificaciones — el sistema nunca muestra notificaciones de otros usuarios. |
| RN-02 | El historial es consultable en cualquier momento independientemente del estado de lectura de cada notificación. |
| RN-03 | Las notificaciones no pueden eliminarse del historial — solo pueden marcarse como leídas. |

---

*MEDISTA — Casos de Uso M6 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
