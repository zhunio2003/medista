# CU-M3-01 — Generar Referencia Médica

**Módulo:** M3 — Referencia Médica  
**Versión:** 1.0  
**Fecha:** 15 Abril 2026

---

## Descripción

Permite al médico generar una referencia médica para derivar a un paciente a otro establecimiento de salud, partiendo siempre de una atención médica existente.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M3-01 |
| **Nombre** | Generar referencia médica |
| **Actor principal** | Médico |
| **Módulo** | M3 — Referencia Médica |
| **Requisitos asociados** | RF-M3-01, RF-M3-02, RF-M3-03, RF-M3-04, RF-M3-05, RF-M3-06, RF-M3-08 |

---

## Precondiciones

1. El médico tiene sesión activa en el sistema.
2. Existe una atención médica previamente guardada para el paciente.

---

## Postcondiciones

1. La referencia médica queda registrada y vinculada a la atención médica de origen.
2. El estudiante recibe notificación con los datos del establecimiento de salud destino.
3. El Decano recibe notificación de que se generó una nueva referencia médica.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Médico | Selecciona la opción "Generar referencia médica" desde una atención médica existente. |
| 2 | Sistema | Precarga automáticamente los datos fijos: nombre de la institución (Instituto Superior Universitario TEC Azuay), cédula del paciente, servicio (Departamento Médico), nombre del profesional y código MSP desde la sesión activa. *[include: Precargar datos]* |
| 3 | Médico | Completa los datos del establecimiento de salud destino: entidad del sistema, nombre del establecimiento, servicio y especialidad. |
| 4 | Médico | Selecciona el motivo de referencia entre las opciones predefinidas: Limitada capacidad resolutiva, Falta de profesional u Otros. |
| 5 | Médico | Ingresa el resumen del cuadro clínico y los hallazgos relevantes mediante texto libre. |
| 6 | Médico | Registra hasta dos diagnósticos con su código CIE-10 y los clasifica como Presuntivo o Definitivo. *[include: Buscar CIE-10]* |
| 7 | Médico | Confirma y guarda la referencia médica. |
| 8 | Sistema | Almacena la referencia vinculada a la atención médica de origen. |
| 9 | Sistema | Notifica automáticamente al estudiante con los datos del establecimiento destino y la especialidad. *[include: Notificar al estudiante]* |
| 10 | Sistema | Notifica automáticamente al Decano que se generó una nueva referencia médica. *[include: Notificar al Decano]* |

---

## Flujos Alternativos

### FA-01 — Médico cancela (cualquier paso)

| Paso | Actor | Acción |
|---|---|---|
| Xa | Médico | Selecciona la opción de cancelar. |
| Xb | Sistema | Descarta todos los datos ingresados sin crear ningún registro ni enviar notificaciones. |
| Xc | — | El caso de uso termina. |

### FA-02 — Médico descarga el PDF (paso 7)

| Paso | Actor | Acción |
|---|---|---|
| 7a | Médico | Selecciona la opción de descargar el PDF de la referencia. *[extend: CU-M3-02 Descargar PDF de referencia]* |
| 7b | — | El flujo continúa desde el paso 8. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | Toda referencia médica debe estar vinculada a una atención médica existente. No se puede crear una referencia de forma independiente. |
| RN-02 | Los datos precargados (institución, cédula, servicio, profesional y código MSP) no pueden ser modificados manualmente por el médico. |
| RN-03 | Solo el rol Médico puede generar referencias médicas. |
| RN-04 | La notificación al estudiante incluye únicamente el establecimiento destino y la especialidad — no expone datos clínicos detallados. |
| RN-05 | La notificación al Decano no expone datos clínicos individuales del paciente referido. |

---

*MEDISTA — Casos de Uso M3 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
