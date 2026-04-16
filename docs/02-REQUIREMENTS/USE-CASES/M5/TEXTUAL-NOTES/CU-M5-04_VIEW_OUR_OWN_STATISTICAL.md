# CU-M5-04 — Ver Estadísticas Propias

**Módulo:** M5 — Dashboard Estadístico y Reportes  
**Versión:** 1.0  
**Fecha:** 16 Abril 2026

---

## Descripción

Permite al médico visualizar indicadores estadísticos agregados exclusivamente de las atenciones médicas registradas por su propia cuenta, para monitorear su actividad clínica personal.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M5-04 |
| **Nombre** | Ver estadísticas propias |
| **Actor principal** | Médico |
| **Módulo** | M5 — Dashboard Estadístico y Reportes |
| **Requisitos asociados** | RF-M5-08 |

---

## Precondiciones

1. El médico tiene sesión activa en el sistema.
2. El médico tiene al menos una atención registrada en el sistema.

---

## Postcondiciones

1. El médico visualiza los indicadores estadísticos agregados de sus propias atenciones médicas.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Médico | Accede al módulo de estadísticas desde su panel. |
| 2 | Sistema | Muestra los indicadores estadísticos agregados de las atenciones registradas por el médico: total de atenciones por período, diagnósticos más frecuentes, distribución por carrera y pacientes recurrentes. |
| 3 | Médico | Consulta los indicadores presentados. |

---

## Flujos Alternativos

### FA-01 — Sin atenciones registradas (paso 2)

| Paso | Actor | Acción |
|---|---|---|
| 2a | Sistema | Detecta que el médico no tiene atenciones registradas. |
| 2b | Sistema | Informa al médico que aún no tiene atenciones registradas para generar estadísticas. |
| 2c | — | El caso de uso termina. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | El médico visualiza únicamente estadísticas de sus propias atenciones — el sistema nunca muestra datos de atenciones registradas por otros médicos. |
| RN-02 | Las estadísticas son siempre datos agregados — no exponen expedientes clínicos individuales de ningún estudiante. |
| RN-03 | Esta vista es independiente del dashboard del Decano y tiene alcance limitado a la actividad clínica del médico autenticado. |

---

*MEDISTA — Casos de Uso M5 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
