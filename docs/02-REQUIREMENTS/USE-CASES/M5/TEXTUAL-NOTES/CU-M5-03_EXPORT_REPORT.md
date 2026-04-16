# CU-M5-03 — Exportar Reporte

**Módulo:** M5 — Dashboard Estadístico y Reportes  
**Versión:** 1.0  
**Fecha:** 16 Abril 2026

---

## Descripción

Permite al Decano exportar los datos estadísticos del dashboard en formato PDF institucional o Excel para su análisis y presentación ante autoridades del Instituto Superior Universitario TEC Azuay.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M5-03 |
| **Nombre** | Exportar reporte |
| **Actor principal** | Decano |
| **Módulo** | M5 — Dashboard Estadístico y Reportes |
| **Requisitos asociados** | RF-M5-04, RF-M5-05, RF-M5-07 |

---

## Precondiciones

1. El Decano tiene sesión activa en el sistema.
2. El Decano se encuentra en el dashboard estadístico con datos disponibles.

---

## Postcondiciones

1. El Decano recibe el archivo del reporte en el formato seleccionado.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Decano | Selecciona la opción "Exportar reporte" desde el dashboard estadístico. |
| 2 | Sistema | Presenta las opciones de formato disponibles: PDF institucional o Excel (.xlsx). |
| 3 | Decano | Selecciona el formato deseado. |
| 4 | Sistema | Genera el archivo con los datos actualmente visibles en el dashboard, respetando los filtros activos al momento de la exportación y aplicando el formato institucional correspondiente. |
| 5 | Sistema | Entrega el archivo al Decano para su descarga inmediata. |

---

## Flujos Alternativos

### FA-01 — Error en generación del archivo (paso 4)

| Paso | Actor | Acción |
|---|---|---|
| 4a | Sistema | No puede generar el archivo por error interno. |
| 4b | Sistema | Informa al Decano que no fue posible generar el reporte e invita a intentarlo nuevamente. |
| 4c | — | El caso de uso termina sin entregar ningún archivo. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | El reporte exportado contiene únicamente datos estadísticos agregados — nunca expedientes clínicos individuales de ningún estudiante. |
| RN-02 | El PDF exportado debe tener formato institucional del Instituto Superior Universitario TEC Azuay, apto para presentación ante autoridades. |
| RN-03 | El Excel exportado debe tener estructura organizada con encabezados claros para facilitar el análisis externo de los datos. |
| RN-04 | El reporte refleja los datos con los filtros activos al momento de la exportación — si no hay filtros activos, incluye todos los datos disponibles. |
| RN-05 | Solo el Decano puede exportar reportes desde el dashboard estadístico. |

---

*MEDISTA — Casos de Uso M5 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
