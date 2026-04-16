# CU-M4-02 — Ver Evolución de Signos Vitales

**Módulo:** M4 — Historial Clínico  
**Versión:** 1.0  
**Fecha:** 16 Abril 2026

---

## Descripción

Permite al médico visualizar gráficas interactivas de la evolución de los signos vitales de un paciente a lo largo del tiempo, facilitando la identificación de tendencias clínicas entre consultas.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M4-02 |
| **Nombre** | Ver evolución de signos vitales |
| **Actor principal** | Médico |
| **Módulo** | M4 — Historial Clínico |
| **Requisitos asociados** | RF-M4-02, RF-M4-06, RF-M4-08 |

---

## Precondiciones

1. El médico tiene sesión activa en el sistema.
2. El paciente tiene al menos dos atenciones registradas con signos vitales.

---

## Postcondiciones

1. El médico visualiza las gráficas de evolución de signos vitales del paciente a lo largo del tiempo.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Médico | Desde el historial clínico del paciente selecciona la opción "Ver evolución de signos vitales". |
| 2 | Sistema | Muestra gráficas interactivas de evolución de peso, IMC y presión arterial registrados en cada atención a lo largo del tiempo. |
| 3 | Médico | Consulta las gráficas para identificar tendencias y variaciones entre consultas. |

---

## Flujos Alternativos

### FA-01 — Datos insuficientes para graficar (paso 2)

| Paso | Actor | Acción |
|---|---|---|
| 2a | Sistema | Detecta que el paciente tiene menos de dos registros de signos vitales. |
| 2b | Sistema | Informa al médico que no hay suficientes datos para generar gráficas de evolución. |
| 2c | — | El caso de uso termina. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | Las gráficas son de solo lectura — no permiten modificar ningún dato clínico. |
| RN-02 | Solo el rol Médico puede visualizar las gráficas de evolución de signos vitales. |
| RN-03 | Se requieren al menos dos registros de signos vitales para generar una gráfica de evolución con tendencia visible. |

---

*MEDISTA — Casos de Uso M4 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
