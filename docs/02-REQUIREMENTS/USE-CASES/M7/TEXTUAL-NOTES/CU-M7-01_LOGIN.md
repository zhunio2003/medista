# CU-M7-01 — Iniciar Sesión

**Módulo:** M7 — Seguridad y Auditoría  
**Versión:** 1.0  
**Fecha:** 16 Abril 2026

---

## Descripción

Permite a cualquier usuario del sistema autenticarse mediante su correo institucional @tecazuay.edu.ec y contraseña, obteniendo acceso a las funcionalidades correspondientes a su rol.

---

## Información General

| Campo | Contenido |
|---|---|
| **ID** | CU-M7-01 |
| **Nombre** | Iniciar sesión |
| **Actores** | Usuario (Médico, Decano, Administrador, Estudiante) |
| **Módulo** | M7 — Seguridad y Auditoría |
| **Requisitos asociados** | RF-M7-01, RF-M7-02, RF-M7-03, RF-M7-09, RF-M7-11 |

---

## Precondiciones

1. El usuario tiene una cuenta activa en el sistema.
2. El usuario tiene acceso a la red institucional.

---

## Postcondiciones

1. El usuario tiene sesión activa con token JWT válido.
2. El sistema registra el intento de autenticación exitoso en el log de auditoría con usuario, timestamp e IP de origen.

---

## Flujo Principal

| Paso | Actor | Acción |
|---|---|---|
| 1 | Usuario | Ingresa su correo institucional @tecazuay.edu.ec y contraseña en el formulario de autenticación. |
| 2 | Sistema | Valida que el correo pertenezca al dominio @tecazuay.edu.ec. *[include: Validar dominio institucional]* |
| 3 | Sistema | Valida las credenciales ingresadas contra los registros de la base de datos. *[include: Validar credenciales]* |
| 4 | Sistema | Genera el token JWT de acceso con duración de 15 minutos y el refresh token con duración de 7 días. |
| 5 | Sistema | Registra el acceso exitoso en el log de auditoría con usuario, timestamp e IP de origen. |
| 6 | Usuario | Accede al sistema con las funcionalidades correspondientes a su rol. |

---

## Flujos Alternativos

### FA-01 — Dominio inválido (paso 2)

| Paso | Actor | Acción |
|---|---|---|
| 2a | Sistema | Detecta que el correo ingresado no pertenece al dominio @tecazuay.edu.ec. |
| 2b | Sistema | Rechaza el intento e informa al usuario que el correo no corresponde al dominio institucional. |
| 2c | Sistema | Registra el intento fallido en el log de auditoría. |
| 2d | — | El caso de uso termina. |

### FA-02 — Credenciales incorrectas (paso 3)

| Paso | Actor | Acción |
|---|---|---|
| 3a | Sistema | Detecta que las credenciales ingresadas no coinciden con ninguna cuenta activa. |
| 3b | Sistema | Rechaza el intento, registra el fallo en el log de auditoría e incrementa el contador de intentos fallidos para esa IP. |
| 3c | Sistema | Informa al usuario que las credenciales son incorrectas. |
| 3d | — | El usuario puede intentar nuevamente mientras no supere el límite de intentos. |

### FA-03 — Límite de intentos superado (paso 3)

| Paso | Actor | Acción |
|---|---|---|
| 3a | Sistema | Detecta que la IP ha superado el máximo de 5 intentos fallidos por minuto. |
| 3b | Sistema | Bloquea temporalmente las solicitudes provenientes de esa IP. *[extend: Bloquear IP]* |
| 3c | — | El caso de uso termina. |

---

## Reglas de Negocio

| ID | Regla |
|---|---|
| RN-01 | Solo se permiten correos bajo el dominio @tecazuay.edu.ec para todos los roles del sistema. |
| RN-02 | El sistema permite un máximo de 5 intentos fallidos por minuto por dirección IP antes de aplicar el bloqueo temporal. |
| RN-03 | Todo intento de autenticación — exitoso o fallido — queda registrado en el log de auditoría de forma inmutable. |
| RN-04 | Las contraseñas se almacenan cifradas con BCrypt factor de trabajo 12 — nunca en texto plano. |
| RN-05 | Las cuentas desactivadas no pueden autenticarse independientemente de las credenciales ingresadas. |

---

*MEDISTA — Casos de Uso M7 v1.0 — Instituto Superior Universitario TEC Azuay — Abril 2026*
