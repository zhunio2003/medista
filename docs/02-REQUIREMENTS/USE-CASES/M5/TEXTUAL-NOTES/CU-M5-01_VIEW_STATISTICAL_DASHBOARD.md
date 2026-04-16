# CU-M5-01 — Ver Dashboard Estadístico

**Módulo:** M5 — Dashboard Estadístico y Reportes  
**Versión:** 1.0  
**Fecha:** 16 Abril 2026

---

## Descripción

Permite al Decano visualizar los indicadores estadísticos agregados de las atenciones médicas del Departamento Médico en tiempo real, para la toma de decisiones institucional.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M5-01 |
| **Nombre** | Ver dashboard estadístico |
| **Actor principal** | Decano |
| **Módulo** | M5 — Dashboard Estadístico y Reportes |
| **Requisitos asociados** | RF-M5-01, RF-M5-02, RF-M5-07 |

---

## Precondiciones

1. El Decano tiene sesión activa en el sistema.
2. Existen atenciones médicas registradas en el sistema.

---

## Postcondiciones

1. El Decano visualiza los indicadores estadísticos actualizados del Departamento Médico.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Decano | Accede al módulo de Dashboard Estadístico. |
| 2 | Sistema | Muestra los indicadores actualizados en tiempo real: total de atenciones por período, enfermedades más frecuentes, distribución por carrera y ciclo, distribución por género y grupo etario, promedio de IMC general y por carrera, cantidad de referencias médicas generadas, picos de demanda y tasa de recurrencia de pacientes. |
| 3 | Decano | Consulta los indicadores presentados. |
| 4 | Sistema | Destaca visualmente cualquier anomalía o incremento inusual detectado en el volumen de atenciones o en la frecuencia de un diagnóstico específico. |

---

## Flujos Alternativos

### FA-01 — Sin datos suficientes (paso 2)

| Paso | Actor | Acción |
|---|---|---|
| 2a | Sistema | Detecta que no existen suficientes datos para generar los indicadores. |
| 2b | Sistema | Informa al Decano que aún no hay datos suficientes para mostrar estadísticas. |
| 2c | — | El caso de uso termina. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | El Decano accede exclusivamente a datos estadísticos agregados — ningún indicador, gráfica ni tabla permite identificar o acceder al expediente clínico de un estudiante individual. |
| RN-02 | El dashboard es de solo lectura. |
| RN-03 | El Administrador del Sistema no tiene acceso a este módulo. |

---

*MEDISTA — Casos de Uso M5 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
