# ADR-006 — Registro de auditoría en PostgreSQL sin política de eliminación

**Estado:** Aceptada  
**Fecha:** 20 Abril 2026  
**Autor:** Miguel Angel Zhunio Remache  

---

## Contexto

MEDISTA gestiona datos clínicos sensibles de estudiantes — categoría de datos personales de alta protección bajo la **Ley Orgánica de Protección de Datos Personales (LOPDP)** de Ecuador. Cualquier sistema que opere sobre este tipo de datos debe ser capaz de responder en todo momento a las preguntas: ¿quién accedió a este expediente?, ¿quién modificó este dato?, ¿desde qué dirección IP?, ¿a qué hora?
 
Hibernate Envers cubre parcialmente este requisito — registra automáticamente qué datos cambiaron en las entidades auditadas. Sin embargo, Envers no registra eventos de acceso que no producen escritura en base de datos: un médico que consulta el historial de un paciente sin modificar nada no deja rastro en las tablas de Envers. La LOPDP exige que estos accesos también sean trazables.
 
La solución requiere una tabla de auditoría operacional que registre acciones con relevancia clínica o de seguridad: inicio y cierre de sesión, acceso a expedientes clínicos, creación y modificación de atenciones, generación de PDFs y exportación de reportes. No se registran eventos de navegación ni interacciones de interfaz sin impacto sobre datos sensibles — hacerlo generaría volúmenes de registros sin valor de auditoría que degradarían el rendimiento y dificultarían el análisis forense cuando sea necesario.
 
Adicionalmente, los registros de auditoría de datos clínicos no pueden eliminarse — la LOPDP exige su retención indefinida. Esto descarta cualquier solución que incluya políticas de expiración o archivado con eliminación.

## Decisión

Se decidió implementar una tabla `audit_logs` en PostgreSQL que registre de forma permanente todas las acciones con relevancia clínica o de seguridad realizadas por los usuarios del sistema. Los registros de esta tabla nunca se eliminan. Se recomienda el particionamiento por rango de fecha mensual como optimización de rendimiento a largo plazo, sin comprometer la retención de datos.

## Opciones Consideradas

- **Opción A — No implementar registro de auditoría operacional:** Confiar únicamente en Hibernate Envers para la trazabilidad. Descartada porque Envers solo registra modificaciones de datos — los accesos de solo lectura a expedientes clínicos no quedan registrados, incumpliendo el requisito de trazabilidad completa de la LOPDP.
- **Opción B — Almacenamiento en base de datos NoSQL:** Usar una base de datos documental como MongoDB para almacenar los logs dado su perfil de escritura masiva y baja frecuencia de lectura. Descartada porque introduce una segunda tecnología de base de datos en el stack sin justificación suficiente para el volumen de datos de este sistema (~500 pacientes, una médico). PostgreSQL con índices bien definidos resuelve el problema sin complejidad operativa adicional.
- **Opción C — Tabla `audit_logs` en PostgreSQL con retención indefinida (elegida):** Una tabla dedicada en PostgreSQL registra todas las acciones relevantes con sus metadatos — usuario, acción, entidad afectada, IP, marca de tiempo. Los registros nunca se eliminan. El particionamiento por fecha permite mantener el rendimiento de consultas a medida que el volumen crece, sin eliminar datos históricos.

## Consecuencias

**Positivas:**
- Cumplimiento directo con la LOPDP — toda acción sobre datos clínicos sensibles es trazable con autoría, marca de tiempo y origen de red.
- Capacidad de auditoría forense completa — ante cualquier incidente de seguridad o acceso indebido, el sistema puede determinar quién accedió a qué expediente y desde dónde.
- Monitoreo continuo del comportamiento del sistema — patrones de acceso inusuales pueden detectarse revisando los logs.
- Sin complejidad operativa adicional — no se agrega ninguna tecnología nueva al stack.

**Negativas:**
- La tabla crecerá de forma indefinida dado que los registros nunca se eliminan. A largo plazo esto requiere una estrategia de particionamiento por fecha para mantener el rendimiento de las consultas.
- El volumen de registros puede ser significativo si el sistema crece — cada acción relevante de cada usuario genera una fila, lo que a escala institucional produce miles de registros diarios.
- Las consultas de auditoría sobre rangos de tiempo amplios pueden ser costosas sin los índices adecuados sobre `user_id` y `created_at`.
 
