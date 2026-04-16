# Matriz de Trazabilidad de Requisitos

**Proyecto:** MEDISTA — Sistema de Gestión de Atención Médica  
**Versión:** 1.0  
**Fecha:** 16 Abril 2026  
**Estándar base:** IEEE 830 adaptado

---

## Guía de Referencias de Origen

| Código | Origen |
|--------|--------|
| VIS | MEDISTA-V2 — Visión del Sistema v1.0 |
| ENT | Entrevista con la médico del Departamento Médico (usuaria principal) |
| LOPDP | Ley Orgánica de Protección de Datos Personales del Ecuador |
| MSP | Acuerdo Ministerial MSP No. 00000125 — Historia Clínica Electrónica |
| FORM | Formulario físico de atención médica del Departamento Médico TEC Azuay |

---

## M1 — Gestión de Pacientes

| ID Requisito | Descripción resumida | Origen | Caso de uso |
|---|---|---|---|
| RF-M1-01 | Registro de paciente nuevo con datos de filiación completos | VIS, ENT, FORM | CU-M1-01 |
| RF-M1-02 | Cédula como identificador único — sin duplicados | VIS, ENT | CU-M1-01 |
| RF-M1-03 | Cálculo automático de edad desde fecha de nacimiento | VIS, ENT | CU-M1-01 |
| RF-M1-04 | Búsqueda de pacientes por cédula, nombres o apellidos con tolerancia a errores tipográficos | VIS, ENT | CU-M1-02 |
| RF-M1-05 | Visualización del perfil completo del paciente con acceso al historial | VIS | CU-M1-03 |
| RF-M1-06 | Edición de datos de filiación cuando cambian | VIS, ENT | CU-M1-04 |
| RF-M1-07 | Solo el Médico puede crear y editar pacientes | VIS, LOPDP | CU-M1-01, CU-M1-04 |
| RF-M1-08 | Campos obstétricos habilitados solo para pacientes de género femenino | VIS, ENT, FORM | CU-M1-01, CU-M1-04 |
| RF-M1-09 | Validación de formato de cédula ecuatoriana | VIS, ENT | CU-M1-01 |

---

## M2 — Atención Médica

| ID Requisito | Descripción resumida | Origen | Caso de uso |
|---|---|---|---|
| RF-M2-01 | Creación de atención médica asociada a paciente existente con fecha automática | VIS, ENT, FORM | CU-M2-01 |
| RF-M2-02 | Registro de motivo de consulta en texto libre | VIS, FORM | CU-M2-01 |
| RF-M2-03 | Registro y herencia de antecedentes entre consultas por categorías | VIS, ENT, FORM | CU-M2-01 |
| RF-M2-04 | Registro de enfermedad actual en texto libre | VIS, FORM | CU-M2-01 |
| RF-M2-05 | Registro de signos vitales con validación numérica | VIS, FORM | CU-M2-01 |
| RF-M2-06 | Cálculo automático de IMC desde peso y talla | VIS, ENT, FORM | CU-M2-01 |
| RF-M2-07 | Registro y cálculo automático de Escala de Glasgow | VIS, FORM | CU-M2-01 |
| RF-M2-08 | Registro de examen físico por nueve sistemas con opción Normal o hallazgos | VIS, FORM | CU-M2-01 |
| RF-M2-09 | Sección de emergencias obstétricas condicional para pacientes femeninas | VIS, ENT, FORM | CU-M2-01 |
| RF-M2-10 | Registro de exámenes complementarios con opción de adjuntar archivo | VIS, FORM | CU-M2-01 |
| RF-M2-11 | Registro de diagnóstico con buscador CIE-10 y clasificación presuntivo/definitivo | VIS, ENT, FORM | CU-M2-01 |
| RF-M2-12 | Registro de tratamiento en texto libre | VIS, FORM | CU-M2-01 |
| RF-M2-13 | Datos del profesional precargados desde la sesión activa | VIS, MSP, FORM | CU-M2-01 |
| RF-M2-14 | Inmutabilidad de atenciones — solo notas de corrección | VIS, MSP, LOPDP | CU-M2-02 |
| RF-M2-15 | Validación de rangos fisiológicos en signos vitales | VIS, ENT | CU-M2-01 |

---

## M3 — Referencia Médica

| ID Requisito | Descripción resumida | Origen | Caso de uso |
|---|---|---|---|
| RF-M3-01 | Referencia generada solo desde atención médica existente | VIS, ENT, FORM | CU-M3-01 |
| RF-M3-02 | Precarga automática de datos institucionales y del profesional | VIS, FORM | CU-M3-01 |
| RF-M3-03 | Registro de datos del establecimiento de salud destino | VIS, FORM | CU-M3-01 |
| RF-M3-04 | Selección de motivo de referencia entre opciones predefinidas | VIS, FORM | CU-M3-01 |
| RF-M3-05 | Registro de resumen clínico y hallazgos en texto libre | VIS, FORM | CU-M3-01 |
| RF-M3-06 | Registro de hasta dos diagnósticos CIE-10 con clasificación | VIS, FORM | CU-M3-01 |
| RF-M3-07 | Generación de PDF con formato idéntico al formulario institucional | VIS, ENT, FORM | CU-M3-02 |
| RF-M3-08 | Notificación automática al estudiante y al Decano al generar referencia | VIS | CU-M3-01 |

---

## M4 — Historial Clínico

| ID Requisito | Descripción resumida | Origen | Caso de uso |
|---|---|---|---|
| RF-M4-01 | Línea de tiempo cronológica de atenciones con vista resumida y detalle expandible | VIS, ENT | CU-M4-01 |
| RF-M4-02 | Gráficas interactivas de evolución de signos vitales | VIS, ENT | CU-M4-02 |
| RF-M4-03 | Listado de diagnósticos recurrentes por paciente | VIS | CU-M4-01 |
| RF-M4-04 | Historial de referencias médicas del paciente | VIS | CU-M4-04 |
| RF-M4-05 | Filtros por rango de fechas y diagnóstico CIE-10 | VIS, ENT | CU-M4-03 |
| RF-M4-06 | Vista completa del historial para el Médico | VIS, LOPDP | CU-M4-01, CU-M4-02, CU-M4-03, CU-M4-04 |
| RF-M4-07 | Vista simplificada del historial para el Estudiante en app móvil | VIS | CU-M4-05 |
| RF-M4-08 | Historial de solo lectura para todos los roles | VIS, MSP | CU-M4-01, CU-M4-02, CU-M4-03, CU-M4-04, CU-M4-05 |

---

## M5 — Dashboard Estadístico y Reportes

| ID Requisito | Descripción resumida | Origen | Caso de uso |
|---|---|---|---|
| RF-M5-01 | Dashboard interactivo con métricas en tiempo real | VIS, ENT | CU-M5-01 |
| RF-M5-02 | Detección y visualización de anomalías e incrementos inusuales | VIS, ENT | CU-M5-01 |
| RF-M5-03 | Filtros inteligentes cruzados con comparativa entre períodos | VIS, ENT | CU-M5-02 |
| RF-M5-04 | Exportación de reportes a PDF institucional | VIS, ENT | CU-M5-03 |
| RF-M5-05 | Exportación de datos estadísticos a Excel | VIS, ENT | CU-M5-03 |
| RF-M5-06 | Generación y envío automático de reportes periódicos al Decano | VIS | CU-M8-03 |
| RF-M5-07 | Restricción a datos agregados — sin expedientes individuales | VIS, LOPDP | CU-M5-01, CU-M5-02, CU-M5-03 |
| RF-M5-08 | Estadísticas propias del Médico limitadas a sus atenciones | VIS | CU-M5-04 |

---

## M6 — Notificaciones Inteligentes

| ID Requisito | Descripción resumida | Origen | Caso de uso |
|---|---|---|---|
| RF-M6-01 | Notificación al estudiante cuando se registra su atención | VIS | CU-M6-01 |
| RF-M6-02 | Notificación al estudiante cuando se genera una referencia a su nombre | VIS | CU-M6-01 |
| RF-M6-03 | Recordatorio de seguimiento al estudiante | VIS | CU-M6-01 |
| RF-M6-04 | Alerta al Médico por paciente frecuente | VIS, ENT | CU-M6-01 |
| RF-M6-05 | Recordatorio de seguimientos pendientes al Médico | VIS | CU-M6-01 |
| RF-M6-06 | Alerta al Médico por diagnóstico con frecuencia inusual | VIS, ENT | CU-M6-01 |
| RF-M6-07 | Alerta al Decano por anomalías institucionales | VIS, ENT | CU-M6-01 |
| RF-M6-08 | Notificación al Decano cuando se genera una referencia | VIS | CU-M6-01 |
| RF-M6-09 | Canales de notificación: bandeja interna, correo y push móvil | VIS | CU-M6-01 |
| RF-M6-10 | Umbrales de alertas configurables por el Administrador | VIS | CU-M6-03 |
| RF-M6-11 | Historial de notificaciones consultable por cada usuario | VIS | CU-M6-02 |

---

## M7 — Seguridad y Auditoría

| ID Requisito | Descripción resumida | Origen | Caso de uso |
|---|---|---|---|
| RF-M7-01 | Autenticación JWT con tokens de acceso (15 min) y refresh tokens (7 días) | VIS, LOPDP | CU-M7-01 |
| RF-M7-02 | Cifrado de contraseñas con BCrypt factor 12 | VIS, LOPDP | CU-M7-01 |
| RF-M7-03 | Validación de dominio @tecazuay.edu.ec para todos los usuarios | VIS, LOPDP | CU-M7-01 |
| RF-M7-04 | Expiración automática de sesión por inactividad | VIS, LOPDP | CU-M7-02 |
| RF-M7-05 | Control de acceso por roles en todos los endpoints | VIS, LOPDP | CU-M7-01 |
| RF-M7-06 | Cifrado AES-256 de datos clínicos sensibles en reposo | VIS, LOPDP | CU-M7-01 |
| RF-M7-07 | HTTPS obligatorio con TLS 1.3 en todas las comunicaciones | VIS, LOPDP | CU-M7-01 |
| RF-M7-08 | Log de auditoría inmutable con usuario, acción, recurso, timestamp e IP | VIS, MSP, LOPDP | CU-M7-03 |
| RF-M7-09 | Registro de todos los intentos de autenticación exitosos y fallidos | VIS, LOPDP | CU-M7-01 |
| RF-M7-10 | Integridad de logs mediante firma HMAC-SHA256 | VIS, MSP | CU-M7-03 |
| RF-M7-11 | Rate limiting: máximo 5 intentos de login por minuto por IP | VIS, LOPDP | CU-M7-01 |

---

## M8 — Administración del Sistema

| ID Requisito | Descripción resumida | Origen | Caso de uso |
|---|---|---|---|
| RF-M8-01 | Gestión de usuarios: crear, editar y desactivar cuentas | VIS | CU-M8-01 |
| RF-M8-02 | Gestión de roles y permisos por usuario | VIS, LOPDP | CU-M8-01 |
| RF-M8-03 | Gestión del catálogo de carreras y ciclos | VIS, ENT | CU-M8-02 |
| RF-M8-04 | Gestión del catálogo de establecimientos de salud | VIS, ENT | CU-M8-02 |
| RF-M8-05 | Gestión del catálogo CIE-10 con carga masiva inicial | VIS | CU-M8-02 |
| RF-M8-06 | Configuración de umbrales de notificaciones automáticas | VIS | CU-M6-03 |
| RF-M8-07 | Configuración de reportes automáticos periódicos | VIS | CU-M8-03 |
| RF-M8-08 | Respaldos automáticos cifrados con ejecución manual disponible | VIS, LOPDP | CU-M8-04 |

---

## Resumen de Cobertura

| Módulo | Total RF | RF cubiertos | Cobertura |
|--------|----------|--------------|-----------|
| M1 — Gestión de Pacientes | 9 | 9 | 100% |
| M2 — Atención Médica | 15 | 15 | 100% |
| M3 — Referencia Médica | 8 | 8 | 100% |
| M4 — Historial Clínico | 8 | 8 | 100% |
| M5 — Dashboard y Reportes | 8 | 8 | 100% |
| M6 — Notificaciones | 11 | 11 | 100% |
| M7 — Seguridad y Auditoría | 11 | 11 | 100% |
| M8 — Administración | 8 | 8 | 100% |
| **Total** | **78** | **78** | **100%** |

---

*MEDISTA — Matriz de Trazabilidad de Requisitos v1.0*  
*Instituto Superior Universitario TEC Azuay — Abril 2026*
