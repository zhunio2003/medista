# CU-M1-01 — Registrar Paciente Nuevo

**Módulo:** M1 — Gestión de Pacientes  
**Versión:** 1.0  
**Fecha:** 14 de Abril 2026

---

## Descripción

Permite al médico registrar un nuevo estudiante en el sistema con todos sus datos de filiación durante su primera visita al Departamento Médico.

---
  
## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M1-01 |
| **Nombre** | Registrar paciente nuevo |
| **Actor principal** | Médico |
| **Módulo** | M1 — Gestión de Pacientes |
| **Requisitos asociados** | RF-M1-01, RF-M1-02, RF-M1-03, RF-M1-08, RF-M1-09 |

---

## Precondiciones

1. El médico tiene sesión activa en el sistema.
2. El estudiante no existe previamente en el sistema (no hay registro con su número de cédula).

---

## Postcondiciones

1. El paciente queda registrado en el sistema con todos sus datos de filiación.
2. El sistema queda listo para crear una atención médica asociada a ese paciente.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Médico | Selecciona la opción "Registrar paciente nuevo". |
| 2 | Sistema | Presenta el formulario de datos de filiación vacío. |
| 3 | Médico | Ingresa todos los datos de filiación del estudiante: nombres, apellidos, carrera, ciclo, número de cédula, dirección, barrio, parroquia, cantón, provincia, teléfono, fecha de nacimiento, lugar de nacimiento, país, género, estado civil y tipo de sangre. |
| 4 | Sistema | Calcula automáticamente la edad a partir de la fecha de nacimiento ingresada. *[include: CU-M1-05 Calcular edad]* |
| 5 | Sistema | Valida el formato y dígito verificador de la cédula ingresada. *[include: CU-M1-06 Validar cédula ecuatoriana]* |
| 6 | Sistema | Verifica que la cédula no exista previamente en la base de datos. |
| 7 | Médico | Confirma el registro. |
| 8 | Sistema | Almacena el paciente y muestra el perfil creado con mensaje de confirmación de éxito. |

---

## Flujos Alternativos

### FA-01 — Cédula duplicada (paso 6)

| Paso | Actor | Acción |
|---|---|---|
| 6a | Sistema | Detecta que ya existe un paciente registrado con esa cédula. |
| 6b | Sistema | Muestra un mensaje informando al médico que el paciente ya está registrado y ofrece la opción de buscarlo directamente. |
| 6c | — | El caso de uso termina sin crear un registro duplicado. |

### FA-02 — Formato de cédula inválido (paso 5)

| Paso | Actor | Acción |
|---|---|---|
| 5a | Sistema | Detecta que la cédula no cumple con el formato de cédula ecuatoriana válida (10 dígitos, dígito verificador correcto). |
| 5b | Sistema | Muestra un mensaje de error indicando que la cédula ingresada no es válida. |
| 5c | Médico | Corrige el valor ingresado. |
| 5d | — | El flujo retoma desde el paso 5. |

### FA-03 — Paciente de género femenino (paso 3)

| Paso | Actor | Acción |
|---|---|---|
| 3a | Sistema | Detecta que el género ingresado corresponde al sexo femenino. |
| 3b | Sistema | Habilita automáticamente los campos de historial obstétrico en el formulario. *[extend: CU-M1-07 Habilitar campos obstétricos]* |
| 3c | — | El flujo continúa desde el paso 4. |

### FA-04 — Médico cancela el registro (cualquier paso)

| Paso | Actor | Acción |
|---|---|---|
| Xa | Médico | Selecciona la opción de cancelar el registro. |
| Xb | Sistema | Descarta todos los datos ingresados y regresa a la pantalla anterior. |
| Xc | — | No se crea ningún registro. El caso de uso termina. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | La cédula es el identificador único del paciente. No pueden existir dos pacientes con la misma cédula en el sistema. |
| RN-02 | La edad se calcula automáticamente a partir de la fecha de nacimiento y no puede ser ingresada ni modificada manualmente. |
| RN-03 | Solo el rol Médico puede ejecutar este caso de uso. Los roles Decano, Administrador del Sistema y Estudiante no tienen acceso a esta funcionalidad. |
| RN-04 | El formato de cédula válido corresponde a la cédula de identidad ecuatoriana: 10 dígitos numéricos con dígito verificador correcto según el algoritmo del Registro Civil del Ecuador. |
| RN-05 | Los campos de historial obstétrico se habilitan exclusivamente cuando el género registrado es femenino. Para pacientes de género masculino, estos campos no están disponibles ni visibles. |

---

*MEDISTA — Casos de Uso M1 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
