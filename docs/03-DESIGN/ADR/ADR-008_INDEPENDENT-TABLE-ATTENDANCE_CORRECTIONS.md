# ADR-007 — Tabla independiente attendance_corrections para enmiendas de atenciones médicas

**Estado:** Aceptada  
**Fecha:** 20 Abril 2026  
**Autor:** Miguel Angel Zhunio Remache  

---

## Contexto

Como establece el ADR-003, las atenciones médicas son inmutables tras su creación. Sin embargo, en la práctica clínica surgen situaciones legítimas que requieren agregar información posterior a una atención ya guardada — una aclaración sobre el diagnóstico, una corrección de un dato registrado incorrectamente, o una novedad clínica relevante que debe quedar vinculada al acto médico original.
 
El sistema debe ofrecer un mecanismo para registrar estas enmiendas sin comprometer la inmutabilidad del registro original. La pregunta de diseño es cómo implementar ese mecanismo a nivel de modelo de datos.
 
La solución más simple sería agregar una columna `correction_note TEXT` dentro de la tabla `medical_attendances`. Sin embargo, esta aproximación limita al médico a una única enmienda por atención — si necesita agregar una segunda corrección, debe sobrescribir la primera, perdiendo la trazabilidad de la enmienda anterior. En un contexto clínico donde cada modificación debe quedar documentada con autoría y marca de tiempo, sobrescribir una enmienda previa es inaceptable.

## Decisión

Se decidió implementar una tabla independiente `attendance_corrections` que almacena las enmiendas sobre atenciones médicas como registros acumulativos. Cada enmienda es un nuevo registro con su propio texto, autoría y marca de tiempo. Una atención puede acumular cero o múltiples enmiendas a lo largo del tiempo. Ninguna enmienda puede ser modificada ni eliminada una vez registrada.

## Opciones Consideradas

- **Opción A — Columna `correction_note TEXT` en `medical_attendances`:** Agregar un campo de texto directamente en la tabla de atenciones para registrar correcciones. Descartada porque limita al médico a una única enmienda por atención — una segunda corrección sobrescribiría la primera, destruyendo la trazabilidad histórica de las enmiendas anteriores e incumpliendo el principio de inmutabilidad que rige todo el módulo clínico.
- **Opción B — Tabla independiente `attendance_corrections` (elegida):** Cada enmienda genera un nuevo registro en una tabla dedicada, vinculada a la atención original mediante clave foránea. Las enmiendas se acumulan cronológicamente y ninguna sobrescribe a otra. Cada registro incluye el texto de la enmienda, el médico que la registró y la marca de tiempo exacta, satisfaciendo el requisito de trazabilidad completa exigido por la normativa MSP.

## Consecuencias

**Positivas:**
- El médico puede registrar múltiples enmiendas sobre la misma atención sin límite — cada una queda documentada de forma independiente con su autoría y marca de tiempo.
- Hibernate Envers puede auditar la tabla `attendance_corrections` de forma independiente, registrando el historial completo de todas las enmiendas realizadas sobre cada atención.
- La inmutabilidad del registro original en `medical_attendances` se preserva en su totalidad — ninguna enmienda toca la atención base.
- El historial clínico del paciente refleja con exactitud la evolución del conocimiento médico sobre cada consulta, incluyendo todas las correcciones posteriores con su contexto temporal.

**Negativas:**
- Incrementa la complejidad del modelo de datos con una tabla adicional y su lógica de aplicación asociada.
- Las consultas que necesiten mostrar una atención con todas sus enmiendas deben realizar un JOIN adicional con `attendance_corrections`, añadiendo complejidad a las queries del historial clínico.
- La interfaz de usuario debe gestionar dos contextos distintos dentro de la misma vista de atención — el registro original inmutable y las enmiendas acumuladas — lo que requiere un diseño de UI claro para no generar confusión en el médico. 
