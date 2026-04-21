# ADR-003 — Inmutabilidad de las atenciones médicas

**Estado:** Aceptada  
**Fecha:** 20 Abril 2026  
**Autor:** Miguel Angel Zhunio Remache  

---

## Contexto

Cada atención médica registrada en el sistema constituye un acto clínico formal — un documento que certifica que una consulta ocurrió en una fecha determinada, fue atendida por un profesional identificado, y produjo un diagnóstico y tratamiento específicos. El **Acuerdo Ministerial MSP No. 00000125** establece que la historia clínica electrónica debe preservarse íntegra e inalterable: ningún dato registrado en una atención puede ser modificado retroactivamente sin dejar evidencia de ello.
 
Permitir la edición directa de una atención médica después de su creación violaría este principio. Si un médico pudiera sobrescribir el diagnóstico, el tratamiento o los signos vitales de una consulta pasada, el sistema perdería su valor como registro clínico oficial — cualquier dato podría haber sido alterado sin dejar rastro, comprometiendo tanto la integridad del historial del paciente como la responsabilidad legal del profesional.
 
Sin embargo, en la práctica clínica surgen situaciones legítimas que requieren enmendar información registrada — un error tipográfico en el tratamiento, una actualización del diagnóstico tras recibir resultados de laboratorio, o una aclaración sobre los hallazgos del examen físico. El sistema debe ofrecer un mecanismo para gestionar estas situaciones sin comprometer la inmutabilidad del registro original.

## Decisión

Se decidió que los registros de atención médica son inmutables tras su creación. No se permiten operaciones `UPDATE` sobre ningún campo clínico de la tabla `medical_attendances`. Las correcciones o novedades posteriores se registran en la tabla `attendance_corrections`, donde cada entrada es un nuevo registro acumulativo con texto libre, marca de tiempo y firma del profesional que la realizó. El registro original permanece inalterado en todo momento.

## Opciones Consideradas

- **Opción A — Permitir la edición directa de la atención médica:** El médico puede modificar cualquier campo de una atención ya guardada mediante una operación `UPDATE` estándar. Descartada porque viola el Acuerdo MSP No. 00000125 al permitir la alteración retroactiva de registros clínicos sin trazabilidad, elimina la confiabilidad del historial como documento oficial y expone al sistema a riesgos legales por manipulación de datos médicos.
- **Opción B — Tabla de correcciones acumulativas `attendance_corrections` (elegida):** La atención original es de solo creación. Cualquier enmienda posterior se registra como una nota en `attendance_corrections`, vinculada a la atención original mediante clave foránea. Múltiples correcciones pueden acumularse cronológicamente sobre la misma atención. Cada corrección registra quién la realizó y cuándo, satisfaciendo el requisito de trazabilidad completa. 


## Consecuencias

**Positivas:**
- Cumplimiento directo con el Acuerdo MSP No. 00000125 — el registro clínico original es inalterable y toda enmienda queda documentada con autoría y marca de tiempo.
- El historial clínico del paciente es confiable como documento oficial — ningún dato puede haber sido modificado silenciosamente.
- Hibernate Envers audita tanto la creación de la atención como cada corrección agregada, proporcionando trazabilidad completa a nivel de base de datos.
- La responsabilidad legal del profesional queda protegida — el sistema certifica exactamente qué registró el médico y en qué momento.

**Negativas:**
- Incrementa la complejidad del modelo de datos al requerir una tabla adicional `attendance_corrections` y la lógica de aplicación asociada para gestionarla.
- La interfaz de usuario debe comunicar claramente al médico que no puede editar una atención guardada y guiarlo hacia el mecanismo de corrección — un diseño de UX que requiere atención para no generar confusión operativa.
- Las consultas que necesiten mostrar la versión más completa de una atención deben combinar los datos de `medical_attendances` con sus correcciones en `attendance_corrections`, añadiendo complejidad a las queries del historial clínico.