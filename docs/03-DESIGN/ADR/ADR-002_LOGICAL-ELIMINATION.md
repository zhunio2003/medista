# ADR-002 — Eliminación lógica sobre eliminación física

**Estado:** Aceptada  
**Fecha:** 20 Abril 2026  
**Autor:** Miguel Angel Zhunio Remache  

---

## Contexto

El sistema gestiona datos clínicos sensibles de estudiantes — atenciones médicas, diagnósticos, referencias, antecedentes — que por su naturaleza médica y legal requieren un tratamiento especial frente a las operaciones de eliminación.
 
En un sistema convencional, eliminar un registro significa ejecutar un `DELETE` que lo remueve físicamente de la base de datos. Para MEDISTA-V2, esta aproximación es incompatible con dos marcos normativos aplicables: la **Ley Orgánica de Protección de Datos Personales (LOPDP)** de Ecuador, que exige trazabilidad completa sobre los datos personales gestionados, y el **Acuerdo Ministerial MSP No. 00000125**, que establece que la historia clínica electrónica debe preservarse íntegra y ser inalterable. Eliminar físicamente un registro clínico constituiría una infracción directa a ambas normativas.
 
Adicionalmente, la eliminación física es irreversible. Cualquier error operativo — un clic accidental, una operación masiva mal ejecutada — destruiría datos que no pueden recuperarse, salvo desde un respaldo completo de la base de datos.

## Decisión

Se decidió implementar eliminación lógica en todas las entidades clínicas del sistema. Cada tabla clínica incluye una columna booleana `is_active` con valor por defecto `true`. Cuando un registro debe "eliminarse", la operación ejecutada es `UPDATE SET is_active = false`. El registro permanece físicamente en la base de datos y nunca se ejecuta un `DELETE` sobre datos clínicos.

## Opciones Consideradas

- **Opción A — Eliminación física (`DELETE`):** Eliminar el registro de la base de datos de forma permanente. Descartada porque viola la LOPDP y el Acuerdo MSP No. 00000125, destruye el historial clínico de forma irreversible y no permite auditar qué existió ni cuándo fue removido.
- **Opción B — Eliminación lógica con `is_active` (elegida):** Añadir una columna `is_active BOOLEAN` a todas las tablas clínicas. La eliminación se implementa como `UPDATE SET is_active = false`. Los registros inactivos se excluyen de todas las consultas operacionales mediante la condición `WHERE is_active = true`, pero permanecen disponibles para auditoría y trazabilidad histórica.
 

## Consecuencias

**Positivas:**
- Cumplimiento directo con la LOPDP y el Acuerdo MSP No. 00000125 — ningún dato clínico se destruye, toda la historia es trazable.
- Los errores operativos son reversibles — reactivar un registro es tan simple como ejecutar `UPDATE SET is_active = true`.
- Hibernate Envers puede auditar el cambio de estado, registrando quién "eliminó" el registro y cuándo.
- La integridad referencial se preserva — los registros relacionados no quedan con claves foráneas huérfanas.  

**Negativas:**
- Toda consulta operacional del sistema debe incluir la condición `WHERE is_active = true` para excluir registros inactivos. Omitir esta condición en cualquier query es un bug silencioso que muestra datos que el usuario no debería ver. Este es el principal riesgo de mantenimiento del patrón y requiere disciplina sostenida durante toda la implementación.
- La base de datos crece con el tiempo porque los registros nunca se eliminan físicamente. Para el volumen de datos de MEDISTA (~500 pacientes) esto no representa un problema operativo, pero debe considerarse en proyecciones de crecimiento a largo plazo.
- Las consultas de conteo y estadísticas deben filtrar correctamente por `is_active` para no distorsionar los indicadores del dashboard.