# MEDISTA-V2 — Sistema de Gestión de Atención Médica

## Instituto Superior Universitario TEC Azuay

### Departamento Médico

---

## 1. Descripción General

**Nombre del sistema:** MEDISTA-V2 — Sistema de Gestión de Atención Médica

**Institución:** Instituto Superior Universitario TEC Azuay

**Departamento:** Departamento Médico

**Propósito:** Digitalizar completamente el proceso de atención médica del departamento médico del instituto, reemplazando el formulario en papel por un sistema web y móvil que permita registrar, consultar, analizar y resguardar la información clínica de los estudiantes.

**Arquitectura:** Monolito modular — una sola aplicación organizada internamente por módulos bien separados, preparada para extraer módulos a servicios independientes si se requiere en el futuro.

**Población actual:** ~500 estudiantes, 16 carreras, 1 médico. Diseñado para escalar a miles de usuarios.

---

## 2. Problema que Resuelve

- Eliminar el manejo en papel de los formularios de atención médica.
- Centralizar la información clínica de todos los estudiantes.
- Permitir análisis estadístico y toma de decisiones basada en datos.
- Cumplir con la normativa ecuatoriana de protección de datos de salud.
- Escalar el sistema a futuro sin necesidad de reescribir.

---

## 3. Roles del Sistema

### 3.1 Médico (Rol principal)

- **Plataforma:** Web
- **Autenticación:** Credenciales propias (usuario y contraseña)
- **Acceso:** Gestión clínica completa
- **Funciones:** Registrar pacientes nuevos, crear atenciones médicas, generar referencias médicas, ver historiales clínicos, buscar pacientes, recibir alertas clínicas, ver estadísticas de sus atenciones.
- **Escalabilidad:** Si se incorporan más médicos, cada uno tendrá su cuenta con el mismo nivel de acceso clínico.

### 3.2 Administrador / Decano

- **Plataforma:** Web
- **Autenticación:** Credenciales propias (usuario y contraseña)
- **Acceso:** Estadísticas, reportes y supervisión institucional
- **Funciones:** Dashboards estadísticos con filtros inteligentes, reportes exportables, alertas institucionales, gestión de usuarios y catálogos.
- **Restricción:** NO tiene acceso a expedientes clínicos individuales. Solo ve datos agregados (por normativa de protección de datos de salud).

### 3.3 Estudiante

- **Plataforma:** App móvil
- **Autenticación:** Correo institucional (@tecazuay.edu.ec, formato: nombre.apellido.est@tecazuay.edu.ec). Solo se permite este dominio; cualquier otro correo es rechazado.
- **Acceso:** Solo lectura de sus propias atenciones médicas.
- **Funciones:** Ver historial simplificado de atenciones (fecha, motivo, diagnóstico, tratamiento), recibir notificaciones.
- **Restricción:** No edita nada, no llena datos. El médico es quien registra al estudiante en su primera visita.

---

## 4. Flujo Principal de Operación

1. Un estudiante llega al departamento médico.
2. Si es **primera vez**, el médico lo registra en el sistema con todos los datos de filiación.
3. Si **ya fue registrado**, el médico busca por cédula o nombre y ya tiene todos sus datos.
4. El médico crea una nueva atención médica (formulario digitalizado).
5. Si necesita derivar, genera una referencia médica con PDF descargable.
6. El estudiante puede ver sus atenciones desde la app móvil iniciando sesión con su correo institucional.
7. El decano consulta dashboards y reportes desde la web.

---

## 5. Módulos del Sistema

---

### MÓDULO 1 — Gestión de Pacientes (Estudiantes)

**Propósito:** Registrar y administrar los datos personales de los estudiantes que acuden al departamento médico.

**Funcionalidades:**

- Registro de paciente nuevo por parte del médico con los campos de filiación:
  - Nombres
  - Apellidos
  - Carrera
  - Ciclo
  - Nº Cédula
  - Dirección de domicilio
  - Barrio
  - Parroquia
  - Cantón
  - Provincia
  - Teléfono
  - Fecha de nacimiento
  - Lugar de nacimiento
  - País
  - Edad (calculada automáticamente)
  - Género
  - Estado civil (Soltero, Casado, Viudo, Divorciado, Unión Libre)
  - Tipo de sangre
- Búsqueda rápida de pacientes por cédula, nombres o apellidos.
- Edición de datos personales cuando cambian (cambio de carrera, ciclo, dirección, etc.).
- Perfil del paciente con acceso directo a su historial clínico completo.

**Reglas de negocio:**

- La cédula es el identificador único; no puede haber duplicados.
- Solo el médico puede crear y editar pacientes.
- La edad se calcula automáticamente a partir de la fecha de nacimiento.

---

### MÓDULO 2 — Atención Médica

**Propósito:** Digitalizar el formulario de atención médica completo. Es el corazón del sistema.

**Funcionalidades:**

- Crear nueva atención médica asociada a un paciente ya registrado.
- La fecha se registra automáticamente.

**Campos del formulario:**

**2.1 Motivo de consulta**
- Texto libre.

**2.2 Antecedentes familiares y personales**
- Categorías: Alergias, Clínico, Ginecológico, Traumatológico, Quirúrgico, Farmacológico.
- Opción de "No aplica".
- Los antecedentes se acumulan entre consultas: si en la primera visita el estudiante reporta alergia a penicilina, eso aparece precargado en consultas posteriores y puede actualizarse.

**2.3 Enfermedad actual**
- Texto libre.

**2.4 Signos vitales**
- Presión arterial (sistólica / diastólica)
- Peso (kg)
- Talla (m)
- IMC (calculado automáticamente: peso / talla²)
- Frecuencia cardíaca (FC)
- Frecuencia respiratoria (FR)
- Temperatura (T°)
- Saturación de oxígeno (Sat.O2)
- Escala de Glasgow:
  - Ocular
  - Verbal
  - Motora
  - Total (calculado automáticamente: ocular + verbal + motora)
- Llenado capilar
- Reflejo pupilar

**2.5 Examen físico por sistemas**
- Piel y faneras
- Cabeza
- Cuello
- Tórax
- Corazón
- Abdomen
- Región inguinal
- Miembros superiores
- Miembros inferiores
- Cada sistema puede marcarse como **Normal (N)** o describir hallazgos anormales en texto libre.

**2.6 Emergencias obstétricas** *(sección condicional — aparece solo cuando aplica)*
- Menarca
- Ritmo menstrual (Regular / Irregular)
- Ciclos
- Fecha de última menstruación (FUM)
- Inicio de vida sexual activa (IVSA)
- Nº de parejas sexuales
- G (gestaciones) / A (abortos) / P (partos) / C (cesáreas)
- Dismenorrea (marcar si aplica)
- Mastodinia (marcar si aplica)
- En caso de embarazo:
  - Fecha probable de parto (FPP)
  - Semanas de gestación (SG)
  - Controles
  - Inmunizaciones

**2.7 Exámenes complementarios**
- Opción de "No aplica" o texto libre.

**2.8 Diagnóstico**
- Buscador integrado del catálogo CIE-10.
- El médico escribe el nombre de la enfermedad y el sistema sugiere opciones con su código.
- Marcar si es Presuntivo o Definitivo.

**2.9 Tratamiento**
- Texto libre.

**2.10 Datos del profesional**
- Nombre del profesional que atiende (precargado del usuario logueado).
- Código MSP.
- Timestamp automático de la consulta.

**Reglas de negocio:**

- No se puede crear una atención sin un paciente previamente registrado.
- IMC y total de Glasgow se calculan automáticamente.
- Los antecedentes se heredan de consultas anteriores y pueden actualizarse.
- Una atención guardada **no se elimina**; solo se puede agregar una nota de corrección (por auditoría y normativa).

---

### MÓDULO 3 — Referencia Médica

**Propósito:** Gestionar las derivaciones cuando el médico necesita referir al estudiante a otro establecimiento de salud.

**Funcionalidades:**

- Generar referencia médica desde una atención médica existente.
- Campos precargados:
  - Nombre de la institución (TEC Azuay)
  - Cédula del paciente
  - Servicio (Departamento Médico)
- Campos de la referencia:
  - Entidad del sistema
  - Establecimiento de salud destino
  - Servicio
  - Especialidad
  - Fecha
  - Motivo de referencia: Limitada capacidad resolutiva / Falta de profesional / Otros
  - Resumen del cuadro clínico (texto libre)
  - Hallazgos relevantes (texto libre)
  - Diagnóstico 1 y 2 con código CIE-10 (presuntivo o definitivo)
  - Nombre del profesional, código MSP, firma
- Generación de **PDF con formato idéntico al formulario actual** de la doctora.

**Reglas de negocio:**

- Toda referencia debe estar vinculada a una atención médica.
- Se notifica al estudiante y al decano cuando se genera una referencia.

---

### MÓDULO 4 — Historial Clínico

**Propósito:** Visualizar la evolución médica completa de un paciente a lo largo del tiempo.

**Funcionalidades:**

- Línea de tiempo cronológica de todas las atenciones médicas del paciente.
- Vista rápida de cada atención (diagnóstico y fecha), expandible al detalle completo.
- Gráficas de evolución de signos vitales (peso, IMC, presión arterial a lo largo del tiempo).
- Listado de diagnósticos recurrentes.
- Historial de referencias médicas realizadas.
- Filtros por rango de fechas y por tipo de diagnóstico.

**Reglas de negocio:**

- Solo el médico ve el historial completo.
- El estudiante en la app móvil ve versión simplificada: fecha, motivo, diagnóstico, tratamiento.

---

### MÓDULO 5 — Dashboard Estadístico y Reportes

**Propósito:** Proporcionar inteligencia de datos al decano y al médico para toma de decisiones.

**Dashboard — Indicadores disponibles:**

- Total de atenciones por período con comparativas entre semestres.
- Enfermedades más frecuentes (top 10, top 20) — general y filtrable.
- Distribución de atenciones por carrera y por ciclo.
- Distribución por género y grupo etario.
- Promedio de IMC general y por carrera.
- Cantidad de referencias médicas y destinos más frecuentes.
- Picos de demanda (días, semanas o meses con más consultas).
- Tasa de recurrencia (pacientes que regresan más de X veces).

**Filtros inteligentes cruzados:**

El decano puede combinar múltiples filtros simultáneamente:
- Por carrera
- Por ciclo
- Por período / semestre
- Por género
- Por rango de edad
- Por diagnóstico (CIE-10)
- Por tipo de atención
- Comparativas entre períodos

*Ejemplo: "Casos de infecciones respiratorias en la carrera de enfermería, primer ciclo, semestre octubre 2025 - marzo 2026, mujeres entre 18 y 22 años, comparado con el semestre anterior."*

**Reportes exportables:**

- PDF con formato institucional para presentar a autoridades.
- Excel para manipulación de datos.
- Reporte periódico automático configurable (semanal o mensual).

**Reglas de negocio:**

- El decano ve datos agregados, nunca expedientes individuales.
- El médico puede ver estadísticas de sus propias atenciones.

---

### MÓDULO 6 — Notificaciones Inteligentes

**Propósito:** Alertar proactivamente a cada rol sobre situaciones relevantes.

**Para el Estudiante:**
- Notificación cuando su atención médica fue registrada (resumen).
- Notificación cuando fue referido a otro establecimiento (detalles de a dónde ir).
- Recordatorio de seguimiento si el médico lo programa.

**Para el Médico:**
- Alerta de paciente frecuente (estudiante que supera umbral de visitas en un período).
- Recordatorio de seguimientos pendientes programados.
- Alerta de tendencia epidemiológica (incremento inusual de un mismo diagnóstico).

**Para el Decano:**
- Alerta de incremento inusual en volumen de atenciones.
- Alerta de posible brote (muchos casos del mismo diagnóstico en poco tiempo).
- Notificación cuando se genera una referencia médica.
- Resumen periódico automático de actividad del departamento.

**Canales de notificación:**
- Bandeja de notificaciones dentro del sistema.
- Correo electrónico.
- Push notification en la app móvil (para estudiantes).

**Reglas de negocio:**
- Los umbrales de alertas son configurables por el administrador.
- Las notificaciones se almacenan y se pueden consultar históricamente.

---

### MÓDULO 7 — Seguridad y Auditoría

**Propósito:** Proteger los datos de salud conforme a la normativa ecuatoriana y mantener trazabilidad completa.

**Seguridad:**

- Autenticación por correo institucional para estudiantes (solo @tecazuay.edu.ec).
- Autenticación por credenciales para médico y decano (contraseñas encriptadas).
- Control de acceso por roles (cada rol solo ve lo que le corresponde).
- Cifrado de datos sensibles de salud en la base de datos.
- Sesiones con expiración automática por inactividad.
- HTTPS obligatorio en todas las comunicaciones.

**Auditoría:**

- Log completo de acciones: quién accedió, qué hizo, cuándo, desde dónde.
- Registro de cada creación y modificación de atenciones médicas.
- Las atenciones no se eliminan; solo se corrigen con nota adjunta.
- Log de accesos al sistema (intentos exitosos y fallidos).
- Todo el log es inmutable y consultable por el administrador.

**Normativa aplicable:**

- Ley Orgánica de Protección de Datos Personales (LOPDP) de Ecuador.
- Normativa del Ministerio de Salud Pública (MSP) sobre historia clínica electrónica.
- Principios de confidencialidad, integridad y disponibilidad de datos de salud.

---

### MÓDULO 8 — Administración del Sistema

**Propósito:** Configurar y mantener el sistema.

**Funcionalidades:**

- Gestión de usuarios (crear, editar, desactivar cuentas de médicos y administradores).
- Gestión de catálogos:
  - Carreras (las 16 del TEC Azuay)
  - Ciclos
  - Códigos CIE-10
  - Establecimientos de salud para referencias
- Configuración de umbrales de notificaciones.
- Configuración de reportes automáticos.
- Respaldo de base de datos.

---

### MÓDULO FUTURO — Agenda y Citas

**Estado:** No se implementa en la primera versión. La arquitectura queda preparada para incorporarlo.

**Idea general:**

- Permitir al médico agendar citas de seguimiento.
- Permitir al estudiante ver sus citas programadas desde la app.
- Recordatorios automáticos antes de la cita.

---

## 6. Plataformas

| Plataforma | Usuarios | Uso |
|---|---|---|
| **Web** | Médico, Decano/Admin | Gestión clínica, estadísticas, administración |
| **App Móvil** | Estudiante | Consulta de historial personal, notificaciones |

---

## 7. Generación de PDFs

- El sistema genera PDFs de atención médica y referencia médica con el **formato idéntico al formulario actual** diseñado por la doctora del departamento.
- Esto garantiza continuidad institucional y facilita la transición del papel al digital.

---

## 8. Infraestructura

- **Servidor:** Local del instituto (por ahora).
- **Base de datos:** Independiente y separada del sistema académico del instituto.
- **Arquitectura:** Monolito modular, preparado para escalar a nube si se necesita.
- **Escalabilidad:** Diseñado para soportar de 500 a miles de usuarios sin cambios estructurales.

---

## 9. Características Profesionales Clave

- IMC calculado automáticamente al ingresar peso y talla.
- Glasgow total calculado automáticamente.
- CIE-10 con buscador y autocompletado.
- Sección obstétrica condicional (aparece solo cuando aplica).
- Antecedentes que se acumulan entre consultas.
- PDFs idénticos al formulario original.
- Dashboard con filtros inteligentes cruzados.
- Alertas automáticas de patrones epidemiológicos.
- Auditoría completa e inmutable.
- Cumplimiento de normativa ecuatoriana (LOPDP y MSP).
- Arquitectura escalable.

---

## 10. Tipo de Proyecto

- **Origen:** Proyecto de fin de ciclo.
- **Institución:** Instituto Superior Universitario TEC Azuay.
- **Alcance:** Implementación real en el departamento médico.

---

*Documento generado como base del proyecto MEDISTA-V2. Este documento define la visión, módulos y reglas de negocio del sistema. No incluye arquitectura técnica, diseño de base de datos ni stack tecnológico, los cuales se definirán en la siguiente fase.*
