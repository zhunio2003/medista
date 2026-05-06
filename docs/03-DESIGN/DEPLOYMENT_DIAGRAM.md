# Diagrama de Despliegue — MEDISTA

**Proyecto:** MEDISTA — Sistema de Gestión de Atención Médica  
**Institución:** Instituto Superior Universitario TEC Azuay  
**Versión:** 1.0  
**Fecha:** 5 de mayo de 2026  
**Fase:** Fase 2 — Diseño del Sistema

---

## Tabla de Contenidos

1. [Descripción General](#1-descripción-general)
2. [Inventario de Nodos](#2-inventario-de-nodos)
3. [Descripción Detallada de Nodos](#3-descripción-detallada-de-nodos)
   - 3.1 [Servidor Local del Instituto](#31-servidor-local-del-instituto)
   - 3.2 [PC / Laptop Institucional](#32-pc--laptop-institucional)
   - 3.3 [Dispositivo Android del Estudiante](#33-dispositivo-android-del-estudiante)
   - 3.4 [Infraestructura Google (FCM)](#34-infraestructura-google-fcm)
4. [Artefactos Desplegados por Nodo](#4-artefactos-desplegados-por-nodo)
   - 4.1 [Artefactos en el Servidor Local](#41-artefactos-en-el-servidor-local)
   - 4.2 [Artefactos en el Cliente Web](#42-artefactos-en-el-cliente-web)
   - 4.3 [Artefactos en el Dispositivo Android](#43-artefactos-en-el-dispositivo-android)
5. [Canales de Comunicación entre Nodos](#5-canales-de-comunicación-entre-nodos)
   - 5.1 [Tabla de Conexiones](#51-tabla-de-conexiones)
   - 5.2 [Detalle por Canal](#52-detalle-por-canal)
6. [Red Docker Interna](#6-red-docker-interna)
   - 6.1 [Topología de Contenedores](#61-topología-de-contenedores)
   - 6.2 [Puertos y Exposición](#62-puertos-y-exposición)
7. [Flujos de Despliegue por Escenario](#7-flujos-de-despliegue-por-escenario)
8. [Decisiones de Diseño de Infraestructura](#8-decisiones-de-diseño-de-infraestructura)

---

## 1. Descripción General

Este documento especifica la arquitectura de despliegue de MEDISTA. Define los nodos físicos y lógicos que conforman el entorno de ejecución del sistema, los artefactos desplegados en cada nodo, los canales de comunicación entre ellos y las decisiones de infraestructura que justifican la topología elegida.

El sistema opera sobre **cuatro nodos**: el servidor local del instituto (que aloja la totalidad de la infraestructura interna), los equipos institucionales de los usuarios web (médico, decano, administrador), los dispositivos Android de los estudiantes, y la infraestructura de Google como proveedor externo del servicio de notificaciones push. Todos los componentes internos del sistema se orquestan mediante Docker Compose en el servidor del instituto, sin dependencia de infraestructura en la nube.

La comunicación entre nodos externos (clientes) y el servidor se realiza exclusivamente a través de HTTPS sobre la red LAN institucional. Ningún componente de almacenamiento (PostgreSQL, Redis) está expuesto fuera de la red Docker interna.

El diagrama visual de despliegue se encuentra en el archivo `docs/02-DESIGN/diagrams/deployment-diagram.png` del repositorio.

---

## 2. Inventario de Nodos

| # | Nodo | Tipo | Ubicación | Descripción |
|---|------|------|-----------|-------------|
| N1 | Servidor Local del Instituto | Servidor físico on-premise | Instalaciones del TEC Azuay | Ejecuta la totalidad de los contenedores Docker del sistema |
| N2 | PC / Laptop Institucional | Dispositivo cliente | Red LAN del instituto | Equipo de escritorio o portátil desde el que opera el personal institucional |
| N3 | Dispositivo Android del Estudiante | Dispositivo móvil | Red LAN o red móvil (internet) | Smartphone Android del estudiante con la app instalada |
| N4 | Infraestructura Google (FCM) | Servicio en la nube externo | Servidores de Google (internet) | Servicio de entrega de notificaciones push gestionado por Google |

---

## 3. Descripción Detallada de Nodos

### 3.1 Servidor Local del Instituto

Servidor físico administrado por el departamento de TI del Instituto Superior Universitario TEC Azuay. Constituye el nodo central del sistema: aloja y ejecuta todos los contenedores Docker que componen la infraestructura interna de MEDISTA.

El servidor está conectado a la red LAN institucional y es accesible desde los equipos del instituto a través del nombre de host o dirección IP asignada internamente. El único puerto expuesto a la red LAN es el 443 (HTTPS), a través del contenedor Nginx. Todos los demás puertos de los servicios internos están restringidos a la red Docker privada.

| Atributo | Detalle |
|----------|---------|
| Tipo | Servidor físico on-premise |
| Sistema operativo | Linux (Ubuntu Server recomendado) |
| Orquestación | Docker Compose |
| Puerto expuesto a la red LAN | 443 (HTTPS, a través de Nginx) |
| Red interna de contenedores | `medista-network` (bridge Docker) |
| Acceso externo a internet | Saliente únicamente, hacia FCM de Google |
| Respaldo de datos | `pg_dump` programado + cifrado GPG sobre volumen persistente |

### 3.2 PC / Laptop Institucional

Equipo de escritorio o portátil utilizado por el personal de la institución: médico, decano y administrador del sistema. No requiere ninguna instalación de software específico más allá de un navegador web moderno. La interfaz web Angular SPA se sirve desde Nginx y se carga completamente en el browser del usuario.

| Atributo | Detalle |
|----------|---------|
| Tipo | Dispositivo cliente web |
| Usuarios | Médico, Decano, Administrador del Sistema |
| Requisito | Navegador web moderno (Chrome, Firefox, Edge) |
| Conectividad | Red LAN institucional |
| Software instalado | Ninguno (aplicación en browser) |
| Comunicación con el servidor | HTTPS/REST + WebSocket/STOMP sobre puerto 443 |

### 3.3 Dispositivo Android del Estudiante

Smartphone Android perteneciente al estudiante. Ejecuta la aplicación móvil nativa de MEDISTA instalada como APK. La app provee acceso de solo lectura al historial clínico personal del estudiante y recibe notificaciones push institucionales a través de Firebase Cloud Messaging.

El dispositivo puede estar conectado a la red LAN del instituto (WiFi) o a la red móvil del operador. Cuando está en la red LAN, se comunica directamente con el servidor del instituto. El acceso desde redes externas no está contemplado en el alcance actual del sistema — la app requiere acceso a la red LAN para las operaciones REST.

| Atributo | Detalle |
|----------|---------|
| Tipo | Dispositivo móvil cliente |
| Usuarios | Estudiante |
| Sistema operativo requerido | Android 8.0 (API level 26) o superior |
| Distribución | APK instalado directamente (sin Google Play Store) |
| Conectividad para REST | Red LAN institucional (WiFi) |
| Conectividad para FCM | Requiere acceso a internet (LAN o red móvil) |
| Comunicación con el servidor | HTTPS/REST sobre puerto 443 |
| Comunicación con FCM | HTTPS saliente hacia servidores de Google |

### 3.4 Infraestructura Google (FCM)

Servicio externo de Firebase Cloud Messaging gestionado íntegramente por Google. No forma parte del despliegue del sistema — es un proveedor de servicio de terceros. El backend de MEDISTA se comunica con FCM de forma saliente para solicitar el envío de notificaciones push, y FCM se encarga de entregarlas al dispositivo Android del estudiante.

| Atributo | Detalle |
|----------|---------|
| Tipo | Servicio en la nube externo (SaaS) |
| Proveedor | Google LLC |
| API utilizada | FCM HTTP v1 API |
| Autenticación | OAuth 2.0 con cuenta de servicio de Google |
| Control del sistema | Ninguno sobre la entrega final al dispositivo |
| Dependencia de disponibilidad | Best-effort; fallo en FCM no afecta operaciones clínicas |

---

## 4. Artefactos Desplegados por Nodo

### 4.1 Artefactos en el Servidor Local

Todos los artefactos del servidor se despliegan como contenedores Docker orquestados mediante un único archivo `docker-compose.yml` ubicado en `infrastructure/docker-compose.yml` del repositorio.

| # | Artefacto | Contenedor | Imagen base | Puerto interno | Descripción |
|---|-----------|-----------|-------------|----------------|-------------|
| A1 | `medista-nginx` | `nginx` | `nginx:alpine` | 443 (expuesto a LAN) | Proxy inverso. Sirve la SPA Angular estática y enruta peticiones al backend |
| A2 | `medista-backend` | `app` | `eclipse-temurin:21-jre` | 8080 (interno) | JAR ejecutable del backend Spring Boot |
| A3 | `medista-db` | `postgres` | `postgres:16-alpine` | 5432 (interno) | Base de datos relacional principal del sistema |
| A4 | `medista-redis` | `redis` | `redis:7-alpine` | 6379 (interno) | Almacén en memoria para caché, blacklist JWT y rate limiting |
| A5 | `medista-prometheus` | `prometheus` | `prom/prometheus` | 9090 (interno) | Recolección de métricas del sistema |
| A6 | `medista-grafana` | `grafana` | `grafana/grafana` | 3000 (interno, acceso restringido) | Dashboards de monitoreo de infraestructura |

> **Nota sobre los archivos estáticos de Angular:** El build de producción de la aplicación Angular (`ng build --configuration production`) genera archivos estáticos (HTML, JS, CSS) que se copian al contenedor Nginx durante la construcción de la imagen. No existe un contenedor separado para el frontend.

### 4.2 Artefactos en el Cliente Web

| # | Artefacto | Tipo | Origen |
|---|-----------|------|--------|
| A7 | Angular SPA | Archivos estáticos (HTML/JS/CSS) | Servidos por Nginx desde el servidor del instituto |

La Angular SPA no se instala en el equipo del usuario. Se descarga y ejecuta en el browser en cada sesión, aprovechando el caché del navegador para los recursos estáticos.

### 4.3 Artefactos en el Dispositivo Android

| # | Artefacto | Tipo | Distribución |
|---|-----------|------|-------------|
| A8 | MEDISTA Android App | APK firmado | Instalación directa (sideload) |

---

## 5. Canales de Comunicación entre Nodos

### 5.1 Tabla de Conexiones

| # | Canal | Origen | Destino | Protocolo | Puerto | Dirección |
|---|-------|--------|---------|-----------|--------|-----------|
| C1 | Cliente web → Servidor | N2 (PC Institucional) | N1 (Servidor) | HTTPS / REST | 443 | Bidireccional |
| C2 | Cliente web → Servidor (WS) | N2 (PC Institucional) | N1 (Servidor) | WebSocket / STOMP | 443 | Bidireccional |
| C3 | Cliente móvil → Servidor | N3 (Android) | N1 (Servidor) | HTTPS / REST | 443 | Bidireccional |
| C4 | Servidor → FCM | N1 (Servidor) | N4 (Google FCM) | HTTPS / FCM HTTP v1 | 443 | Saliente |
| C5 | FCM → Cliente móvil | N4 (Google FCM) | N3 (Android) | FCM Protocol | — | Entrante |

### 5.2 Detalle por Canal

#### C1 — HTTPS / REST (PC Institucional → Servidor)

Canal principal de comunicación entre el cliente web y el sistema. Transporta la totalidad de las operaciones CRUD del personal institucional: creación de atenciones médicas, consulta de historiales, generación de reportes y administración del sistema.

- **Terminación TLS:** Nginx gestiona el certificado SSL y termina la conexión HTTPS. El tráfico dentro del servidor viaja en HTTP plano por la red Docker privada.
- **Autenticación:** Cada petición incluye el token JWT en el header `Authorization: Bearer <token>`.
- **Codificación:** JSON para el cuerpo de las peticiones y respuestas. Los PDFs se descargan como flujos binarios (`application/pdf`).

#### C2 — WebSocket / STOMP (PC Institucional → Servidor)

Canal de comunicación persistente para la entrega de notificaciones en tiempo real al cliente web. Se establece tras la autenticación del usuario y se mantiene activo durante toda la sesión.

- **Handshake:** Se inicia con un HTTP Upgrade request sobre el mismo puerto 443 de Nginx, que lo eleva a WebSocket hacia el backend.
- **Autenticación:** El token JWT se transmite en el header del handshake. La conexión es rechazada si el token no es válido.
- **Topics:** `/user/{id}/notifications` (personal) y `/topic/institutional` (broadcast institucional).

#### C3 — HTTPS / REST (Android → Servidor)

Canal de comunicación entre la app Android y el sistema. Limitado a operaciones de consulta de solo lectura sobre el historial clínico del estudiante autenticado.

- **Alcance de red:** Requiere conectividad a la red LAN institucional. El acceso desde redes externas no está habilitado en la versión actual.
- **Autenticación:** Idéntica al cliente web — JWT en header `Authorization: Bearer <token>`.
- **Timeout:** Configurado en la app para gestionar condiciones de red inestables en WiFi institucional.

#### C4 — HTTPS / FCM HTTP v1 (Servidor → Google)

Llamada HTTP saliente desde el backend de MEDISTA hacia la API de Firebase Cloud Messaging. Es la única comunicación del sistema que requiere acceso a internet desde el servidor.

- **Autenticación con Google:** OAuth 2.0 mediante cuenta de servicio. El backend obtiene un access token de corta duración y lo renueva antes de su expiración.
- **Payload:** JSON con campos `title`, `body` y `data` (información contextual para la app Android).
- **Resiliencia:** Las notificaciones push son best-effort. Un fallo en este canal no interrumpe ninguna operación clínica. El estado de cada notificación se registra en la tabla `notifications` de PostgreSQL.

#### C5 — FCM Protocol (Google → Android)

Entrega final de la notificación al dispositivo del estudiante, gestionada íntegramente por la infraestructura de Google. El sistema de MEDISTA no tiene control ni visibilidad sobre este tramo de la comunicación.

- **Comportamiento en background:** FCM entrega la notificación como system notification del sistema operativo Android.
- **Comportamiento en foreground:** La app captura el mensaje a través de `FirebaseMessagingService` y lo presenta en la interfaz.
- **Requisito de conectividad:** El dispositivo Android requiere conexión a internet activa para recibir notificaciones push.

---

## 6. Red Docker Interna

### 6.1 Topología de Contenedores

Todos los contenedores del servidor se conectan a una red Docker de tipo bridge denominada `medista-network`. Esta red es privada y aislada — ningún contenedor interno es accesible desde la red LAN del instituto excepto a través de Nginx.

```
Red LAN del Instituto (192.168.x.x)
│
│ :443 (HTTPS)
▼
┌──────────────────────────────────────────────────────────────┐
│                    medista-network (Docker bridge)           │
│                                                              │
│  ┌─────────────┐    HTTP :8080    ┌──────────────────────┐   │
│  │    nginx    │ ───────────────► │   spring-boot app    │   │
│  │  (A1)       │                  │   (A2)               │   │
│  └─────────────┘                  └──────────────────────┘   │
│                                          │         │          │
│                                  JDBC    │  RESP   │          │
│                                  :5432   │  :6379  │          │
│                                    ▼         ▼               │
│                             ┌──────────┐ ┌───────┐           │
│                             │ postgres │ │ redis │           │
│                             │  (A3)    │ │ (A4)  │           │
│                             └──────────┘ └───────┘           │
│                                                              │
│  ┌──────────────┐           ┌──────────────────────────┐     │
│  │  prometheus  │ ◄──scrape─│    spring-boot actuator  │     │
│  │  (A5)        │           │    /actuator/prometheus   │     │
│  └──────────────┘           └──────────────────────────┘     │
│         │                                                    │
│  ┌──────▼───────┐                                            │
│  │   grafana    │                                            │
│  │   (A6)       │ ◄── acceso restringido a admin             │
│  └──────────────┘                                            │
└──────────────────────────────────────────────────────────────┘
                              │
                              │ HTTPS saliente :443
                              ▼
                    Firebase Cloud Messaging (Google)
```

### 6.2 Puertos y Exposición

| Contenedor | Puerto interno | Expuesto a LAN | Expuesto fuera de Docker | Notas |
|-----------|---------------|----------------|--------------------------|-------|
| `nginx` | 443 | ✅ Sí | ✅ Sí (único punto de entrada) | Puerto de entrada al sistema |
| `app` | 8080 | ❌ No | ❌ No | Solo accesible desde `nginx` en la red Docker |
| `postgres` | 5432 | ❌ No | ❌ No | Solo accesible desde `app` en la red Docker |
| `redis` | 6379 | ❌ No | ❌ No | Solo accesible desde `app` en la red Docker |
| `prometheus` | 9090 | ❌ No | ❌ No | Solo accesible internamente |
| `grafana` | 3000 | ❌ No (acceso restringido) | ❌ No | Acceso manual habilitado por admin en caso de necesidad |

> **Principio aplicado:** mínima superficie de ataque. Solo Nginx tiene exposición a la red. Todos los demás servicios operan en aislamiento de red total.

---

## 7. Flujos de Despliegue por Escenario

Esta sección describe cómo se materializa el despliegue en los escenarios operativos del sistema.

### Escenario 1 — Despliegue Inicial (Primera Instalación)

```
1. Clonar repositorio en el servidor:
   git clone https://github.com/[org]/medista.git

2. Configurar variables de entorno:
   cp infrastructure/.env.example infrastructure/.env
   # Editar: credenciales de BD, clave JWT, credenciales FCM, etc.

3. Construir imágenes y levantar contenedores:
   docker compose -f infrastructure/docker-compose.yml up -d --build

4. Flyway ejecuta automáticamente las migraciones de base de datos al iniciar el backend.

5. Verificar estado de todos los contenedores:
   docker compose -f infrastructure/docker-compose.yml ps

6. Cargar catálogo CIE-10 (script de carga masiva):
   docker exec medista-app java -jar /scripts/load-cie10.jar
```

### Escenario 2 — Actualización del Sistema (Nueva Versión)

```
1. En el servidor, actualizar el repositorio:
   git pull origin main

2. Reconstruir solo el contenedor afectado (generalmente el backend):
   docker compose -f infrastructure/docker-compose.yml up -d --build app

3. Flyway detecta y aplica automáticamente las nuevas migraciones pendientes.

4. Verificar logs del backend para confirmar arranque exitoso:
   docker compose logs -f app
```

### Escenario 3 — Instalación de la App Android (Estudiante)

```
1. Conectar el dispositivo Android a la red WiFi institucional.
2. Descargar el APK desde la URL interna publicada por el administrador.
3. Habilitar "Instalar desde fuentes desconocidas" en el dispositivo.
4. Instalar el APK.
5. Ingresar con credenciales institucionales (correo + contraseña).
```

---

## 8. Decisiones de Diseño de Infraestructura

### Despliegue on-premise sobre el servidor del instituto

La decisión de desplegar en el servidor local del instituto, en lugar de en la nube, responde a tres factores concretos: el instituto ya cuenta con infraestructura de servidor existente, los datos clínicos de estudiantes son datos sensibles bajo la LOPDP y mantenerlos en infraestructura controlada por la institución reduce el riesgo y la complejidad de cumplimiento normativo, y el costo operativo de un servidor propio es inferior al de servicios en la nube para esta escala (~500 usuarios).

La arquitectura con Docker Compose es deliberadamente migrable: si en el futuro la institución decide moverse a la nube o adoptar Kubernetes, los contenedores son portables sin cambios en el código.

### Nginx como único punto de entrada a la red

Todo el tráfico externo entra por el puerto 443 de Nginx. Esta decisión elimina la necesidad de gestionar certificados SSL en el backend, centraliza el control de acceso a nivel de red, y permite que todos los servicios internos operen sin exposición directa. En términos de seguridad, reduce la superficie de ataque al mínimo posible.

### Separación de responsabilidades entre Prometheus/Grafana y el backend

El monitoreo no es parte de la lógica de negocio. Prometheus recolecta métricas de Spring Boot Actuator de forma pasiva (scraping), sin que el backend necesite conocer ni depender de Prometheus. Esta separación garantiza que un fallo en el sistema de monitoreo no afecte la disponibilidad del sistema clínico.

### Angular como archivos estáticos en Nginx (sin servidor de renderizado)

El build de producción de Angular genera archivos estáticos que son servidos directamente por Nginx. Esta decisión elimina la necesidad de un contenedor adicional para el frontend y simplifica el despliegue. Nginx es extremadamente eficiente sirviendo archivos estáticos y gestiona correctamente el routing de la SPA mediante `try_files`.

### Acceso móvil limitado a red LAN

La restricción de que la app Android requiera conectividad LAN para las operaciones REST es una decisión deliberada de alcance para la versión actual. Exponer el servidor a internet implicaría consideraciones adicionales de seguridad (VPN, firewall, gestión de DNS público) que exceden el scope del proyecto. La app mantiene la capacidad de recibir notificaciones push vía FCM desde cualquier red, ya que ese canal es externo al servidor.
