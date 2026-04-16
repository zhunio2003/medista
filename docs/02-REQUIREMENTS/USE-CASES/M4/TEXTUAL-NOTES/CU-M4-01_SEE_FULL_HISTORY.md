# CU-M4-01 — Ver Historial Completo

**Módulo:** M4 — Historial Clínico  
**Versión:** 1.0  
**Fecha:** 16 Abril 2026

---

## Descripción

Permite al médico visualizar la línea de tiempo cronológica completa de todas las atenciones médicas de un paciente, con acceso al detalle de cada atención.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M4-01 |
| **Nombre** | Ver historial completo |
| **Actor principal** | Médico |
| **Módulo** | M4 — Historial Clínico |
| **Requisitos asociados** | RF-M4-01, RF-M4-03, RF-M4-04, RF-M4-06, RF-M4-08 |

---

## Precondiciones

1. El médico tiene sesión activa en el sistema.
2. El paciente existe en el sistema y tiene al menos una atención registrada.

---

## Postcondiciones

1. El médico visualiza la línea de tiempo completa de atenciones del paciente.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Médico | Accede al perfil del paciente y selecciona la opción "Ver historial clínico". |
| 2 | Sistema | Muestra la línea de tiempo cronológica de todas las atenciones del paciente, presentando en vista resumida la fecha y el diagnóstico principal de cada una. |
| 3 | Médico | Selecciona una atención para expandirla. |
| 4 | Sistema | Muestra el detalle completo del formulario de atención: motivo de consulta, antecedentes, enfermedad actual, signos vitales, examen físico, diagnóstico CIE-10, tratamiento y notas de corrección si las hay. |

---

## Flujos Alternativos

### FA-01 — Paciente sin atenciones (paso 2)

| Paso | Actor | Acción |
|---|---|---|
| 2a | Sistema | Detecta que el paciente no tiene atenciones registradas en el sistema. |
| 2b | Sistema | Informa al médico que el paciente no tiene atenciones registradas. |
| 2c | — | El caso de uso termina. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | El historial clínico es de solo lectura. Ningún dato puede crearse, modificarse ni eliminarse desde esta vista. |
| RN-02 | Solo el rol Médico tiene acceso al historial completo del paciente. |
| RN-03 | Las notas de corrección vinculadas a una atención se muestran junto al registro original. |

---

*MEDISTA — Casos de Uso M4 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
