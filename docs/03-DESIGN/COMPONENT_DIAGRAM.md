# Diagrama de Componentes — MEDISTA

**Proyecto:** MEDISTA — Sistema de Gestión de Atención Médica  
**Institución:** Instituto Superior Universitario TEC Azuay  
**Versión:** 1.0  
**Fecha:** 4 de mayo de 2026  
**Fase:** Fase 2 — Diseño del Sistema

---

## Tabla de Contenidos

1. [Descripción General](#1-descripción-general)
2. [Inventario de Componentes](#2-inventario-de-componentes)
3. [Descripción Detallada de Componentes](#3-descripción-detallada-de-componentes)
   - 3.1 [Angular Web App](#31-angular-web-app)
   - 3.2 [Android App](#32-android-app-kotlin)
   - 3.3 [Nginx Reverse Proxy](#33-nginx-reverse-proxy)
   - 3.4 [Spring Boot Backend](#34-spring-boot-backend)
   - 3.5 [PostgreSQL 16](#35-postgresql-16)
   - 3.6 [Redis 7](#36-redis-7)
   - 3.7 [Firebase Cloud Messaging](#37-firebase-cloud-messaging-fcm)
4. [Interfaces y Protocolos de Comunicación](#4-interfaces-y-protocolos-de-comunicación)
   - 4.1 [Tabla de Relaciones](#41-tabla-de-relaciones)
   - 4.2 [Detalle por Canal de Comunicación](#42-detalle-por-canal-de-comunicación)
5. [Flujos de Comunicación por Caso de Uso](#5-flujos-de-comunicación-por-caso-de-uso)
6. [Decisiones de Diseño de Comunicación](#6-decisiones-de-diseño-de-comunicación)

---

## 1. Descripción General

Este documento especifica la arquitectura de componentes de MEDISTA. Define los bloques funcionales que conforman el sistema, sus responsabilidades individuales, y la totalidad de los canales de comunicación que los interconectan, incluyendo los protocolos, puertos, direcciones de flujo y propósito de cada interfaz.

El sistema está compuesto por **siete componentes**: dos clientes (web y móvil), un proxy inverso, un backend central, dos motores de almacenamiento, y un servicio externo de mensajería. Todos los componentes internos se despliegan en el servidor local del instituto mediante Docker Compose. El único componente externo al servidor es Firebase Cloud Messaging, gestionado por Google.

El diagrama visual de componentes se encuentra en el archivo `docs/02-DESIGN/diagrams/component-diagram.png` del repositorio.

---

## 2. Inventario de Componentes

| # | Componente | Tipo | Ubicación | Tecnología |
|---|-----------|------|-----------|------------|
| C1 | Angular Web App | Cliente web | Browser del usuario | Angular 19, Angular Material, NgRx Signals, Tailwind CSS |
| C2 | Android App | Cliente móvil | Dispositivo del estudiante | Kotlin, Android nativo |
| C3 | Nginx Reverse Proxy | Proxy inverso | Servidor local del instituto | Nginx (contenedor Docker) |
| C4 | Spring Boot Backend | Servidor de aplicación | Servidor local del instituto | Java 21, Spring Boot 3.3 (contenedor Docker) |
| C5 | PostgreSQL 16 | Base de datos relacional | Servidor local del instituto | PostgreSQL 16 (contenedor Docker) |
| C6 | Redis 7 | Almacén en memoria | Servidor local del instituto | Redis 7 (contenedor Docker) |
| C7 | Firebase Cloud Messaging | Servicio externo de mensajería | Infraestructura de Google | FCM HTTP v1 API |

---

## 3. Descripción Detallada de Componentes

### 3.1 Angular Web App

Aplicación web de página única (SPA) desarrollada con Angular 19. Constituye la interfaz principal del sistema, destinada al personal médico, administrativo y directivo de la institución. Provee acceso a la totalidad de los módulos funcionales del sistema: registro de pacientes, atenciones médicas, referencias, historial clínico, dashboard estadístico, notificaciones y administración.

Se ejecuta íntegramente en el navegador del usuario y no depende de ningún servidor de renderizado. Toda la comunicación con el backend ocurre a través de Nginx: peticiones REST para operaciones de datos y una conexión WebSocket/STOMP persistente para la recepción de notificaciones en tiempo real.

| Atributo | Detalle |
|----------|---------|
| Tipo | Single Page Application (SPA) |
| Usuarios | Médico, Decano, Administrador del Sistema |
| Canales de salida | HTTPS/REST (operaciones CRUD), WebSocket/STOMP (notificaciones) |
| Estado del cliente | Gestionado con NgRx Signals |
| Autenticación | JWT almacenado en memoria (no en localStorage) |

### 3.2 Android App (Kotlin)

Aplicación móvil nativa desarrollada en Kotlin para la plataforma Android. Destinada exclusivamente a los estudiantes de la institución, provee acceso de solo lectura al historial clínico personal del usuario autenticado. No permite crear ni modificar ningún registro clínico.

Adicionalmente, mantiene un canal de recepción de notificaciones push mediante Firebase Cloud Messaging, lo que permite al backend despachar alertas institucionales y clínicas directamente al dispositivo del estudiante, incluso cuando la aplicación no está en primer plano.

| Atributo | Detalle |
|----------|---------|
| Tipo | Aplicación móvil nativa Android |
| Usuarios | Estudiante |
| Canales de salida | HTTPS/REST (consulta de historial) |
| Canales de entrada | FCM Push (notificaciones del backend) |
| Autenticación | JWT + correo institucional |
| Permisos de datos | Solo lectura sobre el propio historial |

### 3.3 Nginx Reverse Proxy

Servidor proxy inverso que actúa como punto de entrada único al sistema desde la red institucional. Todo el tráfico externo — tanto desde el cliente web como desde el cliente móvil — atraviesa Nginx antes de alcanzar el backend.

Sus responsabilidades son: terminación SSL/TLS (los clientes hablan HTTPS con Nginx; Nginx habla HTTP interno con el backend), enrutamiento de peticiones REST hacia el backend, y elevación de protocolo para conexiones WebSocket (upgrade de HTTP a WS). Esta separación oculta la topología interna del servidor y centraliza el control de acceso a nivel de red.

| Atributo | Detalle |
|----------|---------|
| Tipo | Reverse proxy |
| Puerto expuesto | 443 (HTTPS) |
| Puerto interno | 8080 (HTTP hacia Spring Boot) |
| Responsabilidades | Terminación SSL/TLS, enrutamiento REST, WebSocket upgrade |
| Despliegue | Contenedor Docker, accesible desde la red LAN institucional |

### 3.4 Spring Boot Backend

Monolito modular desarrollado con Java 21 y Spring Boot 3.3. Contiene la totalidad de la lógica de negocio del sistema, organizada en ocho módulos funcionales independientes con separación estricta de capas internas (controller → service → repository → mapper → entity).

Expone una API REST consumida por ambos clientes, gestiona autenticación y autorización mediante JWT con Spring Security, persiste datos en PostgreSQL a través de JPA/Hibernate, utiliza Redis para caché, blacklist de tokens y rate limiting, coordina el envío de notificaciones push a través de FCM, y emite eventos en tiempo real a los clientes web mediante WebSocket/STOMP.

| Atributo | Detalle |
|----------|---------|
| Tipo | Monolito modular |
| Puerto interno | 8080 |
| Módulos | M1 Pacientes, M2 Atención, M3 Referencia, M4 Historial, M5 Dashboard, M6 Notificaciones, M7 Seguridad, M8 Administración |
| Autenticación | Spring Security + JWT (RS256) |
| Auditoría | Hibernate Envers + audit_logs con HMAC-SHA256 |
| Rate limiting | Bucket4j con backend Redis |
| Migraciones DB | Flyway |

### 3.5 PostgreSQL 16

Sistema de gestión de base de datos relacional que constituye la fuente de verdad del sistema. Almacena la totalidad de los datos clínicos, de usuarios, auditoría y configuración. Su esquema es gestionado íntegramente mediante migraciones versionadas con Flyway, garantizando que sea reproducible en cualquier entorno.

Opera con dos extensiones habilitadas: `pgcrypto` para el cifrado simétrico de campos clínicos sensibles a nivel de base de datos, y `pg_trgm` para la indexación de trigramas que habilita la búsqueda difusa y autocompletado del catálogo CIE-10.

| Atributo | Detalle |
|----------|---------|
| Tipo | Base de datos relacional |
| Puerto interno | 5432 |
| Extensiones activas | `pgcrypto`, `pg_trgm` |
| Tablas | 19 tablas (ver Modelo de Datos v1.0) |
| Acceso | Exclusivo desde Spring Boot Backend — no expuesto a la red |
| Persistencia | Volumen Docker persistente |
| Gestión de esquema | Flyway (migraciones versionadas) |

### 3.6 Redis 7

Almacén de datos en memoria que cumple tres roles diferenciados y bien delimitados dentro del sistema. Su naturaleza volátil es aceptable en los tres casos, dado que ninguno de estos usos requiere persistencia duradera: el caché se puede reconstruir desde PostgreSQL, la blacklist de tokens pierde relevancia cuando los tokens expiran, y los contadores de rate limiting se reinician por diseño.

| Rol | Descripción | Estructura de datos |
|-----|-------------|---------------------|
| Caché de consultas | Almacena respuestas de consultas frecuentes (dashboard, CIE-10) para reducir carga sobre PostgreSQL | String (JSON serializado) con TTL |
| Blacklist de JWT | Registra tokens revocados tras logout explícito, permitiendo invalidación inmediata antes de su expiración natural | Set con TTL igual al tiempo restante del token |
| Rate limiting | Actúa como backend de Bucket4j para el control de intentos de autenticación fallidos y límites por endpoint | Contador con ventana deslizante |

| Atributo | Detalle |
|----------|---------|
| Tipo | Almacén en memoria (key-value) |
| Puerto interno | 6379 |
| Protocolo | RESP (REdis Serialization Protocol) |
| Acceso | Exclusivo desde Spring Boot Backend — no expuesto a la red |
| Persistencia | No requerida (datos volátiles por diseño) |

### 3.7 Firebase Cloud Messaging (FCM)

Servicio externo de mensajería gestionado por Google, utilizado exclusivamente para el envío de notificaciones push a la aplicación móvil Android. El backend despacha mensajes a FCM a través de su HTTP v1 API; FCM se encarga de la entrega al dispositivo del estudiante, independientemente de si la aplicación está activa o en segundo plano.

FCM es el único componente externo al servidor local del instituto. Su uso implica una dependencia de conectividad a internet saliente desde el servidor, exclusivamente para este canal.

| Atributo | Detalle |
|----------|---------|
| Tipo | Servicio externo (Google) |
| Protocolo de entrada | HTTPS (FCM HTTP v1 API) — desde Spring Boot |
| Protocolo de salida | FCM Protocol (TCP) — hacia dispositivo Android |
| Dirección | Unidireccional: Backend → FCM → Android App |
| Alcance | Solo notificaciones push móvil |
| Dependencia de red | Requiere conectividad a internet saliente desde el servidor |

---

## 4. Interfaces y Protocolos de Comunicación

### 4.1 Tabla de Relaciones

| # | Origen | Destino | Protocolo | Puerto | Dirección | Propósito |
|---|--------|---------|-----------|--------|-----------|-----------|
| I1 | Angular Web App | Nginx | HTTPS | 443 | → | Transmisión cifrada de peticiones REST desde el cliente web |
| I2 | Android App | Nginx | HTTPS | 443 | → | Transmisión cifrada de peticiones REST desde el cliente móvil |
| I3 | Nginx | Spring Boot Backend | HTTP | 8080 | → | Reenvío interno de peticiones tras terminación SSL en el proxy |
| I4 | Angular Web App | Spring Boot Backend (vía Nginx) | WebSocket / STOMP | 443 (upgrade) | ↔ | Canal bidireccional persistente para notificaciones en tiempo real web |
| I5 | Spring Boot Backend | PostgreSQL 16 | JDBC (TCP) | 5432 | → | Lectura y escritura de todos los datos clínicos, usuarios y auditoría |
| I6 | Spring Boot Backend | Redis 7 | RESP (TCP) | 6379 | → | Gestión de caché, blacklist de tokens JWT y control de rate limiting |
| I7 | Spring Boot Backend | Firebase Cloud Messaging | HTTPS | 443 | → | Despacho de notificaciones push hacia dispositivos Android |
| I8 | Firebase Cloud Messaging | Android App | FCM Protocol (TCP) | — | → | Entrega de notificaciones push al dispositivo del estudiante |

### 4.2 Detalle por Canal de Comunicación

#### I1 / I2 — HTTPS/REST (Clientes → Nginx)

Protocolo de transferencia de hipertexto seguro utilizado por ambos clientes para todas las operaciones de datos: autenticación, registro de pacientes, guardado de atenciones, consulta de historial, generación de reportes y administración del sistema.

- **Seguridad:** TLS 1.2/1.3. El certificado SSL es terminado en Nginx. Los clientes nunca hablan directamente con el backend.
- **Autenticación:** Cada petición incluye un token JWT en el header `Authorization: Bearer <token>`.
- **Formato de datos:** JSON en request y response body para todos los endpoints de la API REST.
- **Idempotencia:** Las operaciones de lectura (GET) son idempotentes. Las operaciones de escritura críticas (registro de atención médica) incluyen mecanismos de idempotencia en el backend para prevenir duplicados ante reintentos del cliente.

#### I3 — HTTP interno (Nginx → Spring Boot)

Canal de comunicación interna entre el proxy y el backend, dentro de la red Docker. Al haber terminado SSL en Nginx, este tramo opera en HTTP plano, lo cual es aceptable dado que nunca sale de la red privada del contenedor.

- **Seguridad:** Red Docker privada, no expuesta a la red institucional.
- **Timeout:** Configurado en Nginx para manejar operaciones de larga duración (generación de reportes PDF con JasperReports).

#### I4 — WebSocket / STOMP (Angular Web App ↔ Backend)

Protocolo de comunicación bidireccional persistente utilizado para la entrega de notificaciones en tiempo real a la interfaz web. A diferencia de REST, WebSocket mantiene una conexión abierta, lo que permite al servidor enviar mensajes al cliente sin que este los solicite explícitamente.

STOMP (Simple Text Oriented Messaging Protocol) se usa como protocolo de mensajería sobre WebSocket, proporcionando un modelo de publicación/suscripción con topics nombrados.

- **Handshake:** La conexión se inicia con un HTTP Upgrade request sobre el mismo puerto 443, manejado por Nginx.
- **Topics de suscripción:** Cada usuario autenticado se suscribe a su topic personal (`/user/{id}/notifications`) y a un topic institucional (`/topic/institutional`).
- **Autenticación:** El token JWT se envía en el header del handshake inicial. Spring Security valida el token antes de permitir la conexión.
- **Uso:** Exclusivo para el módulo M6 — Notificaciones Inteligentes en la interfaz web.

#### I5 — JDBC (Spring Boot → PostgreSQL)

Interfaz de conectividad de base de datos Java utilizada por Spring Data JPA e Hibernate para toda la persistencia del sistema. La gestión del pool de conexiones se realiza con HikariCP, incluido por defecto en Spring Boot.

- **Pool de conexiones:** HikariCP con configuración de tamaño máximo acorde a la carga esperada (≤ 500 usuarios concurrentes).
- **Transacciones:** Gestionadas declarativamente mediante `@Transactional` de Spring. Todas las operaciones de escritura son transaccionales — ante error parcial, la operación completa se revierte.
- **Migraciones:** Flyway ejecuta automáticamente los scripts de migración pendientes al iniciar el backend, garantizando que el esquema de base de datos esté siempre sincronizado con la versión del código desplegado.
- **Cifrado de campos:** Los campos clínicos sensibles son cifrados/descifrados mediante las funciones `pgp_sym_encrypt` / `pgp_sym_decrypt` de la extensión `pgcrypto`, invocadas desde las queries de Hibernate.

#### I6 — RESP (Spring Boot → Redis)

REdis Serialization Protocol, el protocolo nativo de Redis. La comunicación se realiza a través del cliente Lettuce, incluido por defecto en Spring Boot Data Redis.

- **Uso 1 — Caché:** Spring Cache con anotaciones `@Cacheable` / `@CacheEvict`. Las entradas de caché tienen TTL configurado por tipo de dato (consultas del dashboard: 5 minutos; catálogo CIE-10: 60 minutos).
- **Uso 2 — Blacklist JWT:** Al realizar logout, el JTI (JWT ID) del token revocado se almacena en Redis con TTL igual al tiempo restante hasta la expiración natural del token. Cada petición entrante verifica que su JTI no esté en la blacklist antes de ser procesada.
- **Uso 3 — Rate limiting:** Bucket4j utiliza Redis como backend distribuido para los contadores de rate limiting. Controla intentos de login fallidos (máximo 5 por IP en 15 minutos) y límites generales por endpoint.

#### I7 — HTTPS / FCM HTTP v1 API (Spring Boot → Firebase)

Llamada HTTP saliente desde el backend hacia la API de Firebase Cloud Messaging para el despacho de notificaciones push. Esta es la única comunicación del sistema que requiere conectividad a internet desde el servidor.

- **Autenticación con FCM:** OAuth 2.0 con cuenta de servicio de Google (service account JSON). El backend obtiene un access token de corta duración antes de cada envío o reutiliza uno en caché.
- **Payload:** Mensaje FCM con campos `title`, `body` y `data` (payload personalizado para la app Android).
- **Manejo de fallos:** Las notificaciones push son best-effort. Un fallo en FCM no interrumpe ninguna operación clínica del sistema. El módulo M6 registra el estado de cada notificación (entregada / fallida) en la tabla `notifications`.

#### I8 — FCM Protocol (Firebase → Android App)

Entrega final de la notificación al dispositivo del estudiante, gestionada íntegramente por la infraestructura de Google. El sistema no tiene control sobre este tramo.

- **Comportamiento en background:** FCM entrega la notificación como system notification incluso cuando la app no está activa, utilizando el servicio de mensajería del sistema operativo Android.
- **Comportamiento en foreground:** La app recibe el mensaje a través del `FirebaseMessagingService` y lo muestra como notificación in-app.

---

## 5. Flujos de Comunicación por Caso de Uso

Esta sección ilustra los canales activados en los flujos principales del sistema.

### Flujo 1 — Registro de una Atención Médica

```
Médico (browser)
  → [I1] HTTPS/REST → Nginx
  → [I3] HTTP → Spring Boot Backend
  → [I5] JDBC → PostgreSQL 16 (INSERT medical_attendances + tablas relacionadas)
  → [I6] RESP → Redis 7 (invalidar caché del historial del paciente)
  ← Spring Boot Backend responde 201 Created
  ← Nginx reenvía respuesta al browser
```

### Flujo 2 — Consulta del Historial Clínico (App Móvil)

```
Estudiante (Android)
  → [I2] HTTPS/REST → Nginx
  → [I3] HTTP → Spring Boot Backend
  → [I6] RESP → Redis 7 (verificar JWT en blacklist + consultar caché)
    → Si cache HIT: responde directamente desde Redis
    → Si cache MISS:
      → [I5] JDBC → PostgreSQL 16 (SELECT historial del paciente)
      → [I6] RESP → Redis 7 (almacenar resultado en caché)
  ← Spring Boot Backend responde 200 OK con el historial
  ← Nginx reenvía respuesta al dispositivo
```

### Flujo 3 — Envío de Notificación Push al Estudiante

```
Spring Boot Backend (evento clínico o epidemiológico detectado)
  → [I6] RESP → Redis 7 (INSERT en tabla notifications — estado: PENDING)
  → [I7] HTTPS → Firebase Cloud Messaging
  → [I8] FCM Protocol → Android App del estudiante
  → [I6] RESP → Redis 7 (UPDATE estado de notificación: DELIVERED / FAILED)
```

### Flujo 4 — Notificación en Tiempo Real en la Web

```
Spring Boot Backend (evento detectado)
  → [I4] WebSocket/STOMP → Angular Web App
    (publicación en topic /user/{id}/notifications)
  Angular Web App muestra la alerta en la interfaz sin recargar la página
```

### Flujo 5 — Logout Seguro

```
Usuario (browser o Android)
  → [I1/I2] HTTPS/REST POST /auth/logout → Nginx → Spring Boot Backend
  → [I6] RESP → Redis 7 (SET jti:<token_id> con TTL = tiempo restante del token)
  ← Spring Boot responde 200 OK
  En peticiones posteriores con ese token:
  → Spring Boot verifica JTI en Redis → encuentra entrada → rechaza con 401
```

---

## 6. Decisiones de Diseño de Comunicación

### Nginx como punto de entrada único

Toda la comunicación externa pasa por Nginx, nunca directamente al backend. Esta decisión centraliza la terminación SSL, simplifica la gestión de certificados, y permite que los contenedores internos (Spring Boot, PostgreSQL, Redis) operen en una red Docker privada no expuesta a la red institucional.

### WebSocket solo para la interfaz web, FCM para móvil

Las notificaciones en tiempo real utilizan canales diferentes según la plataforma por razones técnicas y de ciclo de vida. En la web, el browser mantiene conexiones WebSocket activas mientras el tab está abierto. En móvil, el sistema operativo Android gestiona la entrega de notificaciones push de forma nativa a través de FCM, incluso cuando la app no está activa. Usar WebSocket en el móvil requeriría que la app esté permanentemente en primer plano, lo cual no es viable.

### PostgreSQL y Redis no expuestos a la red

Ambos motores de almacenamiento operan exclusivamente dentro de la red Docker interna. Solo Spring Boot puede comunicarse con ellos. Esta decisión elimina una clase entera de vectores de ataque (acceso directo a la base de datos desde la red institucional) y es coherente con el principio de mínimo privilegio exigido por la LOPDP.

### HTTP interno entre Nginx y Spring Boot

La comunicación entre Nginx y el backend es HTTP plano (no HTTPS). Esto es una práctica estándar en arquitecturas con reverse proxy cuando ambos componentes operan en la misma red privada de contenedores. El cifrado TLS en este tramo añadiría overhead computacional sin beneficio de seguridad real, dado que el tráfico nunca abandona la red Docker privada.
