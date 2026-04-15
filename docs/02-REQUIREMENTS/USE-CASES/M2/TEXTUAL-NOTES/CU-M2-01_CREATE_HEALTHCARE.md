# CU-M2-01 — Crear Atención Médica

**Módulo:** M2 — Atención Médica  
**Versión:** 1.0  
**Fecha:** 14 de Abril 2026

---

## Descripción

Permite al médico registrar una atención médica completa para un paciente existente, digitalizando el formulario físico actual del Departamento Médico del Instituto Superior Universitario TEC Azuay.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M2-01 |
| **Nombre** | Crear atención médica |
| **Actor principal** | Médico |
| **Módulo** | M2 — Atención Médica |
| **Requisitos asociados** | RF-M2-01, RF-M2-02, RF-M2-03, RF-M2-04, RF-M2-05, RF-M2-06, RF-M2-07, RF-M2-08, RF-M2-09, RF-M2-10, RF-M2-11, RF-M2-12, RF-M2-13, RF-M2-14, RF-M2-15 |

---

## Precondiciones

1. El médico tiene sesión activa en el sistema.
2. El paciente está previamente registrado en el sistema.

---

## Postcondiciones

1. La atención médica queda registrada como registro inmutable en el sistema.
2. El sistema notifica automáticamente al estudiante con el resumen de su atención.
3. El log de auditoría registra la creación con usuario, timestamp e IP de origen.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Médico | Busca y selecciona el paciente al que va a atender. |
| 2 | Sistema | Presenta el formulario de atención médica con fecha y hora precargadas automáticamente y los datos del profesional extraídos de la sesión activa. |
| 3 | Sistema | Precarga los antecedentes familiares y personales registrados en atenciones anteriores del paciente. *[include: Precargar antecedentes]* |
| 4 | Médico | Ingresa el motivo de consulta mediante texto libre. |
| 5 | Médico | Revisa los antecedentes precargados y los actualiza si es necesario. |
| 6 | Médico | Ingresa la descripción de la enfermedad actual mediante texto libre. |
| 7 | Médico | Ingresa los signos vitales: presión arterial sistólica y diastólica, peso, talla, frecuencia cardíaca, frecuencia respiratoria, temperatura, saturación de oxígeno, escala de Glasgow (ocular, verbal, motora), llenado capilar y reflejo pupilar. |
| 8 | Sistema | Calcula automáticamente el IMC a partir del peso y talla ingresados. *[include: Calcular IMC]* |
| 9 | Sistema | Calcula automáticamente el total de Glasgow como suma de los tres componentes ingresados. *[include: Calcular Glasgow]* |
| 10 | Médico | Registra el examen físico por sistemas marcando Normal o describiendo hallazgos anormales en texto libre para cada uno de los nueve sistemas. |
| 11 | Médico | Registra exámenes complementarios en texto libre o marca "No aplica". |
| 12 | Médico | Busca y selecciona el diagnóstico mediante el catálogo CIE-10 y lo clasifica como Presuntivo o Definitivo. *[include: Buscar CIE-10]* |
| 13 | Médico | Ingresa el tratamiento indicado mediante texto libre. |
| 14 | Médico | Confirma y guarda la atención médica. |
| 15 | Sistema | Almacena la atención como registro inmutable, registra el evento en el log de auditoría y notifica automáticamente al estudiante. |

---

## Flujos Alternativos

### FA-01 — Paciente de género femenino (paso 2)

| Paso | Actor | Acción |
|---|---|---|
| 2a | Sistema | Detecta que el género del paciente es femenino. |
| 2b | Sistema | Habilita automáticamente la sección de emergencias obstétricas en el formulario. *[extend: Habilitar campos obstétricos]* |
| 2c | Médico | Completa los campos obstétricos que apliquen: menarca, ritmo menstrual, FUM, IVSA, fórmula obstétrica, dismenorrea, mastodinia y datos de embarazo si corresponde. |
| 2d | — | El flujo continúa desde el paso 4. |

### FA-02 — Médico adjunta examen complementario (paso 11)

| Paso | Actor | Acción |
|---|---|---|
| 11a | Médico | Selecciona la opción de adjuntar un archivo. *[extend: Adjuntar examen complementario]* |
| 11b | Sistema | Valida que el archivo tenga un formato permitido: PDF, JPG o PNG. |
| 11c | Sistema | Vincula el archivo a la atención médica en curso. |
| 11d | — | El flujo continúa desde el paso 12. |

### FA-03 — Dato inválido en signos vitales (paso 7)

| Paso | Actor | Acción |
|---|---|---|
| 7a | Sistema | Detecta que uno o más campos de signos vitales contienen valores fuera del rango fisiológicamente razonable o de tipo de dato incorrecto. |
| 7b | Sistema | Muestra mensaje de error indicando el campo específico y el rango válido. |
| 7c | Médico | Corrige el valor indicado. |
| 7d | — | El flujo retoma desde el paso 7. |

### FA-04 — Médico cancela la atención (cualquier paso)

| Paso | Actor | Acción |
|---|---|---|
| Xa | Médico | Selecciona la opción de cancelar. |
| Xb | Sistema | Descarta todos los datos ingresados sin crear ningún registro. |
| Xc | — | El caso de uso termina. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | No se puede crear una atención médica sin un paciente previamente registrado en el sistema. |
| RN-02 | La fecha, hora y datos del profesional (nombre y código MSP) se registran automáticamente desde la sesión activa y no pueden ser modificados manualmente. |
| RN-03 | Una atención médica guardada no puede eliminarse ni modificarse directamente. Cualquier corrección debe realizarse mediante CU-M2-02 — Agregar nota de corrección. |
| RN-04 | Los antecedentes familiares y personales se heredan automáticamente de atenciones anteriores del mismo paciente y pueden actualizarse en cada nueva atención. |
| RN-05 | El IMC y el total de Glasgow se calculan automáticamente y no pueden ser ingresados ni modificados manualmente por el médico. |
| RN-06 | Solo el rol Médico puede crear atenciones médicas. Ningún otro rol tiene acceso a esta funcionalidad. |
| RN-07 | La sección de emergencias obstétricas se habilita exclusivamente cuando el género registrado del paciente es femenino. |

---

*MEDISTA — Casos de Uso M2 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
