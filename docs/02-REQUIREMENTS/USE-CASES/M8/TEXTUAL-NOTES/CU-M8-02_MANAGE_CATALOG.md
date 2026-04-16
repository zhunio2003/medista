# CU-M8-02 — Gestionar Catálogos

**Módulo:** M8 — Administración del Sistema  
**Versión:** 1.0  
**Fecha:** 16 Abril 2026

---

## Descripción

Permite al Administrador del Sistema gestionar los catálogos institucionales del sistema: carreras y ciclos, establecimientos de salud para referencias médicas y códigos CIE-10 para diagnósticos.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M8-02 |
| **Nombre** | Gestionar catálogos |
| **Actor principal** | Administrador del Sistema |
| **Módulo** | M8 — Administración del Sistema |
| **Requisitos asociados** | RF-M8-03, RF-M8-04, RF-M8-05 |

---

## Precondiciones

1. El Administrador del Sistema tiene sesión activa en el sistema.

---

## Postcondiciones

1. Los cambios en los catálogos quedan aplicados y disponibles en todo el sistema de forma inmediata.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Administrador | Accede a la sección de gestión de catálogos. |
| 2 | Sistema | Muestra los catálogos disponibles: carreras y ciclos, establecimientos de salud y códigos CIE-10. |
| 3 | Administrador | Selecciona el catálogo a gestionar. |
| 4 | Sistema | Muestra el listado de registros del catálogo seleccionado con su estado (activo / inactivo). |
| 5 | Administrador | Selecciona una acción: agregar, editar o desactivar un registro del catálogo. |
| 6 | Administrador | Completa los datos requeridos y confirma la acción. |
| 7 | Sistema | Aplica los cambios y los deja disponibles en todo el sistema de forma inmediata. |

---

## Flujos Alternativos

### FA-01 — Registro duplicado (paso 6)

| Paso | Actor | Acción |
|---|---|---|
| 6a | Sistema | Detecta que ya existe un registro idéntico en el catálogo seleccionado. |
| 6b | Sistema | Informa al Administrador que el registro ya existe y rechaza la duplicación. |
| 6c | Administrador | Corrige los datos o cancela la operación. |

### FA-02 — Carga masiva del catálogo CIE-10 (paso 5)

| Paso | Actor | Acción |
|---|---|---|
| 5a | Administrador | Selecciona la opción de carga masiva del catálogo CIE-10. |
| 5b | Sistema | Solicita el archivo con los códigos a importar. |
| 5c | Administrador | Sube el archivo con los más de 14.000 códigos CIE-10. |
| 5d | Sistema | Procesa e importa los códigos al catálogo, creando los índices de búsqueda necesarios. |
| 5e | Sistema | Informa al Administrador el resultado de la importación con el número de registros procesados. |

### FA-03 — Administrador cancela (cualquier paso)

| Paso | Actor | Acción |
|---|---|---|
| Xa | Administrador | Selecciona la opción de cancelar. |
| Xb | Sistema | Descarta todos los cambios sin modificar ningún registro del catálogo. |
| Xc | — | El caso de uso termina. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | Los registros de catálogos no se eliminan físicamente — solo se desactivan para preservar la integridad referencial de los datos históricos. |
| RN-02 | Solo el Administrador del Sistema puede gestionar los catálogos institucionales. |
| RN-03 | Los cambios en catálogos aplican de forma inmediata en todo el sistema sin necesidad de reinicio. |

---

*MEDISTA — Casos de Uso M8 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
