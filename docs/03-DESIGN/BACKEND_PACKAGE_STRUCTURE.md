# Estructura de Paquetes del Backend — MEDISTA

**Proyecto:** MEDISTA — Sistema de Gestión de Atención Médica  
**Institución:** Instituto Superior Universitario TEC Azuay  
**Versión:** 1.0  
**Fecha:** 6 de mayo de 2026  
**Fase:** Fase 2 — Diseño del Sistema (Diseño de Bajo Nivel)  

---

## Tabla de Contenidos

1. [Descripción General](#1-descripción-general)
2. [Convenciones del Proyecto](#2-convenciones-del-proyecto)
3. [Patrón Arquitectónico: Module-First con Aislamiento `api/internal`](#3-patrón-arquitectónico-module-first-con-aislamiento-apiinternal)
4. [Estructura General del Backend](#4-estructura-general-del-backend)
5. [Paquetes Transversales](#5-paquetes-transversales)
   - 5.1 [`config/`](#51-config)
   - 5.2 [`common/`](#52-common)
   - 5.3 [`exception/`](#53-exception)
6. [Estructura por Módulo](#6-estructura-por-módulo)
   - 6.1 [Módulo M7 — Seguridad y Auditoría](#61-módulo-m7--seguridad-y-auditoría)
   - 6.2 [Módulo M1 — Gestión de Pacientes](#62-módulo-m1--gestión-de-pacientes)
   - 6.3 [Módulo M2 — Atención Médica](#63-módulo-m2--atención-médica)
   - 6.4 [Módulo M3 — Referencia Médica](#64-módulo-m3--referencia-médica)
   - 6.5 [Módulo M4 — Historial Clínico](#65-módulo-m4--historial-clínico)
   - 6.6 [Módulo M5 — Dashboard y Reportes](#66-módulo-m5--dashboard-y-reportes)
   - 6.7 [Módulo M6 — Notificaciones Inteligentes](#67-módulo-m6--notificaciones-inteligentes)
   - 6.8 [Módulo M8 — Administración del Sistema](#68-módulo-m8--administración-del-sistema)
7. [Patrones de Diseño Aplicados](#7-patrones-de-diseño-aplicados)
8. [Dependencias entre Módulos](#8-dependencias-entre-módulos)
9. [Estructura de Tests](#9-estructura-de-tests)
10. [Decisiones de Diseño (ADRs Resumidos)](#10-decisiones-de-diseño-adrs-resumidos)
11. [Trazabilidad con el Modelo de Datos](#11-trazabilidad-con-el-modelo-de-datos)
12. [Glosario](#12-glosario)

---

## 1. Descripción General

Este documento especifica la organización física del código fuente del backend de MEDISTA. Define el árbol de paquetes Java completo, los patrones de diseño aplicados en cada capa y el contrato de aislamiento entre los ocho módulos funcionales del sistema.

El backend de MEDISTA es un monolito modular implementado con Java 21 y Spring Boot 3.3, cuya estructura interna está organizada bajo un esquema *module-first* con separación estricta entre el contrato público de cada módulo y su implementación interna. Esta organización es el puente entre las decisiones arquitectónicas formalizadas en el Diagrama de Componentes y el Modelo de Datos, y la fase de implementación que se ejecutará a continuación.

El documento asume que el lector está familiarizado con los siguientes entregables previos: la Visión del Sistema, el Stack Tecnológico, la Especificación de Requisitos de Software, el Diagrama de Componentes, el Diagrama de Despliegue, los Diagramas de Secuencia y el Modelo de Datos. Cada decisión presentada aquí responde directamente a restricciones definidas en esos documentos.

---

## 2. Convenciones del Proyecto

### 2.1 Group ID y Paquete Raíz

El identificador de grupo Maven del proyecto es `ec.tecazuay.medista`. Este valor sigue la convención de dominio invertido recomendada por Oracle, utilizando el dominio educativo de Ecuador (`ec`) seguido del identificador institucional (`tecazuay`) y el nombre del producto (`medista`).

Todo el código fuente del backend reside bajo el paquete raíz `ec.tecazuay.medista`. La clase principal de arranque, `MedistaApplication`, se ubica directamente en este paquete raíz para que Spring Boot detecte automáticamente todos los componentes mediante *component scanning*.

### 2.2 Convención de Nombrado de Clases

Toda clase del backend sigue una convención de nombrado uniforme que permite identificar su responsabilidad a partir de su nombre.

| Tipo de elemento | Convención | Ejemplo |
|------------------|------------|---------|
| Entidad JPA | `Xxx` (singular, sin sufijo) | `Patient`, `MedicalAttendance` |
| Repositorio | `XxxRepository` (interfaz) | `PatientRepository` |
| Service público (interfaz) | `XxxService` | `PatientService` |
| Service de consulta read-only | `XxxQueryService` | `MedicalAttendanceQueryService` |
| Implementación de service | `XxxServiceImpl` | `PatientServiceImpl` |
| Controller REST | `XxxController` | `PatientController` |
| Mapper MapStruct | `XxxMapper` (interfaz) | `PatientMapper` |
| DTO de petición | `XxxRequest` | `CreatePatientRequest` |
| DTO de respuesta | `XxxResponse` | `PatientResponse` |
| Evento de dominio | `XxxEvent` (verbo en pasado) | `MedicalAttendanceCreatedEvent` |
| Excepción | `XxxException` | `PatientNotFoundException` |
| Configuración Spring | `XxxConfig` | `SecurityConfig` |
| Listener de eventos | `XxxEventListener` | `AuditEventListener` |
| Validador Bean Validation | `XxxValidator` | `CedulaValidator` |

### 2.3 Convención de Nombrado de Paquetes

Los nombres de paquetes Java se escriben en minúsculas, sin guiones ni guiones bajos, conforme a la *Java Language Specification*. Esta restricción del lenguaje obliga a unir términos compuestos: el módulo de Atención Médica reside en `medicalattendance` (no `medical-attendance` ni `medical_attendance`), y el módulo de Historial Clínico en `clinicalhistory`.

### 2.4 Idioma del Código

El código fuente, los nombres de clases, métodos, variables, paquetes, los mensajes de commit, la documentación inline (Javadoc), los nombres de endpoints REST y los identificadores en general se escriben en inglés. Esta convención es consistente con la práctica estándar de la industria del software y maximiza el valor del proyecto como pieza de portafolio.

Los entregables académicos, la documentación funcional y los mensajes mostrados al usuario final se redactan en español, conforme al contexto institucional del proyecto.

---

## 3. Patrón Arquitectónico: Module-First con Aislamiento `api/internal`

### 3.1 Decisión Arquitectónica

El backend de MEDISTA está organizado como un **monolito modular con aislamiento explícito entre módulos**. Cada uno de los ocho módulos funcionales del sistema reside en su propio paquete Java de primer nivel y mantiene una división interna entre dos sub-paquetes: `api/`, que contiene el contrato público del módulo, e `internal/`, que contiene su implementación privada.

Esta decisión se tomó tras evaluar tres alternativas:

- **Module-first laxo:** organización por carpetas sin restricciones de importación. Pragmático pero el aislamiento depende de la disciplina del desarrollador y se erosiona con el tiempo.
- **Module-first con aislamiento `api/internal/`** *(seleccionado)*: cada módulo expone únicamente lo que está bajo `api/`. El resto es implementación privada. La regla se *enforce* automáticamente con tests de ArchUnit.
- **Comunicación exclusiva por eventos:** desacoplamiento máximo, pero introduce complejidad de debugging y latencia que no se justifican para un sistema con un único usuario médico activo y aproximadamente 500 estudiantes.

La opción seleccionada provee los beneficios del aislamiento modular sin la sobrecarga operacional del enfoque puramente basado en eventos, y permite enforzar la regla de aislamiento de forma automatizada en el pipeline de build.

### 3.2 Anatomía de un Módulo

Cada módulo del sistema sigue exactamente la misma estructura interna de dos zonas:

**Zona `api/` — contrato público.** Contiene únicamente los elementos que otros módulos pueden importar:

- Las **interfaces** de los services expuestos hacia el exterior del módulo.
- Los **DTOs de respuesta** que viajan en esas interfaces.
- Los **eventos de dominio** que el módulo publica.
- Las **excepciones públicas** que otros módulos pueden capturar o propagar.

**Zona `internal/` — implementación privada.** Contiene la totalidad de la maquinaria interna del módulo, oculta al resto del sistema:

- `controller/` — controladores REST.
- `service/impl/` — implementaciones concretas de los services.
- `repository/` — repositorios Spring Data JPA.
- `entity/` — entidades JPA.
- `mapper/` — mappers MapStruct.
- `dto/request/` — DTOs de petición (uso exclusivo del controlador del propio módulo).
- `listener/` — listeners de eventos de otros módulos, cuando aplica.
- `exception/` — excepciones puramente internas, cuando aplica.

### 3.3 La Regla del Aislamiento

La regla del aislamiento del backend se enuncia formalmente así:

> Ninguna clase fuera del módulo `X` puede importar directa o transitivamente ningún tipo declarado bajo el paquete `ec.tecazuay.medista.X.internal`. Las únicas dependencias inter-módulo permitidas son hacia tipos declarados bajo `ec.tecazuay.medista.X.api`.

Esta regla es absoluta y se aplica a entidades JPA, repositorios, implementaciones de service, controladores, mappers y DTOs de petición. Ningún módulo accede a la base de datos de otro módulo a través de su repositorio: lo hace invocando al `XxxService` expuesto en su `api/`.

### 3.4 Comunicación entre Módulos

Existen exactamente dos mecanismos legítimos de comunicación entre módulos:

**Mecanismo 1 — Invocación directa al `api/` del módulo dependido.** Se utiliza cuando el módulo invocador necesita una respuesta sincrónica para continuar su flujo. Ejemplo: el módulo de Historial Clínico (M4) consulta atenciones del paciente invocando `MedicalAttendanceQueryService` del `api/` de M2.

**Mecanismo 2 — Publicación y consumo de eventos de dominio.** Se utiliza cuando la operación es reactiva, no requiere respuesta inmediata, y desacoplar al productor del consumidor mejora el diseño. Los eventos se publican mediante `ApplicationEventPublisher` de Spring y se consumen con `@TransactionalEventListener(phase = AFTER_COMMIT)`, lo que garantiza que un consumidor jamás reaccione a una operación que no fue persistida exitosamente. Ejemplo: M2 publica `MedicalAttendanceCreatedEvent` y M6 (Notificaciones) lo escucha para evaluar umbrales de alerta.

La regla de selección entre ambos mecanismos es la siguiente: si el flujo requiere respuesta inmediata para producir el valor de retorno del controller, se utiliza el Mecanismo 1; si el flujo es una consecuencia post-transacción que puede ejecutarse asincrónicamente, se utiliza el Mecanismo 2.

### 3.5 Enforce Automático con ArchUnit

La regla del aislamiento se *enforce* mediante una batería de tests de **ArchUnit** que se ejecutan como parte del pipeline de build estándar de Maven. La violación de cualquier regla detiene el build y previene el merge.

```java
@AnalyzeClasses(packages = "ec.tecazuay.medista")
class PackageStructureTest {

    @ArchTest
    static final ArchRule modules_should_only_depend_on_api_of_other_modules =
        noClasses()
            .that().resideInAPackage("..internal..")
            .should().dependOnClassesThat()
            .resideInAPackage("ec.tecazuay.medista.(*)..internal..")
            .as("internal de un módulo no debe ser importado desde otro módulo");

    @ArchTest
    static final ArchRule controllers_only_in_internal =
        classes().that().areAnnotatedWith(RestController.class)
            .should().resideInAPackage("..internal.controller..");

    @ArchTest
    static final ArchRule entities_only_in_internal =
        classes().that().areAnnotatedWith(Entity.class)
            .should().resideInAPackage("..internal.entity..");

    @ArchTest
    static final ArchRule repositories_only_in_internal =
        classes().that().areAssignableTo(JpaRepository.class)
            .should().resideInAPackage("..internal.repository..");
}
```

Estos tests transforman el aislamiento modular de una convención documentada en una restricción enforzable por el sistema de integración continua.

---

## 4. Estructura General del Backend

La organización física del directorio `backend/` del repositorio es la siguiente:

```
backend/
├── pom.xml
├── Dockerfile
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ec/tecazuay/medista/
│   │   │       ├── MedistaApplication.java
│   │   │       │
│   │   │       ├── config/                  ← Configuración Spring transversal
│   │   │       ├── common/                  ← Utilidades, base classes, cross-cutting
│   │   │       ├── exception/               ← GlobalExceptionHandler único
│   │   │       │
│   │   │       ├── security/                ← M7 — Seguridad y Auditoría
│   │   │       ├── patient/                 ← M1 — Gestión de Pacientes
│   │   │       ├── medicalattendance/       ← M2 — Atención Médica
│   │   │       ├── referral/                ← M3 — Referencia Médica
│   │   │       ├── clinicalhistory/         ← M4 — Historial Clínico
│   │   │       ├── dashboard/               ← M5 — Dashboard y Reportes
│   │   │       ├── notification/            ← M6 — Notificaciones Inteligentes
│   │   │       └── administration/          ← M8 — Administración del Sistema
│   │   │
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       ├── db/migration/                ← Scripts Flyway (V1__*.sql, V2__*.sql, ...)
│   │       ├── reports/                     ← Plantillas JasperReports (.jrxml)
│   │       └── static/
│   │
│   └── test/
│       ├── java/ec/tecazuay/medista/
│       │   ├── architecture/                ← Tests ArchUnit (enforce api/internal)
│       │   ├── patient/                     ← Tests unitarios e integración por módulo
│       │   ├── medicalattendance/
│       │   └── ...                          ← Un paquete de tests por módulo
│       └── resources/
│           └── application-test.yml
```

El directorio del backend contiene tres bloques bajo `src/main/java/ec/tecazuay/medista/`: la clase de arranque `MedistaApplication`, los tres paquetes transversales (`config/`, `common/`, `exception/`) y los ocho paquetes de módulos funcionales. Los paquetes están organizados en orden de dependencia conceptual: primero los transversales que todos los módulos consumen, después los módulos en orden de aparición en el sistema.

El directorio `src/main/resources/` contiene la configuración por perfil de Spring Boot, los scripts de migración Flyway, las plantillas JasperReports y los recursos estáticos servidos por la aplicación. El directorio `src/test/` espeja la estructura de paquetes de `main` y añade el paquete `architecture/` con los tests de ArchUnit.

---

## 5. Paquetes Transversales

Los paquetes transversales contienen código que es consumido por más de un módulo. Su existencia evita la duplicación y centraliza decisiones que deben ser uniformes en todo el sistema.

### 5.1 `config/`

Concentra toda la configuración Spring del backend, organizada en sub-paquetes por dominio funcional. Agrupar la configuración por dominio (en lugar de tener todas las clases `@Configuration` en una sola carpeta plana) facilita la navegación y deja explícita la responsabilidad de cada bean.

```
config/
├── security/
│   ├── SecurityConfig.java                  ← @EnableWebSecurity, SecurityFilterChain
│   ├── JwtAuthenticationFilter.java         ← OncePerRequestFilter
│   ├── JwtAuthenticationEntryPoint.java     ← Manejo de 401
│   ├── CustomAccessDeniedHandler.java       ← Manejo de 403
│   ├── PasswordEncoderConfig.java           ← BCrypt(strength=12)
│   ├── CorsConfig.java
│   └── MethodSecurityConfig.java            ← @EnableMethodSecurity para @PreAuthorize
├── persistence/
│   ├── JpaConfig.java                       ← @EnableJpaAuditing, @EnableJpaRepositories
│   ├── EnversConfig.java                    ← Configuración Hibernate Envers
│   └── FlywayConfig.java
├── cache/
│   └── RedisConfig.java                     ← RedisTemplate, CacheManager
├── web/
│   ├── WebMvcConfig.java                    ← Interceptors, message converters
│   ├── WebSocketConfig.java                 ← @EnableWebSocketMessageBroker, STOMP
│   └── JacksonConfig.java                   ← Serializers personalizados
├── ratelimit/
│   └── Bucket4jConfig.java                  ← Buckets por IP
├── async/
│   └── AsyncConfig.java                     ← @EnableAsync, ThreadPoolTaskExecutor
├── scheduling/
│   └── SchedulingConfig.java                ← @EnableScheduling
├── observability/
│   ├── PrometheusConfig.java
│   └── OpenTelemetryConfig.java
├── openapi/
│   └── OpenApiConfig.java                   ← Swagger / OpenAPI 3
└── fcm/
    └── FcmConfig.java                       ← Carga de credenciales Firebase
```

La configuración de Spring Security reside en `config/security/` (no en el módulo M7) porque es infraestructura transversal: los filtros JWT, el `SecurityFilterChain`, el `PasswordEncoder` y los handlers de 401/403 son consumidos por la totalidad del sistema. La lógica funcional de seguridad (autenticación, gestión de usuarios, audit_logs) sí reside en M7.

### 5.2 `common/`

Contiene utilidades, base classes, validadores transversales y DTOs genéricos compartidos por múltiples módulos. El nombre `common` es preferido sobre `shared` o `core` por consistencia con el ecosistema Spring y para evitar la confusión con el término técnico *core* de Domain-Driven Design.

```
common/
├── audit/
│   ├── AuditorAware.java                    ← @CreatedBy, @LastModifiedBy resolution
│   ├── BaseAuditableEntity.java             ← @MappedSuperclass con audit fields
│   └── MedistaRevisionEntity.java           ← Extiende RevisionEntity de Envers, agrega userId
├── dto/
│   ├── PageResponse.java                    ← Respuesta paginada genérica
│   ├── ErrorResponse.java                   ← Shape estándar de error
│   ├── ValidationErrorResponse.java
│   └── ApiResponse.java                     ← Wrapper opcional
├── exception/
│   ├── BusinessRuleViolationException.java  ← Excepción base de negocio (RuntimeException)
│   ├── ResourceNotFoundException.java       ← Excepción base 404
│   ├── ConflictException.java               ← Excepción base 409
│   └── ValidationException.java
├── validation/
│   ├── annotation/
│   │   ├── Cedula.java                      ← Validador de cédula ecuatoriana
│   │   ├── VitalSignRange.java
│   │   └── EnumValue.java
│   └── validator/
│       ├── CedulaValidator.java             ← Lógica del módulo 10 ecuatoriano
│       ├── VitalSignRangeValidator.java
│       └── EnumValueValidator.java
└── util/
    ├── DateUtils.java
    ├── HmacUtils.java                       ← Helpers HMAC-SHA256 para audit chain
    └── PaginationUtils.java
```

Las excepciones declaradas en `common/exception/` son las **excepciones base** de la jerarquía. Las excepciones específicas de cada módulo (por ejemplo `PatientNotFoundException`) extienden de estas bases, lo que permite que el `GlobalExceptionHandler` las mapee a códigos HTTP correctos sin necesidad de un `@ExceptionHandler` por cada subtipo.

### 5.3 `exception/`

Contiene una sola clase: el `GlobalExceptionHandler` único del backend.

```
exception/
└── GlobalExceptionHandler.java              ← @ControllerAdvice ÚNICO en todo el backend
```

La decisión de mantener un único `@ControllerAdvice` global, en lugar de uno por módulo, garantiza una respuesta de error uniforme en toda la API REST. El handler captura las excepciones declaradas en `common/exception/` y las traduce a respuestas HTTP estandarizadas según la siguiente tabla:

| Tipo de excepción | Código HTTP | Cuerpo de respuesta |
|-------------------|-------------|---------------------|
| `ResourceNotFoundException` | 404 Not Found | `ErrorResponse` |
| `BusinessRuleViolationException` | 422 Unprocessable Entity | `ErrorResponse` |
| `ConflictException` | 409 Conflict | `ErrorResponse` |
| `ValidationException` | 400 Bad Request | `ValidationErrorResponse` |
| `MethodArgumentNotValidException` | 400 Bad Request | `ValidationErrorResponse` |
| `InsufficientPermissionsException` | 403 Forbidden | `ErrorResponse` |
| `Exception` (cualquier no manejada) | 500 Internal Server Error | `ErrorResponse` |

---

## 6. Estructura por Módulo

Cada módulo del sistema sigue exactamente la misma estructura interna definida en la Sección 3. Las subsecciones siguientes especifican, para cada uno de los ocho módulos: la responsabilidad funcional del módulo, las entidades JPA que gestiona (con su correspondiente tabla del modelo de datos), el árbol de paquetes completo y las decisiones de diseño específicas.

El orden de presentación es: M7 (Seguridad) primero por ser la base sobre la que opera todo el sistema; M1, M2 y M3 a continuación por constituir el núcleo clínico con entidades reales; M4 y M5 como agregadores read-only que dependen del núcleo; M6 como módulo reactivo; y M8 al final por gestionar catálogos y configuración del sistema.

### 6.1 Módulo M7 — Seguridad y Auditoría

**Responsabilidad.** Gestiona la autenticación, autorización, recuperación de contraseña, administración de usuarios y registro inmutable de auditoría del sistema. Es el módulo que valida la identidad de todos los usuarios y registra cada acción relevante para el cumplimiento de la LOPDP y del Acuerdo MSP No. 00000125.

**Entidades JPA gestionadas.**

| Tabla del modelo de datos | Entidad JPA |
|---------------------------|-------------|
| `users` | `User` (auditada con Hibernate Envers) |
| `audit_logs` | `AuditLog` (append-only, sin Envers porque ya es inmutable por diseño) |

**Árbol de paquetes.**

```
security/
├── api/
│   ├── AuthenticationService.java               ← login, logout, refresh
│   ├── PasswordRecoveryService.java
│   ├── AuditLogService.java                     ← consulta de logs (decano/admin)
│   ├── UserService.java                         ← gestión de usuarios (consumido por M8)
│   ├── dto/
│   │   ├── LoginResponse.java                   ← contiene tokens
│   │   ├── UserResponse.java
│   │   ├── AuditLogResponse.java
│   │   └── CurrentUserResponse.java
│   ├── event/
│   │   ├── UserLoggedInEvent.java
│   │   ├── UserLoggedOutEvent.java
│   │   └── PasswordChangedEvent.java
│   └── exception/
│       ├── InvalidCredentialsException.java
│       ├── UserNotFoundException.java
│       ├── TokenExpiredException.java
│       └── InsufficientPermissionsException.java
│
└── internal/
    ├── controller/
    │   ├── AuthenticationController.java        ← /api/v1/auth/*
    │   ├── PasswordRecoveryController.java
    │   ├── UserController.java                  ← /api/v1/users (admin)
    │   └── AuditLogController.java              ← /api/v1/audit-logs (decano/admin)
    ├── service/impl/
    │   ├── AuthenticationServiceImpl.java
    │   ├── PasswordRecoveryServiceImpl.java
    │   ├── UserServiceImpl.java
    │   ├── AuditLogServiceImpl.java
    │   ├── jwt/
    │   │   ├── JwtTokenProvider.java            ← genera y valida JWT (RS256)
    │   │   ├── JwtBlacklistService.java         ← Redis-backed blacklist
    │   │   └── JwtKeyLoader.java                ← carga llaves RSA
    │   ├── audit/
    │   │   ├── AuditLogWriter.java              ← escribe audit_logs con HMAC chain
    │   │   └── HmacChainCalculator.java         ← HMAC-SHA256 encadenado
    │   └── ratelimit/
    │       └── LoginRateLimiter.java            ← Bucket4j contra Redis
    ├── repository/
    │   ├── UserRepository.java
    │   └── AuditLogRepository.java
    ├── entity/
    │   ├── User.java                            ← @Audited
    │   ├── AuditLog.java                        ← append-only
    │   └── enums/
    │       ├── Role.java                        ← role_enum: MEDICO, DECANO, ADMINISTRADOR, ESTUDIANTE
    │       └── AuditAction.java                 ← audit_action_enum
    ├── mapper/
    │   ├── UserMapper.java
    │   └── AuditLogMapper.java
    ├── listener/
    │   └── AuditEventListener.java              ← escucha eventos de TODOS los módulos
    └── dto/request/
        ├── LoginRequest.java
        ├── RefreshTokenRequest.java
        ├── PasswordRecoveryRequest.java
        ├── PasswordResetRequest.java
        ├── ChangePasswordRequest.java
        ├── CreateUserRequest.java
        └── UpdateUserRequest.java
```

**Decisiones de diseño.**

La entidad `User` reside en M7 (no en M8) porque la autenticación es una responsabilidad de seguridad, y la entidad contiene el campo `password_hash` y la relación con `Role`, ambos conceptos del dominio de seguridad. M8 consume `UserService` desde el `api/` para sus pantallas de administración de usuarios, sin acceder directamente a la entidad ni al repositorio.

El `AuditEventListener` es el componente más transversal del sistema. Suscribe a todos los eventos de dominio relevantes publicados por cualquier módulo y los traduce a entradas inmutables de la tabla `audit_logs`. La escritura aplica encadenamiento HMAC-SHA256 para garantizar la detección de manipulación posterior de los registros.

La configuración de Spring Security (filtros, `SecurityFilterChain`, beans de codificación de contraseña) **no** reside en este módulo: vive en `config/security/` por ser infraestructura transversal consumida por todo el sistema.

### 6.2 Módulo M1 — Gestión de Pacientes

**Responsabilidad.** Gestiona el registro de pacientes (estudiantes), sus datos demográficos y sus antecedentes familiares y personales acumulados. Es la fuente de verdad para la identidad clínica de los estudiantes del sistema.

**Entidades JPA gestionadas.**

| Tabla del modelo de datos | Entidad JPA |
|---------------------------|-------------|
| `patients` | `Patient` (auditada con Hibernate Envers) |
| `patient_background` | `PatientBackground` (auditada con Hibernate Envers) |

**Árbol de paquetes.**

```
patient/
├── api/
│   ├── PatientService.java
│   ├── PatientBackgroundService.java
│   ├── dto/
│   │   ├── PatientResponse.java
│   │   ├── PatientSummaryResponse.java       ← versión liviana para listados
│   │   └── PatientBackgroundResponse.java
│   ├── event/
│   │   ├── PatientRegisteredEvent.java
│   │   └── PatientDeactivatedEvent.java
│   └── exception/
│       ├── PatientNotFoundException.java
│       └── DuplicateCedulaException.java
│
└── internal/
    ├── controller/
    │   ├── PatientController.java               ← /api/v1/patients
    │   └── PatientBackgroundController.java     ← /api/v1/patients/{id}/background
    ├── service/impl/
    │   ├── PatientServiceImpl.java
    │   └── PatientBackgroundServiceImpl.java
    ├── repository/
    │   ├── PatientRepository.java
    │   └── PatientBackgroundRepository.java
    ├── entity/
    │   ├── Patient.java                         ← @Audited
    │   ├── PatientBackground.java               ← @Audited
    │   └── enums/
    │       ├── Gender.java                      ← gender_enum
    │       ├── MaritalStatus.java               ← marital_status_enum
    │       └── BloodType.java                   ← blood_type_enum
    ├── mapper/
    │   └── PatientMapper.java
    └── dto/request/
        ├── CreatePatientRequest.java
        ├── UpdatePatientRequest.java
        └── UpdatePatientBackgroundRequest.java
```

**Decisiones de diseño.**

El catálogo de carreras académicas (`careers`) **no reside en M1** a pesar de que `Patient` lo referencia mediante FK. La entidad `Career` reside en M8 (Administración del Sistema) por ser un catálogo administrado por el Administrador del Sistema. M1 consume `CareerCatalogService` desde el `api/` de M8 cuando necesita validar o presentar carreras.

`PatientBackgroundService` se separa de `PatientService` porque ambas entidades tienen ciclos de vida distintos: el paciente se crea una sola vez, mientras que los antecedentes se actualizan progresivamente a lo largo de múltiples consultas. Mantenerlos como services separados respeta el principio de responsabilidad única y evita un service inflado con responsabilidades mezcladas.

### 6.3 Módulo M2 — Atención Médica

**Responsabilidad.** Gestiona el registro y consulta de atenciones médicas, sus diagnósticos asociados, los hallazgos del examen físico, los datos obstétricos condicionales, los exámenes complementarios con archivos adjuntos, las notas de corrección sobre atenciones inmutables y la consulta del catálogo CIE-10 con autocompletado por trigrama.

Es el módulo más grande del sistema: implementa la digitalización completa del formulario físico de atención médica del Departamento Médico.

**Entidades JPA gestionadas.**

| Tabla del modelo de datos | Entidad JPA |
|---------------------------|-------------|
| `medical_attendances` | `MedicalAttendance` (auditada con Hibernate Envers, inmutable tras creación) |
| `attendance_corrections` | `AttendanceCorrection` (append-only) |
| `complementary_exams` | `ComplementaryExam` (auditada) |
| `diagnoses` | `Diagnosis` (auditada) |
| `physical_exam_findings` | `PhysicalExamFinding` (auditada) |
| `obstetric_emergency` | `ObstetricEmergency` (auditada) |
| `cie10_codes` | `Cie10Code` (catálogo, no auditado) |

**Árbol de paquetes.**

```
medicalattendance/
├── api/
│   ├── MedicalAttendanceService.java
│   ├── MedicalAttendanceQueryService.java       ← consultas read-only para otros módulos
│   ├── AttendanceCorrectionService.java
│   ├── ComplementaryExamService.java
│   ├── DiagnosisService.java
│   ├── Cie10CatalogService.java                 ← consulta read-only del catálogo
│   ├── dto/
│   │   ├── MedicalAttendanceResponse.java
│   │   ├── MedicalAttendanceSummaryResponse.java
│   │   ├── DiagnosisResponse.java
│   │   ├── PhysicalExamFindingResponse.java
│   │   ├── ObstetricEmergencyResponse.java
│   │   ├── ComplementaryExamResponse.java
│   │   ├── AttendanceCorrectionResponse.java
│   │   └── Cie10CodeResponse.java
│   ├── event/
│   │   ├── MedicalAttendanceCreatedEvent.java   ← consumido por M6 (umbrales) y M7 (audit)
│   │   ├── DiagnosisRecordedEvent.java          ← consumido por M5 y M6
│   │   └── AttendanceCorrectedEvent.java
│   └── exception/
│       ├── MedicalAttendanceNotFoundException.java
│       ├── ImmutableAttendanceException.java
│       ├── InvalidVitalSignsException.java
│       ├── InvalidGlasgowScaleException.java
│       └── Cie10CodeNotFoundException.java
│
└── internal/
    ├── controller/
    │   ├── MedicalAttendanceController.java     ← /api/v1/attendances
    │   ├── AttendanceCorrectionController.java  ← /api/v1/attendances/{id}/corrections
    │   ├── ComplementaryExamController.java     ← upload/download archivos
    │   └── Cie10CatalogController.java          ← /api/v1/cie10 (autocomplete)
    ├── service/impl/
    │   ├── MedicalAttendanceServiceImpl.java
    │   ├── AttendanceCorrectionServiceImpl.java
    │   ├── ComplementaryExamServiceImpl.java
    │   ├── DiagnosisServiceImpl.java
    │   ├── Cie10CatalogServiceImpl.java
    │   ├── BmiCalculator.java                   ← cálculo IMC
    │   ├── GlasgowCalculator.java               ← cálculo Total Glasgow
    │   └── VitalSignsValidator.java             ← validación rangos clínicos
    ├── repository/
    │   ├── MedicalAttendanceRepository.java
    │   ├── AttendanceCorrectionRepository.java
    │   ├── ComplementaryExamRepository.java
    │   ├── DiagnosisRepository.java
    │   ├── PhysicalExamFindingRepository.java
    │   ├── ObstetricEmergencyRepository.java
    │   └── Cie10CodeRepository.java             ← @Query con pg_trgm para búsqueda difusa
    ├── entity/
    │   ├── MedicalAttendance.java               ← @Audited, sin setters de campos clínicos
    │   ├── AttendanceCorrection.java            ← append-only
    │   ├── ComplementaryExam.java               ← @Audited
    │   ├── Diagnosis.java                       ← @Audited
    │   ├── PhysicalExamFinding.java             ← @Audited
    │   ├── ObstetricEmergency.java              ← @Audited
    │   ├── Cie10Code.java                       ← catálogo, no auditado
    │   └── enums/
    │       ├── BodySystem.java                  ← body_system_enum
    │       └── DiagnosisStatus.java             ← diagnosis_status_enum
    ├── mapper/
    │   ├── MedicalAttendanceMapper.java
    │   ├── DiagnosisMapper.java
    │   ├── ComplementaryExamMapper.java
    │   └── Cie10CodeMapper.java
    ├── storage/
    │   ├── ComplementaryExamFileStorage.java    ← interfaz Strategy
    │   └── LocalFileSystemStorageImpl.java      ← implementación filesystem local
    └── dto/request/
        ├── CreateMedicalAttendanceRequest.java  ← agregado completo
        ├── PhysicalExamFindingRequest.java
        ├── ObstetricEmergencyRequest.java
        ├── DiagnosisRequest.java
        ├── ComplementaryExamRequest.java
        ├── CreateAttendanceCorrectionRequest.java
        └── Cie10SearchRequest.java
```

**Decisiones de diseño.**

`BmiCalculator` y `GlasgowCalculator` son clases independientes en lugar de métodos privados del service. Esta separación responde al ADR-005 del Modelo de Datos: ambos valores se persisten para preservar exactitud histórica, y el cálculo es lógica clínica testeable que conviene mantener aislada para que evolucione sin afectar al service.

El `ComplementaryExamFileStorage` se define como interfaz con una implementación concreta `LocalFileSystemStorageImpl` que escribe al sistema de ficheros del servidor, conforme al ADR del Modelo de Datos que excluye el almacenamiento de BLOBs en PostgreSQL. La definición como interfaz aplica el patrón Strategy y permite sustituir la implementación por un backend de almacenamiento distinto (S3, MinIO) en futuras versiones sin modificar el service.

Los eventos de dominio publicados por este módulo (`MedicalAttendanceCreatedEvent`, `DiagnosisRecordedEvent`, `AttendanceCorrectedEvent`) se publican mediante `ApplicationEventPublisher` y son consumidos en fase `AFTER_COMMIT`. Esto garantiza que un evento jamás se dispare por una operación que falló su transacción.

`Cie10CatalogService` es read-only y vive en M2 porque las atenciones médicas son su consumidor principal. La administración del catálogo (carga masiva inicial, actualización periódica) reside en M8 mediante `Cie10CatalogAdminService`. Ambos services operan sobre la misma tabla pero atienden propósitos disjuntos.

### 6.4 Módulo M3 — Referencia Médica

**Responsabilidad.** Gestiona la generación y consulta de referencias médicas hacia establecimientos de salud externos, los diagnósticos asociados a cada referencia y el catálogo de establecimientos de salud destino.

**Entidades JPA gestionadas.**

| Tabla del modelo de datos | Entidad JPA |
|---------------------------|-------------|
| `medical_referrals` | `MedicalReferral` (auditada) |
| `referral_diagnoses` | `ReferralDiagnosis` (auditada) |
| `health_establishments` | `HealthEstablishment` (auditada) |

**Árbol de paquetes.**

```
referral/
├── api/
│   ├── MedicalReferralService.java
│   ├── HealthEstablishmentService.java          ← consulta del catálogo
│   ├── dto/
│   │   ├── MedicalReferralResponse.java
│   │   ├── ReferralDiagnosisResponse.java
│   │   └── HealthEstablishmentResponse.java
│   ├── event/
│   │   └── MedicalReferralCreatedEvent.java     ← consumido por M5 y M7
│   └── exception/
│       ├── MedicalReferralNotFoundException.java
│       └── HealthEstablishmentNotFoundException.java
│
└── internal/
    ├── controller/
    │   ├── MedicalReferralController.java       ← /api/v1/referrals
    │   └── HealthEstablishmentController.java   ← /api/v1/health-establishments
    ├── service/impl/
    │   ├── MedicalReferralServiceImpl.java
    │   └── HealthEstablishmentServiceImpl.java
    ├── repository/
    │   ├── MedicalReferralRepository.java
    │   ├── ReferralDiagnosisRepository.java
    │   └── HealthEstablishmentRepository.java
    ├── entity/
    │   ├── MedicalReferral.java                 ← @Audited
    │   ├── ReferralDiagnosis.java               ← @Audited
    │   ├── HealthEstablishment.java             ← @Audited
    │   └── enums/
    │       ├── ReferralReason.java              ← referral_reason_enum
    │       └── EstablishmentEntityType.java
    ├── mapper/
    │   ├── MedicalReferralMapper.java
    │   └── HealthEstablishmentMapper.java
    └── dto/request/
        ├── CreateMedicalReferralRequest.java
        ├── ReferralDiagnosisRequest.java
        └── CreateHealthEstablishmentRequest.java
```

**Decisiones de diseño.**

La relación opcional entre `MedicalReferral` y `MedicalAttendance` (definida en el modelo de datos) implica que una referencia puede generarse desde una atención existente o como acto independiente. M3 no escucha eventos de M2: el flujo de creación es iniciado siempre por el usuario desde el frontend, que decide si la referencia se vincula a una atención o no.

El catálogo `health_establishments` se mantiene en M3 a pesar de ser administrado por el rol Administrador del Sistema. La razón es funcional: la entidad y su CRUD pertenecen conceptualmente al dominio de referencias médicas, donde se utilizan. M8 invoca `HealthEstablishmentService` desde el `api/` para sus pantallas de administración de catálogos.

### 6.5 Módulo M4 — Historial Clínico

**Responsabilidad.** Provee la visualización agregada del historial clínico de un paciente, combinando datos de los módulos M1, M2 y M3 en un único response coherente. Sirve dos perfiles de consumo distintos: la vista administrativa (médico, decano) que accede al historial completo de cualquier paciente, y la vista del estudiante a través de la app móvil, que accede únicamente a su propio historial.

**Entidades JPA gestionadas.** Ninguna. M4 es un módulo agregador read-only que consume las APIs públicas de M1, M2 y M3.

**Árbol de paquetes.**

```
clinicalhistory/
├── api/
│   ├── ClinicalHistoryService.java
│   ├── dto/
│   │   ├── ClinicalHistoryResponse.java         ← agregado completo
│   │   ├── AttendanceTimelineEntryResponse.java ← entrada de la línea de tiempo
│   │   └── MyClinicalHistoryResponse.java       ← vista restrictiva del estudiante
│   └── exception/
│       └── UnauthorizedHistoryAccessException.java
│
└── internal/
    ├── controller/
    │   ├── ClinicalHistoryController.java       ← /api/v1/patients/{id}/history
    │   └── MyClinicalHistoryController.java     ← /api/v1/me/history (app móvil)
    ├── service/impl/
    │   ├── ClinicalHistoryServiceImpl.java
    │   └── ClinicalHistoryAggregator.java       ← orquesta llamadas a api/ de M1, M2, M3
    └── dto/request/
        └── ClinicalHistoryFilterRequest.java
```

**Decisiones de diseño.**

`ClinicalHistoryAggregator` es el componente central del módulo. Importa exclusivamente desde `patient.api`, `medicalattendance.api` y `referral.api`. Bajo ninguna circunstancia accede a entidades JPA o repositorios de otros módulos. Este aislamiento garantiza que M4 jamás bypase la lógica de negocio implementada en los services de los módulos dependidos.

Los controladores se separan por audiencia: `ClinicalHistoryController` para personal administrativo y `MyClinicalHistoryController` para estudiantes. La separación responde a tres diferencias sustantivas: rutas distintas, autorización distinta (`@PreAuthorize` con roles diferentes) y DTOs de respuesta con niveles de detalle distintos. Mezclarlos en un único controlador introduciría lógica condicional basada en rol dentro del controller, anti-patrón que viola el principio de controlador delgado.

### 6.6 Módulo M5 — Dashboard y Reportes

**Responsabilidad.** Provee las consultas de agregación estadística del sistema y la generación de reportes en formato PDF y Excel. Sirve los KPIs del dashboard institucional, los datos para los gráficos cross-filtros de Chart.js y los reportes programados configurados en M8.

**Entidades JPA gestionadas.** Ninguna. M5 consulta tablas clínicas mediante queries de agregación con projections. No requiere almacenamiento dedicado dado el volumen de datos del sistema (~500 pacientes).

**Árbol de paquetes.**

```
dashboard/
├── api/
│   ├── DashboardService.java
│   ├── ReportGenerationService.java
│   ├── dto/
│   │   ├── DashboardSummaryResponse.java        ← KPIs y totales
│   │   ├── AttendancesByCareerResponse.java
│   │   ├── DiagnosesByMonthResponse.java
│   │   ├── ReferralsByEstablishmentResponse.java
│   │   ├── VitalSignsTrendResponse.java
│   │   └── ChartDataResponse.java               ← genérico para Chart.js
│   └── exception/
│       └── ReportGenerationException.java
│
└── internal/
    ├── controller/
    │   ├── DashboardController.java             ← /api/v1/dashboard/*
    │   └── ReportController.java                ← /api/v1/reports/*
    ├── service/impl/
    │   ├── DashboardServiceImpl.java
    │   ├── ReportGenerationServiceImpl.java
    │   ├── PdfReportGenerator.java              ← integración JasperReports
    │   ├── ExcelReportGenerator.java            ← integración Apache POI
    │   └── DashboardQueryBuilder.java           ← arma queries de agregación
    ├── repository/
    │   ├── DashboardStatisticsRepository.java   ← @Query nativas de agregación
    │   └── projection/
    │       ├── AttendanceCountByCareer.java
    │       ├── DiagnosisFrequency.java
    │       └── MonthlyAttendanceCount.java
    └── dto/request/
        └── DashboardFilterRequest.java
```

**Decisiones de diseño.**

`DashboardStatisticsRepository` es el único repositorio del sistema que no está vinculado a una entidad concreta. Define queries nativas de agregación cross-table mediante `@Query` y mapea resultados a *interface projections* declaradas en `repository/projection/`. Esta es la forma idiomática que Spring Data ofrece para repositorios de consulta de solo lectura sobre múltiples tablas; introducir una entidad JPA artificial únicamente para soportar consultas de agregación constituiría un anti-patrón.

`PdfReportGenerator` y `ExcelReportGenerator` son clases independientes en lugar de métodos del service principal. Sus APIs subyacentes (JasperReports y Apache POI) son sustancialmente distintas y combinarlas en una sola clase produciría un componente con dos responsabilidades disjuntas. La separación facilita la evolución independiente de cada generador y permite testearlos de forma aislada.

Las plantillas JasperReports (`.jrxml`) residen en `src/main/resources/reports/`, no en el paquete Java, conforme a la convención estándar de la librería. El generador las carga en tiempo de ejecución desde el classpath.

### 6.7 Módulo M6 — Notificaciones Inteligentes

**Responsabilidad.** Gestiona las notificaciones del sistema: recepción de eventos de dominio de otros módulos, evaluación de umbrales configurables, persistencia de notificaciones generadas, entrega en tiempo real al cliente web mediante WebSocket/STOMP y entrega push al cliente móvil mediante Firebase Cloud Messaging.

**Entidades JPA gestionadas.**

| Tabla del modelo de datos | Entidad JPA |
|---------------------------|-------------|
| `notifications` | `Notification` (no auditada — no es dato clínico) |
| `notification_thresholds` | `NotificationThreshold` (auditada — los cambios sí se auditan) |

**Árbol de paquetes.**

```
notification/
├── api/
│   ├── NotificationService.java
│   ├── PushNotificationService.java             ← envío directo (uso por otros módulos)
│   ├── dto/
│   │   ├── NotificationResponse.java
│   │   └── NotificationThresholdResponse.java
│   ├── event/
│   │   └── NotificationDeliveredEvent.java
│   └── exception/
│       ├── NotificationNotFoundException.java
│       └── PushDeliveryException.java
│
└── internal/
    ├── controller/
    │   ├── NotificationController.java          ← /api/v1/notifications
    │   └── NotificationThresholdController.java ← gestión de umbrales (admin)
    ├── service/impl/
    │   ├── NotificationServiceImpl.java
    │   ├── PushNotificationServiceImpl.java     ← integra FCM
    │   └── ThresholdEvaluationService.java      ← evalúa umbrales contra eventos
    ├── repository/
    │   ├── NotificationRepository.java
    │   └── NotificationThresholdRepository.java
    ├── entity/
    │   ├── Notification.java
    │   ├── NotificationThreshold.java           ← @Audited
    │   └── enums/
    │       ├── NotificationType.java            ← notification_type_enum
    │       └── ThresholdType.java               ← threshold_type_enum
    ├── mapper/
    │   └── NotificationMapper.java
    ├── listener/
    │   ├── MedicalAttendanceEventListener.java  ← escucha M2
    │   ├── DiagnosisEventListener.java          ← escucha M2
    │   └── MedicalReferralEventListener.java    ← escucha M3
    ├── websocket/
    │   ├── NotificationWebSocketController.java ← @MessageMapping STOMP
    │   └── NotificationWebSocketBroadcaster.java
    ├── fcm/
    │   ├── FcmClient.java                       ← cliente HTTP a FCM HTTP v1 API
    │   └── FcmMessageBuilder.java
    └── dto/request/
        ├── CreateNotificationRequest.java
        └── CreateNotificationThresholdRequest.java
```

**Decisiones de diseño.**

M6 es el módulo más reactivo del sistema. Su patrón de integración predominante es la escucha de eventos publicados por M2 y M3, traducidos a notificaciones por el `ThresholdEvaluationService`. Los listeners residen en `internal/listener/` y son la única dependencia entrante de M6 hacia las APIs de M2 y M3 (importan los eventos publicados desde sus respectivos paquetes `api/event/`).

Los canales de salida (WebSocket y FCM) se aíslan en sub-paquetes propios (`websocket/`, `fcm/`) por dos razones: utilizan librerías y configuraciones distintas, y representan tecnologías de entrega que pueden evolucionar de forma independiente. Esta separación facilita el reemplazo o adición de canales (por ejemplo, correo electrónico) en versiones futuras.

`ThresholdEvaluationService` se mantiene como clase independiente y no como método del `NotificationService` porque su lógica es la responsabilidad reactiva más compleja del sistema: evalúa umbrales por carrera, por diagnóstico, por frecuencia y por evento aislado. La separación facilita la adición de nuevos tipos de umbral sin modificar el service principal.

### 6.8 Módulo M8 — Administración del Sistema

**Responsabilidad.** Gestiona los catálogos del sistema (carreras académicas, códigos CIE-10), la configuración de respaldos automáticos de base de datos, la programación de reportes automáticos y la consulta del estado de salud del sistema.

**Entidades JPA gestionadas.**

| Tabla del modelo de datos | Entidad JPA |
|---------------------------|-------------|
| `careers` | `Career` (auditada — los cambios administrativos se auditan) |
| `backup_logs` | `BackupLog` (append-only) |
| `automatic_reports` | `AutomaticReport` (auditada) |

**Árbol de paquetes.**

```
administration/
├── api/
│   ├── CareerCatalogService.java
│   ├── Cie10CatalogAdminService.java            ← admin del catálogo (carga masiva)
│   ├── BackupService.java
│   ├── AutomaticReportService.java
│   ├── SystemConfigurationService.java
│   ├── dto/
│   │   ├── CareerResponse.java
│   │   ├── BackupLogResponse.java
│   │   ├── AutomaticReportResponse.java
│   │   └── SystemHealthResponse.java
│   └── exception/
│       ├── CareerNotFoundException.java
│       ├── BackupExecutionException.java
│       └── DuplicateCareerException.java
│
└── internal/
    ├── controller/
    │   ├── CareerController.java                ← /api/v1/admin/careers
    │   ├── Cie10AdminController.java            ← /api/v1/admin/cie10
    │   ├── BackupController.java                ← /api/v1/admin/backups
    │   ├── AutomaticReportController.java
    │   └── SystemHealthController.java          ← /api/v1/admin/health
    ├── service/impl/
    │   ├── CareerCatalogServiceImpl.java
    │   ├── Cie10CatalogAdminServiceImpl.java
    │   ├── BackupServiceImpl.java
    │   ├── AutomaticReportServiceImpl.java
    │   ├── SystemConfigurationServiceImpl.java
    │   └── scheduler/
    │       ├── BackupScheduler.java             ← @Scheduled
    │       ├── AutomaticReportScheduler.java
    │       └── Cie10CacheRefreshScheduler.java
    ├── repository/
    │   ├── CareerRepository.java
    │   ├── BackupLogRepository.java
    │   └── AutomaticReportRepository.java
    ├── entity/
    │   ├── Career.java                          ← @Audited
    │   ├── BackupLog.java                       ← append-only
    │   ├── AutomaticReport.java                 ← @Audited
    │   └── enums/
    │       ├── BackupTrigger.java
    │       ├── BackupStatus.java
    │       └── ReportFrequency.java
    ├── mapper/
    │   ├── CareerMapper.java
    │   └── AutomaticReportMapper.java
    └── dto/request/
        ├── CreateCareerRequest.java
        ├── UpdateCareerRequest.java
        ├── Cie10BulkLoadRequest.java
        └── CreateAutomaticReportRequest.java
```

**Decisiones de diseño.**

Todos los componentes anotados con `@Scheduled` del backend residen en `administration/internal/service/impl/scheduler/`. Esta concentración facilita la inspección y auditoría de todos los procesos autónomos del sistema desde un único punto del código.

`Cie10CatalogAdminService` y `Cie10CatalogService` (M2) coexisten sobre la misma tabla `cie10_codes`. El primero implementa la carga masiva inicial y la actualización del catálogo, ambas operaciones administrativas; el segundo provee búsqueda difusa con `pg_trgm` para autocompletado durante el registro de atenciones. Esta coexistencia de dos services con propósitos disjuntos sobre la misma entidad es un patrón válido cuando los modos de uso son sustantivamente diferentes.

`Career` reside en M8 (no en M1) porque su gestión es competencia exclusiva del Administrador del Sistema. M1 consume el catálogo a través de `CareerCatalogService` desde el `api/` de M8.

---

## 7. Patrones de Diseño Aplicados

La siguiente tabla consolida los patrones de diseño implementados en el backend, su propósito, su ubicación física en el árbol de paquetes y un ejemplo concreto del sistema.

| Patrón | Propósito | Ubicación | Ejemplo |
|--------|-----------|-----------|---------|
| **Module API/Internal** | Aislamiento entre módulos del monolito | Subpaquetes `api/` e `internal/` de cada módulo | `patient.api.PatientService` (público) vs `patient.internal.service.impl.PatientServiceImpl` (privado) |
| **Service Layer** | Concentración de lógica de negocio en una capa dedicada, separada del controlador | Interfaces en `<modulo>.api.*Service`; implementaciones en `<modulo>.internal.service.impl.*ServiceImpl` | `MedicalAttendanceService` / `MedicalAttendanceServiceImpl` |
| **Repository** | Abstracción del acceso a datos mediante interfaces Spring Data JPA | `<modulo>.internal.repository.*Repository` | `PatientRepository extends JpaRepository<Patient, Long>` |
| **DTO (Request/Response)** | Aislamiento del modelo de persistencia respecto al contrato de la API | Request: `<modulo>.internal.dto.request.*Request`. Response: `<modulo>.api.dto.*Response` | `CreatePatientRequest` (entrada) / `PatientResponse` (salida) |
| **Mapper (MapStruct)** | Conversión Entity ↔ DTO en tiempo de compilación, sin reflexión | `<modulo>.internal.mapper.*Mapper` | `PatientMapper` con métodos `toResponse(Patient)`, `toEntity(CreatePatientRequest)` |
| **Thin Controller** | Controlador limitado a recibir, validar, delegar y retornar | `<modulo>.internal.controller.*Controller` | `PatientController` invoca `PatientService` y retorna directamente el resultado mapeado |
| **Centralized Exception Handling** | Único punto de traducción de excepciones a respuestas HTTP | `exception.GlobalExceptionHandler` (`@ControllerAdvice`) | Todas las excepciones se mapean en una sola clase a `ErrorResponse` con código HTTP correcto |
| **Audit Pattern (Envers)** | Versionado automático de entidades clínicas | Anotación `@Audited` en entidades de `<modulo>.internal.entity.*` | `MedicalAttendance` produce automáticamente `medical_attendances_aud` |
| **Audit Pattern (HMAC chain)** | Registro inmutable de acciones de seguridad con detección de manipulación | `security.internal.service.impl.audit.*` | `AuditLogWriter` + `HmacChainCalculator` escriben encadenando con HMAC-SHA256 |
| **Domain Events** | Comunicación desacoplada entre módulos | Eventos en `<modulo>.api.event.*Event`. Listeners en `<modulo>.internal.listener.*EventListener` | M2 publica `MedicalAttendanceCreatedEvent`; M6 lo consume con `@TransactionalEventListener(AFTER_COMMIT)` |
| **Strategy** | Sustitución de implementaciones concretas detrás de una interfaz | Interfaces en `<modulo>.internal.storage` u otros sub-paquetes específicos | `ComplementaryExamFileStorage` con implementación `LocalFileSystemStorageImpl` |
| **Builder (Lombok)** | Construcción fluida y legible de objetos complejos | Anotación `@Builder` en DTOs y entidades complejas | `MedicalAttendance.builder().patient(p).attendedBy(u)...build()` |
| **Specification (planificado)** | Consultas dinámicas con filtros componibles | `<modulo>.internal.repository.specification.*Specification` cuando aplique | Filtros del Dashboard combinables (carrera + género + rango fechas) |

---

## 8. Dependencias entre Módulos

### 8.1 Diagrama de Dependencias

```
                    ┌─────────────┐
                    │  security   │ ← M7 (autoriza a todos)
                    └──────▲──────┘
                           │
                           │
   ┌─────────┐      ┌──────┴──────┐      ┌──────────────┐
   │ patient │◄─────┤medicalattend│─────►│   referral   │
   │   M1    │      │    M2       │      │     M3       │
   └────▲────┘      └──────▲──────┘      └──────▲───────┘
        │                  │                    │
        │  api/            │  api/              │  api/
        └──────────────────┼────────────────────┘
                           │
              ┌────────────┴────────────┐
              │                         │
       ┌──────▼─────────┐      ┌────────▼────────┐      ┌──────────────┐
       │clinicalhistory │      │   dashboard     │      │ notification │
       │      M4        │      │      M5         │      │      M6      │
       └────────────────┘      └─────────────────┘      └──────▲───────┘
                                                               │
                                                       (escucha eventos)
                                                               │
                                                        de M2, M3, M7

       ┌────────────────┐
       │ administration │ ← M8 (catálogos consumidos por M1, M2, M3)
       └────────────────┘
```

### 8.2 Tabla de Dependencias Permitidas

| Módulo | Depende de (api/) | Publica eventos | Escucha eventos de |
|--------|-------------------|-----------------|--------------------|
| M7 — security | — | `UserLoggedInEvent`, `UserLoggedOutEvent`, `PasswordChangedEvent` | Todos (vía `AuditEventListener`) |
| M1 — patient | M7, M8 | `PatientRegisteredEvent`, `PatientDeactivatedEvent` | — |
| M2 — medicalattendance | M1, M7, M8 | `MedicalAttendanceCreatedEvent`, `DiagnosisRecordedEvent`, `AttendanceCorrectedEvent` | — |
| M3 — referral | M1, M2, M7, M8 | `MedicalReferralCreatedEvent` | — |
| M4 — clinicalhistory | M1, M2, M3, M7 | — | — |
| M5 — dashboard | M1, M2, M3, M7 | — | — |
| M6 — notification | M7 | `NotificationDeliveredEvent` | M2, M3 |
| M8 — administration | M7 | — | — |

### 8.3 Reglas de Circulación

El grafo de dependencias del backend está organizado en **cuatro capas conceptuales** que fluyen de arriba hacia abajo sin ciclos:

1. **Capa transversal:** M7 (Seguridad) sostiene la autorización de todo el sistema y es consumido por todos los módulos restantes.
2. **Núcleo clínico:** M1, M2 y M3 contienen las entidades clínicas reales del sistema. M2 depende de M1 (toda atención requiere paciente). M3 depende de M2 y M1 (las referencias se vinculan opcionalmente a atenciones).
3. **Agregadores y reactivos:** M4 y M5 son consumidores read-only del núcleo. M6 es reactivo y consume eventos publicados por el núcleo.
4. **Administración:** M8 gestiona catálogos consumidos por el núcleo y configuración del sistema.

La regla fundamental que preserva la ausencia de ciclos es la siguiente: **un módulo de capa superior nunca puede ser dependido por un módulo de capa inferior**. M1 jamás importa de M4; M2 jamás importa de M6. El acoplamiento inverso, cuando es necesario, se implementa exclusivamente mediante eventos de dominio.

---

## 9. Estructura de Tests

El directorio `src/test/java/ec/tecazuay/medista/` espeja la estructura de paquetes de `src/main/java`, con un sub-paquete dedicado por módulo y un sub-paquete adicional `architecture/` que contiene los tests de ArchUnit que enforzan las reglas estructurales documentadas en este documento.

```
src/test/java/ec/tecazuay/medista/
├── architecture/
│   ├── PackageStructureTest.java       ← enforce api/internal aislamiento
│   ├── LayerDependencyTest.java        ← enforce capas (controller→service→repository)
│   └── NamingConventionTest.java       ← enforce convenciones de nombrado
├── common/
├── security/
├── patient/
├── medicalattendance/
├── referral/
├── clinicalhistory/
├── dashboard/
├── notification/
└── administration/
```

### 9.1 Convenciones de Nombrado de Tests

| Tipo de test | Sufijo | Herramienta | Ejemplo |
|--------------|--------|-------------|---------|
| Test unitario de service | `Test` | JUnit 5 + Mockito | `PatientServiceImplTest` |
| Test de integración de repositorio | `IT` | JUnit 5 + Testcontainers (PostgreSQL) | `PatientRepositoryIT` |
| Test de integración de controller | `IT` | JUnit 5 + `@SpringBootTest` + MockMvc | `PatientControllerIT` |
| Test de ArchUnit | `Test` (en `architecture/`) | ArchUnit | `PackageStructureTest` |

### 9.2 Estrategia de Aislamiento

Los tests unitarios mockean dependencias externas (otros services, repositorios) y verifican la lógica del componente bajo prueba en aislamiento. Los tests de integración utilizan Testcontainers para levantar un contenedor PostgreSQL real, garantizando que el comportamiento de Hibernate, Flyway, Envers y `pg_trgm` se verifica contra la misma versión del motor que se utiliza en producción.

---

## 10. Decisiones de Diseño (ADRs Resumidos)

Este capítulo registra las decisiones arquitectónicas no obvias de la estructura de paquetes. Cada ADR documenta el contexto, la decisión tomada, su justificación y sus consecuencias.

### ADR-PKG-001 — Aislamiento Module-First con `api/internal/`

**Contexto.** El backend monolítico modular requiere una estrategia de organización interna que permita evolucionar cada módulo de forma independiente sin que la disciplina del desarrollador sea el único mecanismo de control.

**Decisión.** Cada módulo se divide en `api/` (contrato público) e `internal/` (implementación privada). La regla se enforza con tests de ArchUnit que detienen el build si se viola.

**Justificación.** Combina el pragmatismo del módulo-first laxo con el rigor del aislamiento real. Hace explícito el contrato público de cada módulo y permite que terceros lo consuman sin acoplarse a detalles de implementación.

**Consecuencias.** Aumenta ligeramente la cantidad de archivos por módulo (cada service tiene interfaz pública + impl privada), pero la inversión se recupera al primer cambio interno que se quiere hacer sin afectar consumidores externos.

### ADR-PKG-002 — `User` reside en `security/`, no en `administration/`

**Contexto.** La entidad `User` representa al usuario del sistema. Es gestionada operacionalmente por el rol Administrador (M8) pero contiene atributos de seguridad (`password_hash`, `role`).

**Decisión.** `User` es entidad JPA del módulo M7 (Seguridad). M8 consume `UserService` desde `security.api` para sus operaciones de gestión.

**Justificación.** La autenticación es responsabilidad del módulo de seguridad. Mover `User` a M8 obligaría a importar tipos de seguridad (`Role`, password hashing) en un módulo de catálogos administrativos, invirtiendo la dirección natural de las dependencias.

**Consecuencias.** M8 no accede directamente a `User`; opera sobre `UserResponse` y publica intenciones mediante `CreateUserRequest`/`UpdateUserRequest` que viajan a través de `UserService`.

### ADR-PKG-003 — Catálogos en el Módulo que los Usa

**Contexto.** El sistema mantiene tres catálogos: `careers`, `cie10_codes`, `health_establishments`. Todos son administrados por el rol Administrador (M8) pero son consumidos intensivamente por M1, M2 y M3 respectivamente.

**Decisión.** Las entidades catálogo residen en el módulo donde son consumidas con mayor frecuencia. `Career` reside en `administration/` (caso especial donde el uso administrativo predomina). `Cie10Code` reside en M2. `HealthEstablishment` reside en M3. Cuando M8 necesita administrar un catálogo que reside en otro módulo, lo hace a través del `api/` del módulo propietario.

**Justificación.** Mantener todos los catálogos en M8 produciría dependencias del núcleo clínico hacia un módulo administrativo, invirtiendo la dirección conceptual. Mantenerlos en su módulo de uso conserva la cohesión funcional.

**Consecuencias.** M8 expone services de administración (`Cie10CatalogAdminService`) sobre catálogos cuya entidad reside en otro módulo. La administración se realiza mediante el `api/` del módulo dueño.

### ADR-PKG-004 — `GlobalExceptionHandler` Único

**Contexto.** Spring permite múltiples `@ControllerAdvice`, uno por módulo si se desea. La pregunta es si el manejo de excepciones debe estar centralizado o distribuido.

**Decisión.** Existe una única clase `GlobalExceptionHandler` en el paquete transversal `exception/`.

**Justificación.** La forma de las respuestas de error debe ser idéntica en toda la API REST. Un único handler central garantiza consistencia y constituye el único lugar donde inspeccionar la traducción excepción-a-HTTP.

**Consecuencias.** Cada módulo declara excepciones específicas que extienden las bases de `common/exception/`, lo que permite al handler central mapearlas correctamente sin necesitar un `@ExceptionHandler` por subtipo.

### ADR-PKG-005 — Eventos con `@TransactionalEventListener(AFTER_COMMIT)`

**Contexto.** Los módulos reactivos (M6 Notificaciones, M7 Auditoría) consumen eventos publicados por los módulos transaccionales (M1, M2, M3). Existe el riesgo de reaccionar a operaciones que terminan en rollback.

**Decisión.** Todos los listeners utilizan `@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)`. Los eventos se publican dentro de la transacción del productor pero solo se entregan a los consumidores tras la confirmación exitosa.

**Justificación.** Garantiza coherencia eventual: ningún consumidor reacciona a un hecho que no ocurrió. Es la práctica estándar para integración intra-monolito mediante eventos.

**Consecuencias.** Si un listener falla, no afecta la transacción del productor. Las acciones de los listeners (notificaciones, audit logs) deben ser idempotentes o gestionar sus propios errores, dado que no participan de la transacción original.

### ADR-PKG-006 — `BmiCalculator` y `GlasgowCalculator` como Clases Independientes

**Contexto.** El cálculo de IMC y del Total Glasgow podría ser un método del `MedicalAttendanceService`. La decisión es si separarlos en clases dedicadas.

**Decisión.** Ambos cálculos son clases independientes en `medicalattendance.internal.service.impl.BmiCalculator` y `GlasgowCalculator`.

**Justificación.** El ADR del Modelo de Datos exige que ambos valores se persistan para preservar exactitud histórica. Las clases independientes facilitan el testing aislado y aíslan cambios futuros de la fórmula del resto del service.

**Consecuencias.** El service inyecta ambas clases. Son spring beans (`@Component`) sin estado, perfectamente testeables con JUnit puro sin necesidad de levantar el contexto Spring.

---

## 11. Trazabilidad con el Modelo de Datos

La siguiente tabla establece la correspondencia uno-a-uno entre cada tabla del Modelo de Datos y la entidad JPA que la representa, indicando el paquete del backend donde reside.

| # | Tabla del modelo | Entidad JPA | Paquete del backend |
|---|------------------|-------------|---------------------|
| 1 | `users` | `User` | `security.internal.entity` |
| 2 | `audit_logs` | `AuditLog` | `security.internal.entity` |
| 3 | `backup_logs` | `BackupLog` | `administration.internal.entity` |
| 4 | `patients` | `Patient` | `patient.internal.entity` |
| 5 | `patient_background` | `PatientBackground` | `patient.internal.entity` |
| 6 | `careers` | `Career` | `administration.internal.entity` |
| 7 | `medical_attendances` | `MedicalAttendance` | `medicalattendance.internal.entity` |
| 8 | `physical_exam_findings` | `PhysicalExamFinding` | `medicalattendance.internal.entity` |
| 9 | `obstetric_emergency` | `ObstetricEmergency` | `medicalattendance.internal.entity` |
| 10 | `complementary_exams` | `ComplementaryExam` | `medicalattendance.internal.entity` |
| 11 | `diagnoses` | `Diagnosis` | `medicalattendance.internal.entity` |
| 12 | `attendance_corrections` | `AttendanceCorrection` | `medicalattendance.internal.entity` |
| 13 | `cie10_codes` | `Cie10Code` | `medicalattendance.internal.entity` |
| 14 | `medical_referrals` | `MedicalReferral` | `referral.internal.entity` |
| 15 | `referral_diagnoses` | `ReferralDiagnosis` | `referral.internal.entity` |
| 16 | `health_establishments` | `HealthEstablishment` | `referral.internal.entity` |
| 17 | `notifications` | `Notification` | `notification.internal.entity` |
| 18 | `notification_thresholds` | `NotificationThreshold` | `notification.internal.entity` |
| 19 | `automatic_reports` | `AutomaticReport` | `administration.internal.entity` |

Las tablas de auditoría generadas automáticamente por Hibernate Envers (sufijo `_aud`) no se enumeran porque son derivadas de las entidades anotadas con `@Audited` y residen en el mismo paquete que su entidad fuente.

---

## 12. Glosario

| Término | Definición |
|---------|------------|
| `api/` | Sub-paquete de un módulo que contiene su contrato público (interfaces de service, DTOs de respuesta, eventos, excepciones). Único sub-paquete que otros módulos pueden importar. |
| `internal/` | Sub-paquete de un módulo que contiene su implementación privada (controllers, impls, repositorios, entidades, mappers). No puede ser importado desde fuera del módulo. |
| ArchUnit | Librería Java para testing de reglas arquitectónicas. Permite enforzar restricciones de paquetes, dependencias y nombrado como tests JUnit. |
| `@Audited` | Anotación de Hibernate Envers que marca una entidad para versionado automático. Genera una tabla `*_aud` paralela con el historial completo de cambios. |
| `@TransactionalEventListener` | Variante de `@EventListener` de Spring que ejecuta el handler en una fase específica de la transacción del productor (`BEFORE_COMMIT`, `AFTER_COMMIT`, `AFTER_ROLLBACK`, etc.). |
| `@ControllerAdvice` | Anotación de Spring MVC que define una clase como handler global para excepciones lanzadas desde cualquier controller. |
| MapStruct | Generador de código Java para conversión entre entidades y DTOs. Genera el código de mapeo en tiempo de compilación, sin reflexión en runtime. |
| Projection | Concepto de Spring Data: interfaz que define un subconjunto de campos de una entidad o de una query, permitiendo retornar formas de datos personalizadas sin clases DTO adicionales. |
| `@Scheduled` | Anotación de Spring que marca un método para ejecución periódica autónoma según expresión cron o tasa fija. |
| Strategy (patrón) | Patrón de diseño que define una familia de algoritmos intercambiables tras una interfaz común, permitiendo seleccionar la implementación en runtime o por configuración. |
| Thin Controller | Controlador REST cuya única responsabilidad es recibir la petición, validar la entrada con Bean Validation, delegar al service y retornar la respuesta. No contiene lógica de negocio. |

---

**Fin del documento — MEDISTA Estructura de Paquetes del Backend v1.0**
