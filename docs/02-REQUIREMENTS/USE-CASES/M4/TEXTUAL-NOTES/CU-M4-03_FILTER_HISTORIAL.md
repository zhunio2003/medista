# CU-M4-03 — Filtrar Historial

**Módulo:** M4 — Historial Clínico  
**Versión:** 1.0  
**Fecha:** 16 Abril 2026

---

## Descripción

Permite al médico aplicar filtros sobre el historial clínico de un paciente para encontrar atenciones específicas por rango de fechas o tipo de diagnóstico CIE-10.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M4-03 |
| **Nombre** | Filtrar historial |
| **Actor principal** | Médico |
| **Módulo** | M4 — Historial Clínico |
| **Requisitos asociados** | RF-M4-05, RF-M4-06, RF-M4-08 |

---

## Precondiciones

1. El médico tiene sesión activa en el sistema.
2. El médico se encuentra en el historial clínico de un paciente con al menos una atención registrada.

---

## Postcondiciones

1. El historial muestra únicamente las atenciones que coinciden con los filtros aplicados.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Médico | Desde el historial clínico selecciona uno o más filtros disponibles: rango de fechas, tipo de diagnóstico CIE-10 o ambos combinados. |
| 2 | Sistema | Aplica los filtros seleccionados y muestra únicamente las atenciones que coinciden con los criterios. |
| 3 | Médico | Consulta los resultados filtrados. |
| 4 | Médico | Elimina los filtros para regresar a la vista completa del historial. |
| 5 | Sistema | Restaura la vista completa del historial sin filtros aplicados. |

---

## Flujos Alternativos

### FA-01 — Sin resultados para los filtros aplicados (paso 2)

| Paso | Actor | Acción |
|---|---|---|
| 2a | Sistema | No encuentra atenciones que coincidan con los criterios seleccionados. |
| 2b | Sistema | Informa al médico que no existen atenciones que coincidan con los filtros aplicados. |
| 2c | — | El médico puede modificar los filtros o eliminarlos para volver a la vista completa. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | Los filtros pueden aplicarse de forma individual o combinada simultáneamente. |
| RN-02 | El filtrado es de solo lectura — no permite crear, modificar ni eliminar ningún dato clínico. |
| RN-03 | Solo el rol Médico puede filtrar el historial clínico completo. |

---

*MEDISTA — Casos de Uso M4 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
