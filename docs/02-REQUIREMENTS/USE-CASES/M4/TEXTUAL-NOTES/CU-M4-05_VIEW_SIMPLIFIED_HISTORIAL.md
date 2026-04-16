# CU-M4-05 — Ver Historial Simplificado

**Módulo:** M4 — Historial Clínico  
**Versión:** 1.0  
**Fecha:** Abril 2026

---

## Descripción

Permite al estudiante consultar desde la aplicación móvil un resumen simplificado de sus propias atenciones médicas registradas en el Departamento Médico del Instituto Superior Universitario TEC Azuay.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M4-05 |
| **Nombre** | Ver historial simplificado |
| **Actor principal** | Estudiante |
| **Módulo** | M4 — Historial Clínico |
| **Requisitos asociados** | RF-M4-07, RF-M4-08 |

---

## Precondiciones

1. El estudiante tiene sesión activa en la aplicación móvil autenticado con correo institucional @tecazuay.edu.ec.
2. El estudiante tiene al menos una atención médica registrada en el sistema.

---

## Postcondiciones

1. El estudiante visualiza el listado de sus atenciones médicas personales en formato simplificado.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Estudiante | Accede a la sección "Mi historial" en la aplicación móvil. |
| 2 | Sistema | Muestra el listado cronológico de todas las atenciones médicas del estudiante, presentando para cada una: fecha, motivo de consulta, diagnóstico y tratamiento. |
| 3 | Estudiante | Selecciona una atención para ver su resumen. |
| 4 | Sistema | Muestra el resumen completo de la atención seleccionada con los cuatro campos visibles: fecha, motivo de consulta, diagnóstico y tratamiento. |

---

## Flujos Alternativos

### FA-01 — Estudiante sin atenciones registradas (paso 2)

| Paso | Actor | Acción |
|---|---|---|
| 2a | Sistema | Detecta que el estudiante no tiene atenciones médicas registradas. |
| 2b | Sistema | Informa al estudiante que aún no tiene atenciones médicas registradas en el sistema. |
| 2c | — | El caso de uso termina. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | El estudiante solo puede visualizar sus propias atenciones médicas — el sistema nunca expone datos de otros estudiantes. |
| RN-02 | La vista simplificada no expone signos vitales, examen físico por sistemas, datos obstétricos ni notas de corrección — solo fecha, motivo de consulta, diagnóstico y tratamiento. |
| RN-03 | Es de solo lectura — el estudiante no puede crear, modificar ni eliminar ningún dato desde esta vista. |
| RN-04 | Esta funcionalidad es accesible exclusivamente desde la aplicación móvil con autenticación por correo institucional @tecazuay.edu.ec. |

---

*MEDISTA-V2 — Casos de Uso M4 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
