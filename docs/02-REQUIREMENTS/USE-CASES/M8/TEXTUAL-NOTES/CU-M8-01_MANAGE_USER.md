# CU-M8-01 — Gestionar Usuarios

**Módulo:** M8 — Administración del Sistema  
**Versión:** 1.0  
**Fecha:** 16 Abril 2026

---

## Descripción

Permite al Administrador del Sistema crear, editar y desactivar cuentas de usuario para los roles Médico, Decano y Administrador del Sistema, garantizando que solo el personal autorizado tenga acceso al sistema.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M8-01 |
| **Nombre** | Gestionar usuarios |
| **Actor principal** | Administrador del Sistema |
| **Módulo** | M8 — Administración del Sistema |
| **Requisitos asociados** | RF-M8-01, RF-M8-02 |

---

## Precondiciones

1. El Administrador del Sistema tiene sesión activa en el sistema.

---

## Postcondiciones

1. Los cambios realizados sobre los usuarios quedan aplicados en el sistema y registrados en el log de auditoría.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Administrador | Accede a la sección de gestión de usuarios. |
| 2 | Sistema | Muestra el listado de usuarios registrados con nombre, correo institucional, rol y estado (activo / inactivo). |
| 3 | Administrador | Selecciona una acción: crear usuario, editar usuario o desactivar usuario. |
| 4 | Sistema | Presenta el formulario correspondiente a la acción seleccionada. |
| 5 | Administrador | Completa los datos requeridos y asigna el rol correspondiente al usuario. *[include: Asignar rol]* |
| 6 | Administrador | Confirma la acción. |
| 7 | Sistema | Aplica los cambios y registra el evento en el log de auditoría con usuario, acción ejecutada, timestamp e IP de origen. |

---

## Flujos Alternativos

### FA-01 — Correo ya registrado al crear usuario (paso 5)

| Paso | Actor | Acción |
|---|---|---|
| 5a | Sistema | Detecta que ya existe un usuario registrado con el correo institucional ingresado. |
| 5b | Sistema | Informa al Administrador que el correo ya está en uso y rechaza la creación del duplicado. |
| 5c | Administrador | Corrige el correo ingresado o cancela la operación. |

### FA-02 — Administrador cancela (cualquier paso)

| Paso | Actor | Acción |
|---|---|---|
| Xa | Administrador | Selecciona la opción de cancelar. |
| Xb | Sistema | Descarta todos los cambios sin aplicar ninguna modificación al sistema. |
| Xc | — | El caso de uso termina. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | Los usuarios no se eliminan físicamente del sistema — solo se desactivan para preservar la trazabilidad completa del log de auditoría. |
| RN-02 | Solo el Administrador del Sistema puede crear, editar y desactivar cuentas de usuario. |
| RN-03 | Todo cambio sobre cuentas de usuario queda registrado en el log de auditoría de forma inmutable. |
| RN-04 | Un usuario desactivado no puede autenticarse en el sistema independientemente de sus credenciales. |

---

*MEDISTA — Casos de Uso M8 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
