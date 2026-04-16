# CU-M5-02 — Aplicar Filtros Cruzados

**Módulo:** M5 — Dashboard Estadístico y Reportes  
**Versión:** 1.0  
**Fecha:** 16 Abril 2026

---

## Descripción

Permite al Decano aplicar múltiples filtros simultáneos sobre el dashboard estadístico para analizar las atenciones médicas por carrera, ciclo, período, género, edad, diagnóstico y otros criterios combinados.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M5-02 |
| **Nombre** | Aplicar filtros cruzados |
| **Actor principal** | Decano |
| **Módulo** | M5 — Dashboard Estadístico y Reportes |
| **Requisitos asociados** | RF-M5-03, RF-M5-07 |

---

## Precondiciones

1. El Decano tiene sesión activa en el sistema.
2. El Decano se encuentra en el dashboard estadístico con datos disponibles.

---

## Postcondiciones

1. El dashboard muestra únicamente los indicadores que corresponden a los filtros aplicados.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Decano | Selecciona uno o más filtros disponibles: carrera, ciclo, período o semestre, género, rango de edad, diagnóstico CIE-10 o tipo de atención. |
| 2 | Sistema | Aplica los filtros de forma combinada y actualiza todos los indicadores del dashboard mostrando únicamente los datos que corresponden a los criterios seleccionados. |
| 3 | Decano | Consulta los resultados filtrados. |
| 4 | Decano | Elimina los filtros para regresar a la vista general del dashboard. |
| 5 | Sistema | Restaura el dashboard con todos los datos sin filtros aplicados. |

---

## Flujos Alternativos

### FA-01 — Sin resultados para los filtros aplicados (paso 2)

| Paso | Actor | Acción |
|---|---|---|
| 2a | Sistema | No encuentra datos que coincidan con los criterios seleccionados. |
| 2b | Sistema | Informa al Decano que no existen datos que coincidan con los filtros aplicados. |
| 2c | — | El Decano puede modificar o eliminar los filtros. |

### FA-02 — Decano compara con período anterior (paso 3)

| Paso | Actor | Acción |
|---|---|---|
| 3a | Decano | Selecciona la opción de comparar los resultados filtrados con el período anterior equivalente. *[extend: Comparar con período anterior]* |
| 3b | Sistema | Muestra los indicadores filtrados del período actual junto con los del período anterior para comparación directa. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | Los filtros pueden aplicarse de forma individual o combinada simultáneamente sin restricción de cantidad. |
| RN-02 | Los resultados filtrados son siempre datos agregados — nunca exponen información clínica individual de ningún estudiante. |
| RN-03 | Solo el Decano puede aplicar filtros cruzados en el dashboard estadístico. |

---

*MEDISTA — Casos de Uso M5 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
