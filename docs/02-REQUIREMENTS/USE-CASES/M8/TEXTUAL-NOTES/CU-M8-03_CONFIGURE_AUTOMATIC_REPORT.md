# CU-M8-03 — Configurar Reportes Automáticos

**Módulo:** M8 — Administración del Sistema  
**Versión:** 1.0  
**Fecha:** 16 Abril 2026

---

## Descripción

Permite al Administrador del Sistema configurar la generación y envío automático de reportes estadísticos periódicos al Decano, definiendo el intervalo, los filtros aplicados y los destinatarios.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M8-03 |
| **Nombre** | Configurar reportes automáticos |
| **Actor principal** | Administrador del Sistema |
| **Módulo** | M8 — Administración del Sistema |
| **Requisitos asociados** | RF-M8-07, RF-M5-06 |

---

## Precondiciones

1. El Administrador del Sistema tiene sesión activa en el sistema.

---

## Postcondiciones

1. La configuración de reportes automáticos queda guardada y activa en el scheduler del sistema.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Administrador | Accede a la sección de configuración de reportes automáticos. |
| 2 | Sistema | Muestra la configuración actual: intervalo de generación, filtros aplicados y destinatarios configurados. |
| 3 | Administrador | Configura el intervalo de generación: semanal, mensual o semestral. |
| 4 | Administrador | Configura los filtros estadísticos que se aplicarán al reporte y los destinatarios que lo recibirán por correo. |
| 5 | Administrador | Confirma los cambios. |
| 6 | Sistema | Guarda la configuración y la aplica al scheduler de reportes automáticos de forma inmediata. |

---

## Flujos Alternativos

### FA-01 — Administrador cancela (cualquier paso)

| Paso | Actor | Acción |
|---|---|---|
| Xa | Administrador | Selecciona la opción de cancelar. |
| Xb | Sistema | Descarta todos los cambios manteniendo la configuración anterior sin ninguna modificación. |
| Xc | — | El caso de uso termina. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | Los reportes automáticos se generan y envían por correo electrónico al Decano según el intervalo configurado por el Administrador. |
| RN-02 | Solo el Administrador del Sistema puede configurar los parámetros de los reportes automáticos. |
| RN-03 | Los reportes automáticos contienen únicamente datos estadísticos agregados — nunca expedientes clínicos individuales de ningún estudiante. |

---

*MEDISTA — Casos de Uso M8 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
