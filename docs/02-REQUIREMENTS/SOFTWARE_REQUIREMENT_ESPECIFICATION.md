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

<!-- PENDIENTE -->

### 3.6 M6 — Notificaciones Inteligentes

<!-- PENDIENTE -->

### 3.7 M7 — Seguridad y Auditoría

<!-- PENDIENTE -->

### 3.8 M8 — Administración del Sistema

<!-- PENDIENTE -->

---

## 4. Requisitos No Funcionales

### 4.1 Rendimiento

<!-- PENDIENTE -->

### 4.2 Seguridad

<!-- PENDIENTE -->

### 4.3 Usabilidad

<!-- PENDIENTE -->

### 4.4 Disponibilidad y Confiabilidad

<!-- PENDIENTE -->

### 4.5 Escalabilidad

<!-- PENDIENTE -->

### 4.6 Cumplimiento Normativo

<!-- PENDIENTE -->

### 4.7 Mantenibilidad

<!-- PENDIENTE -->

---

## 5. Requisitos de Interfaz Externa

### 5.1 Interfaces de Usuario

<!-- PENDIENTE -->

### 5.2 Interfaces de Hardware

<!-- PENDIENTE -->

### 5.3 Interfaces de Software

<!-- PENDIENTE -->

### 5.4 Interfaces de Comunicación

<!-- PENDIENTE -->

---

## 6. Restricciones del Sistema

<!-- PENDIENTE -->

---

## 7. Apéndices

### 7.1 Apéndice A — Formato del Formulario de Atención Médica Actual

<!-- PENDIENTE -->

### 7.2 Apéndice B — Tabla de Requisitos por Rol

<!-- PENDIENTE -->

---

*MEDISTA-V2 — Especificación de Requisitos de Software v1.0*
*Instituto Superior Universitario TEC Azuay — Abril 2026*
