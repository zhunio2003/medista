# CU-M4-04 — Ver Historial de Referencias

**Módulo:** M4 — Historial Clínico  
**Versión:** 1.0  
**Fecha:** 16 Abril 2026

---

## Descripción

Permite al médico visualizar el listado completo de referencias médicas generadas para un paciente a lo largo del tiempo, con acceso al detalle de cada una.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M4-04 |
| **Nombre** | Ver historial de referencias |
| **Actor principal** | Médico |
| **Módulo** | M4 — Historial Clínico |
| **Requisitos asociados** | RF-M4-04, RF-M4-06, RF-M4-08 |

---

## Precondiciones

1. El médico tiene sesión activa en el sistema.
2. El paciente tiene al menos una referencia médica registrada en el sistema.

---

## Postcondiciones

1. El médico visualiza el listado completo de referencias médicas generadas para el paciente.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Médico | Desde el historial clínico del paciente selecciona la opción "Ver historial de referencias". |
| 2 | Sistema | Muestra el listado cronológico de todas las referencias médicas del paciente, indicando para cada una: fecha, establecimiento de salud destino y diagnóstico asociado. |
| 3 | Médico | Selecciona una referencia para ver su detalle completo. |
| 4 | Sistema | Muestra el detalle completo de la referencia: datos del establecimiento destino, motivo de referencia, resumen clínico, hallazgos y diagnósticos CIE-10. |

---

## Flujos Alternativos

### FA-01 — Paciente sin referencias (paso 2)

| Paso | Actor | Acción |
|---|---|---|
| 2a | Sistema | Detecta que el paciente no tiene referencias médicas registradas. |
| 2b | Sistema | Informa al médico que el paciente no tiene referencias médicas registradas. |
| 2c | — | El caso de uso termina. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | El historial de referencias es de solo lectura — no permite crear, modificar ni eliminar referencias desde esta vista. |
| RN-02 | Solo el rol Médico puede ver el historial de referencias de un paciente. |

---

*MEDISTA — Casos de Uso M4 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
