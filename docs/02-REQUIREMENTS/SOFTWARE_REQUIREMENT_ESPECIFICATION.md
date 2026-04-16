<div align="center">
    <h1>SOFTWARE REQUIREMENT ESPECIFICATION</h1>
    <h2>MEDISTA</h2>
    <h3>Sistema de Gestión de Atención Médica</h3>
    
</div>

---

&nbsp;

&nbsp;

**Autor:** Miguel Angel Zhunio Remache

**Proyecto:** MEDISTA

**Departamento:** Departamento Médico — Instituto Superior Universitario TEC Azuay

**Estándar base:** IEEE 830 (adaptado)

**Versión:** 1.0

**Fecha:** 13 de Abril 2026

---

## Tabla de Contenidos

1. [Introducción](#1-introducción)
   - 1.1 [Propósito](#11-propósito)
   - 1.2 [Alcance](#12-alcance)
   - 1.3 [Definiciones, Acrónimos y Abreviaturas](#13-definiciones-acrónimos-y-abreviaturas)
   - 1.4 [Referencias](#14-referencias)
   - 1.5 [Visión General del Documento](#15-visión-general-del-documento)

2. [Descripción General del Sistema](#2-descripción-general-del-sistema)
   - 2.1 [Perspectiva del Producto](#21-perspectiva-del-producto)
   - 2.2 [Funciones Principales del Sistema](#22-funciones-principales-del-sistema)
   - 2.3 [Características de los Usuarios](#23-características-de-los-usuarios)
   - 2.4 [Restricciones Generales](#24-restricciones-generales)
   - 2.5 [Suposiciones y Dependencias](#25-suposiciones-y-dependencias)

3. [Requisitos Funcionales](#3-requisitos-funcionales)
   - 3.1 [M1 — Gestión de Pacientes](#31-m1--gestión-de-pacientes)
   - 3.2 [M2 — Atención Médica](#32-m2--atención-médica)
   - 3.3 [M3 — Referencia Médica](#33-m3--referencia-médica)
   - 3.4 [M4 — Historial Clínico](#34-m4--historial-clínico)
   - 3.5 [M5 — Dashboard Estadístico y Reportes](#35-m5--dashboard-estadístico-y-reportes)
   - 3.6 [M6 — Notificaciones Inteligentes](#36-m6--notificaciones-inteligentes)
   - 3.7 [M7 — Seguridad y Auditoría](#37-m7--seguridad-y-auditoría)
   - 3.8 [M8 — Administración del Sistema](#38-m8--administración-del-sistema)

4. [Requisitos No Funcionales](#4-requisitos-no-funcionales)
   - 4.1 [Rendimiento](#41-rendimiento)
   - 4.2 [Seguridad](#42-seguridad)
   - 4.3 [Usabilidad](#43-usabilidad)
   - 4.4 [Disponibilidad y Confiabilidad](#44-disponibilidad-y-confiabilidad)
   - 4.5 [Escalabilidad](#45-escalabilidad)
   - 4.6 [Cumplimiento Normativo](#46-cumplimiento-normativo)
   - 4.7 [Mantenibilidad](#47-mantenibilidad)

5. [Requisitos de Interfaz Externa](#5-requisitos-de-interfaz-externa)
   - 5.1 [Interfaces de Usuario](#51-interfaces-de-usuario)
   - 5.2 [Interfaces de Hardware](#52-interfaces-de-hardware)
   - 5.3 [Interfaces de Software](#53-interfaces-de-software)
   - 5.4 [Interfaces de Comunicación](#54-interfaces-de-comunicación)

6. [Restricciones del Sistema](#6-restricciones-del-sistema)

7. [Apéndices](#7-apéndices)
   - 7.1 [Apéndice A — Formato del Formulario de Atención Médica Actual](#71-apéndice-a--formato-del-formulario-de-atención-médica-actual)
   - 7.2 [Apéndice B — Tabla de Requisitos por Rol](#72-apéndice-b--tabla-de-requisitos-por-rol)

---

## 1. Introducción

### 1.1 Propósito

El presente documento constituye la Especificación de Requisitos de Software (ERS) del sistema MEDISTA. Su propósito es definir de forma completa, precisa y verificable los requisitos funcionales y no funcionales del sistema, estableciendo el contrato formal entre el equipo de desarrollo y los stakeholders de la institución respecto a qué debe hacer el sistema, bajo qué condiciones y con qué restricciones.
Este documento está dirigido a la médico del Departamento Médico del Instituto Superior Universitario TEC Azuay, al Decano de la institución, y al desarrollador responsable del sistema. Sirve como referencia base para las fases de diseño, implementación, verificación y aceptación del proyecto.

### 1.2 Alcance

**MEDISTA** es un sistema de gestión de atención médica web y móvil desarrollado para el Departamento Médico del Instituto Superior Universitario TEC Azuay. El sistema digitaliza completamente el proceso de atención clínica actualmente gestionado en papel, permitiendo el registro centralizado de datos de filiación y datos clínicos de los estudiantes, el seguimiento del historial médico por paciente, la generación de referencias médicas en formato institucional, y el análisis estadístico de las atenciones para apoyar la toma de decisiones y la detección temprana de tendencias epidemiológicas.

**MEDISTA** se limita exclusivamente a la gestión de atenciones médicas del departamento. Quedan fuera del alcance de la presente versión: el módulo de agenda y citas médicas, la gestión de inventario de medicamentos, la integración con sistemas externos del Ministerio de Salud Pública o del IESS, la gestión financiera o de cobros, y cualquier otra área del departamento médico no relacionada directamente con el proceso de atención clínica.

### 1.3 Definiciones, Acrónimos y Abreviaturas

#### Acrónimos y Abreviaturas del Proyecto
 
| Término | Definición |
|---------|------------|
| ERS / SRS | Especificación de Requisitos de Software (*Software Requirements Specification*). Documento formal que define qué debe hacer un sistema de software. |
| MEDISTA-V2 | Sistema de Gestión de Atención Médica, versión 2. Sistema desarrollado para el Departamento Médico del Instituto Superior Universitario TEC Azuay. |
| TEC Azuay | Instituto Superior Universitario TEC Azuay. Institución educativa de nivel superior en Cuenca, Ecuador. |
| IEEE 830 | Estándar internacional del Instituto de Ingenieros Eléctricos y Electrónicos para la elaboración de especificaciones de requisitos de software. |
 
#### Acrónimos Médicos y Clínicos
 
| Término | Definición |
|---------|------------|
| CIE-10 | Clasificación Internacional de Enfermedades, décima revisión. Catálogo de más de 14.000 códigos de diagnóstico publicado por la Organización Mundial de la Salud (OMS). |
| IMC | Índice de Masa Corporal. Indicador que relaciona el peso y la talla de una persona (kg/m²) para evaluar su estado nutricional. |
| FC | Frecuencia Cardíaca. Número de latidos del corazón por minuto. |
| FR | Frecuencia Respiratoria. Número de respiraciones por minuto. |
| PA | Presión Arterial. Fuerza que ejerce la sangre sobre las paredes de las arterias, expresada en milímetros de mercurio (mmHg). |
| Sat.O2 | Saturación de Oxígeno. Porcentaje de hemoglobina saturada con oxígeno en la sangre. |
| Glasgow | Escala de Glasgow. Escala neurológica que evalúa el nivel de conciencia de un paciente mediante tres parámetros: respuesta ocular, verbal y motora. |
| FUM | Fecha de Última Menstruación. |
| IVSA | Inicio de Vida Sexual Activa. |
| FPP | Fecha Probable de Parto. |
| SG | Semanas de Gestación. |
| G / A / P / C | Gestaciones / Abortos / Partos / Cesáreas. Indicadores del historial obstétrico de una paciente. |
| MSP | Ministerio de Salud Pública del Ecuador. |
| UAT | Pruebas de Aceptación del Usuario (*User Acceptance Testing*). Fase de verificación en la que el usuario final valida que el sistema cumple con sus requisitos. |
 
#### Acrónimos Normativos y Legales
 
| Término | Definición |
|---------|------------|
| LOPDP | Ley Orgánica de Protección de Datos Personales. Normativa ecuatoriana que regula la recopilación, almacenamiento y tratamiento de datos personales, con disposiciones especiales para datos sensibles de salud. |
| Acuerdo MSP No. 00000125 | Acuerdo Ministerial del MSP que regula el manejo de historias clínicas en formato electrónico en Ecuador, estableciendo requisitos de integridad, trazabilidad, confidencialidad e inmutabilidad. |
 
#### Acrónimos Tecnológicos
 
| Término | Definición |
|---------|------------|
| JWT | *JSON Web Token*. Estándar de autenticación que permite transmitir información de identidad entre partes de forma segura y sin estado en el servidor. |
| API REST | *Application Programming Interface* de estilo arquitectónico REST (*Representational State Transfer*). Interfaz de comunicación entre el frontend y el backend del sistema. |
| SPA | *Single Page Application*. Aplicación web que carga una sola página HTML y actualiza dinámicamente el contenido sin recargar el navegador. |
| ORM | *Object-Relational Mapping*. Técnica que permite interactuar con una base de datos relacional usando objetos del lenguaje de programación. |
| DTO | *Data Transfer Object*. Objeto usado para transportar datos entre capas del sistema sin exponer las entidades de la base de datos directamente. |
| AES-256 | *Advanced Encryption Standard* con clave de 256 bits. Algoritmo de cifrado simétrico usado para proteger datos sensibles en reposo. |
| TLS | *Transport Layer Security*. Protocolo criptográfico que garantiza la seguridad de las comunicaciones en red (base de HTTPS). |
| HTTPS | *Hypertext Transfer Protocol Secure*. Protocolo de comunicación web cifrado mediante TLS. |
| STOMP | *Simple Text Oriented Messaging Protocol*. Protocolo de mensajería sobre WebSocket usado para notificaciones en tiempo real. |
| FCM | *Firebase Cloud Messaging*. Servicio de Google para el envío de notificaciones push a dispositivos móviles. |
| PDF | *Portable Document Format*. Formato de documento electrónico independiente del sistema operativo, usado para la generación de formularios de atención médica y referencias. |
| Docker | Plataforma de contenedores que permite empaquetar y desplegar aplicaciones en entornos aislados y reproducibles. |
| CI/CD | *Continuous Integration / Continuous Deployment*. Prácticas de automatización del ciclo de construcción, prueba y despliegue de software. |

### 1.4 Referencias

#### Normativas y Estándares
 
| Referencia | Descripción |
|------------|-------------|
| IEEE 830-1998 | IEEE Recommended Practice for Software Requirements Specifications. Instituto de Ingenieros Eléctricos y Electrónicos. |
| LOPDP | Ley Orgánica de Protección de Datos Personales. Registro Oficial Suplemento 459, Ecuador, 26 de mayo de 2021. |
| Acuerdo MSP No. 00000125 | Acuerdo Ministerial del Ministerio de Salud Pública del Ecuador. Norma para el manejo de la historia clínica en formato electrónico. |
 
#### Documentos Internos del Proyecto
 
| Referencia | Descripción |
|------------|-------------|
| MEDISTA — Visión del Sistema v1.0 | Define los módulos, roles, flujos de operación y reglas de negocio del sistema. Base funcional de este ERS. |
| MEDISTA — Arquitectura Técnica v1.0 | Define el stack tecnológico completo y las justificaciones de cada decisión técnica. |
| MEDISTA — Estudio de Factibilidad v1.0 | Evalúa la viabilidad técnica, operativa, económica, legal y temporal del proyecto. |
| Formulario de Atención Médica — Departamento Médico TEC Azuay | Formulario físico actual diseñado por la médico de la institución. Referencia directa para la digitalización del Módulo de Atención Médica y la generación de PDFs institucionales. |

### 1.5 Visión General del Documento

El presente documento se organiza en siete secciones. La Sección 2 presenta la descripción general del sistema, abarcando su perspectiva dentro del contexto institucional, un resumen de sus funciones principales, las características de cada tipo de usuario, las restricciones generales de diseño y las suposiciones y dependencias que condicionan el desarrollo. La Sección 3 especifica los requisitos funcionales del sistema, organizados por módulo, describiendo en detalle qué debe hacer el sistema en cada área funcional. La Sección 4 define los requisitos no funcionales, es decir, las condiciones de calidad bajo las cuales el sistema debe operar: rendimiento, seguridad, usabilidad, disponibilidad, escalabilidad, cumplimiento normativo y mantenibilidad. La Sección 5 establece los requisitos de interfaz externa, especificando cómo el sistema interactúa con usuarios, hardware, software de terceros y canales de comunicación. La Sección 6 detalla las restricciones del sistema que delimitan las decisiones de diseño e implementación. Finalmente, la Sección 7 contiene los apéndices de referencia: el formato del formulario de atención médica actual y la tabla de requisitos por rol.

---

## 2. Descripción General del Sistema

### 2.1 Perspectiva del Producto

MEDISTA es un sistema nuevo que reemplaza completamente el proceso manual de atención médica del Departamento Médico del Instituto Superior Universitario TEC Azuay, actualmente gestionado mediante formularios en papel. El sistema se despliega en el servidor local de la institución y opera de forma autónoma, sin integrarse ni compartir datos con el sistema académico institucional ni con ningún sistema externo en la presente versión.
 
El sistema está compuesto por tres componentes de software independientes que operan de forma coordinada: una aplicación web para el personal médico y administrativo, una aplicación móvil para los estudiantes, y un backend centralizado que gestiona la lógica de negocio, la base de datos y las comunicaciones entre componentes. Toda la información clínica generada por el sistema reside exclusivamente en la base de datos institucional, separada físicamente de cualquier otro sistema del instituto. 

### 2.2 Funciones Principales del Sistema

MEDISTA organiza sus funcionalidades en ocho módulos. El **Módulo de Gestión de Pacientes** permite al médico registrar, buscar y actualizar los datos de filiación de los estudiantes que acuden al departamento. El **Módulo de Atención Médica** digitaliza el formulario de atención clínica completo, incluyendo signos vitales, examen físico por sistemas, diagnóstico con código CIE-10 y tratamiento. El **Módulo de Referencia Médica** gestiona las derivaciones a otros establecimientos de salud y genera el documento de referencia en PDF con formato institucional.
 
El **Módulo de Historial Clínico** permite visualizar la evolución médica completa de un paciente a lo largo del tiempo, incluyendo gráficas de signos vitales y diagnósticos recurrentes. El **Módulo de Dashboard Estadístico y Reportes** proporciona indicadores agregados de las atenciones médicas con filtros inteligentes cruzados y exportación a PDF y Excel para la toma de decisiones institucional. El **Módulo de Notificaciones Inteligentes** alerta proactivamente a cada rol sobre situaciones clínicas o epidemiológicas relevantes, incluyendo detección de posibles brotes y pacientes frecuentes.
 
El **Módulo de Seguridad y Auditoría** garantiza la protección de los datos clínicos mediante autenticación por roles, cifrado de información sensible y un registro de auditoría completo e inmutable. El **Módulo de Administración del Sistema** permite gestionar usuarios, catálogos institucionales, umbrales de notificaciones y respaldos de base de datos.

### 2.3 Características de los Usuarios

El sistema contempla cuatro tipos de usuarios, cada uno con plataforma, nivel técnico y responsabilidades diferenciadas:
 
| Rol | Plataforma | Nivel Técnico | Responsabilidades en el Sistema |
|-----|-----------|---------------|--------------------------------|
| Médico | Web | Básico. Usuario con formación clínica, no tecnológica. Familiarizado con el formulario físico actual. | Registrar pacientes nuevos, crear atenciones médicas, generar referencias médicas, consultar historiales clínicos y recibir alertas clínicas. Es el único rol que produce datos clínicos en el sistema. |
| Decano | Web | Intermedio. Usuario con capacidad de interpretar gráficas y reportes estadísticos. | Consultar dashboards estadísticos con datos agregados de las atenciones médicas, exportar reportes institucionales y recibir alertas de situaciones epidemiológicas. No tiene acceso a expedientes clínicos individuales. |
| Administrador del Sistema | Web | Avanzado. Usuario con conocimiento técnico de administración de sistemas. | Gestionar cuentas de usuario, roles y catálogos institucionales, configurar umbrales de notificaciones, supervisar respaldos de base de datos y realizar tareas de mantenimiento técnico del sistema. |
| Estudiante | Móvil | Básico. Usuario con manejo cotidiano de aplicaciones móviles. | Consultar su propio historial clínico personal y recibir notificaciones relacionadas con sus atenciones médicas. Rol de solo lectura — no introduce ni modifica ningún dato en el sistema. |

### 2.4 Restricciones Generales

Las siguientes restricciones condicionan el diseño e implementación del sistema y no son negociables dentro del alcance del presente proyecto:
 
| # | Restricción | Descripción |
|---|-------------|-------------|
| RG-01 | Cumplimiento LOPDP | El sistema maneja datos de salud clasificados como datos sensibles bajo la Ley Orgánica de Protección de Datos Personales del Ecuador. Todo tratamiento, almacenamiento y acceso a estos datos debe cumplir con las disposiciones de esta ley, incluyendo cifrado, control de acceso y auditoría. |
| RG-02 | Cumplimiento Acuerdo MSP No. 00000125 | Los registros clínicos electrónicos deben cumplir con los requisitos de integridad, trazabilidad, confidencialidad e inmutabilidad establecidos por el Ministerio de Salud Pública del Ecuador. Las atenciones médicas no pueden eliminarse físicamente del sistema. |
| RG-03 | Despliegue en infraestructura local | El sistema debe desplegarse exclusivamente en el servidor local del Instituto Superior Universitario TEC Azuay. No se permite el uso de servicios de nube pública para el almacenamiento de datos clínicos en la presente versión. |
| RG-04 | Autenticación de estudiantes por dominio institucional | Los estudiantes solo pueden autenticarse en la aplicación móvil mediante una dirección de correo electrónico con dominio @tecazuay.edu.ec. Cualquier otro dominio debe ser rechazado automáticamente por el sistema. |
| RG-05 | Separación de datos por rol | El Decano y el Administrador del Sistema no tienen acceso a expedientes clínicos individuales. El acceso a datos clínicos detallados está restringido exclusivamente al rol Médico. |
| RG-06 | Registro de estudiantes a cargo del médico | Los estudiantes no pueden autoregistrarse en el sistema. El médico es el único responsable de crear el perfil de un estudiante en su primera visita al departamento. |
| RG-07 | Stack tecnológico definido | El sistema debe desarrollarse utilizando el stack tecnológico establecido en el documento MEDISTA-V2 — Arquitectura Técnica v1.0. No se permite la incorporación de tecnologías fuera de este stack sin una revisión formal del documento de arquitectura. |

### 2.5 Suposiciones y Dependencias

Las siguientes suposiciones se consideran verdaderas para el desarrollo e implementación de MEDISTA-V2. Si alguna de estas condiciones cambia, el alcance, el diseño o el cronograma del proyecto podría verse afectado.
 
#### Suposiciones de Desarrollo
 
| # | Suposición |
|---|------------|
| SD-01 | El desarrollador cuenta con los conocimientos técnicos necesarios en el stack tecnológico definido, o con la capacidad de adquirirlos durante el proceso de desarrollo. |
| SD-02 | El desarrollador dispone de un equipo de desarrollo personal con capacidad suficiente para ejecutar el entorno local del sistema durante todo el período de desarrollo. |
| SD-03 | El formulario físico de atención médica actual, diseñado por la médico del departamento, está disponible como referencia visual para la digitalización del sistema y la generación de PDFs institucionales. |
| SD-04 | La médico del departamento está disponible para sesiones de validación de requisitos y pruebas de aceptación durante el período de desarrollo. |
 
#### Suposiciones Institucionales
 
| # | Suposición |
|---|------------|
| SI-01 | El Instituto Superior Universitario TEC Azuay dispone de un servidor local con las especificaciones mínimas requeridas para el despliegue del sistema. |
| SI-02 | El área de tecnología de la institución otorga al equipo de desarrollo acceso al servidor para el despliegue y configuración inicial del sistema. |
| SI-03 | El mantenimiento técnico del servidor — actualizaciones de sistema operativo, disponibilidad de red y respaldo de hardware — es responsabilidad del área de tecnología de la institución, no del desarrollador. |
| SI-04 | Todos los estudiantes que acuden al Departamento Médico cuentan con una dirección de correo electrónico institucional activa bajo el dominio @tecazuay.edu.ec. |
 
#### Suposiciones de Negocio
 
| # | Suposición |
|---|------------|
| SN-01 | El médico es el único responsable de generar datos clínicos en el sistema. Ningún otro rol puede crear ni modificar atenciones médicas, referencias o historiales clínicos. |
| SN-02 | El médico es el único responsable de registrar a los estudiantes en el sistema durante su primera visita al departamento. Los estudiantes no tienen capacidad de autoregistro. |
| SN-03 | El dominio de correo institucional @tecazuay.edu.ec permanece vigente y sin cambios durante el ciclo de vida del sistema. Cualquier cambio de dominio requerirá una actualización formal de la configuración del sistema. |
| SN-04 | Durante el período de implementación inicial, el Departamento Médico opera con un único médico. Si se incorporan más médicos en el futuro, el sistema está diseñado para soportarlo sin cambios estructurales. |
 
---

## 3. Requisitos Funcionales

### 3.1 M1 — Gestión de Pacientes
 
Este módulo gestiona los datos personales de los estudiantes que acuden al Departamento Médico. Es el módulo base del sistema — ningún otro módulo clínico puede operar sin un paciente previamente registrado.
 
| ID | Nombre | Descripción | Prioridad |
|----|--------|-------------|-----------|
| RF-M1-01 | Registro de paciente nuevo | El sistema debe permitir al médico registrar un nuevo paciente con los siguientes datos de filiación: nombres, apellidos, carrera, ciclo, número de cédula, dirección de domicilio, barrio, parroquia, cantón, provincia, teléfono, fecha de nacimiento, lugar de nacimiento, país, género, estado civil y tipo de sangre. | Alta |
| RF-M1-02 | Cédula como identificador único | El sistema debe validar que el número de cédula ingresado no exista previamente en la base de datos. Si ya existe un paciente con esa cédula, el sistema debe rechazar el registro e informar al médico. | Alta |
| RF-M1-03 | Cálculo automático de edad | El sistema debe calcular y mostrar automáticamente la edad del paciente a partir de la fecha de nacimiento ingresada. La edad debe actualizarse en cada consulta posterior sin intervención manual. | Alta |
| RF-M1-04 | Búsqueda de pacientes | El sistema debe permitir al médico buscar pacientes existentes por número de cédula, nombres o apellidos. La búsqueda debe ser tolerante a errores tipográficos menores y mostrar resultados en tiempo real mientras el médico escribe. | Alta |
| RF-M1-05 | Visualización del perfil del paciente | El sistema debe mostrar el perfil completo de un paciente con todos sus datos de filiación y un acceso directo a su historial clínico. | Alta |
| RF-M1-06 | Edición de datos de filiación | El sistema debe permitir al médico actualizar los datos de filiación de un paciente cuando estos cambien (carrera, ciclo, dirección, teléfono, entre otros). | Media |
| RF-M1-07 | Restricción de acceso al registro y edición | Solo el rol Médico puede crear y editar pacientes. Los roles Decano, Administrador del Sistema y Estudiante no tienen acceso a estas funciones. | Alta |
| RF-M1-08 | Campos condicionales por género | El sistema debe habilitar campos adicionales de historial obstétrico únicamente cuando el género del paciente registrado corresponda al sexo femenino. Para pacientes de género masculino, estos campos no deben estar disponibles. | Alta |
| RF-M1-09 | Validación de formato de cédula | El sistema debe validar que el número de cédula ingresado cumpla con el formato válido de cédula de identidad ecuatoriana (10 dígitos, dígito verificador correcto). | Alta |

### 3.2 M2 — Atención Médica

Este módulo digitaliza el formulario de atención médica completo del Departamento Médico. Es el módulo de mayor complejidad y valor funcional del sistema — toda la actividad clínica del departamento se registra aquí.
 
| ID | Nombre | Descripción | Prioridad |
|----|--------|-------------|-----------|
| RF-M2-01 | Creación de nueva atención médica | El sistema debe permitir al médico crear una nueva atención médica asociada a un paciente previamente registrado. No es posible crear una atención sin un paciente existente en el sistema. La fecha y hora de creación se registran automáticamente. | Alta |
| RF-M2-02 | Registro de motivo de consulta | El sistema debe permitir al médico ingresar el motivo de consulta del paciente mediante texto libre. | Alta |
| RF-M2-03 | Registro y herencia de antecedentes | El sistema debe permitir registrar antecedentes familiares y personales organizados en seis categorías: Alergias, Clínico, Ginecológico, Traumatológico, Quirúrgico y Farmacológico, con opción de marcar "No aplica". Si el paciente tiene atenciones previas, los antecedentes registrados deben aparecer precargados y editables en la nueva atención. | Alta |
| RF-M2-04 | Registro de enfermedad actual | El sistema debe permitir al médico describir la enfermedad actual del paciente mediante texto libre. | Alta |
| RF-M2-05 | Registro de signos vitales | El sistema debe permitir ingresar los siguientes signos vitales con validación de tipo de dato numérico en cada campo: presión arterial sistólica y diastólica, peso (kg), talla (m), frecuencia cardíaca, frecuencia respiratoria, temperatura y saturación de oxígeno. | Alta |
| RF-M2-06 | Cálculo automático de IMC | El sistema debe calcular automáticamente el Índice de Masa Corporal (IMC) al ingresar el peso y la talla del paciente, usando la fórmula IMC = peso / talla². El resultado debe mostrarse en tiempo real sin intervención del médico. | Alta |
| RF-M2-07 | Registro y cálculo automático de Glasgow | El sistema debe permitir ingresar los tres componentes de la Escala de Glasgow: respuesta ocular, verbal y motora. El total debe calcularse automáticamente como la suma de los tres valores y mostrarse en tiempo real. Adicionalmente, el sistema debe registrar llenado capilar y reflejo pupilar. | Alta |
| RF-M2-08 | Registro de examen físico por sistemas | El sistema debe permitir registrar el examen físico de los siguientes nueve sistemas: Piel y faneras, Cabeza, Cuello, Tórax, Corazón, Abdomen, Región inguinal, Miembros superiores y Miembros inferiores. Cada sistema debe poder marcarse como Normal o incluir una descripción textual de hallazgos anormales. | Alta |
| RF-M2-09 | Sección de emergencias obstétricas condicional | El sistema debe mostrar automáticamente la sección de emergencias obstétricas únicamente cuando el género del paciente registrado sea femenino. Esta sección incluye: menarca, ritmo menstrual, ciclos, fecha de última menstruación, inicio de vida sexual activa, número de parejas sexuales, fórmula obstétrica (G/A/P/C), dismenorrea, mastodinia y datos de embarazo en caso de aplicar (FPP, semanas de gestación, controles e inmunizaciones). Para pacientes de género masculino, esta sección no debe ser visible ni accesible. | Alta |
| RF-M2-10 | Registro de exámenes complementarios | El sistema debe permitir al médico registrar exámenes complementarios con una descripción textual y la posibilidad de adjuntar un archivo por cada examen (formatos permitidos: PDF, JPG, PNG). También debe estar disponible la opción de marcar "No aplica". | Media |
| RF-M2-11 | Registro de diagnóstico con CIE-10 | El sistema debe permitir al médico registrar uno o más diagnósticos por atención. Cada diagnóstico debe poder buscarse mediante un buscador integrado del catálogo CIE-10, el cual debe sugerir resultados en tiempo real mientras el médico escribe el nombre de la enfermedad o su código. Cada diagnóstico debe clasificarse como Presuntivo o Definitivo. | Alta |
| RF-M2-12 | Registro de tratamiento | El sistema debe permitir al médico registrar el tratamiento indicado para el paciente mediante texto libre. | Alta |
| RF-M2-13 | Datos del profesional precargados | El sistema debe registrar automáticamente en cada atención el nombre del profesional y su código MSP, extraídos de la sesión activa del médico que está creando la atención. El médico no debe ingresar estos datos manualmente. | Alta |
| RF-M2-14 | Inmutabilidad de atenciones guardadas | Una atención médica guardada no puede ser eliminada ni modificada directamente. Si el médico necesita corregir un dato, el sistema debe permitir agregar una nota de corrección vinculada a la atención original, preservando el registro histórico completo. | Alta |
| RF-M2-15 | Validación de tipos de dato en signos vitales | El sistema debe validar que los campos de signos vitales acepten únicamente valores numéricos dentro de rangos fisiológicamente razonables, rechazando entradas inválidas e informando al médico del error. | Alta |

### 3.3 M3 — Referencia Médica

Este módulo gestiona las derivaciones médicas cuando el paciente necesita ser referido a otro establecimiento de salud. Toda referencia médica debe estar vinculada a una atención médica existente.
 
| ID | Nombre | Descripción | Prioridad |
|----|--------|-------------|-----------|
| RF-M3-01 | Creación de referencia desde atención médica | El sistema debe permitir al médico generar una referencia médica únicamente desde una atención médica existente. No es posible crear una referencia médica de forma independiente. | Alta |
| RF-M3-02 | Precarga automática de datos | Al iniciar la creación de una referencia, el sistema debe precargar automáticamente los siguientes campos desde la atención médica y la sesión activa: nombre de la institución (Instituto Superior Universitario TEC Azuay), número de cédula del paciente, servicio (Departamento Médico), nombre del profesional y código MSP. El médico no debe ingresar estos datos manualmente. | Alta |
| RF-M3-03 | Registro de datos del establecimiento destino | El sistema debe permitir al médico completar los siguientes campos del establecimiento de salud al que se deriva al paciente: entidad del sistema, nombre del establecimiento de salud, servicio y especialidad. | Alta |
| RF-M3-04 | Selección de motivo de referencia | El sistema debe permitir al médico seleccionar el motivo de la referencia entre las siguientes tres opciones predefinidas: Limitada capacidad resolutiva, Falta de profesional, u Otros. | Alta |
| RF-M3-05 | Registro de resumen clínico y hallazgos | El sistema debe permitir al médico ingresar mediante texto libre el resumen del cuadro clínico del paciente y los hallazgos relevantes que justifican la derivación. | Alta |
| RF-M3-06 | Registro de diagnósticos con CIE-10 | El sistema debe permitir registrar hasta dos diagnósticos en la referencia médica, cada uno con su código CIE-10 y clasificación como Presuntivo o Definitivo. El médico puede importar los diagnósticos directamente desde la atención médica vinculada o buscarlos nuevamente en el catálogo CIE-10. | Alta |
| RF-M3-07 | Generación de PDF institucional | El sistema debe generar un documento PDF de la referencia médica con formato idéntico al formulario de referencia físico actual del Departamento Médico del Instituto Superior Universitario TEC Azuay, incluyendo el logo institucional y todos los campos definidos en el formulario oficial. | Alta |
| RF-M3-08 | Notificación automática al generar referencia | Al guardar una referencia médica, el sistema debe notificar automáticamente al estudiante referido con los detalles del establecimiento de salud destino, y al Decano con un aviso de que se ha generado una nueva referencia. | Media |

### 3.4 M4 — Historial Clínico

Este módulo permite visualizar la evolución médica completa de un paciente a lo largo del tiempo. Es un módulo exclusivamente de consulta — ningún dato puede ser creado ni modificado desde aquí.
 
| ID | Nombre | Descripción | Prioridad |
|----|--------|-------------|-----------|
| RF-M4-01 | Línea de tiempo de atenciones | El sistema debe mostrar todas las atenciones médicas de un paciente organizadas cronológicamente en una línea de tiempo. Cada atención debe presentar en vista resumida la fecha y el diagnóstico principal, con la posibilidad de expandirla para visualizar el detalle completo del formulario de atención. | Alta |
| RF-M4-02 | Gráficas de evolución de signos vitales | El sistema debe mostrar gráficas interactivas que representen la evolución de los signos vitales del paciente a lo largo del tiempo, incluyendo peso, IMC y presión arterial. Las gráficas deben permitir identificar tendencias entre consultas. | Alta |
| RF-M4-03 | Listado de diagnósticos recurrentes | El sistema debe mostrar un listado de los diagnósticos que se repiten con mayor frecuencia en el historial del paciente, identificados por su nombre y código CIE-10. | Media |
| RF-M4-04 | Historial de referencias médicas | El sistema debe mostrar el listado completo de referencias médicas generadas para el paciente, con fecha, establecimiento de salud destino y diagnóstico asociado. | Media |
| RF-M4-05 | Filtros del historial | El sistema debe permitir filtrar las atenciones del historial por rango de fechas y por tipo de diagnóstico CIE-10, de forma individual o combinada. | Media |
| RF-M4-06 | Vista completa para el médico | El médico debe tener acceso a la visualización completa del historial clínico del paciente, incluyendo todos los campos del formulario de atención, gráficas de signos vitales, diagnósticos recurrentes, referencias médicas y filtros avanzados. | Alta |
| RF-M4-07 | Vista simplificada para el estudiante | El estudiante debe tener acceso desde la aplicación móvil a una versión simplificada de su propio historial clínico, que muestre para cada atención: fecha, motivo de consulta, diagnóstico y tratamiento. El estudiante no tiene acceso al detalle clínico completo ni a las gráficas de evolución. | Alta |
| RF-M4-08 | Historial de solo lectura | El historial clínico es un módulo exclusivamente de consulta. Ningún rol puede crear, modificar ni eliminar registros desde esta vista. | Alta |

### 3.5 M5 — Dashboard Estadístico y Reportes

Este módulo proporciona inteligencia de datos al Decano y al médico para la toma de decisiones institucional. Todos los datos presentados son agregados — ningún rol puede acceder a expedientes clínicos individuales desde este módulo.
 
| ID | Nombre | Descripción | Prioridad |
|----|--------|-------------|-----------|
| RF-M5-01 | Dashboard interactivo en tiempo real | El sistema debe mostrar un dashboard interactivo con métricas actualizadas en tiempo real, incluyendo: total de atenciones por período, enfermedades más frecuentes, distribución de atenciones por carrera y ciclo, distribución por género y grupo etario, promedio de IMC general y por carrera, cantidad de referencias médicas generadas, picos de demanda por día o semana, y tasa de recurrencia de pacientes. | Alta |
| RF-M5-02 | Detección de anomalías e incrementos inusuales | El sistema debe identificar y destacar visualmente en el dashboard incrementos inusuales en el volumen de atenciones o en la frecuencia de un diagnóstico específico, alertando sobre posibles anomalías epidemiológicas que requieran atención institucional. | Alta |
| RF-M5-03 | Filtros inteligentes cruzados | El sistema debe permitir al Decano aplicar múltiples filtros de forma simultánea y combinada sobre las métricas del dashboard: por carrera, ciclo, período o semestre, género, rango de edad, diagnóstico CIE-10 y tipo de atención. El sistema debe permitir además comparar los resultados filtrados contra períodos anteriores. | Alta |
| RF-M5-04 | Exportación de reportes a PDF institucional | El sistema debe permitir exportar las métricas visualizadas en el dashboard a un documento PDF con formato institucional del Instituto Superior Universitario TEC Azuay, apto para presentación ante autoridades. | Alta |
| RF-M5-05 | Exportación de datos a Excel | El sistema debe permitir exportar los datos estadísticos a un archivo Excel (.xlsx) con formato estructurado, permitiendo al Decano manipular y analizar los datos fuera del sistema. | Media |
| RF-M5-06 | Reportes automáticos periódicos | El sistema debe generar y enviar automáticamente reportes de actividad del departamento al Decano por correo electrónico en los intervalos configurados: semanal, mensual o semestral. El intervalo debe ser configurable por el Administrador del Sistema. | Media |
| RF-M5-07 | Restricción a datos agregados | El Decano y el médico acceden exclusivamente a datos estadísticos agregados desde este módulo. Ningún indicador, gráfica ni reporte debe permitir identificar o acceder al expediente clínico de un estudiante individual. | Alta |
| RF-M5-08 | Estadísticas propias del médico | El médico puede visualizar estadísticas agregadas limitadas a las atenciones médicas registradas por su propia cuenta, a efectos de monitorear su actividad clínica personal. Esta restricción aplica exclusivamente al módulo de estadísticas — en el Módulo de Historial Clínico (M4), el médico tiene acceso completo a todas las atenciones de un paciente independientemente del médico que las registró. | Media |

### 3.6 M6 — Notificaciones Inteligentes

Este módulo gestiona las alertas y notificaciones proactivas hacia cada rol del sistema. Opera de forma transversal — se activa como consecuencia de eventos generados en otros módulos y no produce datos clínicos propios.
 
| ID | Nombre | Descripción | Prioridad |
|----|--------|-------------|-----------|
| RF-M6-01 | Notificación al estudiante por atención registrada | El sistema debe notificar automáticamente al estudiante cuando el médico registre una nueva atención médica a su nombre, incluyendo un resumen con fecha, motivo de consulta y diagnóstico. | Alta |
| RF-M6-02 | Notificación al estudiante por referencia médica | El sistema debe notificar automáticamente al estudiante cuando se genere una referencia médica a su nombre, incluyendo los datos del establecimiento de salud destino y la especialidad a la que es derivado. | Alta |
| RF-M6-03 | Recordatorio de seguimiento al estudiante | El sistema debe notificar al estudiante cuando el médico programe un seguimiento, indicando la fecha y las instrucciones correspondientes. | Media |
| RF-M6-04 | Alerta al médico por paciente frecuente | El sistema debe alertar al médico cuando un paciente supere el umbral configurado de visitas en un período determinado, identificando al estudiante y el número de atenciones registradas. | Media |
| RF-M6-05 | Recordatorio de seguimientos pendientes al médico | El sistema debe notificar al médico sobre los seguimientos programados pendientes antes de su fecha de vencimiento. | Media |
| RF-M6-06 | Alerta al médico por diagnóstico inusual | El sistema debe alertar al médico cuando un diagnóstico específico aparezca con una frecuencia inusual en un período determinado, sugiriendo una posible tendencia epidemiológica. | Alta |
| RF-M6-07 | Alerta al Decano por anomalías institucionales | El sistema debe notificar al Decano cuando se detecten anomalías en el volumen de atenciones médicas o en la frecuencia de diagnósticos específicos, indicando el tipo de anomalía detectada y el período en que se presenta. | Alta |
| RF-M6-08 | Notificación al Decano por referencia generada | El sistema debe notificar al Decano cada vez que se genere una referencia médica, con información del establecimiento destino y la especialidad, sin exponer datos clínicos individuales del paciente. | Media |
| RF-M6-09 | Canales de notificación | El sistema debe entregar las notificaciones a través de los siguientes canales según el rol del destinatario: bandeja de notificaciones interna del sistema para todos los roles, correo electrónico para todos los roles, y notificación push en la aplicación móvil exclusivamente para estudiantes. | Alta |
| RF-M6-10 | Umbrales configurables | Los umbrales que activan las alertas automáticas — número de visitas por período, frecuencia de diagnósticos, volumen de atenciones — deben ser configurables por el Administrador del Sistema sin necesidad de modificar el código fuente. | Alta |
| RF-M6-11 | Historial de notificaciones | Todas las notificaciones generadas por el sistema deben almacenarse y ser consultables históricamente por el destinatario correspondiente, indicando fecha, tipo de notificación y estado de lectura. | Media |

### 3.7 M7 — Seguridad y Auditoría

Este módulo garantiza la protección de los datos clínicos y la trazabilidad completa de todas las acciones realizadas en el sistema, en cumplimiento de la LOPDP y el Acuerdo Ministerial MSP No. 00000125. Opera de forma transversal — sus controles aplican a todos los módulos del sistema.
 
| ID | Nombre | Descripción | Prioridad |
|----|--------|-------------|-----------|
| RF-M7-01 | Autenticación mediante JWT | El sistema debe implementar autenticación basada en JSON Web Tokens. Los tokens de acceso deben tener una duración máxima de 15 minutos. Los refresh tokens deben tener una duración de 7 días y permitir renovar el token de acceso sin requerir que el usuario inicie sesión nuevamente. | Alta |
| RF-M7-02 | Cifrado de contraseñas con BCrypt | Las contraseñas de los usuarios Médico, Decano y Administrador del Sistema deben almacenarse en la base de datos cifradas mediante el algoritmo BCrypt con factor de trabajo 12. Ninguna contraseña debe almacenarse en texto plano. | Alta |
| RF-M7-03 | Validación de dominio institucional | El sistema debe validar que todos los usuarios se autentiquen exclusivamente con una dirección de correo electrónico bajo el dominio @tecazuay.edu.ec. Cualquier intento de autenticación con un dominio diferente debe ser rechazado automáticamente. | Alta |
| RF-M7-04 | Expiración de sesión por inactividad | El sistema debe cerrar automáticamente la sesión de cualquier usuario que permanezca inactivo durante un período configurable, requiriendo que el usuario se autentique nuevamente para continuar. | Alta |
| RF-M7-05 | Control de acceso por roles | El sistema debe garantizar que cada rol acceda únicamente a los recursos y funcionalidades que le corresponden. Ningún endpoint del sistema debe ser accesible sin autenticación válida. Un usuario autenticado no puede acceder a recursos de un rol diferente al suyo. | Alta |
| RF-M7-06 | Cifrado de datos sensibles en reposo | Los datos clínicos sensibles almacenados en la base de datos — incluyendo diagnósticos, antecedentes médicos y datos obstétricos — deben estar cifrados mediante AES-256 a nivel de columna. | Alta |
| RF-M7-07 | Cifrado del tráfico de red | Todas las comunicaciones entre los clientes (web y móvil) y el servidor deben realizarse exclusivamente mediante HTTPS con TLS 1.3. El acceso por HTTP sin cifrar debe ser redirigido automáticamente a HTTPS. | Alta |
| RF-M7-08 | Log de auditoría inmutable | El sistema debe registrar automáticamente un log de auditoría por cada acción relevante ejecutada en el sistema, incluyendo: usuario que ejecutó la acción, tipo de acción, recurso afectado, timestamp e dirección IP de origen. Los registros de auditoría no pueden ser modificados ni eliminados por ningún rol — solo pueden ser consultados por el Administrador del Sistema. | Alta |
| RF-M7-09 | Registro de intentos de autenticación | El sistema debe registrar en el log de auditoría todos los intentos de inicio de sesión, tanto exitosos como fallidos, incluyendo el número de intentos fallidos consecutivos por usuario e IP. | Alta |
| RF-M7-10 | Integridad de logs mediante HMAC | Cada registro del log de auditoría debe incluir una firma digital generada con HMAC-SHA256 que permita detectar cualquier intento de manipulación del registro. | Alta |
| RF-M7-11 | Rate limiting en autenticación | El sistema debe limitar los intentos de inicio de sesión a un máximo de 5 intentos por minuto por dirección IP. Al superar este límite, el sistema debe bloquear temporalmente las solicitudes provenientes de esa IP e informar al usuario. | Alta |

### 3.8 M8 — Administración del Sistema

Este módulo permite configurar y mantener el sistema en operación. Es accesible exclusivamente por el rol Administrador del Sistema — ningún otro rol tiene acceso a estas funcionalidades.
 
| ID | Nombre | Descripción | Prioridad |
|----|--------|-------------|-----------|
| RF-M8-01 | Gestión de usuarios | El sistema debe permitir al Administrador crear, editar y desactivar cuentas de usuarios para los roles Médico, Decano y Administrador del Sistema. Los usuarios no se eliminan físicamente — solo se desactivan para preservar la trazabilidad de las acciones registradas en el log de auditoría. | Alta |
| RF-M8-02 | Gestión de roles y permisos | El sistema debe permitir al Administrador asignar y modificar el rol de cada usuario, controlando los recursos y funcionalidades a los que tiene acceso dentro del sistema. | Alta |
| RF-M8-03 | Gestión de catálogo de carreras y ciclos | El sistema debe permitir al Administrador gestionar el catálogo de las carreras del Instituto Superior Universitario TEC Azuay y sus ciclos correspondientes, con la posibilidad de agregar, editar y desactivar registros cuando la oferta académica institucional cambie. | Media |
| RF-M8-04 | Gestión de catálogo de establecimientos de salud | El sistema debe permitir al Administrador gestionar el catálogo de establecimientos de salud disponibles como destino en las referencias médicas, con la posibilidad de agregar, editar y desactivar registros. | Media |
| RF-M8-05 | Gestión del catálogo CIE-10 | El sistema debe permitir al Administrador gestionar el catálogo de códigos CIE-10, incluyendo la carga masiva inicial de los más de 14.000 códigos y la posibilidad de agregar o desactivar códigos individualmente. | Media |
| RF-M8-06 | Configuración de umbrales de notificaciones | El sistema debe permitir al Administrador configurar los umbrales que activan las alertas automáticas del módulo de notificaciones, incluyendo: número máximo de visitas por paciente en un período, frecuencia de diagnósticos que activa alerta epidemiológica, y volumen de atenciones que activa alerta institucional. | Alta |
| RF-M8-07 | Configuración de reportes automáticos | El sistema debe permitir al Administrador configurar los intervalos de generación de reportes automáticos — semanal, mensual o semestral — así como los filtros aplicados y los destinatarios de cada reporte. | Media |
| RF-M8-08 | Respaldos automáticos de base de datos | El sistema debe generar respaldos automáticos de la base de datos en los intervalos configurados por el Administrador. Los respaldos deben almacenarse cifrados y el Administrador debe poder ejecutar un respaldo manual en cualquier momento. | Alta |

---

## 4. Requisitos No Funcionales

### 4.1 Rendimiento
 
| ID | Requisito | Métrica |
|----|-----------|---------|
| RNF-01 | Tiempo de respuesta de la interfaz web | Las páginas y formularios del sistema web deben cargar en menos de 2 segundos bajo condiciones normales de uso. |
| RNF-02 | Tiempo de respuesta del buscador CIE-10 | El buscador de diagnósticos CIE-10 debe mostrar sugerencias en menos de 500 milisegundos desde que el médico comienza a escribir. |
| RNF-03 | Tiempo de respuesta de la API | El backend debe responder a las solicitudes de la interfaz web y móvil en menos de 1 segundo para operaciones de consulta y menos de 3 segundos para operaciones de escritura bajo carga normal. |
| RNF-04 | Generación de PDF | El sistema debe generar documentos PDF de atención médica y referencia en menos de 5 segundos. |
| RNF-05 | Usuarios concurrentes | El sistema debe soportar un mínimo de 600 usuarios concurrentes sin degradación perceptible del rendimiento, contemplando el crecimiento futuro de la institución. |
 
### 4.2 Seguridad
 
| ID | Requisito | Detalle |
|----|-----------|---------|
| RNF-06 | Cumplimiento OWASP Top 10 | El sistema debe estar protegido contra las diez vulnerabilidades más críticas de seguridad web definidas por OWASP, incluyendo inyección SQL, cross-site scripting (XSS), broken authentication, exposición de datos sensibles y control de acceso roto. |
| RNF-07 | HTTPS obligatorio | Todas las comunicaciones entre clientes y servidor deben realizarse exclusivamente mediante HTTPS. El acceso por HTTP debe ser redirigido automáticamente a HTTPS en todo momento. |
| RNF-08 | Cifrado de datos en reposo | Los datos clínicos sensibles deben estar cifrados en la base de datos mediante AES-256. Los respaldos de base de datos deben almacenarse cifrados. |
| RNF-09 | Escaneo de vulnerabilidades | El sistema debe ser sometido a un escaneo automatizado de vulnerabilidades con OWASP ZAP antes de su despliegue en producción. |
| RNF-10 | Principio de mínimo privilegio | Cada componente del sistema debe operar con los permisos mínimos necesarios para cumplir su función. Ningún componente debe tener acceso a recursos que no necesite. |
 
### 4.3 Usabilidad
 
| ID | Requisito | Detalle |
|----|-----------|---------|
| RNF-11 | Interfaz intuitiva para usuario con nivel técnico básico | La interfaz web debe ser operable por un usuario con conocimientos básicos de software sin necesidad de capacitación técnica. El flujo de registro de una atención médica completa debe poder completarse sin consultar el manual de usuario. |
| RNF-12 | Consistencia visual | La interfaz debe mantener consistencia visual y de interacción en todas las pantallas, utilizando los mismos patrones de navegación, colores, tipografía y componentes en todo el sistema. |
| RNF-13 | Mensajes de error comprensibles | Los mensajes de error y validación deben estar redactados en lenguaje claro y no técnico, indicando al usuario qué ocurrió y cómo resolverlo. |
| RNF-14 | Diseño responsivo | La interfaz web debe adaptarse correctamente a diferentes resoluciones de pantalla, siendo funcional tanto en computadoras de escritorio como en pantallas más pequeñas. |
| RNF-15 | Aplicación móvil nativa | La aplicación móvil para estudiantes debe seguir las guías de diseño de la plataforma correspondiente (Android / iOS), ofreciendo una experiencia de uso coherente con las convenciones del sistema operativo del dispositivo. |
 
### 4.4 Disponibilidad y Confiabilidad
 
| ID | Requisito | Detalle |
|----|-----------|---------|
| RNF-16 | Disponibilidad continua | El sistema debe estar disponible las 24 horas del día, los 7 días de la semana, dado que los estudiantes pueden consultar su historial en cualquier momento desde la aplicación móvil. |
| RNF-17 | Recuperación ante fallos | En caso de caída del sistema, el tiempo de recuperación objetivo (RTO) debe ser el menor posible. El despliegue mediante Docker Compose debe permitir el reinicio de servicios de forma individual sin afectar a los demás componentes. |
| RNF-18 | Persistencia de datos | La base de datos debe operar sobre volúmenes persistentes que garanticen que ningún dato clínico se pierda ante un reinicio o fallo del contenedor. |
| RNF-19 | Integridad transaccional | Todas las operaciones de escritura en la base de datos deben ser transaccionales — en caso de error parcial, la operación completa debe revertirse para evitar datos inconsistentes. |
 
### 4.5 Escalabilidad
 
| ID | Requisito | Detalle |
|----|-----------|---------|
| RNF-20 | Escalabilidad de usuarios | El sistema debe soportar el crecimiento de la población estudiantil del instituto sin requerir cambios estructurales en la arquitectura, desde los 500 estudiantes actuales hasta miles de usuarios en el futuro. |
| RNF-21 | Arquitectura preparada para escalar | La arquitectura de monolito modular debe estar diseñada de forma que sus módulos puedan extraerse como servicios independientes en el futuro si el crecimiento institucional lo requiere, sin necesidad de reescribir el sistema. |
 
### 4.6 Cumplimiento Normativo
 
| ID | Requisito | Detalle |
|----|-----------|---------|
| RNF-22 | Cumplimiento LOPDP | El sistema debe cumplir en su totalidad con las disposiciones de la Ley Orgánica de Protección de Datos Personales del Ecuador, incluyendo cifrado de datos sensibles, control de acceso, trazabilidad y derechos del titular sobre sus datos. |
| RNF-23 | Cumplimiento Acuerdo MSP No. 00000125 | El sistema debe cumplir con los requisitos de integridad, trazabilidad, confidencialidad e inmutabilidad de la historia clínica electrónica establecidos por el Ministerio de Salud Pública del Ecuador. |
| RNF-24 | Trazabilidad completa | Toda acción realizada sobre datos clínicos debe quedar registrada en el log de auditoría de forma completa e inmutable, garantizando la trazabilidad exigida por la normativa vigente. |
 
### 4.7 Mantenibilidad
 
| ID | Requisito | Detalle |
|----|-----------|---------|
| RNF-25 | Stack tecnológico estándar | El sistema debe desarrollarse con tecnologías maduras, ampliamente adoptadas y con soporte a largo plazo — Java 21, Spring Boot, Angular, PostgreSQL — de forma que cualquier desarrollador con conocimientos estándar del stack pueda mantener y extender el sistema. |
| RNF-26 | Arquitectura modular | El código fuente debe organizarse en módulos independientes y bien delimitados, de forma que un cambio en un módulo no genere efectos no deseados en otros. |
| RNF-27 | Migraciones versionadas de base de datos | Todos los cambios en el esquema de la base de datos deben gestionarse mediante migraciones versionadas con Flyway, garantizando que el esquema sea reproducible y auditable en cualquier entorno. |
| RNF-28 | Documentación técnica | El sistema debe incluir documentación técnica actualizada: API documentada con OpenAPI 3, README con instrucciones de instalación y despliegue, y comentarios en el código para lógica no trivial. |

---

## 5. Requisitos de Interfaz Externa

### 5.1 Interfaces de Usuario
 
El sistema provee dos tipos de interfaz gráfica según el rol del usuario:
 
| Interfaz | Usuarios | Descripción |
|----------|----------|-------------|
| Aplicación web | Médico, Decano, Administrador del Sistema | Interfaz SPA desarrollada en Angular 19 con Angular Material y Tailwind CSS. Accesible desde cualquier navegador web moderno. Diseño responsivo adaptable a diferentes resoluciones de pantalla. |
| Aplicación móvil | Estudiante | Aplicación nativa Android desarrollada en Kotlin. Interfaz simplificada de solo lectura orientada a la consulta del historial clínico personal y recepción de notificaciones push. Compatible con Android 8.0 (API 26) en adelante. | 
### 5.2 Interfaces de Hardware
 
| Componente | Descripción |
|------------|-------------|
| Servidor institucional | Servidor local de gama media del Instituto Superior Universitario TEC Azuay, con sistema operativo Linux, mínimo 4 núcleos, 8 GB de RAM y 100 GB de almacenamiento SSD. El sistema se despliega sobre este servidor mediante Docker Compose. |
| Computadora de escritorio o portátil | Dispositivo desde el cual el Médico, el Decano y el Administrador del Sistema acceden a la aplicación web mediante un navegador moderno. No requiere hardware especializado. |
| Dispositivo móvil | Smartphone Android desde el cual el estudiante accede a la aplicación móvil. No se requieren especificaciones de hardware particulares más allá de las requeridas por el sistema operativo del dispositivo. |
 
### 5.3 Interfaces de Software
 
| Sistema externo | Propósito | Interacción |
|-----------------|-----------|-------------|
| PostgreSQL 16 | Base de datos principal del sistema. Almacena todos los datos clínicos, de usuarios, catálogos y logs de auditoría. | El backend se comunica con PostgreSQL mediante Spring Data JPA e Hibernate. |
| Redis 7 | Almacenamiento en caché de sesiones JWT, catálogo CIE-10 y rate limiting. | El backend se comunica con Redis mediante Spring Data Redis. |
| Firebase Cloud Messaging (FCM) | Servicio de Google para el envío de notificaciones push a los dispositivos móviles de los estudiantes. | El backend se comunica con FCM mediante su API HTTP v1. |
| Servidor de correo electrónico (SMTP) | Envío de notificaciones y reportes automáticos por correo electrónico a todos los roles. | El backend se comunica con el servidor de correo mediante JavaMailSender sobre protocolo SMTP con TLS. |
| Prometheus | Recolección de métricas de infraestructura del sistema: tiempo de respuesta, uso de memoria, consultas por segundo y errores. | El backend expone métricas mediante Spring Boot Actuator y Micrometer, que Prometheus recolecta periódicamente. |
| Grafana | Visualización de métricas de infraestructura recolectadas por Prometheus mediante dashboards interactivos. | Grafana consume los datos de Prometheus para mostrar el estado operativo del sistema en tiempo real. |
 
### 5.4 Interfaces de Comunicación
 
| Protocolo | Uso en MEDISTA |
|-----------|------------------|
| HTTPS / TLS 1.3 | Protocolo base para toda comunicación entre clientes (web y móvil) y el servidor. Obligatorio en todas las rutas — el acceso HTTP sin cifrar es redirigido automáticamente. |
| REST sobre HTTP/1.1 | Protocolo de comunicación entre el frontend Angular y el backend Spring Boot para todas las operaciones de la API. |
| WebSocket + STOMP | Protocolo de mensajería bidireccional en tiempo real utilizado para la entrega de notificaciones instantáneas al médico y al Decano desde la aplicación web. |
| FCM HTTP/2 | Protocolo utilizado por el backend para enviar notificaciones push a los dispositivos móviles de los estudiantes a través de Firebase Cloud Messaging. |
| SMTP con STARTTLS | Protocolo de envío de correo electrónico cifrado utilizado para notificaciones y reportes automáticos dirigidos a todos los roles del sistema. |

---

## 6. Restricciones del Sistema

| ID | Categoría | Restricción |
|----|-----------|-------------|
| RS-01 | Tecnológica | El stack tecnológico del sistema es fijo para la presente versión. El backend debe desarrollarse en Java 21 con Spring Boot 4.0.0, el frontend web en Angular 19, la aplicación móvil en Kotlin nativo para Android, la base de datos principal en PostgreSQL 16 y la caché en Redis 7. No se permite la incorporación de tecnologías alternativas sin una revisión formal del documento de arquitectura. |
| RS-02 | Tecnológica | El despliegue del sistema en la presente versión se realiza exclusivamente mediante Docker Compose en el servidor local del instituto. No se contempla el uso de Kubernetes ni de servicios de infraestructura en la nube pública. |
| RS-03 | Tecnológica | Firebase Cloud Messaging es el único servicio externo permitido en el sistema, limitado exclusivamente al envío de notificaciones push a dispositivos móviles. Ningún dato clínico es transmitido a servicios externos. |
| RS-04 | De datos | Todos los datos clínicos del sistema residen exclusivamente en el servidor local del Instituto Superior Universitario TEC Azuay. No se permite la transferencia, replicación ni exportación de datos clínicos individuales fuera de la infraestructura institucional bajo ninguna circunstancia. |
| RS-05 | De datos | Todos los datos clínicos almacenados en la base de datos deben estar cifrados mediante AES-256 a nivel de columna, dada la naturaleza sensible de la información de salud y las obligaciones impuestas por la LOPDP. |
| RS-06 | De datos | Ningún registro clínico del sistema puede ser eliminado físicamente de la base de datos. Las atenciones médicas, referencias y registros de auditoría son inmutables. Los datos que deban dejarse fuera de uso se desactivan mediante un campo de estado, preservando íntegra la trazabilidad histórica. |
| RS-07 | De datos | Los datos de filiación de los pacientes y los registros de usuarios pueden ser editados únicamente por los roles autorizados. Sin embargo, el historial de cada modificación debe quedar registrado en el log de auditoría, garantizando que ningún cambio sea anónimo ni irrastreable. |
| RS-08 | De infraestructura | El sistema depende exclusivamente del servidor local del instituto para su operación. No existe redundancia de infraestructura en la presente versión — si el servidor no está disponible, el sistema no es accesible. Esta restricción debe ser considerada en el plan de mantenimiento institucional. |
| RS-09 | De negocio | El acceso de estudiantes al sistema está restringido a cuentas con dirección de correo electrónico bajo el dominio @tecazuay.edu.ec. No se permite el registro ni la autenticación de estudiantes con dominios de correo externos. |
| RS-10 | De negocio | La generación de datos clínicos en el sistema — registro de pacientes, atenciones médicas y referencias — es responsabilidad exclusiva del rol Médico. Ningún otro rol puede crear ni modificar información clínica. |

---

## 7. Apéndices

### 7.1 Apéndice A — Formato del Formulario de Atención Médica Actual
 
El formulario físico de atención médica del Departamento Médico del Instituto Superior Universitario TEC Azuay, diseñado por la médico de la institución, constituye la referencia visual principal para la digitalización del Módulo de Atención Médica (M2) y para la generación de PDFs institucionales de los módulos M2 y M3.
 
El formulario físico está compuesto por dos páginas:
 
**Página 1:** Datos de filiación del paciente, motivo de consulta, antecedentes familiares y personales, enfermedad actual, signos vitales (incluyendo Glasgow e IMC), y examen físico por sistemas.
 
**Página 2:** Emergencias obstétricas, exámenes complementarios, diagnóstico con código CIE-10, tratamiento, datos del profesional, y formulario de referencia médica.
  
---
 
### 7.2 Apéndice B — Tabla de Requisitos por Rol
 
La siguiente tabla resume el acceso de cada rol a los módulos del sistema. Sirve como referencia rápida para la validación de requisitos con los stakeholders y como guía para la implementación del control de acceso en el Módulo de Seguridad y Auditoría (M7).
 
| Módulo | Médico | Decano | Administrador | Estudiante |
|--------|--------|--------|---------------|------------|
| M1 — Gestión de Pacientes | ✅ Lectura y escritura | ❌ Sin acceso | ❌ Sin acceso | ❌ Sin acceso |
| M2 — Atención Médica | ✅ Lectura y escritura | ❌ Sin acceso | ❌ Sin acceso | ❌ Sin acceso |
| M3 — Referencia Médica | ✅ Lectura y escritura | ❌ Sin acceso | ❌ Sin acceso | ❌ Sin acceso |
| M4 — Historial Clínico | ✅ Vista completa | ❌ Sin acceso | ❌ Sin acceso | ✅ Vista simplificada (solo sus datos) |
| M5 — Dashboard y Reportes | ✅ Solo sus atenciones | ✅ Datos agregados | ❌ Sin acceso | ❌ Sin acceso |
| M6 — Notificaciones | ✅ Alertas clínicas | ✅ Alertas institucionales | ❌ Sin acceso | ✅ Notificaciones personales |
| M7 — Seguridad y Auditoría | ❌ Sin acceso al log | ❌ Sin acceso al log | ✅ Consulta del log | ❌ Sin acceso |
| M8 — Administración | ❌ Sin acceso | ❌ Sin acceso | ✅ Acceso completo | ❌ Sin acceso |
 
**Referencias de la tabla:**
- ✅ Acceso permitido con el alcance indicado.
- ❌ Acceso denegado. Cualquier intento de acceso debe ser rechazado por el sistema con código de error 403 (Forbidden).
 

---

*MEDISTA — Especificación de Requisitos de Software v1.0*
*Instituto Superior Universitario TEC Azuay — Abril 2026*
