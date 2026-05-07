# MEDISTA — Contratos de API REST

**Versión:** 1.0
**Fecha:** Mayo 2026
**Fase:** 2 — Diseño del Sistema
**Tipo de entregable:** Diseño de Bajo Nivel (LLD)
**Autor:** Joseph Zhunio
**Institución:** Instituto Superior Universitario TEC Azuay

---

## 1. Introducción

### 1.1 Propósito del Documento

Este documento define el contrato formal de la API REST del backend de MEDISTA. Cada endpoint queda especificado con su método HTTP, ruta, roles autorizados, estructura de request, estructura de response y códigos de estado posibles, antes de iniciar la fase de implementación.

El propósito es triple. Primero, fijar el contrato técnico entre el backend (Spring Boot) y los dos clientes — la aplicación web (Angular) y la aplicación móvil (Kotlin nativo) — de modo que el desarrollo de las tres capas pueda avanzar en paralelo sin dependencias bloqueantes. Segundo, garantizar la trazabilidad de cada endpoint hacia los requisitos funcionales del ERS y los casos de uso definidos en la Fase 1, evitando endpoints huérfanos o requisitos sin cobertura. Tercero, documentar las decisiones de diseño transversales — autenticación, paginación, formato de errores, convenciones de nomenclatura — una sola vez, evitando duplicación y contradicciones entre módulos.

### 1.2 Alcance

El documento cubre los ocho módulos del sistema en el orden de implementación definido en la estructura de paquetes del backend: M7 (Seguridad y Auditoría), M1 (Gestión de Pacientes), M2 (Atención Médica), M3 (Referencia Médica), M4 (Historial Clínico), M5 (Dashboard y Reportes), M6 (Notificaciones) y M8 (Administración del Sistema).

Quedan fuera del alcance de este documento las comunicaciones que no se realizan sobre HTTP/REST: los mensajes WebSocket/STOMP del módulo M6 se documentan en el contrato específico de notificaciones en tiempo real, y la integración con Firebase Cloud Messaging para notificaciones push móviles se trata en el documento de integración de servicios externos. Tampoco se incluyen los endpoints de infraestructura expuestos por Spring Boot Actuator (`/actuator/*`) ni los endpoints de métricas de Prometheus, dado que son operativos y no forman parte del contrato funcional con los clientes.

### 1.3 Relación con Otros Entregables

Cada endpoint documentado en este artefacto se traza con los siguientes documentos previos del proyecto:

| Entregable | Relación con este documento |
|------------|------------------------------|
| ERS | Cada endpoint declara explícitamente los requisitos funcionales (RF-Mx-yy) que cubre. |
| Casos de Uso | Cada endpoint declara el caso de uso (CU-Mx-yy) al que sirve, cuando aplica. |
| Diagramas de Secuencia | Los flujos críticos detallados en los diagramas de secuencia se materializan en uno o más endpoints de este documento. |
| Modelo de Datos | Los DTOs de request y response son proyecciones controladas de las entidades del modelo. Ningún endpoint expone la estructura interna de la base de datos. |
| Estructura de Paquetes | Cada controlador mencionado vive físicamente en `internal/controller/` del módulo correspondiente. |

---

## 2. Convenciones del Documento

### 2.1 Estructura de cada Contrato

Cada contrato individual presenta seis secciones en orden fijo:

1. **Tabla de metadatos** — método HTTP, ruta, descripción, roles autorizados, caso de uso asociado y requisitos funcionales cubiertos.
2. **Request body** — campos esperados, tipos de dato, obligatoriedad y reglas de validación.
3. **Response body (éxito)** — campos retornados con sus tipos, en el caso del código de estado de éxito.
4. **Códigos de estado** — códigos HTTP específicos del endpoint con su significado funcional.
5. **Query parameters / Path parameters** — cuando el endpoint los recibe.
6. **Notas técnicas** — observaciones de implementación, reglas de negocio o consideraciones de seguridad relevantes.

### 2.2 Convenciones de Nomenclatura

| Elemento | Convención | Ejemplo |
|----------|-----------|---------|
| Prefijo base de rutas | `/api/v1/` | `/api/v1/patients` |
| Recursos | Sustantivo en plural, kebab-case | `/medical-attendances`, `/audit-logs` |
| Sub-recursos | Anidados bajo el recurso padre | `/patients/{id}/background` |
| Acciones no-CRUD | Verbo después del recurso | `/medical-attendances/{id}/correct` |
| Identificadores en ruta | `{id}` numérico (BIGSERIAL) | `/patients/42` |
| Campos JSON | camelCase | `firstName`, `birthDate` |
| Enums en JSON | UPPER_SNAKE_CASE | `"ROLE_DOCTOR"`, `"PRESUMPTIVE"` |
| Fechas y timestamps | ISO 8601 UTC | `"2026-05-07T14:32:18.123Z"` |
| Fechas sin hora | ISO 8601 date | `"2026-05-07"` |

### 2.3 Métodos HTTP y su Semántica

| Método | Uso |
|--------|-----|
| `GET` | Consultar recurso(s). Idempotente. Sin efectos colaterales sobre el estado del servidor. |
| `POST` | Crear recurso o ejecutar acción no idempotente. |
| `PUT` | Reemplazar recurso completo. Idempotente. |
| `PATCH` | Modificar campos específicos del recurso. |
| `DELETE` | Desactivar recurso (soft delete vía `is_active = false`). MEDISTA no realiza eliminación física por exigencia normativa. |

---

## 3. Reglas Transversales

Estas reglas aplican a **todos los endpoints** del sistema. Se definen una sola vez aquí; cada contrato individual las hereda implícitamente y solo declara las particularidades que no estén cubiertas por estas reglas.

### 3.1 Autenticación

Todos los endpoints excepto los explícitamente marcados como **Público** requieren un token JWT de acceso válido en el header `Authorization`, en el formato:

```
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...
```

El backend valida cuatro condiciones sobre cada token recibido: que la firma RS256 sea válida contra la clave pública configurada, que la expiración (`exp`) no haya sido alcanzada, que el `JTI` no figure en la blacklist de Redis y que el usuario asociado siga activo (`users.is_active = true`). Si cualquiera de las cuatro condiciones falla, el endpoint responde `401 Unauthorized` antes de ejecutar la lógica de negocio.

Los endpoints públicos son: `POST /api/v1/auth/login`, `POST /api/v1/auth/refresh`, `POST /api/v1/auth/password/recovery` y `POST /api/v1/auth/password/reset`.

### 3.2 Autorización por Rol

Una vez autenticado el usuario, cada endpoint declara qué roles tienen permitido invocarlo. La validación se realiza mediante anotaciones `@PreAuthorize("hasRole('...')")` de Spring Security sobre los métodos del controlador. Si el rol del usuario no figura entre los autorizados, el backend responde `403 Forbidden` sin ejecutar la lógica de negocio.

### 3.3 Headers de Request Estándar

| Header | Obligatorio | Descripción |
|--------|-------------|-------------|
| `Authorization` | Sí (excepto endpoints públicos) | `Bearer <accessToken>` |
| `Content-Type` | Sí (en POST/PUT/PATCH) | `application/json` |
| `Accept` | Recomendado | `application/json` |
| `Accept-Language` | Opcional | `es-EC` por defecto |

### 3.4 Headers de Response Estándar

El backend devuelve siempre los siguientes headers de seguridad, configurados a nivel de Spring Security:

| Header | Valor | Propósito |
|--------|-------|-----------|
| `Strict-Transport-Security` | `max-age=31536000; includeSubDomains` | Forzar HTTPS durante un año (RF-M7-07). |
| `X-Content-Type-Options` | `nosniff` | Prevenir MIME sniffing. |
| `X-Frame-Options` | `DENY` | Prevenir clickjacking. |
| `Cache-Control` | `no-store` (en endpoints con datos sensibles) | Evitar caché de datos clínicos. |

### 3.5 Códigos de Estado Transversales

Cualquier endpoint puede devolver estos códigos. No se listan individualmente en cada contrato — solo se documentan los códigos específicos del endpoint que aporten significado funcional adicional.

| Código | Significado | Cuándo se devuelve |
|--------|-------------|---------------------|
| `400 Bad Request` | Solicitud malformada | Body inválido, JSON malformado, validaciones de campo fallidas. |
| `401 Unauthorized` | No autenticado | Token ausente, expirado, en blacklist, malformado o usuario inactivo. |
| `403 Forbidden` | Sin permisos | Token válido pero el rol no autoriza el acceso al recurso. |
| `404 Not Found` | Recurso no encontrado | El identificador solicitado no existe en la base de datos. |
| `409 Conflict` | Conflicto de estado | Recurso duplicado o estado incompatible con la operación solicitada. |
| `422 Unprocessable Entity` | Regla de negocio violada | El payload es válido sintácticamente pero infringe una regla de dominio. |
| `429 Too Many Requests` | Rate limit excedido | Se superó el límite de peticiones para la IP o usuario. |
| `500 Internal Server Error` | Error no manejado | Excepción no capturada por el `GlobalExceptionHandler`. |

### 3.6 Formato Estándar de Errores

Todos los errores siguen la estructura definida en `common/dto/ErrorResponse.java`:

```json
{
  "timestamp": "2026-05-07T14:32:18.123Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Credenciales inválidas",
  "path": "/api/v1/auth/login",
  "traceId": "a3f1b2c4-9e8d-4a7b-b6c5-1d2e3f4a5b6c"
}
```

Para errores de validación (`400 Bad Request`), la estructura se extiende con un arreglo `fieldErrors[]`:

```json
{
  "timestamp": "2026-05-07T14:32:18.123Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/auth/login",
  "traceId": "a3f1b2c4-9e8d-4a7b-b6c5-1d2e3f4a5b6c",
  "fieldErrors": [
    { "field": "email", "message": "Debe pertenecer al dominio @tecazuay.edu.ec" },
    { "field": "password", "message": "La contraseña no puede estar vacía" }
  ]
}
```

El campo `traceId` permite correlacionar el error reportado por el cliente con los logs del backend para análisis de incidentes.

### 3.7 Paginación

Los endpoints de listado siguen el contrato estándar de Spring Data, expuesto mediante un wrapper propio que oculta la estructura interna `Page<T>` de Spring.

**Query parameters de entrada:**

| Parámetro | Tipo | Default | Descripción |
|-----------|------|---------|-------------|
| `page` | Integer | `0` | Página solicitada (base 0). |
| `size` | Integer | `20` | Tamaño de página. Mínimo 1, máximo 100. |
| `sort` | String | varía por endpoint | Campo de ordenamiento + dirección. Ej: `createdAt,desc`. |

**Estructura de la respuesta paginada:**

```json
{
  "content": [ /* array de items del tipo correspondiente */ ],
  "page": 0,
  "size": 20,
  "totalElements": 142,
  "totalPages": 8,
  "first": true,
  "last": false
}
```

### 3.8 Roles del Sistema

| Constante Spring Security | Descripción | Módulos accesibles |
|---------------------------|-------------|---------------------|
| `ROLE_DOCTOR` | Médico del departamento | M1, M2, M3, M4 (lectura), M5 (limitado), M6, M7 (login/logout/me) |
| `ROLE_DEAN` | Decano | M5, M6, M7 (login/logout/me) |
| `ROLE_ADMIN` | Administrador del Sistema | M7 (completo), M8 |
| `ROLE_STUDENT` | Estudiante | M4 (solo su propio historial), M6, M7 (login/logout/me) |

### 3.9 Auditoría Automática

Todo endpoint que modifique estado (POST, PUT, PATCH, DELETE) genera automáticamente un evento que el `AuditEventListener` del módulo M7 persiste en la tabla `audit_logs` con firma HMAC-SHA256 encadenada (RF-M7-08, RF-M7-10). Este comportamiento es transversal y no se documenta en cada contrato individual.

### 3.10 Cifrado de Datos Sensibles

Los datos clínicos sensibles (diagnósticos, antecedentes, datos obstétricos) se almacenan cifrados con AES-256 a nivel de columna mediante Jasypt (RF-M7-06). Este cifrado/descifrado es transparente para los endpoints — los DTOs siempre exponen los datos en claro al cliente autenticado y autorizado.

---

## 4. M7 — Seguridad y Auditoría

El módulo M7 expone los endpoints de autenticación, gestión de sesión, recuperación de contraseña y consulta del log de auditoría. Es el primer módulo en la secuencia de implementación porque todos los demás dependen de su infraestructura: el filtro JWT que valida cada request, el `AuditEventListener` que escucha eventos de los siete módulos restantes, y el control de acceso por rol que se aplica de forma transversal.

Los contratos se agrupan en tres sub-recursos: `/api/v1/auth/*` para autenticación y sesión, `/api/v1/auth/password/*` para gestión de contraseña, y `/api/v1/audit-logs` para consulta de logs.

### 4.1 Autenticación

#### 4.1.1 `POST /api/v1/auth/login`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `POST /api/v1/auth/login` |
| **Descripción** | Autentica al usuario con correo institucional y contraseña, y emite los tokens JWT de acceso y refresh. |
| **Rol(es) autorizados** | Público (no requiere autenticación previa) |
| **Caso de uso** | CU-M7-01 |
| **Requisitos cubiertos** | RF-M7-01, RF-M7-02, RF-M7-03, RF-M7-09, RF-M7-11 |

**Request body**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `email` | String | Sí | Formato email válido. Debe terminar en `@tecazuay.edu.ec`. Longitud máxima 255. |
| `password` | String | Sí | No vacío. Longitud entre 8 y 72 caracteres (límite de BCrypt). |

```json
{
  "email": "doctora.medico@tecazuay.edu.ec",
  "password": "P@ssw0rd2026!"
}
```

**Response body (éxito — `200 OK`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `accessToken` | String | JWT firmado con RS256. Duración 15 minutos. Incluye claims `sub`, `role`, `jti`, `iat`, `exp`. |
| `refreshToken` | String | JWT de larga duración. 7 días. Persistido en BD con su hash. |
| `tokenType` | String | Constante `"Bearer"`. |
| `expiresIn` | Integer | Segundos hasta la expiración del access token. Valor fijo `900`. |
| `user` | Object | Información mínima del usuario para que el cliente pueda renderizar la UI sin un `GET /auth/me` adicional. |
| `user.id` | Long | Identificador del usuario. |
| `user.firstName` | String | Nombre. |
| `user.lastName` | String | Apellido. |
| `user.email` | String | Correo institucional. |
| `user.role` | String | Uno de: `ROLE_DOCTOR`, `ROLE_DEAN`, `ROLE_ADMIN`, `ROLE_STUDENT`. |

```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 900,
  "user": {
    "id": 1,
    "firstName": "María",
    "lastName": "Pérez",
    "email": "doctora.medico@tecazuay.edu.ec",
    "role": "ROLE_DOCTOR"
  }
}
```

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | Autenticación exitosa. Se devuelven los tokens. |
| `400 Bad Request` | Validaciones de campo fallidas (formato de email inválido, contraseña vacía, dominio incorrecto). |
| `401 Unauthorized` | Credenciales incorrectas o usuario inactivo. **Importante:** el mensaje devuelto no distingue entre "usuario no existe" y "contraseña incorrecta", para no facilitar enumeración de cuentas. |
| `429 Too Many Requests` | Se superaron los 5 intentos fallidos por minuto desde la misma IP (RF-M7-11). |

**Notas técnicas**

- El backend registra cada intento — exitoso o fallido — en el log de auditoría con la IP de origen extraída del header `X-Forwarded-For` (Nginx) o, en su ausencia, de la conexión TCP.
- El refresh token se persiste en la base de datos solamente con su hash; el valor en claro se entrega una única vez al cliente en esta respuesta.
- En caso de éxito, el contador de intentos fallidos asociado a la IP se resetea en Redis.

---

#### 4.1.2 `POST /api/v1/auth/logout`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `POST /api/v1/auth/logout` |
| **Descripción** | Invalida el token de acceso actual agregándolo a la blacklist en Redis y revoca el refresh token asociado. |
| **Rol(es) autorizados** | Cualquier usuario autenticado |
| **Caso de uso** | CU-M7-02 |
| **Requisitos cubiertos** | RF-M7-04 |

**Request body**

Sin body. La identificación del token a invalidar se extrae del header `Authorization`.

**Response body (éxito — `204 No Content`)**

Sin body. El éxito se comunica mediante el código de estado.

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `204 No Content` | Token invalidado correctamente. |
| `401 Unauthorized` | Token ausente, malformado o ya invalidado. |

**Notas técnicas**

- El JTI del access token se persiste en Redis con clave `blacklist:{jti}` y TTL igual al tiempo restante de vida del token. Pasada la expiración natural, la entrada se elimina automáticamente, evitando crecimiento indefinido del blacklist.
- El refresh token asociado al usuario se marca como revocado en la base de datos, impidiendo su uso futuro en `POST /auth/refresh`.

---

#### 4.1.3 `POST /api/v1/auth/refresh`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `POST /api/v1/auth/refresh` |
| **Descripción** | Renueva el access token usando un refresh token válido, sin requerir nueva autenticación con credenciales. |
| **Rol(es) autorizados** | Público (no requiere access token, solo refresh token válido) |
| **Caso de uso** | — |
| **Requisitos cubiertos** | RF-M7-01 |

**Request body**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `refreshToken` | String | Sí | No vacío. Debe ser un JWT con firma RS256 válida y formato correcto. |

```json
{
  "refreshToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response body (éxito — `200 OK`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `accessToken` | String | Nuevo JWT de acceso con vigencia de 15 minutos. |
| `refreshToken` | String | Nuevo refresh token. El anterior queda revocado (rotación). |
| `tokenType` | String | Constante `"Bearer"`. |
| `expiresIn` | Integer | Segundos hasta la expiración del nuevo access token (`900`). |

```json
{
  "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 900
}
```

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | Tokens renovados exitosamente. |
| `400 Bad Request` | Body malformado o `refreshToken` ausente. |
| `401 Unauthorized` | Refresh token inválido, expirado, ya usado, revocado, o usuario inactivo. |

**Notas técnicas**

- El sistema implementa **rotación de refresh tokens**: cada uso del endpoint genera un refresh token nuevo y revoca el anterior. Esto detecta robos de token — si un atacante usa un refresh ya rotado, el legítimo dueño verá su sesión cerrada en la próxima petición.

---

#### 4.1.4 `GET /api/v1/auth/me`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/auth/me` |
| **Descripción** | Devuelve la información del usuario autenticado actualmente, derivada del token JWT. |
| **Rol(es) autorizados** | Cualquier usuario autenticado |
| **Caso de uso** | — |
| **Requisitos cubiertos** | RF-M7-05 |

**Response body (éxito — `200 OK`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Identificador del usuario. |
| `firstName` | String | Nombre. |
| `lastName` | String | Apellido. |
| `email` | String | Correo institucional. |
| `role` | String | Rol del usuario. |
| `isActive` | Boolean | Estado de la cuenta. |
| `createdAt` | String | Timestamp ISO 8601 de creación. |

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | Información devuelta correctamente. |
| `401 Unauthorized` | Token ausente, expirado, en blacklist o usuario inactivo. |

---

### 4.2 Recuperación y Cambio de Contraseña

#### 4.2.1 `POST /api/v1/auth/password/recovery`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `POST /api/v1/auth/password/recovery` |
| **Descripción** | Inicia el flujo de recuperación de contraseña enviando un correo con un token de un solo uso al email indicado. |
| **Rol(es) autorizados** | Público |
| **Requisitos cubiertos** | RF-M7-02 |

**Request body**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `email` | String | Sí | Formato email válido. Debe terminar en `@tecazuay.edu.ec`. |

**Response body (éxito — `202 Accepted`)**

Sin body. Se devuelve `202 Accepted` independientemente de que el email exista, para no facilitar enumeración de cuentas.

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `202 Accepted` | Solicitud aceptada. |
| `400 Bad Request` | Email malformado o dominio inválido. |
| `429 Too Many Requests` | Se superó el límite de solicitudes (3 por hora por IP/email). |

---

#### 4.2.2 `POST /api/v1/auth/password/reset`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `POST /api/v1/auth/password/reset` |
| **Descripción** | Restablece la contraseña usando el token recibido por correo electrónico. |
| **Rol(es) autorizados** | Público |
| **Requisitos cubiertos** | RF-M7-02 |

**Request body**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `token` | String | Sí | UUID v4 válido. |
| `newPassword` | String | Sí | Longitud 8-72. Mayúscula, minúscula, dígito y carácter especial. |
| `newPasswordConfirmation` | String | Sí | Idéntico a `newPassword`. |

**Response body (éxito — `204 No Content`)** — Sin body.

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `204 No Content` | Contraseña restablecida correctamente. |
| `400 Bad Request` | Validaciones fallidas. |
| `401 Unauthorized` | Token de recuperación inválido, expirado o ya utilizado. |

**Notas técnicas**

- Tras el reset, todos los refresh tokens activos del usuario se revocan, forzando re-login en todos sus dispositivos.

---

#### 4.2.3 `PUT /api/v1/auth/password`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `PUT /api/v1/auth/password` |
| **Descripción** | Permite a un usuario autenticado cambiar su propia contraseña, requiriendo la contraseña actual. |
| **Rol(es) autorizados** | Cualquier usuario autenticado |
| **Requisitos cubiertos** | RF-M7-02 |

**Request body**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `currentPassword` | String | Sí | No vacío. |
| `newPassword` | String | Sí | Longitud 8-72. Complejidad. Distinta de `currentPassword`. |
| `newPasswordConfirmation` | String | Sí | Idéntico a `newPassword`. |

**Response body (éxito — `204 No Content`)** — Sin body.

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `204 No Content` | Contraseña cambiada correctamente. |
| `400 Bad Request` | Validaciones fallidas. |
| `401 Unauthorized` | `currentPassword` incorrecta. |
| `422 Unprocessable Entity` | Nueva contraseña igual a la actual. |

---

### 4.3 Logs de Auditoría

#### 4.3.1 `GET /api/v1/audit-logs`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/audit-logs` |
| **Descripción** | Lista los registros del log de auditoría con filtros y paginación. |
| **Rol(es) autorizados** | `ROLE_ADMIN` |
| **Caso de uso** | CU-M7-03 |
| **Requisitos cubiertos** | RF-M7-08, RF-M7-09 |

**Query parameters**

| Parámetro | Tipo | Obligatorio | Descripción |
|-----------|------|-------------|-------------|
| `userId` | Long | No | Filtra por usuario que ejecutó la acción. |
| `action` | String (enum) | No | Filtra por tipo de acción. |
| `entityType` | String | No | Filtra por tipo de entidad afectada. |
| `ipAddress` | String | No | Filtra por dirección IP. |
| `dateFrom` | String (ISO 8601) | No | Fecha de inicio del rango. |
| `dateTo` | String (ISO 8601) | No | Fecha de fin del rango. |
| `page` `size` `sort` | — | No | Paginación estándar (sección 3.7). Default sort: `createdAt,desc`. |

**Response body (éxito — `200 OK`)**

Wrapper paginado. Cada elemento de `content[]`:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Identificador del registro. |
| `userId` | Long | Usuario que ejecutó la acción. `null` para acciones anónimas. |
| `userEmail` | String | Correo del usuario. |
| `action` | String | Tipo de acción del enum `audit_action_enum`. |
| `entityType` | String | Tipo de entidad afectada. |
| `entityId` | Long | Identificador de la entidad afectada. |
| `ipAddress` | String | IP de origen. |
| `createdAt` | String | Timestamp ISO 8601. |

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | Listado devuelto correctamente. |
| `400 Bad Request` | Parámetros de filtro malformados. |
| `403 Forbidden` | El usuario no es Administrador. |

---

#### 4.3.2 `GET /api/v1/audit-logs/{id}`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/audit-logs/{id}` |
| **Descripción** | Detalle completo de un registro de auditoría con verificación de integridad HMAC. |
| **Rol(es) autorizados** | `ROLE_ADMIN` |
| **Caso de uso** | CU-M7-03 |
| **Requisitos cubiertos** | RF-M7-08, RF-M7-10 |

**Response body (éxito — `200 OK`)**

Mismos campos que 4.3.1 más:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `userAgent` | String | User-Agent del cliente. |
| `hmacSignature` | String | Firma HMAC-SHA256 en hexadecimal. |
| `integrityVerified` | Boolean | `true` si la cadena HMAC es consistente; `false` si se detectó manipulación. |
| `previousLogId` | Long | ID del registro anterior en la cadena. |

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | Detalle devuelto correctamente. |
| `403 Forbidden` | El usuario no es Administrador. |
| `404 Not Found` | El registro no existe. |

---


## 5. M1 — Gestión de Pacientes

El módulo M1 expone los endpoints para el ciclo de vida del paciente: registro inicial, búsqueda, consulta de perfil, edición de datos de filiación y gestión de antecedentes. Todos los endpoints son de uso exclusivo del médico, salvo la consulta de perfil propio que también puede ejecutar el estudiante sobre sus propios datos.

Los contratos se agrupan en dos sub-recursos: `/api/v1/patients/*` para pacientes y `/api/v1/patients/{id}/background` para antecedentes.

### 5.1 `POST /api/v1/patients`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `POST /api/v1/patients` |
| **Descripción** | Registra un paciente nuevo con sus datos completos de filiación. |
| **Rol(es) autorizados** | `ROLE_DOCTOR` |
| **Caso de uso** | CU-M1-01 |
| **Requisitos cubiertos** | RF-M1-01, RF-M1-02, RF-M1-03, RF-M1-07, RF-M1-08, RF-M1-09 |

**Request body**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `cedula` | String | Sí | 10 dígitos. Algoritmo de dígito verificador ecuatoriano válido. Único en `patients`. |
| `firstName` | String | Sí | 1-100 caracteres. Solo letras y espacios. |
| `lastName` | String | Sí | 1-100 caracteres. Solo letras y espacios. |
| `careerId` | Long | Sí | Debe existir en catálogo `careers` y estar activa. |
| `semester` | String | Sí | 1-20 caracteres. |
| `address` | String | No | Máximo 255. |
| `neighborhood` | String | No | Máximo 100. |
| `parish` | String | No | Máximo 100. |
| `canton` | String | No | Máximo 100. |
| `province` | String | No | Máximo 100. |
| `phone` | String | No | 7-20 caracteres. Formato numérico. |
| `birthDate` | String (date) | Sí | ISO 8601 sin hora. No futura. Edad mínima 14, máxima 100. |
| `birthPlace` | String | No | Máximo 100. |
| `birthCountry` | String | No | Máximo 100. |
| `gender` | String (enum) | Sí | `MALE`, `FEMALE`, `OTHER`. |
| `maritalStatus` | String (enum) | No | `SINGLE`, `MARRIED`, `WIDOWED`, `DIVORCED`, `COMMON_LAW`. |
| `bloodType` | String (enum) | No | `A_POSITIVE`, `A_NEGATIVE`, `B_POSITIVE`, `B_NEGATIVE`, `AB_POSITIVE`, `AB_NEGATIVE`, `O_POSITIVE`, `O_NEGATIVE`. |
| `userId` | Long | No | ID del usuario `ROLE_STUDENT` asociado, si existe. Único en `patients`. |

```json
{
  "cedula": "0123456789",
  "firstName": "Carlos",
  "lastName": "Vásquez",
  "careerId": 3,
  "semester": "Quinto",
  "address": "Av. Solano y 12 de Abril",
  "neighborhood": "El Vergel",
  "parish": "Sucre",
  "canton": "Cuenca",
  "province": "Azuay",
  "phone": "0987654321",
  "birthDate": "2003-08-14",
  "birthPlace": "Cuenca",
  "birthCountry": "Ecuador",
  "gender": "MALE",
  "maritalStatus": "SINGLE",
  "bloodType": "O_POSITIVE",
  "userId": 42
}
```

**Response body (éxito — `201 Created`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Identificador asignado. |
| `cedula` | String | Cédula. |
| `firstName` | String | Nombre. |
| `lastName` | String | Apellido. |
| `career` | Object | Carrera con `{id, name}`. |
| `semester` | String | Ciclo. |
| `age` | Integer | Edad calculada automáticamente desde `birthDate` (RF-M1-03). |
| `gender` | String | Género. |
| `bloodType` | String | Tipo de sangre. |
| `maritalStatus` | String | Estado civil. |
| `phone` `address` `birthDate` `birthPlace` `birthCountry` `neighborhood` `parish` `canton` `province` | — | Tal como fueron enviados. |
| `isActive` | Boolean | Siempre `true` al crear. |
| `createdAt` | String | Timestamp ISO 8601. |

Header `Location: /api/v1/patients/{id}` apuntando al nuevo recurso.

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `201 Created` | Paciente registrado correctamente. |
| `400 Bad Request` | Validaciones de campo fallidas (cédula inválida, formato incorrecto). |
| `409 Conflict` | Cédula ya registrada para otro paciente, o `userId` ya asociado a otro paciente. |
| `422 Unprocessable Entity` | Carrera inactiva o no existente. |

---

### 5.2 `GET /api/v1/patients`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/patients` |
| **Descripción** | Lista pacientes con filtros y paginación. |
| **Rol(es) autorizados** | `ROLE_DOCTOR` |
| **Caso de uso** | CU-M1-02 |
| **Requisitos cubiertos** | RF-M1-04 |

**Query parameters**

| Parámetro | Tipo | Obligatorio | Descripción |
|-----------|------|-------------|-------------|
| `careerId` | Long | No | Filtra por carrera. |
| `gender` | String (enum) | No | Filtra por género. |
| `isActive` | Boolean | No | Default `true`. Si se envía `false`, lista solo desactivados. |
| `page` `size` `sort` | — | No | Paginación. Default sort: `lastName,asc`. |

**Response body (éxito — `200 OK`)**

Wrapper paginado. Cada elemento de `content[]` tiene la estructura simplificada:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Identificador. |
| `cedula` | String | Cédula. |
| `fullName` | String | `"firstName lastName"`. |
| `career` | Object | `{id, name}`. |
| `semester` | String | Ciclo. |
| `age` | Integer | Edad calculada. |
| `gender` | String | Género. |
| `phone` | String | Teléfono. |
| `lastAttendanceDate` | String | Fecha de la última atención registrada (puede ser `null`). |

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | Listado devuelto. |
| `400 Bad Request` | Parámetros malformados. |

---

### 5.3 `GET /api/v1/patients/search`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/patients/search` |
| **Descripción** | Búsqueda difusa de pacientes en tiempo real, tolerante a errores tipográficos. |
| **Rol(es) autorizados** | `ROLE_DOCTOR` |
| **Caso de uso** | CU-M1-02 |
| **Requisitos cubiertos** | RF-M1-04 |

**Query parameters**

| Parámetro | Tipo | Obligatorio | Descripción |
|-----------|------|-------------|-------------|
| `q` | String | Sí | Cadena de búsqueda. Mínimo 2 caracteres. |
| `limit` | Integer | No | Máximo de resultados. Default `10`, máximo `25`. |

**Response body (éxito — `200 OK`)**

Array plano (sin paginación) de hasta `limit` elementos, ordenados por similitud descendente:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Identificador. |
| `cedula` | String | Cédula. |
| `fullName` | String | Nombre completo. |
| `career` | String | Nombre de la carrera. |
| `similarity` | Number | Coeficiente pg_trgm (0 a 1). |

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | Resultados devueltos (puede ser array vacío). |
| `400 Bad Request` | Query `q` ausente o menor a 2 caracteres. |

**Notas técnicas**

- Usa el operador `%` de la extensión `pg_trgm` de PostgreSQL contra los campos `cedula`, `first_name` y `last_name`.
- El umbral de similitud está fijado en `0.3` para minimizar falsos negativos sin generar ruido.

---

### 5.4 `GET /api/v1/patients/{id}`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/patients/{id}` |
| **Descripción** | Devuelve el perfil completo del paciente. |
| **Rol(es) autorizados** | `ROLE_DOCTOR` (cualquier paciente), `ROLE_STUDENT` (solo el suyo, validado por `user_id`) |
| **Caso de uso** | CU-M1-03 |
| **Requisitos cubiertos** | RF-M1-05 |

**Response body (éxito — `200 OK`)**

Mismos campos que `POST /patients` response (sección 5.1) más:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `updatedAt` | String | Timestamp de la última modificación. |
| `attendancesCount` | Integer | Número total de atenciones registradas. |
| `lastAttendanceDate` | String | Fecha de la última atención (`null` si no tiene). |

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | Perfil devuelto correctamente. |
| `403 Forbidden` | Estudiante intentando ver paciente que no le corresponde. |
| `404 Not Found` | Paciente no existe. |

---

### 5.5 `PUT /api/v1/patients/{id}`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `PUT /api/v1/patients/{id}` |
| **Descripción** | Actualiza los datos de filiación del paciente. |
| **Rol(es) autorizados** | `ROLE_DOCTOR` |
| **Caso de uso** | CU-M1-04 |
| **Requisitos cubiertos** | RF-M1-06, RF-M1-07 |

**Request body**

Mismos campos que `POST /patients` (sección 5.1) **excepto `cedula`**, que es inmutable una vez registrada.

**Response body (éxito — `200 OK`)** — Mismo formato que `GET /patients/{id}`.

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | Paciente actualizado. |
| `400 Bad Request` | Validaciones fallidas. |
| `404 Not Found` | Paciente no existe. |
| `409 Conflict` | `userId` ya asociado a otro paciente. |

**Notas técnicas**

- Si el cliente envía `cedula` en el body, el campo se ignora silenciosamente. La cédula se considera identificador inmutable por norma MSP.
- La acción se audita con `PATIENT_UPDATED`. Hibernate Envers genera la versión histórica automáticamente.

---

### 5.6 `DELETE /api/v1/patients/{id}`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `DELETE /api/v1/patients/{id}` |
| **Descripción** | Desactiva el paciente (soft delete: `is_active = false`). No borra registros. |
| **Rol(es) autorizados** | `ROLE_DOCTOR` |
| **Requisitos cubiertos** | RF-M1-07 |

**Response body (éxito — `204 No Content`)** — Sin body.

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `204 No Content` | Paciente desactivado. |
| `404 Not Found` | Paciente no existe. |
| `409 Conflict` | Paciente ya estaba desactivado. |

---

### 5.7 `GET /api/v1/patients/{id}/background`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/patients/{id}/background` |
| **Descripción** | Obtiene los antecedentes familiares y personales del paciente. |
| **Rol(es) autorizados** | `ROLE_DOCTOR` |
| **Requisitos cubiertos** | RF-M2-03 |

**Response body (éxito — `200 OK`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `patientId` | Long | ID del paciente. |
| `allergies` | String | Antecedentes alérgicos. |
| `clinical` | String | Antecedentes clínicos. |
| `gynecological` | String | Antecedentes ginecológicos (solo en pacientes femeninas). |
| `traumatological` | String | Antecedentes traumatológicos. |
| `surgical` | String | Antecedentes quirúrgicos. |
| `pharmacological` | String | Antecedentes farmacológicos. |
| `updatedAt` | String | Timestamp ISO 8601. |
| `updatedBy` | Long | Usuario que actualizó por última vez. |

Si el paciente no tiene antecedentes registrados, el endpoint devuelve `200 OK` con todos los campos `null` excepto `patientId`.

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | Antecedentes devueltos. |
| `404 Not Found` | Paciente no existe. |

---

### 5.8 `PUT /api/v1/patients/{id}/background`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `PUT /api/v1/patients/{id}/background` |
| **Descripción** | Crea o actualiza los antecedentes del paciente (upsert). |
| **Rol(es) autorizados** | `ROLE_DOCTOR` |
| **Requisitos cubiertos** | RF-M2-03 |

**Request body**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `allergies` | String | No | Texto libre. |
| `clinical` | String | No | Texto libre. |
| `gynecological` | String | No | Solo permitido si el paciente tiene `gender = FEMALE`. |
| `traumatological` | String | No | Texto libre. |
| `surgical` | String | No | Texto libre. |
| `pharmacological` | String | No | Texto libre. |

**Response body (éxito — `200 OK`)** — Mismo formato que el GET (sección 5.7).

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | Antecedentes actualizados. |
| `404 Not Found` | Paciente no existe. |
| `422 Unprocessable Entity` | Se intentó establecer `gynecological` en un paciente masculino. |

**Notas técnicas**

- Los campos sensibles (`allergies`, `clinical`, `gynecological`, `surgical`, `pharmacological`) se cifran con AES-256 antes de persistir (RF-M7-06).

---


## 6. M2 — Atención Médica

El módulo M2 expone los endpoints para registrar atenciones médicas, agregar correcciones, manejar exámenes complementarios y generar el PDF institucional. Es el módulo de mayor complejidad del sistema — la creación de una atención compone múltiples sub-recursos (signos vitales, examen físico, diagnósticos, datos obstétricos) en una sola operación atómica.

Las atenciones son **inmutables** una vez guardadas (RF-M2-14). Cualquier corrección se realiza vía notas de corrección, no por edición directa.

### 6.1 `POST /api/v1/medical-attendances`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `POST /api/v1/medical-attendances` |
| **Descripción** | Registra una nueva atención médica completa para un paciente existente. |
| **Rol(es) autorizados** | `ROLE_DOCTOR` |
| **Caso de uso** | CU-M2-01 |
| **Requisitos cubiertos** | RF-M2-01 a RF-M2-13, RF-M2-15 |

**Request body**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `patientId` | Long | Sí | Paciente existente y activo. |
| `reasonForVisit` | String | Sí | Texto libre. Mínimo 3 caracteres. |
| `currentIllness` | String | Sí | Texto libre. |
| `vitalSigns` | Object | Sí | Bloque de signos vitales (ver abajo). |
| `physicalExam` | Array<Object> | No | Array de hallazgos por sistema corporal. |
| `obstetricEmergency` | Object | Condicional | Solo si paciente es femenino. |
| `complementaryExams` | Array<Object> | No | Cada uno con `description` y `notApplicable`. |
| `diagnoses` | Array<Object> | Sí | Mínimo 1, máximo 10. |
| `treatment` | String | Sí | Texto libre. |

**Bloque `vitalSigns`** (todos opcionales individualmente, pero al menos uno debe enviarse):

| Campo | Tipo | Validaciones (rangos fisiológicos) |
|-------|------|-------------------------------------|
| `bloodPressureSystolic` | Integer | 60-250 mmHg |
| `bloodPressureDiastolic` | Integer | 40-150 mmHg |
| `weightKg` | Number | 5.00-300.00 |
| `heightCm` | Number | 50.00-250.00 |
| `heartRate` | Integer | 30-250 |
| `respiratoryRate` | Integer | 5-60 |
| `temperatureCelsius` | Number | 30.0-45.0 |
| `oxygenSaturation` | Integer | 50-100 |
| `glasgowEye` | Integer | 1-4 |
| `glasgowVerbal` | Integer | 1-5 |
| `glasgowMotor` | Integer | 1-6 |
| `capillaryRefill` | String | Texto libre, máximo 20. |
| `pupillaryReflex` | String | Texto libre, máximo 50. |

**Bloque `physicalExam` (cada elemento)**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `bodySystem` | String (enum) | Sí | `SKIN`, `HEAD`, `NECK`, `THORAX`, `HEART`, `ABDOMEN`, `INGUINAL_REGION`, `UPPER_LIMBS`, `LOWER_LIMBS`. |
| `isNormal` | Boolean | Sí | `true` si el sistema no presenta hallazgos. |
| `description` | String | Condicional | Obligatorio si `isNormal = false`. |

**Bloque `obstetricEmergency`** (solo si paciente femenino — RF-M2-09):

| Campo | Tipo | Validaciones |
|-------|------|--------------|
| `menarche` | String | Edad en años, máximo 20. |
| `menstrualRhythmRegular` | String | Máximo 10. |
| `menstrualRhythmIrregular` | String | Máximo 10. |
| `cycles` | String | Máximo 20. |
| `fum` | String (date) | Fecha de última menstruación. |
| `ivsa` | String | Edad inicio vida sexual activa. |
| `sexualPartners` | Integer | ≥ 0. |
| `gapc` | String | Fórmula obstétrica G-A-P-C. Máximo 20. |
| `dysmenorrhea` | Boolean | — |
| `mastodynia` | Boolean | — |
| `isPregnant` | Boolean | — |
| `fpp` | String (date) | Solo si `isPregnant = true`. |
| `gestationalAgeWeeks` | Integer | 0-42. |
| `prenatalControls` | Integer | ≥ 0. |
| `immunizations` | String | Máximo 255. |
| `notes` | String | Texto libre. |

**Bloque `diagnoses` (cada elemento)**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `cie10CodeId` | Long | Sí | Debe existir en `cie10_codes` y estar activo. |
| `description` | String | No | Texto libre del médico. |
| `status` | String (enum) | Sí | `PRESUMPTIVE` o `DEFINITIVE`. |

**Response body (éxito — `201 Created`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Identificador asignado. |
| `patient` | Object | Resumen del paciente: `{id, cedula, fullName, age, gender}`. |
| `attendedBy` | Object | Médico: `{id, fullName, mspCode}` (precargado desde sesión, RF-M2-13). |
| `attendanceDate` | String | Timestamp ISO 8601 (asignado por el servidor). |
| `reasonForVisit` `currentIllness` `treatment` | String | Tal como fueron enviados. |
| `vitalSigns` | Object | Incluye `bmi` calculado automáticamente (RF-M2-06). |
| `vitalSigns.bmi` | Number | Calculado: `weightKg / (heightCm/100)²`. |
| `vitalSigns.glasgowTotal` | Integer | Suma de los tres componentes Glasgow. |
| `physicalExam` | Array | Hallazgos. |
| `obstetricEmergency` | Object | Si aplica. |
| `complementaryExams` | Array | Lista. |
| `diagnoses` | Array | Cada uno con `cie10Code: {code, description}`. |
| `createdAt` | String | Timestamp. |

Header `Location: /api/v1/medical-attendances/{id}`.

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `201 Created` | Atención registrada correctamente. |
| `400 Bad Request` | Validaciones fallidas (rangos fuera de fisiológicos, campos requeridos ausentes). |
| `404 Not Found` | Paciente o código CIE-10 no existe. |
| `422 Unprocessable Entity` | Paciente desactivado, código CIE-10 inactivo, o se enviaron datos obstétricos para paciente masculino (RF-M1-08). |

**Notas técnicas**

- La operación es transaccional (`@Transactional`): si cualquiera de los sub-recursos falla, toda la atención se revierte.
- Datos sensibles (diagnósticos, datos obstétricos, examen físico) se cifran con AES-256.
- Tras la creación, se dispara un evento `MedicalAttendanceCreatedEvent` que el módulo M6 consume para notificar al estudiante (RF-M6-01).

---

### 6.2 `GET /api/v1/medical-attendances`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/medical-attendances` |
| **Descripción** | Lista atenciones médicas con filtros y paginación. |
| **Rol(es) autorizados** | `ROLE_DOCTOR` |

**Query parameters**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `patientId` | Long | Filtra por paciente. |
| `attendedBy` | Long | Filtra por médico (útil para CU-M5-04). |
| `cie10Code` | String | Filtra atenciones que contengan ese diagnóstico. |
| `dateFrom` | String (date) | Inicio del rango. |
| `dateTo` | String (date) | Fin del rango. |
| `page` `size` `sort` | — | Default sort: `attendanceDate,desc`. |

**Response body (éxito — `200 OK`)**

Wrapper paginado. Cada elemento es una vista resumida:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Identificador. |
| `patientFullName` | String | Nombre del paciente. |
| `patientCedula` | String | Cédula. |
| `attendanceDate` | String | Timestamp. |
| `reasonForVisit` | String | Motivo (truncado a 100 caracteres). |
| `mainDiagnosis` | Object | Primer diagnóstico: `{cie10Code, description, status}`. |
| `attendedByName` | String | Médico que atendió. |
| `hasReferral` | Boolean | Si la atención originó una referencia. |
| `correctionsCount` | Integer | Número de notas de corrección asociadas. |

---

### 6.3 `GET /api/v1/medical-attendances/{id}`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/medical-attendances/{id}` |
| **Descripción** | Devuelve el detalle completo de la atención médica. |
| **Rol(es) autorizados** | `ROLE_DOCTOR`, `ROLE_STUDENT` (solo si el paciente está asociado a su `userId`) |

**Response body (éxito — `200 OK`)**

Mismo formato que el `POST /medical-attendances` response (sección 6.1) más:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `corrections` | Array<Object> | Notas de corrección asociadas: `{id, note, createdBy, createdAt}`. |
| `referralId` | Long | ID de la referencia médica si existe (`null` si no). |

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | Detalle devuelto. |
| `403 Forbidden` | Estudiante intentando ver atención que no le corresponde. |
| `404 Not Found` | Atención no existe. |

---

### 6.4 `GET /api/v1/medical-attendances/{id}/pdf`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/medical-attendances/{id}/pdf` |
| **Descripción** | Genera y devuelve el PDF de la atención con formato idéntico al formulario físico institucional. |
| **Rol(es) autorizados** | `ROLE_DOCTOR` |

**Response (éxito — `200 OK`)**

- `Content-Type: application/pdf`
- `Content-Disposition: attachment; filename="atencion_{id}_{fecha}.pdf"`
- Body: bytes binarios del PDF generado por JasperReports.

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | PDF generado y devuelto. |
| `404 Not Found` | Atención no existe. |
| `500 Internal Server Error` | Error en el motor de reportes. |

---

### 6.5 `POST /api/v1/medical-attendances/{id}/corrections`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `POST /api/v1/medical-attendances/{id}/corrections` |
| **Descripción** | Agrega una nota de corrección a una atención existente sin modificar el registro original. |
| **Rol(es) autorizados** | `ROLE_DOCTOR` |
| **Caso de uso** | CU-M2-02 |
| **Requisitos cubiertos** | RF-M2-14 |

**Request body**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `note` | String | Sí | Texto libre. Mínimo 10 caracteres, máximo 2000. |

**Response body (éxito — `201 Created`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | ID de la nota de corrección. |
| `medicalAttendanceId` | Long | ID de la atención. |
| `note` | String | Contenido de la nota. |
| `createdBy` | Object | `{id, fullName}` del médico. |
| `createdAt` | String | Timestamp. |

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `201 Created` | Nota registrada. |
| `400 Bad Request` | Nota muy corta o muy larga. |
| `404 Not Found` | Atención no existe. |

---

### 6.6 `POST /api/v1/medical-attendances/{id}/exams`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `POST /api/v1/medical-attendances/{id}/exams` |
| **Descripción** | Sube un archivo de examen complementario asociado a la atención. |
| **Rol(es) autorizados** | `ROLE_DOCTOR` |
| **Requisitos cubiertos** | RF-M2-10 |

**Request body** (`multipart/form-data`)

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `description` | String | Sí | Texto libre. |
| `file` | Binario | Sí | Formato `application/pdf`, `image/jpeg`, `image/png`. Máximo 10 MB. |

**Response body (éxito — `201 Created`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | ID del examen. |
| `description` | String | Descripción. |
| `fileName` | String | Nombre original del archivo. |
| `fileMimeType` | String | MIME type. |
| `fileSize` | Long | Tamaño en bytes. |
| `createdAt` | String | Timestamp. |

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `201 Created` | Examen subido. |
| `400 Bad Request` | Archivo de formato no permitido o demasiado grande. |
| `404 Not Found` | Atención no existe. |
| `413 Payload Too Large` | Archivo excede 10 MB. |

---

### 6.7 `GET /api/v1/medical-attendances/{id}/exams/{examId}`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/medical-attendances/{id}/exams/{examId}` |
| **Descripción** | Descarga el archivo binario del examen complementario. |
| **Rol(es) autorizados** | `ROLE_DOCTOR`, `ROLE_STUDENT` (solo si el examen pertenece a su atención) |

**Response (éxito — `200 OK`)**

- `Content-Type` igual al `fileMimeType` original.
- `Content-Disposition: attachment; filename="{fileName}"`
- Body: bytes binarios del archivo.

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | Archivo devuelto. |
| `403 Forbidden` | Estudiante intentando descargar examen ajeno. |
| `404 Not Found` | Examen o atención no existe. |

---


## 7. M3 — Referencia Médica

El módulo M3 expone los endpoints para generar referencias médicas a otros establecimientos de salud. Toda referencia debe originarse desde una atención médica existente (RF-M3-01) — no se permiten referencias independientes.

### 7.1 `POST /api/v1/medical-referrals`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `POST /api/v1/medical-referrals` |
| **Descripción** | Crea una referencia médica vinculada a una atención existente. |
| **Rol(es) autorizados** | `ROLE_DOCTOR` |
| **Caso de uso** | CU-M3-01 |
| **Requisitos cubiertos** | RF-M3-01 a RF-M3-06, RF-M3-08 |

**Request body**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `medicalAttendanceId` | Long | Sí | Atención existente. Sin referencia previa asociada (1:1). |
| `healthEstablishmentId` | Long | Sí | Establecimiento del catálogo, activo. |
| `service` | String | No | Máximo 150. |
| `specialty` | String | No | Máximo 150. |
| `referralDate` | String (date) | Sí | No futura mayor a 30 días. |
| `referralReason` | String (enum) | Sí | `LIMITED_RESOLUTION`, `LACK_OF_PROFESSIONAL`, `OTHER`. |
| `clinicalSummary` | String | Sí | Texto libre. Mínimo 10 caracteres. |
| `relevantFindings` | String | No | Texto libre. |
| `diagnoses` | Array<Object> | Sí | Mínimo 1, máximo 2 (RF-M3-06). |

**Bloque `diagnoses` (cada elemento)**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `cie10CodeId` | Long | Sí | Existente y activo. |
| `description` | String | No | Texto libre. |
| `status` | String (enum) | Sí | `PRESUMPTIVE` o `DEFINITIVE`. |

**Response body (éxito — `201 Created`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | ID asignado. |
| `medicalAttendance` | Object | `{id, attendanceDate, patientFullName}`. |
| `healthEstablishment` | Object | `{id, name, entityType}`. |
| `service` `specialty` | String | Tal como fueron enviados. |
| `referralDate` `referralReason` | — | — |
| `clinicalSummary` `relevantFindings` | String | — |
| `diagnoses` | Array | Cada uno con `cie10Code: {code, description}`. |
| `referredBy` | Object | `{id, fullName, mspCode}` del médico (precargado). |
| `institutionName` | String | Constante `"Instituto Superior Universitario TEC Azuay"`. |
| `serviceLabel` | String | Constante `"Departamento Médico"`. |
| `createdAt` | String | Timestamp. |

Header `Location: /api/v1/medical-referrals/{id}`.

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `201 Created` | Referencia creada. |
| `400 Bad Request` | Validaciones fallidas. |
| `404 Not Found` | Atención, establecimiento o código CIE-10 no existe. |
| `409 Conflict` | La atención ya tiene una referencia asociada (1:1). |
| `422 Unprocessable Entity` | Establecimiento o código CIE-10 inactivo. |

**Notas técnicas**

- Tras la creación dispara `MedicalReferralCreatedEvent` que M6 consume para notificar al estudiante (RF-M6-02) y al decano (RF-M6-08).

---

### 7.2 `GET /api/v1/medical-referrals`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/medical-referrals` |
| **Descripción** | Lista referencias médicas con filtros y paginación. |
| **Rol(es) autorizados** | `ROLE_DOCTOR`, `ROLE_DEAN` (solo conteos y datos agregados, sin clínica detallada) |

**Query parameters**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `patientId` | Long | Filtra por paciente. |
| `healthEstablishmentId` | Long | Filtra por establecimiento destino. |
| `referralReason` | String (enum) | Filtra por motivo. |
| `dateFrom` `dateTo` | String (date) | Rango de fechas. |
| `page` `size` `sort` | — | Default sort: `referralDate,desc`. |

**Response body (éxito — `200 OK`)**

Wrapper paginado con elementos resumidos:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Identificador. |
| `referralDate` | String | Fecha. |
| `patientFullName` | String | Solo visible para `ROLE_DOCTOR`. Para `ROLE_DEAN` se devuelve `"***"`. |
| `establishmentName` | String | Establecimiento destino. |
| `service` `specialty` | String | — |
| `referralReason` | String | Motivo. |
| `referredByName` | String | Médico emisor. |

**Notas técnicas**

- Para `ROLE_DEAN`, se aplica un `JsonView` que oculta datos clínicos individuales (RF-M5-07).

---

### 7.3 `GET /api/v1/medical-referrals/{id}`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/medical-referrals/{id}` |
| **Descripción** | Detalle completo de la referencia médica. |
| **Rol(es) autorizados** | `ROLE_DOCTOR`, `ROLE_DEAN` (vista limitada), `ROLE_STUDENT` (solo si la referencia es a su nombre) |

**Response body (éxito — `200 OK`)** — Mismo formato que el `POST /medical-referrals` response (sección 7.1).

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | Detalle devuelto. |
| `403 Forbidden` | Estudiante intentando ver referencia ajena. |
| `404 Not Found` | Referencia no existe. |

---

### 7.4 `GET /api/v1/medical-referrals/{id}/pdf`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/medical-referrals/{id}/pdf` |
| **Descripción** | Genera el PDF de la referencia con formato idéntico al formulario físico institucional. |
| **Rol(es) autorizados** | `ROLE_DOCTOR` |
| **Requisitos cubiertos** | RF-M3-07 |

**Response (éxito — `200 OK`)**

- `Content-Type: application/pdf`
- `Content-Disposition: attachment; filename="referencia_{id}_{fecha}.pdf"`
- Body: bytes binarios del PDF generado por JasperReports con plantilla espejo del formulario físico.

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | PDF devuelto. |
| `404 Not Found` | Referencia no existe. |

---

## 8. M4 — Historial Clínico

El módulo M4 es exclusivamente de **consulta** (RF-M4-08) — ningún endpoint permite crear ni modificar datos. Expone la evolución médica completa del paciente, gráficas de signos vitales, listado de diagnósticos recurrentes y filtros temporales.

Existen dos vistas diferenciadas: la vista completa para el médico (RF-M4-06) y la vista simplificada para el estudiante en app móvil (RF-M4-07).

### 8.1 `GET /api/v1/clinical-history/me`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/clinical-history/me` |
| **Descripción** | Historial clínico simplificado del estudiante autenticado. Endpoint principal de la app móvil. |
| **Rol(es) autorizados** | `ROLE_STUDENT` |
| **Caso de uso** | CU-M4-05 |
| **Requisitos cubiertos** | RF-M4-07 |

**Query parameters**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `dateFrom` `dateTo` | String (date) | Rango de fechas. |
| `page` `size` | Integer | Paginación. Default size 10 (mobile). |

**Response body (éxito — `200 OK`)**

Wrapper paginado de atenciones con vista mínima:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Identificador. |
| `attendanceDate` | String | Fecha. |
| `reasonForVisit` | String | Motivo. |
| `mainDiagnosis` | Object | `{cie10Code, description}`. |
| `treatment` | String | Tratamiento. |
| `hasReferral` | Boolean | Si tiene referencia asociada. |

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | Historial devuelto (puede estar vacío). |
| `404 Not Found` | El usuario autenticado no tiene perfil de paciente asociado. |

---

### 8.2 `GET /api/v1/clinical-history/patients/{patientId}`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/clinical-history/patients/{patientId}` |
| **Descripción** | Historial clínico completo de un paciente. Vista completa del médico con timeline, gráficas y diagnósticos recurrentes. |
| **Rol(es) autorizados** | `ROLE_DOCTOR` |
| **Casos de uso** | CU-M4-01, CU-M4-02, CU-M4-03, CU-M4-04 |
| **Requisitos cubiertos** | RF-M4-01, RF-M4-03, RF-M4-04, RF-M4-06 |

**Query parameters**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `dateFrom` `dateTo` | String (date) | Rango de fechas. |
| `cie10Code` | String | Filtra atenciones que contengan ese diagnóstico (RF-M4-05). |

**Response body (éxito — `200 OK`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `patient` | Object | `{id, cedula, fullName, age, gender, careerName, semester}`. |
| `summary` | Object | Resumen agregado. |
| `summary.totalAttendances` | Integer | Total de atenciones. |
| `summary.firstAttendanceDate` | String | Fecha de la primera atención. |
| `summary.lastAttendanceDate` | String | Fecha de la última atención. |
| `summary.totalReferrals` | Integer | Número de referencias médicas. |
| `recurrentDiagnoses` | Array<Object> | Diagnósticos con frecuencia ≥ 2 (RF-M4-03). |
| `recurrentDiagnoses[].cie10Code` | String | Código CIE-10. |
| `recurrentDiagnoses[].description` | String | Descripción. |
| `recurrentDiagnoses[].count` | Integer | Veces que aparece en el historial. |
| `attendances` | Array<Object> | Timeline cronológico (DESC). Cada elemento es vista resumida (mismo formato que 6.2). |
| `referrals` | Array<Object> | Referencias asociadas (mismo formato que 7.2). |

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | Historial devuelto. |
| `404 Not Found` | Paciente no existe. |

---

### 8.3 `GET /api/v1/clinical-history/patients/{patientId}/vital-signs-evolution`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/clinical-history/patients/{patientId}/vital-signs-evolution` |
| **Descripción** | Series temporales de signos vitales para alimentar las gráficas interactivas (RF-M4-02). |
| **Rol(es) autorizados** | `ROLE_DOCTOR` |
| **Caso de uso** | CU-M4-02 |
| **Requisitos cubiertos** | RF-M4-02 |

**Query parameters**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `metric` | String (enum) | `WEIGHT`, `BMI`, `BLOOD_PRESSURE`, `HEART_RATE`, `TEMPERATURE`, `OXYGEN_SATURATION`. Si se omite, devuelve todas. |
| `dateFrom` `dateTo` | String (date) | Rango. |

**Response body (éxito — `200 OK`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `patientId` | Long | ID del paciente. |
| `series` | Array<Object> | Una serie por métrica solicitada. |
| `series[].metric` | String | Nombre de la métrica. |
| `series[].unit` | String | Unidad (`kg`, `mmHg`, `bpm`, `°C`, `%`). |
| `series[].points` | Array<Object> | Puntos cronológicos. |
| `series[].points[].date` | String | Fecha de la atención. |
| `series[].points[].value` | Number | Valor (para presión, objeto `{systolic, diastolic}`). |

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | Series devueltas. |
| `404 Not Found` | Paciente no existe. |

---

### 8.4 `GET /api/v1/clinical-history/patients/{patientId}/export`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/clinical-history/patients/{patientId}/export` |
| **Descripción** | Exporta el historial clínico completo del paciente como un único PDF. |
| **Rol(es) autorizados** | `ROLE_DOCTOR` |

**Response (éxito — `200 OK`)**

- `Content-Type: application/pdf`
- `Content-Disposition: attachment; filename="historial_{cedula}_{fecha}.pdf"`
- Body: PDF consolidado generado por JasperReports.

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | PDF devuelto. |
| `404 Not Found` | Paciente no existe. |

---


## 9. M5 — Dashboard y Reportes

El módulo M5 expone los endpoints que alimentan el dashboard estadístico del decano y la vista de estadísticas propias del médico (RF-M5-08). Todos los endpoints devuelven **datos agregados** — ningún indicador permite identificar a un estudiante individual (RF-M5-07).

Adicionalmente, expone los endpoints de exportación a PDF y Excel y los de generación de reportes ad-hoc.

### 9.1 `GET /api/v1/dashboard/kpis`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/dashboard/kpis` |
| **Descripción** | KPIs principales del dashboard. Los valores se calculan respetando el rol: el decano ve la totalidad institucional; el médico ve solo sus propias atenciones. |
| **Rol(es) autorizados** | `ROLE_DOCTOR`, `ROLE_DEAN` |
| **Caso de uso** | CU-M5-01, CU-M5-04 |
| **Requisitos cubiertos** | RF-M5-01, RF-M5-08 |

**Query parameters**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `dateFrom` `dateTo` | String (date) | Rango. Default: mes actual. |

**Response body (éxito — `200 OK`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `period` | Object | `{from, to}`. |
| `totalAttendances` | Integer | Total de atenciones en el período. |
| `uniquePatients` | Integer | Pacientes únicos atendidos. |
| `totalReferrals` | Integer | Referencias generadas. |
| `averageBmi` | Number | IMC promedio. |
| `recurrencyRate` | Number | Porcentaje de pacientes con ≥ 2 atenciones (0-100). |
| `topDiagnosis` | Object | Diagnóstico más frecuente: `{cie10Code, description, count}`. |
| `comparison` | Object | Comparación con el período inmediatamente anterior: `{attendancesDelta, referralsDelta}` en porcentaje. |

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | KPIs devueltos. |
| `400 Bad Request` | Rango de fechas inválido. |

---

### 9.2 `GET /api/v1/dashboard/charts/attendances-over-time`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/dashboard/charts/attendances-over-time` |
| **Descripción** | Serie temporal de atenciones agrupadas por día, semana o mes. |
| **Rol(es) autorizados** | `ROLE_DOCTOR`, `ROLE_DEAN` |

**Query parameters**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `dateFrom` `dateTo` | String (date) | Rango (default últimos 90 días). |
| `granularity` | String (enum) | `DAY`, `WEEK`, `MONTH`. Default `DAY`. |

**Response body (éxito — `200 OK`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `granularity` | String | Granularidad aplicada. |
| `points` | Array<Object> | Puntos cronológicos. |
| `points[].date` | String | Fecha del bucket. |
| `points[].count` | Integer | Atenciones en ese bucket. |
| `anomalies` | Array<Object> | Picos o caídas detectadas (RF-M5-02). |
| `anomalies[].date` | String | Fecha del bucket anómalo. |
| `anomalies[].deviationStdDev` | Number | Cuántas desviaciones estándar se aleja del promedio. |

---

### 9.3 `GET /api/v1/dashboard/charts/diagnoses-by-cie10`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/dashboard/charts/diagnoses-by-cie10` |
| **Descripción** | Distribución agregada de diagnósticos por código CIE-10. |
| **Rol(es) autorizados** | `ROLE_DOCTOR`, `ROLE_DEAN` |

**Query parameters**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `dateFrom` `dateTo` | String (date) | Rango. |
| `topN` | Integer | Cantidad de diagnósticos a devolver. Default 10. |

**Response body (éxito — `200 OK`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `period` | Object | `{from, to}`. |
| `data` | Array<Object> | Lista ordenada por frecuencia DESC. |
| `data[].cie10Code` | String | Código. |
| `data[].description` | String | Descripción. |
| `data[].count` | Integer | Frecuencia. |
| `data[].percentage` | Number | Porcentaje del total. |

---

### 9.4 `GET /api/v1/dashboard/charts/attendances-by-career`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/dashboard/charts/attendances-by-career` |
| **Descripción** | Distribución de atenciones por carrera. |
| **Rol(es) autorizados** | `ROLE_DEAN` (visión institucional completa) |

**Response body (éxito — `200 OK`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `period` | Object | Rango. |
| `data` | Array<Object> | Una entrada por carrera. |
| `data[].careerId` | Long | ID. |
| `data[].careerName` | String | Nombre. |
| `data[].attendancesCount` | Integer | Atenciones. |
| `data[].uniquePatients` | Integer | Pacientes únicos. |

---

### 9.5 `GET /api/v1/dashboard/charts/attendances-by-demographics`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/dashboard/charts/attendances-by-demographics` |
| **Descripción** | Distribución demográfica (género y rangos etarios) de las atenciones. |
| **Rol(es) autorizados** | `ROLE_DEAN` |

**Response body (éxito — `200 OK`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `byGender` | Array<Object> | `{gender, count, percentage}`. |
| `byAgeGroup` | Array<Object> | Buckets etarios: `<18`, `18-22`, `23-27`, `28-35`, `>35`. |

---

### 9.6 `POST /api/v1/reports/generate`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `POST /api/v1/reports/generate` |
| **Descripción** | Genera un reporte ad-hoc según filtros y formato solicitados. La generación es síncrona para volúmenes bajos; se ejecuta en background si el rango supera 6 meses. |
| **Rol(es) autorizados** | `ROLE_DOCTOR`, `ROLE_DEAN` |
| **Caso de uso** | CU-M5-03 |
| **Requisitos cubiertos** | RF-M5-04, RF-M5-05 |

**Request body**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `reportType` | String (enum) | Sí | `STATISTICAL_DASHBOARD`, `ATTENDANCES_LIST`, `REFERRALS_LIST`, `DIAGNOSES_DISTRIBUTION`. |
| `format` | String (enum) | Sí | `PDF`, `XLSX`. |
| `dateFrom` `dateTo` | String (date) | Sí | Rango. |
| `filters` | Object | No | Filtros adicionales según `reportType`. |

**Response body (éxito — `202 Accepted`)**

Si se acepta para procesamiento asíncrono:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `reportId` | Long | Identificador del trabajo de reporte. |
| `status` | String | `QUEUED` o `COMPLETED`. |
| `downloadUrl` | String | Si está completo, URL del endpoint 9.7. |

**Response body (éxito — `200 OK` para reportes pequeños)**

Body: bytes binarios del archivo. Headers `Content-Type` y `Content-Disposition` adecuados.

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | Reporte generado y devuelto inline. |
| `202 Accepted` | Reporte aceptado para procesamiento asíncrono. |
| `400 Bad Request` | Validaciones fallidas. |
| `403 Forbidden` | El rol no tiene permitido el `reportType` solicitado. |

---

### 9.7 `GET /api/v1/reports/{id}`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/reports/{id}` |
| **Descripción** | Descarga un reporte previamente generado. |
| **Rol(es) autorizados** | `ROLE_DOCTOR`, `ROLE_DEAN` (solo el solicitante) |

**Response (éxito — `200 OK`)**

- `Content-Type: application/pdf` o `application/vnd.openxmlformats-officedocument.spreadsheetml.sheet`.
- Body: bytes binarios.

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `200 OK` | Archivo devuelto. |
| `202 Accepted` | El reporte aún está en cola; reintentar. |
| `403 Forbidden` | El reporte fue solicitado por otro usuario. |
| `404 Not Found` | Reporte no existe. |
| `410 Gone` | El reporte expiró (los reportes se eliminan a las 24 horas). |

---

## 10. M6 — Notificaciones

El módulo M6 gestiona las notificaciones in-app del usuario. Las notificaciones se generan automáticamente desde otros módulos (M2, M3, M5) — este módulo solo expone endpoints de **consulta y gestión** del estado de lectura.

Las notificaciones push móviles vía Firebase y los mensajes WebSocket en tiempo real están fuera del alcance de los contratos REST y se documentan en el contrato específico de notificaciones en tiempo real.

### 10.1 `GET /api/v1/notifications`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/notifications` |
| **Descripción** | Lista las notificaciones del usuario autenticado con paginación. |
| **Rol(es) autorizados** | Cualquier usuario autenticado |
| **Caso de uso** | CU-M6-02 |
| **Requisitos cubiertos** | RF-M6-11 |

**Query parameters**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `isRead` | Boolean | Filtra por estado de lectura. |
| `type` | String (enum) | `CLINICAL`, `EPIDEMIOLOGICAL`, `INSTITUTIONAL`. |
| `page` `size` `sort` | — | Default sort: `createdAt,desc`. |

**Response body (éxito — `200 OK`)**

Wrapper paginado. Cada elemento:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Identificador. |
| `title` | String | Título. |
| `message` | String | Cuerpo. |
| `type` | String | Tipo. |
| `isRead` | Boolean | Estado de lectura. |
| `sentAt` | String | Timestamp de envío. |
| `createdAt` | String | Timestamp de creación. |

---

### 10.2 `GET /api/v1/notifications/unread-count`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/notifications/unread-count` |
| **Descripción** | Devuelve el contador de notificaciones no leídas. Endpoint optimizado, debe responder en <50 ms. |
| **Rol(es) autorizados** | Cualquier usuario autenticado |

**Response body (éxito — `200 OK`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `count` | Integer | Notificaciones no leídas. |

**Notas técnicas**

- El conteo se cachea en Redis con TTL corto (30s) para optimizar el polling del frontend.
- El índice combinado `(recipient_id, is_read)` en `notifications` garantiza la velocidad de la consulta.

---

### 10.3 `PATCH /api/v1/notifications/{id}/read`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `PATCH /api/v1/notifications/{id}/read` |
| **Descripción** | Marca una notificación como leída. |
| **Rol(es) autorizados** | Cualquier usuario autenticado (solo sus propias notificaciones) |

**Response body (éxito — `204 No Content`)** — Sin body.

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `204 No Content` | Notificación marcada como leída. |
| `403 Forbidden` | La notificación pertenece a otro usuario. |
| `404 Not Found` | Notificación no existe. |

---

### 10.4 `PATCH /api/v1/notifications/read-all`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `PATCH /api/v1/notifications/read-all` |
| **Descripción** | Marca todas las notificaciones del usuario como leídas. |
| **Rol(es) autorizados** | Cualquier usuario autenticado |

**Response body (éxito — `200 OK`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `markedCount` | Integer | Notificaciones afectadas. |

---

### 10.5 `DELETE /api/v1/notifications/{id}`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `DELETE /api/v1/notifications/{id}` |
| **Descripción** | Elimina una notificación de la bandeja del usuario. |
| **Rol(es) autorizados** | Cualquier usuario autenticado (solo sus propias notificaciones) |

**Response body (éxito — `204 No Content`)** — Sin body.

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `204 No Content` | Notificación eliminada. |
| `403 Forbidden` | La notificación pertenece a otro usuario. |
| `404 Not Found` | Notificación no existe. |

---

### 10.6 `POST /api/v1/notifications/devices`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `POST /api/v1/notifications/devices` |
| **Descripción** | Registra un dispositivo móvil para recibir notificaciones push vía Firebase Cloud Messaging. |
| **Rol(es) autorizados** | `ROLE_STUDENT` (la app móvil es solo para estudiantes) |

**Request body**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `fcmToken` | String | Sí | Token FCM. Máximo 4096 caracteres. |
| `deviceType` | String (enum) | Sí | `ANDROID` (en MVP solo Android, RF-V1). |
| `deviceModel` | String | No | Modelo del dispositivo. |
| `appVersion` | String | No | Versión de la app. |

**Response body (éxito — `201 Created`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `deviceId` | Long | ID asignado al dispositivo. |
| `registeredAt` | String | Timestamp. |

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `201 Created` | Dispositivo registrado. |
| `400 Bad Request` | FCM token inválido. |
| `409 Conflict` | El token ya estaba registrado para este usuario (operación idempotente — devuelve el existente). |

---

### 10.7 `DELETE /api/v1/notifications/devices/{deviceId}`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `DELETE /api/v1/notifications/devices/{deviceId}` |
| **Descripción** | Desregistra un dispositivo (logout móvil, desinstalación). |
| **Rol(es) autorizados** | `ROLE_STUDENT` |

**Response body (éxito — `204 No Content`)** — Sin body.

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `204 No Content` | Dispositivo desregistrado. |
| `403 Forbidden` | El dispositivo pertenece a otro usuario. |
| `404 Not Found` | Dispositivo no existe. |

---


## 11. M8 — Administración del Sistema

El módulo M8 expone los endpoints de administración del sistema: gestión de usuarios, mantenimiento de catálogos (carreras, establecimientos de salud, CIE-10), configuración de umbrales de notificaciones, configuración de reportes automáticos y gestión de respaldos. Es el módulo más extenso en cantidad de endpoints, pero todos siguen patrones CRUD estándar.

Acceso restringido exclusivamente al `ROLE_ADMIN` salvo lecturas de catálogos requeridas por otros módulos.

### 11.1 Gestión de Usuarios

#### 11.1.1 `POST /api/v1/users`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `POST /api/v1/users` |
| **Descripción** | Crea un usuario del sistema. La contraseña inicial se genera automáticamente y se envía por correo. |
| **Rol(es) autorizados** | `ROLE_ADMIN` |
| **Caso de uso** | CU-M8-01 |
| **Requisitos cubiertos** | RF-M8-01, RF-M8-02 |

**Request body**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `firstName` | String | Sí | 1-100 caracteres. |
| `lastName` | String | Sí | 1-100 caracteres. |
| `email` | String | Sí | Formato email. Debe terminar en `@tecazuay.edu.ec`. Único en `users`. |
| `role` | String (enum) | Sí | `ROLE_DOCTOR`, `ROLE_DEAN`, `ROLE_ADMIN`, `ROLE_STUDENT`. |

**Response body (éxito — `201 Created`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | ID asignado. |
| `firstName` `lastName` `email` `role` | — | — |
| `isActive` | Boolean | `true` por defecto. |
| `createdAt` | String | Timestamp. |

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `201 Created` | Usuario creado. La contraseña inicial se envió por correo. |
| `400 Bad Request` | Validaciones fallidas. |
| `409 Conflict` | El correo ya está registrado. |

**Notas técnicas**

- La contraseña inicial es un valor aleatorio de 16 caracteres con la complejidad requerida; se entrega solo por correo, nunca en la respuesta.
- El usuario debe cambiarla en su primer login (flag `must_change_password` interno).

---

#### 11.1.2 `GET /api/v1/users`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/users` |
| **Descripción** | Lista usuarios con filtros y paginación. |
| **Rol(es) autorizados** | `ROLE_ADMIN` |

**Query parameters**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `role` | String (enum) | Filtra por rol. |
| `isActive` | Boolean | Default `true`. |
| `q` | String | Búsqueda libre por nombre/email. |
| `page` `size` `sort` | — | Default sort: `lastName,asc`. |

**Response body (éxito — `200 OK`)** — Wrapper paginado de objetos `User` (mismos campos que en 11.1.1 + `updatedAt`).

---

#### 11.1.3 `GET /api/v1/users/{id}`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/users/{id}` |
| **Descripción** | Detalle de un usuario. |
| **Rol(es) autorizados** | `ROLE_ADMIN` |

**Response body (éxito — `200 OK`)** — Mismos campos que `POST /users` response (sección 11.1.1) más `updatedAt`, `lastLoginAt` y, si es estudiante, `patientId` asociado.

---

#### 11.1.4 `PUT /api/v1/users/{id}`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `PUT /api/v1/users/{id}` |
| **Descripción** | Actualiza datos del usuario. El email y el rol no son modificables por este endpoint. |
| **Rol(es) autorizados** | `ROLE_ADMIN` |

**Request body**

| Campo | Tipo | Validaciones |
|-------|------|--------------|
| `firstName` | String | 1-100 caracteres. |
| `lastName` | String | 1-100 caracteres. |

**Response body (éxito — `200 OK`)** — Usuario actualizado.

---

#### 11.1.5 `PATCH /api/v1/users/{id}/role`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `PATCH /api/v1/users/{id}/role` |
| **Descripción** | Cambia el rol de un usuario. Operación auditada críticamente. |
| **Rol(es) autorizados** | `ROLE_ADMIN` |
| **Requisitos cubiertos** | RF-M8-02 |

**Request body**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `role` | String (enum) | Sí | Rol distinto al actual. |
| `reason` | String | Sí | Justificación obligatoria, mínimo 20 caracteres. |

**Response body (éxito — `204 No Content`)** — Sin body.

**Notas técnicas**

- Tras el cambio, todos los tokens del usuario se invalidan (todos los refresh tokens revocados, todos los access tokens activos en blacklist hasta su expiración natural).
- Se registra evento crítico de auditoría con `USER_ROLE_CHANGED` y la justificación del admin.

---

#### 11.1.6 `PATCH /api/v1/users/{id}/activate`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `PATCH /api/v1/users/{id}/activate` |
| **Descripción** | Reactiva una cuenta desactivada. |
| **Rol(es) autorizados** | `ROLE_ADMIN` |

**Response body (éxito — `204 No Content`)** — Sin body.

---

#### 11.1.7 `PATCH /api/v1/users/{id}/deactivate`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `PATCH /api/v1/users/{id}/deactivate` |
| **Descripción** | Desactiva una cuenta. La sesión activa del usuario se cierra inmediatamente. |
| **Rol(es) autorizados** | `ROLE_ADMIN` |

**Response body (éxito — `204 No Content`)** — Sin body.

---

### 11.2 Catálogo de Carreras

#### 11.2.1 `GET /api/v1/careers`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/careers` |
| **Descripción** | Lista las carreras del catálogo. Endpoint público para todos los usuarios autenticados (necesario para crear pacientes). |
| **Rol(es) autorizados** | Cualquier usuario autenticado |
| **Requisitos cubiertos** | RF-M8-03 |

**Query parameters**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `isActive` | Boolean | Default `true`. |

**Response body (éxito — `200 OK`)**

Array plano (no paginado, son ~16 carreras):

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Identificador. |
| `name` | String | Nombre. |
| `isActive` | Boolean | Estado. |

---

#### 11.2.2 `POST /api/v1/careers`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `POST /api/v1/careers` |
| **Rol(es) autorizados** | `ROLE_ADMIN` |

**Request body**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `name` | String | Sí | 1-150 caracteres. Único. |

**Response body (éxito — `201 Created`)** — Objeto `Career`.

---

#### 11.2.3 `PUT /api/v1/careers/{id}`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `PUT /api/v1/careers/{id}` |
| **Rol(es) autorizados** | `ROLE_ADMIN` |

**Request body** — Mismo que `POST /careers`.

**Response body (éxito — `200 OK`)** — `Career` actualizado.

---

#### 11.2.4 `DELETE /api/v1/careers/{id}`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `DELETE /api/v1/careers/{id}` |
| **Descripción** | Desactiva la carrera (soft delete). |
| **Rol(es) autorizados** | `ROLE_ADMIN` |

**Response body (éxito — `204 No Content`)** — Sin body.

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `204 No Content` | Carrera desactivada. |
| `409 Conflict` | La carrera tiene pacientes activos asociados. Migrarlos primero. |

---

### 11.3 Catálogo de Establecimientos de Salud

#### 11.3.1 `GET /api/v1/health-establishments`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/health-establishments` |
| **Rol(es) autorizados** | `ROLE_DOCTOR`, `ROLE_ADMIN` |
| **Requisitos cubiertos** | RF-M8-04 |

**Query parameters**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `q` | String | Búsqueda parcial por nombre. |
| `entityType` | String | Filtra por tipo. |
| `isActive` | Boolean | Default `true`. |

**Response body (éxito — `200 OK`)** — Array plano:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Identificador. |
| `name` | String | Nombre completo. |
| `entityType` | String | Tipo (hospital, clínica, centro especializado). |
| `isActive` | Boolean | Estado. |

---

#### 11.3.2 `POST /api/v1/health-establishments`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `POST /api/v1/health-establishments` |
| **Rol(es) autorizados** | `ROLE_ADMIN` |

**Request body**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `name` | String | Sí | 1-255. Único. |
| `entityType` | String | No | Máximo 100. |

**Response body (éxito — `201 Created`)** — Objeto.

---

#### 11.3.3 `PUT /api/v1/health-establishments/{id}` y `DELETE /api/v1/health-establishments/{id}`

Mismo patrón que carreras (secciones 11.2.3 y 11.2.4).

---

### 11.4 Catálogo CIE-10

#### 11.4.1 `GET /api/v1/cie10-codes`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/cie10-codes` |
| **Descripción** | Búsqueda en el catálogo CIE-10 con autocompletado en tiempo real. |
| **Rol(es) autorizados** | `ROLE_DOCTOR` |
| **Requisitos cubiertos** | RF-M2-11, RF-M3-06 |

**Query parameters**

| Parámetro | Tipo | Obligatorio | Descripción |
|-----------|------|-------------|-------------|
| `q` | String | Sí | Mínimo 2 caracteres. Busca en `code` y `description`. |
| `limit` | Integer | No | Máximo `25`. Default `10`. |

**Response body (éxito — `200 OK`)**

Array plano ordenado por relevancia:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Identificador. |
| `code` | String | Código (ej. `J00`). |
| `description` | String | Descripción de la enfermedad. |

---

#### 11.4.2 `POST /api/v1/cie10-codes/import`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `POST /api/v1/cie10-codes/import` |
| **Descripción** | Carga masiva del catálogo CIE-10 desde un archivo CSV oficial. |
| **Rol(es) autorizados** | `ROLE_ADMIN` |
| **Requisitos cubiertos** | RF-M8-05 |

**Request body** (`multipart/form-data`)

| Campo | Tipo | Validaciones |
|-------|------|--------------|
| `file` | Binario | Formato `text/csv`. Máximo 10 MB. Columnas esperadas: `code`, `description`. |
| `replaceExisting` | Boolean | Default `false`. Si `true`, los códigos existentes se actualizan. |

**Response body (éxito — `202 Accepted`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `jobId` | String | ID del trabajo asíncrono. |
| `expectedRows` | Integer | Filas detectadas en el archivo. |

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `202 Accepted` | Importación encolada. |
| `400 Bad Request` | Archivo malformado. |
| `413 Payload Too Large` | Archivo > 10 MB. |

---

### 11.5 Configuración del Sistema

#### 11.5.1 `GET /api/v1/notification-thresholds`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/notification-thresholds` |
| **Rol(es) autorizados** | `ROLE_ADMIN` |
| **Requisitos cubiertos** | RF-M6-10, RF-M8-06 |

**Response body (éxito — `200 OK`)** — Array de objetos:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Identificador. |
| `thresholdType` | String (enum) | `PATIENT_VISITS`, `EPIDEMIOLOGICAL`, `INSTITUTIONAL`. |
| `value` | Integer | Valor umbral. |
| `periodDays` | Integer | Ventana en días. |
| `isActive` | Boolean | Habilitado. |
| `updatedAt` | String | Timestamp. |
| `updatedBy` | Object | `{id, fullName}`. |

---

#### 11.5.2 `PUT /api/v1/notification-thresholds/{id}`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `PUT /api/v1/notification-thresholds/{id}` |
| **Rol(es) autorizados** | `ROLE_ADMIN` |

**Request body**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `value` | Integer | Sí | ≥ 1. |
| `periodDays` | Integer | Sí | 1-365. |
| `isActive` | Boolean | Sí | — |

**Response body (éxito — `200 OK`)** — Umbral actualizado.

---

#### 11.5.3 `GET /api/v1/automatic-reports`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/automatic-reports` |
| **Rol(es) autorizados** | `ROLE_ADMIN` |
| **Requisitos cubiertos** | RF-M5-06, RF-M8-07 |

**Response body (éxito — `200 OK`)** — Array:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Identificador. |
| `reportType` | String | Tipo de reporte. |
| `frequency` | String (enum) | `WEEKLY`, `MONTHLY`, `SEMESTRAL`. |
| `filters` | Object | Filtros aplicados. |
| `recipients` | Array<String> | Correos destinatarios. |
| `isActive` | Boolean | — |
| `updatedAt` | String | Timestamp. |

---

#### 11.5.4 `POST /api/v1/automatic-reports`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `POST /api/v1/automatic-reports` |
| **Rol(es) autorizados** | `ROLE_ADMIN` |

**Request body**

| Campo | Tipo | Obligatorio | Validaciones |
|-------|------|-------------|--------------|
| `reportType` | String | Sí | Catálogo de tipos. |
| `frequency` | String (enum) | Sí | — |
| `filters` | Object | No | — |
| `recipients` | Array<String> | Sí | Mínimo 1. Máximo 10. Cada uno debe ser email institucional. |

**Response body (éxito — `201 Created`)** — Reporte automático creado.

---

#### 11.5.5 `PUT /api/v1/automatic-reports/{id}` y `DELETE /api/v1/automatic-reports/{id}`

Mismo patrón estándar.

---

### 11.6 Respaldos

#### 11.6.1 `POST /api/v1/backups/trigger`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `POST /api/v1/backups/trigger` |
| **Descripción** | Dispara un respaldo manual de la base de datos. |
| **Rol(es) autorizados** | `ROLE_ADMIN` |
| **Caso de uso** | CU-M8-04 |
| **Requisitos cubiertos** | RF-M8-08 |

**Response body (éxito — `202 Accepted`)**

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `backupId` | Long | ID del trabajo. |
| `status` | String | `IN_PROGRESS`. |

**Códigos de estado**

| Código | Significado |
|--------|-------------|
| `202 Accepted` | Respaldo encolado. |
| `409 Conflict` | Ya existe un respaldo en progreso. |

---

#### 11.6.2 `GET /api/v1/backups`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/backups` |
| **Descripción** | Lista el historial de respaldos. |
| **Rol(es) autorizados** | `ROLE_ADMIN` |

**Query parameters** — Paginación estándar. Default sort: `executedAt,desc`.

**Response body (éxito — `200 OK`)** — Wrapper paginado:

| Campo | Tipo | Descripción |
|-------|------|-------------|
| `id` | Long | Identificador. |
| `executedAt` | String | Timestamp. |
| `triggeredBy` | String (enum) | `MANUAL`, `SCHEDULED`. |
| `requestedBy` | Object | `{id, fullName}` (si MANUAL). |
| `fileName` | String | Nombre del archivo de respaldo. |
| `fileSizeBytes` | Long | Tamaño en bytes. |
| `status` | String (enum) | `IN_PROGRESS`, `COMPLETED`, `FAILED`. |
| `errorMessage` | String | Si `FAILED`, mensaje del error. |

---

#### 11.6.3 `GET /api/v1/backups/{id}`

| Campo | Valor |
|-------|-------|
| **Método + Ruta** | `GET /api/v1/backups/{id}` |
| **Descripción** | Detalle de un respaldo. |
| **Rol(es) autorizados** | `ROLE_ADMIN` |

**Response body (éxito — `200 OK`)** — Mismos campos que el listado.

---

## 12. Resumen de Cobertura

La siguiente tabla resume la cobertura completa del documento. Todos los endpoints quedan trazados a sus casos de uso y requisitos funcionales correspondientes.

| Módulo | Endpoints | Casos de uso cubiertos | RF cubiertos |
|--------|-----------|------------------------|--------------|
| M7 — Seguridad y Auditoría | 9 | CU-M7-01, CU-M7-02, CU-M7-03 | RF-M7-01, 02, 03, 04, 05, 08, 09, 10, 11 |
| M1 — Gestión de Pacientes | 8 | CU-M1-01, CU-M1-02, CU-M1-03, CU-M1-04 | RF-M1-01 a RF-M1-09 |
| M2 — Atención Médica | 7 | CU-M2-01, CU-M2-02 | RF-M2-01 a RF-M2-15 |
| M3 — Referencia Médica | 4 | CU-M3-01, CU-M3-02 | RF-M3-01 a RF-M3-08 |
| M4 — Historial Clínico | 4 | CU-M4-01, CU-M4-02, CU-M4-03, CU-M4-04, CU-M4-05 | RF-M4-01 a RF-M4-08 |
| M5 — Dashboard y Reportes | 7 | CU-M5-01, CU-M5-02, CU-M5-03, CU-M5-04 | RF-M5-01 a RF-M5-08 |
| M6 — Notificaciones | 7 | CU-M6-01, CU-M6-02, CU-M6-03 | RF-M6-01, 02, 09, 10, 11 |
| M8 — Administración | 22 | CU-M8-01, CU-M8-02, CU-M8-03, CU-M8-04 | RF-M8-01 a RF-M8-08 |
| **Total** | **68 endpoints** | **24 casos de uso** | **78 requisitos funcionales** |

### Validación de Cobertura por Categoría de Endpoint

| Tipo de operación | Cantidad |
|-------------------|----------|
| Endpoints de lectura (GET) | 32 |
| Endpoints de creación (POST) | 16 |
| Endpoints de actualización (PUT/PATCH) | 14 |
| Endpoints de desactivación/eliminación (DELETE) | 6 |
| **Total** | **68** |

### Endpoints Públicos (sin autenticación)

| Endpoint | Justificación |
|----------|---------------|
| `POST /api/v1/auth/login` | Punto de entrada al sistema. |
| `POST /api/v1/auth/refresh` | Renovación de tokens sin re-login. |
| `POST /api/v1/auth/password/recovery` | Inicio del flujo de recuperación. |
| `POST /api/v1/auth/password/reset` | Validación con token de un solo uso. |

Todos los demás endpoints requieren autenticación válida y autorización por rol.

---

*MEDISTA — Contratos de API REST v1.0*
*Instituto Superior Universitario TEC Azuay — Mayo 2026*
