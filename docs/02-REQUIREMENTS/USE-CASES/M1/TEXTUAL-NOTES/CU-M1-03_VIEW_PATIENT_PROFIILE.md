# CU-M1-03 — Ver Perfil del Paciente

**Módulo:** M1 — Gestión de Pacientes  
**Versión:** 1.0  
**Fecha:** 14 de Abril 2026

---

## Descripción

Permite al médico visualizar el perfil completo de un paciente registrado, incluyendo todos sus datos de filiación y acceso directo a su historial clínico.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M1-03 |
| **Nombre** | Ver perfil del paciente |
| **Actor principal** | Médico |
| **Módulo** | M1 — Gestión de Pacientes |
| **Requisitos asociados** | RF-M1-05 |

---

## Precondiciones

1. El médico tiene sesión activa en el sistema.
2. El paciente existe en el sistema.

---

## Postcondiciones

1. El médico visualiza todos los datos de filiación del paciente.
2. El médico tiene acceso directo al historial clínico del paciente desde el perfil.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Médico | Selecciona un paciente desde los resultados de búsqueda o desde cualquier otra vista del sistema que referencie al paciente. |
| 2 | Sistema | Muestra el perfil completo del paciente con todos sus datos de filiación: nombres, apellidos, cédula, carrera, ciclo, dirección, teléfono, fecha de nacimiento, edad, género, estado civil y tipo de sangre. |
| 3 | Sistema | Presenta acceso directo al historial clínico del paciente y a la opción de editar sus datos. |
| 4 | Médico | Consulta la información presentada o navega a otra sección desde el perfil. |

---

## Flujos Alternativos

### FA-01 — Médico navega al historial clínico (paso 4)

| Paso | Actor | Acción |
|---|---|---|
| 4a | Médico | Selecciona la opción de ver el historial clínico del paciente. |
| 4b | Sistema | Redirige al módulo M4 — Historial Clínico mostrando todas las atenciones registradas del paciente. |

### FA-02 — Médico navega a editar datos (paso 4)

| Paso | Actor | Acción |
|---|---|---|
| 4a | Médico | Selecciona la opción de editar los datos de filiación. |
| 4b | Sistema | Redirige al caso de uso CU-M1-04 — Editar datos de filiación. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | Solo el rol Médico puede acceder al perfil completo del paciente. |
| RN-02 | El perfil es de solo lectura desde esta vista. Para modificar datos se debe ejecutar CU-M1-04 — Editar datos de filiación. |

---

*MEDISTA — Casos de Uso M1 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
