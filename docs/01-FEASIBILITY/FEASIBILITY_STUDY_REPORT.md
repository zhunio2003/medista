<div align="center">
    <h1>FEASILIBITY STUDY REPORT</h1>
    <h2>MEDISTA</h2>
    <h3>Sistema de Gestión de Atención Médica</h3>
    
</div>

---
 
&nbsp;
 
&nbsp;
 
**Autor:** Miguel Angel Zhunio Remache
 
**Proyecto:** MEDISTA
 
**Departamento:** Departamento Médico — Instituto Superior Universitario TEC Azuay
 
**Versión:** 1.0
 
**Fecha:** Abril 2026
 

---

## Tabla de contenidos

1. [Introducción](#1-introducción)
2. [Descripción del Problema](#2-descripción-del-problema)
3. [Objetivos del Proyecto](#3-objetivos-del-proyecto)
4. [Alcance del Sistema](#4-alcance-del-sistema)
5. [Factibilidad Técnica](#5-factibilidad-técnica)
6. [Factibilidad Operativa](#6-factibilidad-operativa)
7. [Factibilidad Económica](#7-factibilidad-económica)
8. [Factibilidad Legal](#8-factibilidad-legal)
9. [Factibilidad Temporal](#9-factibilidad-temporal)
10. [Análisis de Riesgos](#10-análisis-de-riesgos)
11. [Alternativas Evaluadas](#11-alternativas-evaluadas)
12. [Conclusión General y Recomendación](#12-conclusión-general-y-recomendación)
13. [Anexos](#13-anexos)

---

## 1. Introducción

El presente documento constituye el Estudio de Factibilidad del sistema **MEDISTA**, desarrollado para el Departamento Médico del Instituto Superior Universitario TEC Azuay. Su propósito es evaluar de manera integral y objetiva la viabilidad del proyecto desde las dimensiones técnica, operativa, económica, legal y temporal, proporcionando los fundamentos necesarios para respaldar la decisión de desarrollo e implementación del sistema.

**MEDISTA** es un sistema de gestión de atención médica que digitaliza completamente el proceso de atención clínica del instituto, el cual opera actualmente de forma manual mediante formularios en papel. El sistema contempla una plataforma web para el personal médico y administrativo, y una aplicación móvil para los estudiantes, sirviendo a una población de aproximadamente 500 estudiantes distribuidos en 16 carreras.

Este estudio analiza cada dimensión de factibilidad con base en los requerimientos reales del Departamento Médico, la normativa ecuatoriana aplicable y las condiciones institucionales existentes, con el objetivo de determinar si el proyecto debe ejecutarse, bajo qué condiciones, y con qué consideraciones de riesgo.

---

## 2. Descripción del Problema

El Departamento Médico del Instituto Superior Universitario TEC Azuay gestiona actualmente la totalidad de sus atenciones médicas de forma manual, mediante el llenado físico de un formulario en papel diseñado por el personal médico de la institución. Este formulario recoge, en cada atención, los datos completos de filiación del paciente — nombres, apellidos, cédula, carrera, dirección, entre otros — independientemente de si el estudiante ha sido atendido con anterioridad, lo que genera redundancia sistemática de información y consumo innecesario de tiempo clínico en cada consulta.

Una vez completados, los formularios son almacenados en archivadores físicos, sin ningún mecanismo de organización, búsqueda o análisis. Esta modalidad de almacenamiento expone la información clínica de los estudiantes a riesgos de deterioro, pérdida o acceso no autorizado, comprometiendo la confidencialidad e integridad de datos de salud protegidos por la normativa ecuatoriana vigente.

Las consecuencias de este modelo se manifiestan en tres niveles. A nivel operativo, el personal médico invierte tiempo significativo en el llenado repetitivo de datos que ya fueron registrados en consultas previas, reduciendo el tiempo efectivo dedicado a la atención clínica. A nivel institucional, la ausencia de un sistema centralizado impide realizar análisis estadísticos sobre las atenciones médicas — por período, por carrera, por diagnóstico — limitando la capacidad de detección temprana de tendencias epidemiológicas o situaciones de emergencia sanitaria. A nivel estratégico, la institución carece de información estructurada que respalde la toma de decisiones en materia de salud estudiantil.

---

## 3. Objetivos del Proyecto

### 3.1 Objetivo General
Desarrollar e implementar **MEDISTA**, un sistema de gestión de atención médica web y móvil para el Departamento Médico del Instituto Superior Universitario **TEC Azuay**, que digitalice, centralice y estructure los procesos clínicos actualmente gestionados en papel, mejorando la eficiencia operativa, la seguridad de la información y la capacidad de análisis institucional en materia de salud estudiantil.

### 3.2 Objetivos Específicos
1. Digitalizar el formulario de atención médica eliminando la redundancia de datos mediante el registro único de información de filiación por paciente.
2. Centralizar el historial clínico de cada estudiante en una base de datos estructurada, garantizando acceso inmediato y búsqueda eficiente por parte del personal médico.
3. Implementar un módulo de estadísticas y reportes que permita analizar las atenciones médicas por período, carrera, diagnóstico y otros criterios, apoyando la detección temprana de tendencias epidemiológicas.
4. Garantizar la protección de los datos clínicos de los estudiantes conforme a la Ley Orgánica de Protección de Datos Personales (LOPDP) y la normativa del Ministerio de Salud Pública del Ecuador.
5. Proveer a los estudiantes acceso móvil a su historial clínico personal mediante autenticación con correo institucional.
6. Establecer un sistema de auditoría completo e inmutable que registre toda acción realizada sobre los datos clínicos del sistema.

---

## 4. Alcance del Sistema

### 4.1 Dentro del Alcance
 
MEDISTA-V2 comprende el desarrollo e implementación de los siguientes módulos y componentes:
 
**Módulos funcionales:**
 
| Módulo | Descripción |
|--------|-------------|
| M1 — Gestión de Pacientes | Registro, búsqueda y edición de datos de filiación de estudiantes. |
| M2 — Atención Médica | Digitalización completa del formulario de atención médica, incluyendo signos vitales, examen físico, diagnóstico CIE-10 y tratamiento. |
| M3 — Referencia Médica | Generación de referencias médicas con exportación en PDF de formato idéntico al formulario institucional actual. |
| M4 — Historial Clínico | Visualización cronológica de atenciones, evolución de signos vitales y diagnósticos recurrentes por paciente. |
| M5 — Dashboard Estadístico y Reportes | Indicadores agregados con filtros inteligentes cruzados y exportación a PDF y Excel. |
| M6 — Notificaciones Inteligentes | Alertas clínicas, epidemiológicas e institucionales por canal web, correo electrónico y push móvil. |
| M7 — Seguridad y Auditoría | Autenticación por roles, cifrado de datos clínicos, auditoría inmutable y cumplimiento normativo. |
| M8 — Administración del Sistema | Gestión de usuarios, catálogos, umbrales de notificaciones y respaldos. |
 
**Plataformas:**
- Aplicación web para el personal médico y administrativo.
- Aplicación móvil para estudiantes con acceso de solo lectura a su historial clínico personal.
 
**Usuarios del sistema:**
 
| Rol | Descripción |
|-----|-------------|
| Médico | Gestión clínica completa: registro de pacientes, atenciones, referencias e historial. |
| Decano | Acceso a estadísticas, reportes y supervisión institucional. Solo datos agregados. |
| Administrador del Sistema | Gestión de usuarios, roles, catálogos y configuración técnica del sistema. |
| Estudiante | Solo lectura de su propio historial clínico personal desde la app móvil. |
 
### 4.2 Fuera del Alcance
 
Los siguientes elementos quedan explícitamente excluidos de la presente versión del sistema:
 
| Exclusión | Justificación |
|-----------|---------------|
| Módulo de Agenda y Citas | La arquitectura queda preparada para incorporarlo en una versión futura. |
| Integración con sistemas externos (MSP, IESS, sistema académico) | Fuera del alcance del departamento médico institucional. |
| Gestión de farmacia o inventario de medicamentos | El sistema se limita a la gestión clínica de atenciones médicas. |
| Facturación y gestión de cobros | MEDISTA-V2 no incluye ningún componente financiero. |
| Telemedicina o consultas virtuales | El sistema gestiona atenciones presenciales únicamente. |
 
---

## 5. Factibilidad Técnica

### 5.1 Hardware
 
El sistema será desplegado en el servidor local del Instituto Superior Universitario TEC Azuay, administrado por el área de tecnología de la institución. Para efectos de este estudio se consideran las especificaciones mínimas recomendadas para el despliegue de MEDISTA-V2:
 
| Componente | Especificación Mínima Recomendada |
|------------|-----------------------------------|
| Sistema Operativo | Linux (Ubuntu Server 22.04 LTS o superior) |
| Procesador | 4 núcleos, 2.5 GHz |
| Memoria RAM | 8 GB |
| Almacenamiento | 100 GB SSD |
| Conectividad | Red institucional con acceso interno |
 
Estas especificaciones son suficientes para soportar la población actual de aproximadamente 500 estudiantes y el volumen de atenciones médicas proyectado, con margen de crecimiento. El despliegue se realizará mediante Docker Compose, lo que permite una instalación limpia, aislada y reproducible sobre el sistema operativo existente sin interferir con otros servicios institucionales.
 
### 5.2 Software y Stack Tecnológico
 
El stack tecnológico de MEDISTA fue seleccionado en función de los requerimientos específicos del sistema, detallados en el documento *MEDISTA — Arquitectura Técnica v1.0*. Cada tecnología responde a una necesidad concreta del proyecto:
 
| Requerimiento | Tecnología Seleccionada | Justificación |
|---------------|------------------------|---------------|
| Backend robusto y seguro | Java 21 + Spring Boot 3.3 | Plataforma empresarial con soporte a largo plazo hasta 2031, amplio ecosistema de seguridad y madurez comprobada en sistemas de salud. |
| Protección de datos clínicos | Spring Security + JWT + pgcrypto | Cifrado a nivel de columna, autenticación stateless y control de acceso por roles conforme a la LOPDP. |
| Auditoría inmutable | Hibernate Envers | Registro automático de cada versión de entidades clínicas sin código manual, cumpliendo normativa MSP. |
| Búsqueda CIE-10 eficiente | PostgreSQL pg_trgm + Redis | Búsqueda difusa tolerante a errores tipográficos con respuestas en menos de 50ms mediante caché. |
| Interfaz web profesional | Angular 19 + Angular Material | Framework empresarial con tipado fuerte, formularios reactivos complejos y componentes UI accesibles. |
| App móvil para estudiantes | Ionic + Capacitor | Reutiliza el conocimiento de Angular del equipo, reduciendo curva de aprendizaje y tiempo de desarrollo. |
| Generación de PDFs institucionales | JasperReports | Permite replicar con precisión el formato del formulario físico actual mediante plantillas diseñadas visualmente. |
| Despliegue reproducible | Docker Compose + Nginx | Entorno idéntico entre desarrollo y producción, instalación simplificada en el servidor institucional. |
 
### 5.3 Conocimiento Técnico
 
El sistema es desarrollado por un único desarrollador con formación en el programa de Desarrollo de Software del Instituto Superior Universitario TEC Azuay. El desarrollador cuenta con conocimiento del stack tecnológico definido y la capacidad de adquirir las competencias específicas requeridas durante el proceso de desarrollo. La arquitectura de monolito modular fue elegida deliberadamente para reducir la complejidad operativa frente a alternativas como microservicios, alineando el alcance técnico del sistema con las capacidades del equipo de desarrollo.

---

## 6. Factibilidad Operativa

### 6.1 Disposición de los Usuarios
El proyecto cuenta con una condición operativa favorable desde su origen: el sistema responde a una necesidad identificada y solicitada directamente por la usuaria principal. La médico del Departamento Médico ha manifestado interés en la digitalización del proceso de atención clínica con anterioridad al inicio del proyecto, lo que garantiza una disposición activa hacia la adopción del sistema y elimina la resistencia al cambio como factor de riesgo operativo.

El Decano y las autoridades institucionales del Instituto Superior Universitario TEC Azuay tienen pleno conocimiento del proyecto y han expresado su aprobación formal. Este respaldo institucional es determinante para garantizar las condiciones necesarias de despliegue, acceso al servidor y coordinación con el área de tecnología de la institución.

Los estudiantes, como usuarios de la aplicación móvil, interactuarán con una interfaz de solo lectura de diseño simple e intuitivo, limitada a la consulta de su historial clínico personal y la recepción de notificaciones. El nivel de complejidad de interacción es mínimo, por lo que no se anticipa resistencia ni dificultad de adopción en este grupo.

### 6.2 Capacitación Requerida
La capacitación necesaria para la operación de MEDISTA-V2 se limita al uso funcional del sistema y no involucra conocimientos técnicos de desarrollo, configuración o mantenimiento de software. Se contempla la entrega de los siguientes recursos:

| Recurso | Destinatario | Contenido |
|---|---|---|
| Manual de usuario — Módulo Médico | Médico | Registro de pacientes, creación de atenciones, generación de referencias, consulta de historial. |
| Manual de usuario — Módulo Administrativo | Decano y Administrador del Sistema | Consulta de dashboards, exportación de reportes, gestión de usuarios y catálogos. |
| Manual de usuario — App Móvil | Estudiantes | Inicio de sesión, consulta de historial personal, gestión de notificaciones. |
| Sesión de capacitación presencial | Médico y Administrador del Sistema | Demostración práctica del sistema en el entorno de producción previo al lanzamiento. |

> El mantenimiento técnico del sistema — actualizaciones, respaldos y administración del servidor — estará a cargo del área de tecnología de la institución, por lo que no forma parte del alcance de capacitación del presente proyecto.

---

## 7. Factibilidad Económica

### 7.1 Costos del Proyecto

MEDISTA-V2 es desarrollado en el marco del programa académico de Desarrollo de Software del Instituto Superior Universitario TEC Azuay, lo que determina una estructura de costos significativamente reducida frente a un proyecto de desarrollo comercial equivalente.

**Costos de desarrollo:**

| Rubro | Detalle | Costo |
|-------|---------|-------|
| Desarrollo de software | Proyecto académico desarrollado por estudiante del programa. Sin costo de mano de obra comercial. | $0.00 |
| Licencias de software | Todo el stack tecnológico utiliza herramientas de código abierto y licencias gratuitas (Java, Spring Boot, Angular, PostgreSQL, Docker, etc.). | $0.00 |
| Infraestructura de desarrollo | Equipo personal del desarrollador. Sin costo adicional. | $0.00 |

**Costos de despliegue y operación:**

| Rubro | Detalle | Costo |
|-------|---------|-------|
| Servidor de producción | Servidor local institucional existente del TEC Azuay. Sin costo adicional. | $0.00 |
| Hosting o servicios en la nube | No aplica. El sistema se despliega en infraestructura local del instituto. | $0.00 |
| Dominio o certificado SSL | Uso de certificado institucional o Let's Encrypt (gratuito). | $0.00 |
| Mantenimiento técnico | A cargo del área de tecnología de la institución. | $0.00 |

**Costo total del proyecto: $0.00**

### 7.2 Beneficios del Proyecto

Aunque el proyecto no genera retorno económico directo, los beneficios institucionales son cuantificables y justifican ampliamente su desarrollo e implementación.

**Beneficios operativos:**

| Beneficio | Impacto |
|-----------|---------|
| Eliminación del llenado repetitivo de datos de filiación | Reducción del tiempo invertido por consulta, incrementando el tiempo efectivo de atención clínica. |
| Búsqueda inmediata del historial clínico | Eliminación del tiempo de búsqueda manual en archivadores físicos. |
| Generación automática de PDFs de referencia médica | Reducción del tiempo de elaboración de documentos clínicos. |

**Beneficios institucionales:**

| Beneficio | Impacto |
|-----------|---------|
| Centralización y respaldo de datos clínicos | Eliminación del riesgo de pérdida o deterioro de información por almacenamiento en papel. |
| Cumplimiento de la LOPDP | Reducción del riesgo legal asociado al manejo inadecuado de datos de salud. |
| Capacidad de análisis estadístico | Habilitación de toma de decisiones basada en datos para autoridades institucionales. |
| Detección temprana de tendencias epidemiológicas | Mejora de la capacidad de respuesta ante situaciones de emergencia sanitaria. |

### 7.3 Análisis Costo-Beneficio

Con un costo total de desarrollo e implementación de $0.00 y un conjunto de beneficios operativos, institucionales y normativos directamente alineados con las necesidades del Departamento Médico, el proyecto presenta una relación costo-beneficio ampliamente favorable. La inversión requerida se limita al tiempo de desarrollo del equipo académico, mientras que los beneficios impactan positivamente la operación diaria del departamento, la seguridad de la información clínica y el cumplimiento de la normativa ecuatoriana vigente.

---

## 8. Factibilidad Legal

### 8.1 Normativas Aplicables

MEDISTA maneja datos de salud de personas naturales, clasificados como datos sensibles bajo la legislación ecuatoriana vigente. El sistema está sujeto al cumplimiento de las siguientes normativas:

| Normativa | Descripción |
|-----------|-------------|
| Ley Orgánica de Protección de Datos Personales (LOPDP) | Regula la recopilación, almacenamiento, procesamiento y protección de datos personales en Ecuador, con disposiciones especiales para datos sensibles de salud. |
| Acuerdo Ministerial MSP No. 00000125 | Regula el manejo de historias clínicas en formato electrónico, estableciendo requisitos de integridad, trazabilidad, confidencialidad e inmutabilidad de los registros clínicos. |
| Reglamento Interno Institucional — TEC Azuay | Establece las políticas de manejo y acceso a datos de los estudiantes dentro de la institución. |

### 8.2 Cumplimiento por Normativa

**Ley Orgánica de Protección de Datos Personales (LOPDP)**

MEDISTA cumple con la LOPDP mediante las siguientes medidas implementadas en el sistema:

| Requerimiento LOPDP | Implementación en MEDISTA-V2 |
|--------------------|------------------------------|
| Cifrado de datos sensibles de salud | Cifrado a nivel de columna mediante pgcrypto (AES-256) para diagnósticos, antecedentes y datos obstétricos almacenados en la base de datos. |
| Control de acceso y gestión de roles | Sistema de autenticación con JWT y control de acceso por roles (Médico, Decano, Administrador, Estudiante) mediante Spring Security. Cada rol accede únicamente a la información que le corresponde. |
| Derecho del titular a acceder a sus datos | Los estudiantes pueden consultar su historial clínico personal desde la aplicación móvil, autenticándose con su correo institucional. |
| Consentimiento para el tratamiento de datos | El registro del estudiante en el sistema es realizado por el personal médico en el contexto de una atención clínica presencial, enmarcada en la relación institucional médico-paciente. |
| Restricción de exportación y acceso no autorizado | La exportación de datos está restringida por rol. El Decano accede únicamente a datos agregados, nunca a expedientes individuales. El acceso no autorizado es bloqueado mediante autenticación obligatoria y sesiones con expiración automática. |

**Acuerdo Ministerial MSP No. 00000125 — Historia Clínica Electrónica**

| Requerimiento MSP | Implementación en MEDISTA-V2 |
|------------------|------------------------------|
| Inmutabilidad de registros clínicos | Las atenciones médicas no se eliminan físicamente de la base de datos. Se implementa soft delete mediante campo de estado en todas las entidades, preservando la integridad del historial clínico. |
| Trazabilidad completa de cambios | Auditoría automática mediante Hibernate Envers, que registra cada modificación sobre entidades clínicas: qué cambió, quién lo cambió y cuándo. |
| Registro de accesos y acciones | Log de auditoría que almacena cada acción realizada en el sistema: usuario, acción ejecutada, timestamp e IP de origen. |
| Integridad, fecha y firma del profesional | Cada atención médica registra automáticamente la fecha y hora de creación, el nombre del profesional responsable y su código MSP, garantizando la trazabilidad del acto médico. |

**Reglamento Interno Institucional — TEC Azuay**

| Requerimiento Institucional | Implementación en MEDISTA-V2 |
|----------------------------|------------------------------|
| Acceso restringido a datos de estudiantes | Los datos clínicos individuales son accesibles únicamente por el Médico. El Decano y el Administrador del Sistema no tienen acceso a expedientes clínicos individuales. |
| Gestión de usuarios institucionales | El Administrador del Sistema gestiona las cuentas de acceso, garantizando que solo personal autorizado por la institución tenga credenciales activas en el sistema. |

### 8.3 Conclusión Legal

MEDISTA no presenta impedimentos legales para su desarrollo e implementación. El sistema ha sido diseñado desde su arquitectura base para cumplir con cada requerimiento de la LOPDP y el Acuerdo Ministerial MSP No. 00000125, integrando el cumplimiento normativo como parte estructural del sistema y no como una adición posterior.

---

## 9. Factibilidad Temporal

### 9.1 Período de Desarrollo

El proyecto MEDISTA se desarrolla en un período de 32 días continuos, comprendido entre el 1 de abril y el 2 de mayo de 2026. El desarrollador dedica jornada completa al proyecto durante este período, sin compromisos académicos paralelos que comprometan la disponibilidad de tiempo.

### 9.2 Cronograma por Fases

El desarrollo sigue la metodología Waterfall, con las fases distribuidas de la siguiente manera:

| Fase | Período | Duración | Entregables |
|------|---------|----------|-------------|
| Fase 1 — Requisitos | 1 - 4 de abril | 4 días | Especificación de Requisitos de Software (ERS), casos de uso, matriz de trazabilidad. |
| Fase 2 — Diseño | 5 - 9 de abril | 5 días | Arquitectura del sistema, modelo de datos, contratos de API REST, wireframes. |
| Fase 3 — Implementación | 10 - 25 de abril | 16 días | Código fuente completo, funcional y documentado de todos los módulos del sistema. |
| Fase 4 — Verificación | 26 - 30 de abril | 5 días | Plan de pruebas, casos de prueba, reporte de resultados, UAT con la doctora. |
| Fase 5 — Despliegue | 1 - 2 de mayo | 2 días | Sistema en producción, manuales de usuario, documentación de despliegue. |

### 9.3 Distribución de la Implementación por Módulo

Dado que la Fase 3 concentra el mayor esfuerzo del proyecto, se establece el siguiente orden de implementación por módulo, priorizando el núcleo clínico del sistema:

| Orden | Módulo | Justificación |
|-------|--------|---------------|
| 1 | M7 — Seguridad y Auditoría | Base transversal del sistema. Debe estar implementada antes que cualquier módulo funcional. |
| 2 | M1 — Gestión de Pacientes | Prerequisito de todos los módulos clínicos. Sin pacientes registrados no existe atención médica. |
| 3 | M2 — Atención Médica | Núcleo del sistema. Es el módulo de mayor complejidad y valor funcional. |
| 4 | M3 — Referencia Médica | Depende directamente de una atención médica existente. |
| 5 | M4 — Historial Clínico | Depende de que existan atenciones registradas para visualizar. |
| 6 | M5 — Dashboard y Reportes | Depende de datos acumulados de atenciones para generar estadísticas. |
| 7 | M6 — Notificaciones | Capa transversal que se integra una vez los módulos principales están estables. |
| 8 | M8 — Administración | Módulo de soporte. Se implementa al final cuando el resto del sistema está definido. |

### 9.4 Conclusión Temporal

El cronograma establecido es viable dado que el desarrollador cuenta con disponibilidad completa durante el período de desarrollo y la arquitectura de monolito modular reduce significativamente la complejidad operativa frente a alternativas distribuidas. El orden de implementación por módulo garantiza que el núcleo clínico del sistema esté funcional en las primeras etapas, permitiendo validaciones tempranas con la usuaria principal antes de la entrega final.

---

## 10. Análisis de Riesgos

### 10.1 Metodología de Evaluación

Cada riesgo identificado se evalúa en dos dimensiones: **probabilidad** (Alta / Media / Baja) e **impacto** (Alto / Medio / Bajo), determinando su nivel de criticidad y la estrategia de mitigación correspondiente.

### 10.2 Matriz de Riesgos

| # | Riesgo | Probabilidad | Impacto | Criticidad | Mitigación |
|---|--------|-------------|---------|------------|------------|
| R1 | Implementación incorrecta de seguridad en datos clínicos sensibles | Media | Alto | **Alta** | Uso de librerías probadas y maduras (Spring Security, pgcrypto, JWT). Revisión exhaustiva de cada capa de seguridad durante la fase de verificación. Validación contra checklist OWASP. |
| R2 | Curva de aprendizaje en tecnologías no dominadas (JasperReports, Hibernate Envers, Redis, Firebase Cloud Messaging) | Alta | Medio | **Alta** | Investigación y prototipado de cada tecnología desconocida durante la Fase de Diseño, antes de iniciar la implementación. Arquitectura modular que permite aislar problemas por módulo. |
| R3 | Subestimación del tiempo de implementación del Módulo de Atención Médica | Media | Alto | **Alta** | Es el módulo de mayor complejidad del sistema. Se prioriza en el orden de implementación para detectar problemas temprano. Si hay retraso, los módulos de menor criticidad (Notificaciones, Administración) pueden simplificarse sin afectar el núcleo clínico. |
| R4 | Retrasos en validaciones con la usuaria principal (doctora) | Baja | Medio | **Baja** | La doctora ha confirmado disponibilidad permanente, incluso de forma remota. Las validaciones están planificadas en puntos específicos del cronograma y no bloquean el desarrollo continuo. |
| R5 | Incompatibilidad del sistema con el servidor institucional | Baja | Alto | **Media** | El despliegue mediante Docker Compose garantiza un entorno reproducible e independiente del sistema operativo del servidor. Se realizará una validación del entorno de producción antes de la fase de despliegue. |
| R6 | Pérdida de datos o corrupción durante el desarrollo | Baja | Alto | **Media** | Control de versiones con Git y repositorio remoto en GitHub. Respaldos periódicos de la base de datos de desarrollo. |
| R7 | Alcance mal definido que genere solicitudes de cambio tardías | Baja | Alto | **Media** | El alcance está formalmente definido y documentado en el presente estudio. Cualquier solicitud de cambio posterior a la aprobación de la ERS seguirá un proceso formal de control de cambios. |

### 10.3 Riesgos Descartados

Los siguientes riesgos comunes en proyectos de software fueron evaluados y descartados para MEDISTA:

| Riesgo Descartado | Justificación |
|-------------------|---------------|
| Rotación del equipo de desarrollo | Proyecto de desarrollador único. No aplica. |
| Problemas de comunicación con el cliente | La doctora tiene disposición permanente y es la solicitante original del sistema. |
| Costos fuera de presupuesto | Costo total del proyecto es $0.00. No hay presupuesto que gestionar. |

---

## 11. Alternativas Evaluadas

### 11.1 Metodología de Evaluación

Previo a la decisión de desarrollar MEDISTA como sistema a medida, se evaluaron conceptualmente tres alternativas para resolver la problemática del Departamento Médico del Instituto Superior Universitario TEC Azuay. Cada alternativa fue analizada en función de cinco criterios: adaptabilidad al contexto institucional, cumplimiento normativo ecuatoriano, costo, viabilidad operativa y sostenibilidad a largo plazo.

### 11.2 Alternativas

**Alternativa 1 — Mantener el proceso manual en papel**

| Criterio | Evaluación |
|----------|------------|
| Adaptabilidad | Alta. El proceso actual ya está establecido. |
| Cumplimiento normativo | Bajo. El manejo en papel no garantiza los estándares de seguridad, trazabilidad e integridad exigidos por la LOPDP y el Acuerdo Ministerial MSP No. 00000125. |
| Costo | Nulo. No requiere inversión tecnológica. |
| Viabilidad operativa | Limitada. No permite análisis estadístico, búsqueda eficiente ni escalabilidad. |
| Sostenibilidad | Baja. El crecimiento institucional incrementará la complejidad del proceso manual hasta hacerlo inmanejable. |

**Veredicto:** Descartada. No resuelve el problema y expone a la institución a riesgos normativos y operativos crecientes.

---

**Alternativa 2 — Adoptar un sistema médico genérico existente**

Existen soluciones de gestión médica disponibles en el mercado, tanto de pago como de código abierto. Sin embargo, su evaluación frente al contexto específico del TEC Azuay revela limitaciones críticas:

| Criterio | Evaluación |
|----------|------------|
| Adaptabilidad | Baja. Los sistemas genéricos están diseñados para clínicas y hospitales de uso general, no para departamentos médicos universitarios con un formulario institucional específico y roles particulares como el Decano. |
| Cumplimiento normativo | Incierto. La mayoría de sistemas disponibles no están diseñados bajo la normativa ecuatoriana (LOPDP, MSP), requiriendo adaptaciones costosas o dejando brechas de cumplimiento. |
| Costo | Variable. Las soluciones comerciales implican costos de licenciamiento, implementación y mantenimiento que la institución no está en condición de asumir. |
| Viabilidad operativa | Media. Requeriría capacitación en una herramienta externa y adaptación de los procesos institucionales al sistema, en lugar de que el sistema se adapte a los procesos. |
| Sostenibilidad | Dependiente del proveedor. Actualizaciones, soporte y continuidad quedan fuera del control institucional. |

**Veredicto:** Descartada. Ningún sistema genérico disponible replica el formulario institucional específico de la doctora, cumple con la normativa ecuatoriana de forma nativa, ni se adapta a la estructura de roles del TEC Azuay sin un desarrollo adicional equivalente al de una solución a medida.

---

**Alternativa 3 — Desarrollar MEDISTA a medida SELECCIONADA**

| Criterio | Evaluación |
|----------|------------|
| Adaptabilidad | Alta. El sistema es diseñado específicamente para el Departamento Médico del TEC Azuay, replicando el formulario institucional existente y respetando los procesos ya establecidos. |
| Cumplimiento normativo | Alto. La arquitectura incorpora desde su base los requerimientos de la LOPDP y el Acuerdo Ministerial MSP No. 00000125. |
| Costo | Nulo. Desarrollado como proyecto académico con stack de código abierto y despliegue en infraestructura institucional existente. |
| Viabilidad operativa | Alta. La usuaria principal solicitó el sistema y está disponible para validaciones durante el desarrollo. |
| Sostenibilidad | Alta. El código fuente pertenece a la institución, es mantenible por cualquier desarrollador y está documentado para facilitar su evolución futura. |

**Veredicto:** Seleccionada. Es la única alternativa que resuelve completamente el problema, cumple con la normativa ecuatoriana, se adapta al contexto institucional específico y es viable dentro de las condiciones del proyecto.

### 11.3 Resumen Comparativo

| Criterio | Papel | Sistema Genérico | MEDISTA a Medida |
|----------|-------|-----------------|---------------------|
| Adaptabilidad institucional | ✅ | ❌ | ✅ |
| Cumplimiento LOPDP y MSP | ❌ | ⚠️ | ✅ |
| Costo | ✅ | ❌ | ✅ |
| Capacidad de análisis estadístico | ❌ | ⚠️ | ✅ |
| Sostenibilidad a largo plazo | ❌ | ⚠️ | ✅ |
| **Decisión** | ❌ Descartada | ❌ Descartada | ✅ Seleccionada | 

---

## 12. Conclusión General y Recomendación

### 12.1 Resumen de Factibilidad

El presente estudio evaluó MEDISTA desde seis dimensiones de factibilidad, obteniendo los siguientes resultados:

| Dimensión | Resultado | Observaciones |
|-----------|-----------|---------------|
| Factibilidad Técnica | ✅ Viable | Stack tecnológico maduro y probado. Infraestructura institucional suficiente. Arquitectura alineada con la complejidad del proyecto. |
| Factibilidad Operativa | ✅ Viable | Respaldo institucional total. Usuaria principal solicitante del sistema. Resistencia al cambio nula. |
| Factibilidad Económica | ✅ Viable | Costo total de $0.00. Beneficios operativos, institucionales y normativos ampliamente superiores a la inversión requerida. |
| Factibilidad Legal | ✅ Viable | Sistema diseñado desde su arquitectura base para cumplir con la LOPDP y el Acuerdo Ministerial MSP No. 00000125. Sin impedimentos legales. |
| Factibilidad Temporal | ✅ Viable | Cronograma de 32 días con dedicación completa del desarrollador. Fases distribuidas de forma profesional y realista. |
| Análisis de Riesgos | ✅ Controlado | Riesgos identificados, evaluados y con estrategias de mitigación definidas. Ningún riesgo representa un impedimento para la ejecución del proyecto. |

### 12.2 Conclusión General

MEDISTA-V2 es un proyecto técnica, operativa, económica, legal y temporalmente viable. Las seis dimensiones evaluadas arrojan resultados favorables, sin que ninguna represente un impedimento para su desarrollo e implementación.

Más allá de su viabilidad, el proyecto responde a una necesidad institucional real, concreta y urgente. El Departamento Médico del Instituto Superior Universitario TEC Azuay opera hoy con un proceso completamente manual que expone a la institución a riesgos normativos crecientes, limita la capacidad de análisis y toma de decisiones en materia de salud estudiantil, y consume tiempo clínico valioso en tareas administrativas repetitivas. MEDISTA resuelve cada uno de estos problemas de forma integral, sostenible y sin costo para la institución.

La evaluación de alternativas confirma que el desarrollo a medida es la única solución que combina adaptabilidad al contexto institucional específico, cumplimiento nativo de la normativa ecuatoriana y viabilidad económica plena.

### 12.3 Recomendación

Se recomienda proceder con el desarrollo e implementación de MEDISTA de forma inmediata, siguiendo la metodología Waterfall definida y el cronograma establecido en el presente estudio.

La ejecución del proyecto no solo es viable — es estratégicamente necesaria para que el Instituto Superior Universitario TEC Azuay cumpla con sus obligaciones normativas en materia de protección de datos de salud, modernice la gestión de su Departamento Médico y establezca las bases de un sistema de información clínica escalable y sostenible a largo plazo.

---

## 13. Anexos
