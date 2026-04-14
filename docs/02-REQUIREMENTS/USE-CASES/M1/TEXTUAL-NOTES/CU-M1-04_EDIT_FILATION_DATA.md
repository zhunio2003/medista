# CU-M1-04 — Editar Datos de Filiación

**Módulo:** M1 — Gestión de Pacientes  
**Versión:** 1.0  
**Fecha:** 14 Abril 2026

---

## Descripción

Permite al médico actualizar los datos de filiación de un paciente ya registrado cuando estos han cambiado, como carrera, ciclo, dirección o teléfono.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M1-04 |
| **Nombre** | Editar datos de filiación |
| **Actor principal** | Médico |
| **Módulo** | M1 — Gestión de Pacientes |
| **Requisitos asociados** | RF-M1-06, RF-M1-08 |

---

## Precondiciones

1. El médico tiene sesión activa en el sistema.
2. El paciente existe en el sistema.
3. El médico se encuentra en el perfil del paciente.

---

## Postcondiciones

1. Los datos de filiación del paciente quedan actualizados en el sistema.
2. El sistema registra en el log de auditoría quién realizó el cambio, qué campo se modificó y cuándo.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Médico | Selecciona la opción "Editar datos" desde el perfil del paciente. |
| 2 | Sistema | Presenta el formulario de filiación con los datos actuales precargados y editables. |
| 3 | Médico | Modifica los campos que requieren actualización. |
| 4 | Sistema | Si el género registrado es femenino, mantiene habilitados los campos de historial obstétrico. *[extend: CU-M1-07 Habilitar campos obstétricos]* |
| 5 | Médico | Confirma los cambios realizados. |
| 6 | Sistema | Valida los datos modificados. |
| 7 | Sistema | Almacena los cambios y registra la modificación en el log de auditoría con usuario, campos modificados, valores anteriores, valores nuevos, timestamp e IP de origen. |
| 8 | Sistema | Muestra el perfil actualizado con mensaje de confirmación de éxito. |

---

## Flujos Alternativos

### FA-01 — Dato inválido (paso 6)

| Paso | Actor | Acción |
|---|---|---|
| 6a | Sistema | Detecta que uno o más campos contienen valores inválidos. |
| 6b | Sistema | Muestra un mensaje de error indicando el campo específico que no cumple la validación y el motivo. |
| 6c | Médico | Corrige el valor indicado. |
| 6d | — | El flujo retoma desde el paso 6. |

### FA-02 — Médico cancela la edición (cualquier paso)

| Paso | Actor | Acción |
|---|---|---|
| Xa | Médico | Selecciona la opción de cancelar. |
| Xb | Sistema | Descarta todos los cambios realizados y regresa al perfil del paciente con los datos originales sin modificación. |
| Xc | — | El caso de uso termina sin registrar ningún cambio en auditoría. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | El número de cédula no puede ser modificado — es el identificador único del paciente en el sistema. |
| RN-02 | Todo cambio exitoso queda registrado en el log de auditoría con usuario, campos modificados, valores anteriores, valores nuevos, timestamp e IP. Ningún cambio es anónimo. |
| RN-03 | Solo el rol Médico puede editar datos de filiación. Los demás roles no tienen acceso a esta funcionalidad. |
| RN-04 | Si se modifica la fecha de nacimiento, la edad se recalcula automáticamente. |
| RN-05 | Los campos de historial obstétrico permanecen habilitados únicamente si el género registrado es femenino. Si el género cambia a masculino, estos campos se deshabilitan y sus valores quedan descartados. |

---

*MEDISTA-V2 — Casos de Uso M1 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
