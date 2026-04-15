# CU-M3-02 — Descargar PDF de Referencia

**Módulo:** M3 — Referencia Médica  
**Versión:** 1.0  
**Fecha:** 15 Abril 2026

---

## Descripción

Permite al médico descargar el documento PDF de una referencia médica con formato idéntico al formulario físico actual del Departamento Médico del Instituto Superior Universitario TEC Azuay.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M3-02 |
| **Nombre** | Descargar PDF de referencia |
| **Actor principal** | Médico |
| **Módulo** | M3 — Referencia Médica |
| **Requisitos asociados** | RF-M3-07 |

---

## Precondiciones

1. El médico tiene sesión activa en el sistema.
2. Existe una referencia médica previamente guardada en el sistema.

---

## Postcondiciones

1. El médico recibe el archivo PDF con el formato institucional del formulario de referencia médica del Departamento Médico.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Médico | Selecciona la opción "Descargar PDF" desde una referencia médica existente. |
| 2 | Sistema | Genera el documento PDF con formato idéntico al formulario físico actual, incluyendo todos los datos de la referencia, el logo institucional y los datos del profesional con espacio para firma. |
| 3 | Sistema | Entrega el archivo PDF al médico para su descarga inmediata. |

---

## Flujos Alternativos

### FA-01 — Error en generación del PDF (paso 2)

| Paso | Actor | Acción |
|---|---|---|
| 2a | Sistema | No puede generar el documento PDF por error interno. |
| 2b | Sistema | Informa al médico que no fue posible generar el documento e invita a intentarlo nuevamente. |
| 2c | — | El caso de uso termina sin entregar ningún archivo. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | El PDF generado debe ser idéntico en formato y estructura al formulario físico de referencia médica actual del Departamento Médico, incluyendo logo institucional, tablas y campos en las posiciones exactas del formulario original. |
| RN-02 | El PDF es de solo lectura — no puede ser editado desde el sistema una vez generado. |
| RN-03 | Solo el rol Médico puede descargar PDFs de referencias médicas. |

---

*MEDISTA — Casos de Uso M3 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
