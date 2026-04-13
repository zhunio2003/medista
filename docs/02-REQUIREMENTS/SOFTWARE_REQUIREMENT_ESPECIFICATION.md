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

<!-- PENDIENTE -->

### 2.2 Funciones Principales del Sistema

<!-- PENDIENTE -->

### 2.3 Características de los Usuarios

<!-- PENDIENTE -->

### 2.4 Restricciones Generales

<!-- PENDIENTE -->

### 2.5 Suposiciones y Dependencias

<!-- PENDIENTE -->

---

## 3. Requisitos Funcionales

### 3.1 M1 — Gestión de Pacientes

<!-- PENDIENTE -->

### 3.2 M2 — Atención Médica

<!-- PENDIENTE -->

### 3.3 M3 — Referencia Médica

<!-- PENDIENTE -->

### 3.4 M4 — Historial Clínico

<!-- PENDIENTE -->

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
