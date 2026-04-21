# Modelo de Datos — MEDISTA

**Proyecto:** MEDISTA — Sistema de Gestión de Atención Médica  
**Institución:** Instituto Superior Universitario TEC Azuay  
**Versión:** 1.0  
**Fecha:** 20 Abril 2026  
**Fase:** Fase 2 — Diseño del Sistema  

---

## Tabla de Contenidos

1. [Descripción General](#1-descripción-general)
2. [Principios de Diseño](#2-principios-de-diseño)
3. [Inventario de Entidades](#3-inventario-de-entidades)
4. [Definición de Entidades](#4-definición-de-entidades)
   - 4.1 [Infraestructura Transversal](#41-infraestructura-transversal)
   - 4.2 [M1 — Gestión de Pacientes](#42-m1--gestión-de-pacientes)
   - 4.3 [M2 — Atención Médica](#43-m2--atención-médica)
   - 4.4 [M3 — Referencia Médica](#44-m3--referencia-médica)
   - 4.5 [M6 — Notificaciones Inteligentes](#45-m6--notificaciones-inteligentes)
   - 4.6 [M8 — Administración del Sistema](#46-m8--administración-del-sistema)
5. [Relaciones Principales](#5-relaciones-principales)

---

## 1. Descripción General

Este documento define el modelo de datos relacional completo de MEDISTA. Cubre las 18 tablas del sistema, sus columnas, tipos de dato, restricciones y relaciones entre entidades. Este modelo es la referencia autoritativa para los scripts de migración Flyway, las clases de entidad JPA y los contratos de API REST.

El motor de base de datos es **PostgreSQL 16** con las extensiones `pgcrypto` (cifrado a nivel de campo para datos clínicos sensibles) y `pg_trgm` (indexación de trigramas para la búsqueda difusa del catálogo CIE-10).

Las tablas de revisión de auditoría no están listadas en este documento — son generadas automáticamente por **Hibernate Envers** para cada entidad anotada con `@Audited`. Por cada tabla auditada, Envers produce una tabla `*_aud` correspondiente en la base de datos.

---

## 2. Principios de Diseño

**Separación de identidad y datos clínicos.** La tabla `users` gestiona únicamente autenticación y autorización. Los datos clínicos pertenecen a `patients`. Un médico tiene un registro en `users` pero no en `patients`. Un estudiante tiene ambos — vinculados mediante una clave foránea nullable.

**Eliminación lógica en todas las entidades clínicas.** Ningún registro clínico se elimina físicamente. La LOPDP y el Acuerdo MSP No. 00000125 exigen trazabilidad completa del historial clínico. Todas las tablas clínicas incluyen una columna booleana `is_active`. La eliminación se implementa como `is_active = false`.

**Inmutabilidad de las atenciones médicas.** Una atención médica guardada no puede modificarse ni eliminarse. Las correcciones se registran como notas de corrección vinculadas al registro original, preservando el historial completo tal como lo exige la normativa del MSP.

**Los catálogos son tablas, no enumeraciones de aplicación.** Las carreras, los códigos CIE-10 y los establecimientos de salud se gestionan como tablas de base de datos con operaciones CRUD administrativas. Esto permite su administración en tiempo de ejecución sin redespliegue de la aplicación.

**Los campos calculados se persisten.** El IMC y el Total de Glasgow son calculados por la aplicación pero almacenados como columnas. Esto garantiza que los registros históricos permanezcan exactos aunque las fórmulas de cálculo cambien en versiones futuras.

**Los archivos se almacenan en el sistema de ficheros, no en la base de datos.** El contenido binario de los exámenes complementarios se almacena en el sistema de ficheros del servidor. La base de datos almacena únicamente la ruta del archivo, el nombre original y el tipo MIME. Almacenar BLOBs en PostgreSQL degrada el rendimiento y complica las estrategias de respaldo.

---

## 3. Inventario de Entidades

| # | Tabla | Módulo | Descripción |
|---|-------|--------|-------------|
| 1 | `users` | Transversal | Cuentas de usuario, autenticación y gestión de roles |
| 2 | `audit_logs` | Transversal / M7 | Registro inmutable de todas las acciones relevantes de seguridad |
| 3 | `backup_logs` | Transversal / M8 | Registro de metadatos de todas las ejecuciones de respaldo de base de datos |
| 4 | `patients` | M1 | Datos demográficos e identificación del estudiante |
| 5 | `patient_background` | M1 | Antecedentes familiares y personales acumulados del paciente |
| 6 | `careers` | M1 / M8 | Catálogo de carreras académicas de la institución |
| 7 | `medical_attendances` | M2 | Registro central de la consulta clínica |
| 8 | `physical_exam_findings` | M2 | Hallazgos del examen físico por sistema corporal |
| 9 | `obstetric_emergency` | M2 | Datos obstétricos — condicional según género del paciente |
| 10 | `complementary_exams` | M2 | Registros de exámenes complementarios y archivos adjuntos |
| 11 | `diagnoses` | M2 | Diagnósticos asociados a una atención médica |
| 12 | `cie10_codes` | M2 / M8 | Catálogo de códigos diagnósticos CIE-10 (+14.000 entradas) |
| 13 | `medical_referrals` | M3 | Registros de referencias médicas a establecimientos externos |
| 14 | `referral_diagnoses` | M3 | Diagnósticos comunicados en una referencia médica |
| 15 | `health_establishments` | M3 / M8 | Catálogo de establecimientos de salud destino de referencias |
| 16 | `notifications` | M6 | Notificaciones del sistema entregadas a los usuarios |
| 17 | `notification_thresholds` | M6 / M8 | Umbrales configurables que activan alertas automáticas |
| 18 | `automatic_reports` | M8 | Configuración de generación automática de reportes programados |

**Módulos sin tablas propias:**
- **M4 — Historial Clínico:** Consultas de solo lectura sobre `medical_attendances`, `diagnoses` y `physical_exam_findings`, filtradas por `patient_id`.
- **M5 — Dashboard Estadístico:** Consultas de agregación sobre tablas clínicas existentes. No requiere almacenamiento dedicado con el volumen de datos actual (~500 pacientes).

---

## 4. Definición de Entidades

### 4.1 Infraestructura Transversal

---

#### `users`

Gestiona todas las cuentas de usuario del sistema independientemente del rol. Esta tabla maneja exclusivamente autenticación y autorización — no se almacena ningún dato clínico aquí.

| Columna | Tipo | Restricciones | Descripción |
|---------|------|---------------|-------------|
| `id` | `BIGSERIAL` | PK | Clave primaria sustituta |
| `first_name` | `VARCHAR(100)` | NOT NULL | Nombre del usuario |
| `last_name` | `VARCHAR(100)` | NOT NULL | Apellido del usuario |
| `email` | `VARCHAR(255)` | NOT NULL, UNIQUE | Correo institucional — utilizado como identificador de inicio de sesión |
| `password_hash` | `VARCHAR(255)` | NOT NULL | Hash BCrypt de la contraseña — el texto plano nunca se almacena |
| `role` | `role_enum` | NOT NULL | Uno de: `MEDICO`, `DECANO`, `ADMINISTRADOR`, `ESTUDIANTE` |
| `is_active` | `BOOLEAN` | NOT NULL, DEFAULT true | Indicador de eliminación lógica / desactivación de cuenta |
| `created_at` | `TIMESTAMPTZ` | NOT NULL, DEFAULT now() | Marca de tiempo de creación de la cuenta |
| `updated_at` | `TIMESTAMPTZ` | NOT NULL, DEFAULT now() | Marca de tiempo de última modificación |

**Índices:** `email` (único), `role` (btree)

**Notas:** La columna `role` utiliza un tipo ENUM personalizado de PostgreSQL `role_enum` aplicado a nivel de base de datos. El hash de contraseñas es gestionado por Spring Security con BCrypt (factor de costo 12).

---

#### `audit_logs`

Registro inmutable de todas las acciones relevantes de seguridad realizadas por los usuarios. Esta tabla satisface el requisito de la LOPDP de registrar no solo las modificaciones de datos (cubiertas por Hibernate Envers) sino también los eventos de acceso — como la visualización de expedientes clínicos, la generación de PDFs y la exportación de reportes — acciones que no producen escritura en base de datos pero que deben ser trazables.

Los registros de esta tabla **nunca se eliminan**. Se recomienda el particionamiento por mes en la columna `created_at` como optimización de rendimiento a largo plazo.

| Columna | Tipo | Restricciones | Descripción |
|---------|------|---------------|-------------|
| `id` | `BIGSERIAL` | PK | Clave primaria sustituta |
| `user_id` | `BIGINT` | FK → `users.id`, NOT NULL | Usuario que realizó la acción |
| `action` | `audit_action_enum` | NOT NULL | Uno de: `LOGIN`, `LOGOUT`, `VIEW`, `CREATE`, `UPDATE`, `DELETE`, `EXPORT`, `PRINT` |
| `entity_type` | `VARCHAR(100)` | NULLABLE | Nombre de la entidad afectada (ej. `patient`, `medical_attendance`) |
| `entity_id` | `BIGINT` | NULLABLE | ID del registro afectado |
| `ip_address` | `INET` | NOT NULL | Dirección IP del cliente en el momento de la acción |
| `user_agent` | `TEXT` | NULLABLE | Cadena de agente de usuario del cliente |
| `created_at` | `TIMESTAMPTZ` | NOT NULL, DEFAULT now() | Marca de tiempo de la acción |

**Índices:** `user_id` (btree), `entity_type + entity_id` (btree), `created_at` (btree, candidato a clave de partición)

**Notas:** Solo las acciones con relevancia clínica o de seguridad generan un registro de auditoría. Los eventos de navegación e interacciones de interfaz no producen registros.

---

#### `backup_logs`

Registro de todas las ejecuciones de respaldo de base de datos, tanto automáticas como activadas manualmente. Proporciona al Administrador del Sistema visibilidad sobre el estado de los respaldos sin necesidad de acceder directamente al sistema de ficheros.

| Columna | Tipo | Restricciones | Descripción |
|---------|------|---------------|-------------|
| `id` | `BIGSERIAL` | PK | Clave primaria sustituta |
| `executed_at` | `TIMESTAMPTZ` | NOT NULL | Marca de tiempo de ejecución del respaldo |
| `triggered_by` | `backup_trigger_enum` | NOT NULL | Uno de: `AUTOMATIC`, `MANUAL` |
| `requested_by` | `BIGINT` | FK → `users.id`, NULLABLE | Usuario que activó el respaldo (nulo para respaldos automáticos) |
| `file_name` | `VARCHAR(255)` | NOT NULL | Nombre del archivo de respaldo generado |
| `file_size_bytes` | `BIGINT` | NULLABLE | Tamaño del archivo de respaldo en bytes |
| `status` | `backup_status_enum` | NOT NULL | Uno de: `SUCCESS`, `FAILED` |
| `error_message` | `TEXT` | NULLABLE | Detalle del error si el estado es `FAILED` |

---

### 4.2 M1 — Gestión de Pacientes

---

#### `patients`

Almacena los datos demográficos e identificación de cada estudiante registrado en el departamento médico. Es la entidad raíz del dominio clínico — ninguna atención médica, referencia o historial clínico puede existir sin un registro de paciente previo.

| Columna | Tipo | Restricciones | Descripción |
|---------|------|---------------|-------------|
| `id` | `BIGSERIAL` | PK | Clave primaria sustituta |
| `user_id` | `BIGINT` | FK → `users.id`, NULLABLE, UNIQUE | Cuenta de usuario asociada — nulo hasta que el estudiante activa su cuenta |
| `career_id` | `BIGINT` | FK → `careers.id`, NOT NULL | Carrera académica en la que está matriculado el estudiante |
| `cedula` | `VARCHAR(10)` | NOT NULL, UNIQUE | Número de cédula de identidad ecuatoriana — identificador único del sistema |
| `first_name` | `VARCHAR(100)` | NOT NULL | Nombre del paciente |
| `last_name` | `VARCHAR(100)` | NOT NULL | Apellido del paciente |
| `semester` | `VARCHAR(20)` | NOT NULL | Semestre o ciclo académico actual |
| `address` | `VARCHAR(255)` | NOT NULL | Dirección de domicilio |
| `neighborhood` | `VARCHAR(100)` | NULLABLE | Barrio |
| `parish` | `VARCHAR(100)` | NULLABLE | Parroquia |
| `canton` | `VARCHAR(100)` | NOT NULL | Cantón |
| `province` | `VARCHAR(100)` | NOT NULL | Provincia |
| `phone` | `VARCHAR(20)` | NOT NULL | Número de teléfono de contacto |
| `birth_date` | `DATE` | NOT NULL | Fecha de nacimiento — la edad se calcula dinámicamente, no se almacena |
| `birth_place` | `VARCHAR(100)` | NULLABLE | Ciudad o lugar de nacimiento |
| `birth_country` | `VARCHAR(100)` | NOT NULL, DEFAULT 'Ecuador' | País de nacimiento |
| `gender` | `gender_enum` | NOT NULL | Uno de: `MALE`, `FEMALE`, `OTHER` |
| `marital_status` | `marital_status_enum` | NOT NULL | Uno de: `SINGLE`, `MARRIED`, `WIDOWED`, `DIVORCED`, `FREE_UNION` |
| `blood_type` | `blood_type_enum` | NOT NULL | Uno de: `A+`, `A-`, `B+`, `B-`, `AB+`, `AB-`, `O+`, `O-` |
| `is_active` | `BOOLEAN` | NOT NULL, DEFAULT true | Indicador de eliminación lógica |
| `created_at` | `TIMESTAMPTZ` | NOT NULL, DEFAULT now() | Marca de tiempo de creación del registro |
| `updated_at` | `TIMESTAMPTZ` | NOT NULL, DEFAULT now() | Marca de tiempo de última modificación |

**Índices:** `cedula` (único), `user_id` (único), `last_name + first_name` (btree), `career_id` (btree)

**Notas:** La edad nunca se almacena — se calcula en tiempo de consulta a partir de `birth_date`. Almacenar la edad requeriría actualizaciones constantes y produciría datos desactualizados. La columna `gender` determina la visualización condicional de la sección obstétrica en el formulario de atención médica.

---

#### `patient_background`

Almacena los antecedentes familiares y personales acumulados de un paciente. A diferencia de las atenciones médicas, este registro no es inmutable — se actualiza conforme evolucionan los antecedentes del paciente. Todos los cambios son rastreados por Hibernate Envers.

Un registro por paciente (relación 1:1). Se crea en el momento del primer registro y se actualiza posteriormente.

| Columna | Tipo | Restricciones | Descripción |
|---------|------|---------------|-------------|
| `id` | `BIGSERIAL` | PK | Clave primaria sustituta |
| `patient_id` | `BIGINT` | FK → `patients.id`, NOT NULL, UNIQUE | Paciente propietario del registro |
| `allergies` | `TEXT` | NULLABLE | Antecedentes alérgicos — texto libre |
| `clinical` | `TEXT` | NULLABLE | Antecedentes clínicos — texto libre |
| `gynecological` | `TEXT` | NULLABLE | Antecedentes ginecológicos — texto libre |
| `traumatological` | `TEXT` | NULLABLE | Antecedentes traumatológicos — texto libre |
| `surgical` | `TEXT` | NULLABLE | Antecedentes quirúrgicos — texto libre |
| `pharmacological` | `TEXT` | NULLABLE | Antecedentes farmacológicos — texto libre |
| `updated_at` | `TIMESTAMPTZ` | NOT NULL, DEFAULT now() | Marca de tiempo de última modificación |
| `updated_by` | `BIGINT` | FK → `users.id`, NOT NULL | Usuario que realizó la última modificación |

**Notas:** Los seis campos de antecedentes son nullable — el registro se crea con valores nulos en el momento del registro del paciente y se va completando conforme la médico recopila información en consultas sucesivas. Las tablas de auditoría de Envers capturan cada versión histórica.

---

#### `careers`

Catálogo de carreras académicas ofrecidas por la institución. Normaliza los datos de carrera en los registros de pacientes y permite agrupaciones consistentes en reportes estadísticos.

| Columna | Tipo | Restricciones | Descripción |
|---------|------|---------------|-------------|
| `id` | `BIGSERIAL` | PK | Clave primaria sustituta |
| `name` | `VARCHAR(150)` | NOT NULL, UNIQUE | Nombre completo de la carrera académica |
| `is_active` | `BOOLEAN` | NOT NULL, DEFAULT true | Indicador de eliminación lógica / desactivación |

**Datos iniciales:** 16 registros correspondientes a las carreras actuales de la institución. Cargados mediante migración semilla de Flyway en el primer despliegue.

---

### 4.3 M2 — Atención Médica

---

#### `medical_attendances`

La entidad clínica central del sistema. Cada registro representa una consulta médica completa, mapeando directamente al formulario físico de atención médica actualmente utilizado por el departamento. Es la tabla más compleja del modelo.

Los registros de esta tabla son **inmutables tras su creación**. No se permiten operaciones UPDATE sobre campos clínicos. Las correcciones se gestionan mediante un mecanismo de nota de corrección de solo adición.

| Columna | Tipo | Restricciones | Descripción |
|---------|------|---------------|-------------|
| `id` | `BIGSERIAL` | PK | Clave primaria sustituta |
| `patient_id` | `BIGINT` | FK → `patients.id`, NOT NULL | Paciente que recibe la consulta |
| `attended_by` | `BIGINT` | FK → `users.id`, NOT NULL | Médico que creó el registro |
| `attendance_date` | `TIMESTAMPTZ` | NOT NULL, DEFAULT now() | Fecha y hora de la consulta |
| `reason_for_visit` | `TEXT` | NOT NULL | Motivo de consulta — texto libre |
| `current_illness` | `TEXT` | NULLABLE | Descripción de la enfermedad actual — texto libre |
| `treatment` | `TEXT` | NULLABLE | Tratamiento prescrito — texto libre |
| `blood_pressure_systolic` | `SMALLINT` | NULLABLE | Presión arterial sistólica (mmHg) |
| `blood_pressure_diastolic` | `SMALLINT` | NULLABLE | Presión arterial diastólica (mmHg) |
| `weight_kg` | `NUMERIC(5,2)` | NULLABLE | Peso corporal en kilogramos |
| `height_cm` | `NUMERIC(5,2)` | NULLABLE | Talla en centímetros |
| `bmi` | `NUMERIC(5,2)` | NULLABLE | Índice de Masa Corporal — calculado por la aplicación, persistido para exactitud histórica |
| `heart_rate` | `SMALLINT` | NULLABLE | Frecuencia cardíaca (lpm) |
| `respiratory_rate` | `SMALLINT` | NULLABLE | Frecuencia respiratoria (resp/min) |
| `temperature_celsius` | `NUMERIC(4,1)` | NULLABLE | Temperatura corporal en grados Celsius |
| `oxygen_saturation` | `SMALLINT` | NULLABLE | Saturación periférica de oxígeno (%) |
| `glasgow_eye` | `SMALLINT` | NULLABLE | Escala de Glasgow — apertura ocular (1–4) |
| `glasgow_verbal` | `SMALLINT` | NULLABLE | Escala de Glasgow — respuesta verbal (1–5) |
| `glasgow_motor` | `SMALLINT` | NULLABLE | Escala de Glasgow — respuesta motora (1–6) |
| `glasgow_total` | `SMALLINT` | NULLABLE | Total Escala de Glasgow — calculado por la aplicación, persistido |
| `capillary_refill` | `VARCHAR(20)` | NULLABLE | Descripción del llenado capilar |
| `pupillary_reflex` | `VARCHAR(50)` | NULLABLE | Hallazgos del reflejo pupilar |
| `correction_note` | `TEXT` | NULLABLE | Nota de corrección de solo adición — se completa si la médico necesita corregir el registro tras su creación |
| `is_active` | `BOOLEAN` | NOT NULL, DEFAULT true | Indicador de eliminación lógica |
| `created_at` | `TIMESTAMPTZ` | NOT NULL, DEFAULT now() | Marca de tiempo de creación del registro |

**Índices:** `patient_id` (btree), `attended_by` (btree), `attendance_date` (btree), `is_active` (btree)

---

#### `physical_exam_findings`

Almacena los hallazgos del examen físico por sistema corporal para una atención médica. El examen físico cubre 9 sistemas — cada sistema produce un registro en esta tabla, resultando en exactamente 9 filas por atención.

| Columna | Tipo | Restricciones | Descripción |
|---------|------|---------------|-------------|
| `id` | `BIGSERIAL` | PK | Clave primaria sustituta |
| `medical_attendance_id` | `BIGINT` | FK → `medical_attendances.id`, NOT NULL | Atención propietaria |
| `body_system` | `body_system_enum` | NOT NULL | Uno de: `SKIN_AND_ANNEXES`, `HEAD`, `NECK`, `THORAX`, `HEART`, `ABDOMEN`, `INGUINAL`, `UPPER_LIMBS`, `LOWER_LIMBS` |
| `is_normal` | `BOOLEAN` | NOT NULL | Hallazgo normal (true) o anormal (false) |
| `description` | `TEXT` | NULLABLE | Descripción en texto libre — requerida cuando `is_normal` es false |

**Restricciones:** UNIQUE (`medical_attendance_id`, `body_system`) — un hallazgo por sistema por atención.

---

#### `obstetric_emergency`

Almacena los datos obstétricos recopilados durante una atención médica. Esta sección es condicional — aplica únicamente a pacientes de género femenino. Un registro por atención, creado solo cuando el género del paciente es `FEMALE`.

| Columna | Tipo | Restricciones | Descripción |
|---------|------|---------------|-------------|
| `id` | `BIGSERIAL` | PK | Clave primaria sustituta |
| `medical_attendance_id` | `BIGINT` | FK → `medical_attendances.id`, NOT NULL, UNIQUE | Atención propietaria |
| `menarche` | `VARCHAR(20)` | NULLABLE | Edad de la primera menstruación |
| `menstrual_rhythm_r` | `VARCHAR(10)` | NULLABLE | Ritmo menstrual — valor R |
| `menstrual_rhythm_i` | `VARCHAR(10)` | NULLABLE | Ritmo menstrual — valor I |
| `cycles` | `VARCHAR(20)` | NULLABLE | Descripción de los ciclos menstruales |
| `fum` | `DATE` | NULLABLE | Fecha de la última menstruación |
| `ivsa` | `VARCHAR(20)` | NULLABLE | Inicio de vida sexual activa |
| `sexual_partners` | `SMALLINT` | NULLABLE | Número de parejas sexuales |
| `gapc` | `VARCHAR(20)` | NULLABLE | Fórmula obstétrica G-A-P-C |
| `dismenorrhea` | `BOOLEAN` | NOT NULL, DEFAULT false | Indicador de dismenorrea |
| `mastodynia` | `BOOLEAN` | NOT NULL, DEFAULT false | Indicador de mastodinia |
| `is_pregnant` | `BOOLEAN` | NOT NULL, DEFAULT false | Indicador de embarazo actual |
| `fpp` | `DATE` | NULLABLE | Fecha probable de parto |
| `gestational_age_weeks` | `SMALLINT` | NULLABLE | Edad gestacional en semanas |
| `prenatal_controls` | `SMALLINT` | NULLABLE | Número de controles prenatales |
| `immunizations` | `VARCHAR(255)` | NULLABLE | Notas de inmunizaciones |
| `notes` | `TEXT` | NULLABLE | Notas obstétricas adicionales |

---

#### `complementary_exams`

Registra los exámenes complementarios solicitados o revisados durante una atención médica. Una atención puede tener múltiples exámenes complementarios. Los archivos adjuntos (PDF, JPG, PNG) se almacenan en el sistema de ficheros del servidor — esta tabla almacena únicamente los metadatos de referencia.

| Columna | Tipo | Restricciones | Descripción |
|---------|------|---------------|-------------|
| `id` | `BIGSERIAL` | PK | Clave primaria sustituta |
| `medical_attendance_id` | `BIGINT` | FK → `medical_attendances.id`, NOT NULL | Atención propietaria |
| `description` | `TEXT` | NULLABLE | Descripción del examen o resultado |
| `not_applicable` | `BOOLEAN` | NOT NULL, DEFAULT false | Marca la opción "No Aplica" del formulario físico |
| `file_path` | `VARCHAR(500)` | NULLABLE | Ruta relativa al archivo almacenado en el sistema de ficheros del servidor |
| `file_name` | `VARCHAR(255)` | NULLABLE | Nombre original del archivo tal como fue subido por la médico |
| `file_mime_type` | `VARCHAR(100)` | NULLABLE | Tipo MIME del archivo (ej. `application/pdf`, `image/jpeg`) |
| `created_at` | `TIMESTAMPTZ` | NOT NULL, DEFAULT now() | Marca de tiempo de creación del registro |

---

#### `diagnoses`

Almacena uno o más diagnósticos asociados a una atención médica. Cada diagnóstico está vinculado a una entrada del catálogo CIE-10 y clasificado como presuntivo o definitivo.

| Columna | Tipo | Restricciones | Descripción |
|---------|------|---------------|-------------|
| `id` | `BIGSERIAL` | PK | Clave primaria sustituta |
| `medical_attendance_id` | `BIGINT` | FK → `medical_attendances.id`, NOT NULL | Atención propietaria |
| `cie10_code_id` | `BIGINT` | FK → `cie10_codes.id`, NOT NULL | Código diagnóstico CIE-10 — integridad referencial aplicada a nivel de base de datos |
| `description` | `TEXT` | NULLABLE | Descripción en texto libre del diagnóstico tal como lo ingresó la médico |
| `status` | `diagnosis_status_enum` | NOT NULL | Uno de: `PRESUMPTIVE`, `DEFINITIVE` |
| `created_at` | `TIMESTAMPTZ` | NOT NULL, DEFAULT now() | Marca de tiempo de creación del registro |

---

#### `cie10_codes`

Catálogo de códigos diagnósticos CIE-10 (Clasificación Internacional de Enfermedades, 10.ª revisión). Cargado con más de 14.000 entradas mediante una migración masiva de Flyway en el primer despliegue. Utilizado por la función de autocompletado de diagnósticos en tiempo real.

| Columna | Tipo | Restricciones | Descripción |
|---------|------|---------------|-------------|
| `id` | `BIGSERIAL` | PK | Clave primaria sustituta |
| `code` | `VARCHAR(10)` | NOT NULL, UNIQUE | Código alfanumérico CIE-10 (ej. `J00`, `A09.0`) |
| `description` | `VARCHAR(500)` | NOT NULL | Nombre completo del diagnóstico en español |
| `is_active` | `BOOLEAN` | NOT NULL, DEFAULT true | Permite la desactivación de códigos obsoletos sin eliminarlos |

**Índices:** `code` (único, btree), `description` (GIN con `pg_trgm`) — el índice de trigramas sobre `description` habilita la búsqueda difusa en menos de 500ms sobre más de 14.000 entradas.

---

### 4.4 M3 — Referencia Médica

---

#### `medical_referrals`

Registra las referencias médicas generadas cuando un paciente requiere atención más allá de la capacidad del departamento. Toda referencia debe originarse desde una atención médica existente — no se permiten referencias independientes por regla de negocio.

| Columna | Tipo | Restricciones | Descripción |
|---------|------|---------------|-------------|
| `id` | `BIGSERIAL` | PK | Clave primaria sustituta |
| `medical_attendance_id` | `BIGINT` | FK → `medical_attendances.id`, NOT NULL, UNIQUE | Atención de origen — máximo una referencia por atención |
| `health_establishment_id` | `BIGINT` | FK → `health_establishments.id`, NOT NULL | Establecimiento de salud destino |
| `service` | `VARCHAR(150)` | NULLABLE | Servicio o unidad específica en el establecimiento destino |
| `specialty` | `VARCHAR(150)` | NULLABLE | Especialidad médica solicitada |
| `referral_date` | `DATE` | NOT NULL | Fecha de la referencia |
| `referral_reason` | `referral_reason_enum` | NOT NULL | Uno de: `LIMITED_RESOLUTION`, `LACK_OF_PROFESSIONAL`, `OTHER` |
| `clinical_summary` | `TEXT` | NULLABLE | Resumen del cuadro clínico |
| `relevant_findings` | `TEXT` | NULLABLE | Hallazgos relevantes comunicados al médico receptor |
| `referred_by` | `BIGINT` | FK → `users.id`, NOT NULL | Médico que emite la referencia |
| `created_at` | `TIMESTAMPTZ` | NOT NULL, DEFAULT now() | Marca de tiempo de creación del registro |
| `is_active` | `BOOLEAN` | NOT NULL, DEFAULT true | Indicador de eliminación lógica |

**Notas:** El nombre de la institución ("Instituto Superior Universitario TEC Azuay") y la etiqueta de servicio ("Departamento Médico") son constantes del sistema inyectadas en el momento de la generación del PDF. No se persisten en esta tabla.

---

#### `referral_diagnoses`

Diagnósticos comunicados en una referencia médica. Se mantiene separada de `diagnoses` (diagnósticos de la atención) porque representa el cuadro clínico comunicado al establecimiento receptor — puede diferir o ser un subconjunto de los diagnósticos de la atención.

| Columna | Tipo | Restricciones | Descripción |
|---------|------|---------------|-------------|
| `id` | `BIGSERIAL` | PK | Clave primaria sustituta |
| `referral_id` | `BIGINT` | FK → `medical_referrals.id`, NOT NULL | Referencia propietaria |
| `cie10_code_id` | `BIGINT` | FK → `cie10_codes.id`, NOT NULL | Código diagnóstico CIE-10 |
| `description` | `TEXT` | NULLABLE | Descripción en texto libre del diagnóstico |
| `status` | `diagnosis_status_enum` | NOT NULL | Uno de: `PRESUMPTIVE`, `DEFINITIVE` |
| `created_at` | `TIMESTAMPTZ` | NOT NULL, DEFAULT now() | Marca de tiempo de creación del registro |

---

#### `health_establishments`

Catálogo de establecimientos de salud disponibles como destino de referencias médicas. Gestionado por el Administrador del Sistema desde el módulo M8.

| Columna | Tipo | Restricciones | Descripción |
|---------|------|---------------|-------------|
| `id` | `BIGSERIAL` | PK | Clave primaria sustituta |
| `name` | `VARCHAR(255)` | NOT NULL | Nombre completo del establecimiento de salud |
| `entity_type` | `VARCHAR(100)` | NULLABLE | Tipo de entidad (ej. hospital, clínica, centro especializado) |
| `is_active` | `BOOLEAN` | NOT NULL, DEFAULT true | Indicador de eliminación lógica / desactivación |

---

### 4.5 M6 — Notificaciones Inteligentes

---

#### `notifications`

Almacena todas las notificaciones del sistema entregadas a los usuarios. Las notificaciones se generan automáticamente por el sistema según los umbrales configurados (alertas clínicas, patrones epidemiológicos, alertas de volumen institucional).

| Columna | Tipo | Restricciones | Descripción |
|---------|------|---------------|-------------|
| `id` | `BIGSERIAL` | PK | Clave primaria sustituta |
| `recipient_id` | `BIGINT` | FK → `users.id`, NOT NULL | Usuario que recibe la notificación |
| `title` | `VARCHAR(255)` | NOT NULL | Título corto de la notificación |
| `message` | `TEXT` | NOT NULL | Cuerpo completo del mensaje de notificación |
| `type` | `notification_type_enum` | NOT NULL | Uno de: `CLINICAL`, `EPIDEMIOLOGICAL`, `INSTITUTIONAL` |
| `is_read` | `BOOLEAN` | NOT NULL, DEFAULT false | Indicador de estado de lectura |
| `sent_at` | `TIMESTAMPTZ` | NULLABLE | Marca de tiempo de envío de la notificación |
| `created_at` | `TIMESTAMPTZ` | NOT NULL, DEFAULT now() | Marca de tiempo de creación del registro |

**Índices:** `recipient_id + is_read` (btree) — optimiza las consultas de conteo de notificaciones no leídas.

---

#### `notification_thresholds`

Umbrales configurables que activan la generación automática de alertas. Gestionados exclusivamente por el Administrador del Sistema. Estos valores impulsan la lógica de evaluación del motor de notificaciones.

| Columna | Tipo | Restricciones | Descripción |
|---------|------|---------------|-------------|
| `id` | `BIGSERIAL` | PK | Clave primaria sustituta |
| `threshold_type` | `threshold_type_enum` | NOT NULL, UNIQUE | Uno de: `PATIENT_VISITS`, `EPIDEMIOLOGICAL`, `INSTITUTIONAL` |
| `value` | `INTEGER` | NOT NULL | Valor umbral que activa la alerta |
| `period_days` | `INTEGER` | NOT NULL | Ventana de evaluación en días |
| `is_active` | `BOOLEAN` | NOT NULL, DEFAULT true | Habilita o deshabilita este umbral |
| `updated_at` | `TIMESTAMPTZ` | NOT NULL, DEFAULT now() | Marca de tiempo de última modificación |
| `updated_by` | `BIGINT` | FK → `users.id`, NOT NULL | Administrador que modificó este umbral por última vez |

---

### 4.6 M8 — Administración del Sistema

---

#### `automatic_reports`

Registros de configuración para la generación automática programada de reportes. Cada registro define un trabajo de reporte con su frecuencia, filtros aplicados y lista de destinatarios.

| Columna | Tipo | Restricciones | Descripción |
|---------|------|---------------|-------------|
| `id` | `BIGSERIAL` | PK | Clave primaria sustituta |
| `report_type` | `VARCHAR(100)` | NOT NULL | Identificador de la plantilla de reporte a generar |
| `frequency` | `report_frequency_enum` | NOT NULL | Uno de: `WEEKLY`, `MONTHLY`, `BIANNUAL` |
| `filters` | `JSONB` | NULLABLE | Configuración dinámica de filtros — almacenada como JSON para flexibilidad |
| `recipients` | `JSONB` | NOT NULL | Lista de correos electrónicos de destinatarios — almacenada como arreglo JSON |
| `is_active` | `BOOLEAN` | NOT NULL, DEFAULT true | Habilita o deshabilita este trabajo de reporte |
| `updated_by` | `BIGINT` | FK → `users.id`, NOT NULL | Administrador que modificó esta configuración por última vez |
| `updated_at` | `TIMESTAMPTZ` | NOT NULL, DEFAULT now() | Marca de tiempo de última modificación |

---

## 5. Relaciones Principales

| Relación | Tipo | Descripción |
|----------|------|-------------|
| `users` → `patients` | 1:1 opcional | Un usuario estudiante tiene exactamente un registro de paciente. Un paciente puede existir sin cuenta de usuario (aún no activada). Un usuario médico no tiene registro de paciente. |
| `patients` → `patient_background` | 1:1 obligatoria | Cada paciente tiene exactamente un registro de antecedentes, creado en el momento del registro. |
| `patients` → `careers` | N:1 | Muchos pacientes pertenecen a una misma carrera. |
| `patients` → `medical_attendances` | 1:N | Un paciente puede tener muchas atenciones médicas a lo largo del tiempo. |
| `medical_attendances` → `physical_exam_findings` | 1:N (fijo 9) | Cada atención produce exactamente 9 registros de hallazgos del examen físico, uno por sistema corporal. |
| `medical_attendances` → `obstetric_emergency` | 1:1 opcional | Una atención puede tener cero o un registro obstétrico, condicional al género del paciente. |
| `medical_attendances` → `complementary_exams` | 1:N | Una atención puede tener cero o muchos registros de exámenes complementarios. |
| `medical_attendances` → `diagnoses` | 1:N | Una atención puede tener uno o muchos diagnósticos. |
| `medical_attendances` → `medical_referrals` | 1:1 opcional | Una atención puede generar cero o una referencia médica. |
| `medical_referrals` → `referral_diagnoses` | 1:N | Una referencia comunica uno o más diagnósticos al establecimiento receptor. |
| `diagnoses` → `cie10_codes` | N:1 | Muchos diagnósticos referencian un mismo código CIE-10. |
| `referral_diagnoses` → `cie10_codes` | N:1 | Muchos diagnósticos de referencia referencian un mismo código CIE-10. |
| `medical_referrals` → `health_establishments` | N:1 | Muchas referencias pueden dirigirse al mismo establecimiento de salud. |
| `notifications` → `users` | N:1 | Muchas notificaciones entregadas a un mismo usuario. |

---