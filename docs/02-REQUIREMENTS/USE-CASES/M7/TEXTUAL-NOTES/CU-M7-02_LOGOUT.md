# CU-M7-02 — Cerrar Sesión

**Módulo:** M7 — Seguridad y Auditoría  
**Versión:** 1.0  
**Fecha:** Abril 2026

---

## Descripción

Permite a cualquier usuario del sistema cerrar su sesión activa de forma manual o automática por inactividad, invalidando el token JWT y registrando el evento en el log de auditoría.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M7-02 |
| **Nombre** | Cerrar sesión |
| **Actores** | Usuario (Médico, Decano, Administrador, Estudiante) |
| **Módulo** | M7 — Seguridad y Auditoría |
| **Requisitos asociados** | RF-M7-04 |

---

## Precondiciones

1. El usuario tiene sesión activa en el sistema.

---

## Postcondiciones

1. El token JWT queda invalidado en la blacklist de Redis y no puede reutilizarse.
2. El sistema registra el cierre de sesión en el log de auditoría con usuario, timestamp e IP de origen.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Usuario | Selecciona la opción "Cerrar sesión". |
| 2 | Sistema | Invalida el token JWT activo agregándolo a la blacklist en Redis. |
| 3 | Sistema | Registra el cierre de sesión en el log de auditoría con usuario, timestamp e IP de origen. |
| 4 | Sistema | Redirige al usuario a la pantalla de inicio de sesión. |

---

## Flujos Alternativos

### FA-01 — Cierre automático por inactividad (paso 1)

| Paso | Actor | Acción |
|---|---|---|
| 1a | Sistema | Detecta que el usuario ha superado el período de inactividad configurado sin realizar ninguna acción. |
| 1b | Sistema | Invalida el token JWT automáticamente y lo agrega a la blacklist en Redis. |
| 1c | Sistema | Registra el cierre automático en el log de auditoría. |
| 1d | Sistema | Redirige al usuario a la pantalla de inicio de sesión con un mensaje informando que la sesión expiró por inactividad. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | El token JWT invalidado no puede reutilizarse aunque no haya alcanzado su tiempo de expiración natural. |
| RN-02 | Todo cierre de sesión — manual o automático — queda registrado en el log de auditoría de forma inmutable. |
| RN-03 | El cierre automático por inactividad aplica a todos los roles sin excepción. El período de inactividad es configurable por el Administrador del Sistema. |

---

*MEDISTA-V2 — Casos de Uso M7 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
