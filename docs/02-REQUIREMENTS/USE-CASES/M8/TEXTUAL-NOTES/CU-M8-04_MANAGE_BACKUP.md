# CU-M8-04 — Gestionar Respaldos

**Módulo:** M8 — Administración del Sistema  
**Versión:** 1.0  
**Fecha:** 16 Abril 2026

---

## Descripción

Permite al Administrador del Sistema configurar los respaldos automáticos de la base de datos y ejecutar respaldos manuales en cualquier momento, garantizando la persistencia y recuperabilidad de los datos clínicos.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M8-04 |
| **Nombre** | Gestionar respaldos |
| **Actor principal** | Administrador del Sistema |
| **Módulo** | M8 — Administración del Sistema |
| **Requisitos asociados** | RF-M8-08 |

---

## Precondiciones

1. El Administrador del Sistema tiene sesión activa en el sistema.

---

## Postcondiciones

1. El respaldo automático queda configurado y activo en el scheduler, o el respaldo manual queda generado, cifrado y almacenado correctamente.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Administrador | Accede a la sección de gestión de respaldos. |
| 2 | Sistema | Muestra la configuración actual de respaldos automáticos y el historial de respaldos generados con fecha, tamaño y estado. |
| 3 | Administrador | Configura el intervalo de generación de respaldos automáticos. |
| 4 | Administrador | Confirma la configuración. |
| 5 | Sistema | Guarda la configuración y la aplica al scheduler de respaldos automáticos de forma inmediata. |

---

## Flujos Alternativos

### FA-01 — Administrador ejecuta respaldo manual (paso 2)

| Paso | Actor | Acción |
|---|---|---|
| 2a | Administrador | Selecciona la opción de generar un respaldo inmediato. *[extend: Respaldo manual]* |
| 2b | Sistema | Genera el respaldo completo de la base de datos, lo cifra con GPG/AES-256 y lo almacena en la ubicación configurada. |
| 2c | Sistema | Informa al Administrador que el respaldo fue generado exitosamente, indicando fecha, hora y tamaño del archivo. |

### FA-02 — Error en generación de respaldo (FA-01)

| Paso | Actor | Acción |
|---|---|---|
| 2b1 | Sistema | No puede completar la generación del respaldo por error interno. |
| 2b2 | Sistema | Informa al Administrador que no fue posible generar el respaldo e invita a intentarlo nuevamente. |
| 2b3 | — | El caso de uso termina sin generar ningún archivo de respaldo. |

### FA-03 — Administrador cancela (cualquier paso)

| Paso | Actor | Acción |
|---|---|---|
| Xa | Administrador | Selecciona la opción de cancelar. |
| Xb | Sistema | Descarta los cambios manteniendo la configuración de respaldos anterior sin modificación. |
| Xc | — | El caso de uso termina. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | Todos los respaldos — automáticos y manuales — se almacenan cifrados con GPG/AES-256 antes de ser guardados. |
| RN-02 | Solo el Administrador del Sistema puede configurar y ejecutar respaldos de base de datos. |
| RN-03 | El historial de respaldos generados es consultable en cualquier momento desde esta sección. |

---

*MEDISTA — Casos de Uso M8 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
