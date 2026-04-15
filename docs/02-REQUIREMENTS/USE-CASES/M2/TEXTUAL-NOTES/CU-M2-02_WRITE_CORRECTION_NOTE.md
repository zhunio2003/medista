# CU-M2-02 — Agregar Nota de Corrección

**Módulo:** M2 — Atención Médica  
**Versión:** 1.0  
**Fecha:** 15 Abril 2026

---

## Descripción

Permite al médico agregar una nota de corrección sobre una atención médica ya guardada, preservando el registro original inmutable conforme a la normativa del MSP y la LOPDP.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M2-02 |
| **Nombre** | Agregar nota de corrección |
| **Actor principal** | Médico |
| **Módulo** | M2 — Atención Médica |
| **Requisitos asociados** | RF-M2-14 |

---

## Precondiciones

1. El médico tiene sesión activa en el sistema.
2. Existe una atención médica previamente guardada en el sistema.

---

## Postcondiciones

1. La nota de corrección queda vinculada a la atención original sin modificarla.
2. El log de auditoría registra quién agregó la nota, cuándo y desde qué IP de origen.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Médico | Navega al historial clínico del paciente y selecciona la atención que requiere corrección. |
| 2 | Sistema | Muestra el detalle completo de la atención seleccionada en modo solo lectura. |
| 3 | Médico | Selecciona la opción "Agregar nota de corrección". |
| 4 | Sistema | Presenta un campo de texto para ingresar la nota de corrección. |
| 5 | Médico | Redacta la nota explicando el dato erróneo y el valor correcto. |
| 6 | Médico | Confirma la nota de corrección. |
| 7 | Sistema | Vincula la nota a la atención original con timestamp automático y datos del profesional extraídos de la sesión activa, y registra el evento en el log de auditoría. |
| 8 | Sistema | Muestra la atención con la nota de corrección adjunta y visible. |

---

## Flujos Alternativos

### FA-01 — Médico cancela (cualquier paso)

| Paso | Actor | Acción |
|---|---|---|
| Xa | Médico | Selecciona la opción de cancelar. |
| Xb | Sistema | Descarta el texto ingresado sin modificar ningún registro. |
| Xc | — | El caso de uso termina. |

### FA-02 — Nota vacía (paso 6)

| Paso | Actor | Acción |
|---|---|---|
| 6a | Sistema | Detecta que el campo de texto está vacío. |
| 6b | Sistema | Rechaza la confirmación e informa al médico que la nota no puede estar vacía. |
| 6c | Médico | Ingresa el texto de la nota. |
| 6d | — | El flujo retoma desde el paso 6. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | La atención médica original nunca se modifica. La nota de corrección es un registro adicional vinculado que coexiste con el original. |
| RN-02 | Una atención médica puede acumular múltiples notas de corrección a lo largo del tiempo. |
| RN-03 | Cada nota registra automáticamente el nombre del profesional que la agregó, timestamp e IP de origen. Ninguna nota es anónima. |
| RN-04 | Solo el rol Médico puede agregar notas de corrección. |

---

*MEDISTA — Casos de Uso M2 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
