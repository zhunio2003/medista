# CU-M6-03 — Configurar Umbrales de Alerta

**Módulo:** M6 — Notificaciones Inteligentes  
**Versión:** 1.0  
**Fecha:** 16 Abril 2026

---

## Descripción

Permite al Administrador del Sistema configurar los umbrales numéricos que controlan cuándo el motor de alertas genera notificaciones automáticas por patrones clínicos o institucionales inusuales.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M6-03 |
| **Nombre** | Configurar umbrales de alerta |
| **Actor principal** | Administrador del Sistema |
| **Módulo** | M6 — Notificaciones Inteligentes |
| **Requisitos asociados** | RF-M6-10, RF-M8-06 |

---

## Precondiciones

1. El Administrador del Sistema tiene sesión activa en el sistema.

---

## Postcondiciones

1. Los nuevos umbrales quedan guardados y activos para el motor de alertas del sistema de forma inmediata.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Administrador | Accede a la sección de configuración de umbrales de alertas. |
| 2 | Sistema | Muestra los umbrales actualmente configurados: número máximo de visitas por paciente en un período, frecuencia de diagnósticos que activa alerta epidemiológica y volumen de atenciones que activa alerta institucional. |
| 3 | Administrador | Modifica los valores de uno o más umbrales según las necesidades institucionales. |
| 4 | Administrador | Confirma los cambios. |
| 5 | Sistema | Guarda los nuevos umbrales y los aplica al motor de alertas de forma inmediata sin necesidad de reiniciar el sistema. |

---

## Flujos Alternativos

### FA-01 — Valor inválido (paso 3)

| Paso | Actor | Acción |
|---|---|---|
| 3a | Sistema | Detecta que el valor ingresado no es válido — no es numérico, es negativo o es cero. |
| 3b | Sistema | Rechaza el valor e informa al Administrador que el umbral ingresado no es válido indicando el criterio de validación. |
| 3c | Administrador | Corrige el valor ingresado. |
| 3d | — | El flujo retoma desde el paso 4. |

### FA-02 — Administrador cancela (cualquier paso)

| Paso | Actor | Acción |
|---|---|---|
| Xa | Administrador | Selecciona la opción de cancelar. |
| Xb | Sistema | Descarta todos los cambios realizados y mantiene los umbrales anteriores sin ninguna modificación. |
| Xc | — | El caso de uso termina. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | Solo el Administrador del Sistema puede configurar umbrales de alertas — ningún otro rol tiene acceso a esta funcionalidad. |
| RN-02 | Los cambios en umbrales aplican de forma inmediata al motor de alertas sin necesidad de reiniciar el sistema. |
| RN-03 | Los umbrales deben ser valores numéricos enteros positivos mayores a cero. |

---

*MEDISTA — Casos de Uso M6 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
