# CU-M1-02 — Buscar Paciente

**Módulo:** M1 — Gestión de Pacientes  
**Versión:** 1.0  
**Fecha:** 14 Abril 2026

---

## Descripción

Permite al médico localizar un paciente ya registrado en el sistema mediante criterios de búsqueda por cédula, nombres o apellidos, con soporte de búsqueda difusa tolerante a errores tipográficos.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M1-02 |
| **Nombre** | Buscar paciente |
| **Actor principal** | Médico |
| **Módulo** | M1 — Gestión de Pacientes |
| **Requisitos asociados** | RF-M1-04 |

---

## Precondiciones

1. El médico tiene sesión activa en el sistema.
2. Existe al menos un paciente registrado en el sistema.

---

## Postcondiciones

1. El médico visualiza la lista de resultados que coinciden con el criterio de búsqueda.
2. El médico puede seleccionar un paciente de los resultados para ver su perfil.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Médico | Accede al buscador de pacientes. |
| 2 | Médico | Ingresa un criterio de búsqueda: número de cédula, nombres o apellidos. |
| 3 | Sistema | Ejecuta búsqueda difusa tolerante a errores tipográficos sobre la base de datos de pacientes. *[include: Búsqueda difusa]* |
| 4 | Sistema | Muestra los resultados coincidentes en tiempo real mientras el médico escribe. |
| 5 | Médico | Selecciona el paciente deseado de la lista de resultados. |
| 6 | Sistema | Navega al perfil completo del paciente seleccionado. |

---

## Flujos Alternativos

### FA-01 — Sin resultados (paso 4)

| Paso | Actor | Acción |
|---|---|---|
| 4a | Sistema | No encuentra ningún paciente que coincida con el criterio ingresado. |
| 4b | Sistema | Informa al médico que no existe ningún paciente con ese criterio y ofrece la opción de registrar un paciente nuevo. |
| 4c | — | El caso de uso termina. |

### FA-02 — Médico cancela la búsqueda (cualquier paso)

| Paso | Actor | Acción |
|---|---|---|
| Xa | Médico | Selecciona la opción de cancelar o limpia el campo de búsqueda. |
| Xb | Sistema | Limpia los resultados y regresa al estado inicial del buscador. |
| Xc | — | El caso de uso termina. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | La búsqueda es tolerante a errores tipográficos menores mediante búsqueda difusa (pg_trgm). |
| RN-02 | Los resultados se actualizan en tiempo real mientras el médico escribe, sin necesidad de confirmar la búsqueda manualmente. |
| RN-03 | Solo el rol Médico puede ejecutar este caso de uso. |

---

*MEDISTA — Casos de Uso M1 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
