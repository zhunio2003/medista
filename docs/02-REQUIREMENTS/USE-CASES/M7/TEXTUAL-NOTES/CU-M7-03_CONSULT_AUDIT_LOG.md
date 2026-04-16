# CU-M7-03 — Consultar Log de Auditoría

**Módulo:** M7 — Seguridad y Auditoría  
**Versión:** 1.0  
**Fecha:** 16 Abril 2026

---

## Descripción

Permite al Administrador del Sistema consultar el registro completo e inmutable de todas las acciones ejecutadas en el sistema, garantizando la trazabilidad exigida por la LOPDP y el Acuerdo Ministerial MSP No. 00000125.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M7-03 |
| **Nombre** | Consultar log de auditoría |
| **Actor principal** | Administrador del Sistema |
| **Módulo** | M7 — Seguridad y Auditoría |
| **Requisitos asociados** | RF-M7-08, RF-M7-09, RF-M7-10 |

---

## Precondiciones

1. El Administrador del Sistema tiene sesión activa en el sistema.
2. Existen registros en el log de auditoría.

---

## Postcondiciones

1. El Administrador visualiza los registros de auditoría correspondientes a los criterios consultados.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Administrador | Accede a la sección de log de auditoría. |
| 2 | Sistema | Muestra el listado cronológico de registros de auditoría indicando para cada uno: usuario que ejecutó la acción, tipo de acción, recurso afectado, timestamp e IP de origen. |
| 3 | Administrador | Aplica filtros para acotar los resultados: por usuario, por tipo de acción, por rango de fechas o por IP de origen. |
| 4 | Sistema | Muestra los registros que coinciden con los filtros aplicados. |
| 5 | Administrador | Selecciona un registro para ver su detalle completo. |
| 6 | Sistema | Muestra el detalle completo del registro seleccionado incluyendo la firma HMAC-SHA256 de integridad. |

---

## Flujos Alternativos

### FA-01 — Sin registros para los filtros aplicados (paso 4)

| Paso | Actor | Acción |
|---|---|---|
| 4a | Sistema | No encuentra registros que coincidan con los criterios seleccionados. |
| 4b | Sistema | Informa al Administrador que no existen registros que coincidan con los filtros aplicados. |
| 4c | — | El Administrador puede modificar o eliminar los filtros para ampliar la búsqueda. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | El log de auditoría es estrictamente de solo lectura — ningún rol puede modificar, eliminar ni alterar ningún registro bajo ninguna circunstancia. |
| RN-02 | Solo el Administrador del Sistema puede consultar el log de auditoría. Ningún otro rol tiene acceso a esta funcionalidad. |
| RN-03 | Cada registro incluye una firma digital HMAC-SHA256 que permite detectar cualquier intento de manipulación posterior a su creación. |
| RN-04 | El log registra tanto accesos exitosos como fallidos, y toda acción relevante ejecutada sobre datos clínicos, usuarios y configuración del sistema. |

---

*MEDISTA — Casos de Uso M7 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
