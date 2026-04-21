# ADR-004 — referral_diagnoses como tabla independiente de diagnoses

**Estado:** Aceptada  
**Fecha:** 20 Abril 2026  
**Autor:** Miguel Angel Zhunio Remache  

---

## Contexto

El sistema maneja diagnósticos en dos contextos distintos: los diagnósticos registrados durante una atención médica, y los diagnósticos comunicados en una referencia médica hacia un establecimiento externo. Ambos comparten la misma estructura base — código CIE-10, descripción en texto libre y clasificación presuntivo/definitivo.
 
Durante el modelado surgió la necesidad de decidir si estos dos tipos de diagnóstico debían coexistir en una única tabla o residir en tablas independientes. La tentación de unificarlos es natural dado que su estructura es casi idéntica, pero su contexto de dominio es diferente: un diagnóstico de atención responde a la pregunta *¿qué determinó el médico durante la consulta?*, mientras que un diagnóstico de referencia responde a *¿qué cuadro clínico comunica el médico al establecimiento receptor?*. Ambos pueden coincidir o diferir — la médico podría referir al paciente con un subconjunto de los diagnósticos de la atención, o con una descripción adaptada para el contexto de la derivación.

## Decisión

Se decidió implementar dos tablas independientes: `diagnoses` para los diagnósticos de atenciones médicas, y `referral_diagnoses` para los diagnósticos de referencias médicas. Aunque ambas tablas comparten la misma estructura de columnas, cada una tiene su propia clave foránea hacia su entidad propietaria correspondiente y representa un concepto de dominio distinto.

## Opciones Consideradas

- **Opción A — Tabla única `diagnoses` con dos claves foráneas nullable:** Una sola tabla contendría tanto los diagnósticos de atenciones como los de referencias, diferenciados por dos columnas: `medical_attendance_id` y `referral_id`, donde exactamente una de las dos tendría valor y la otra sería `NULL`. Descartada porque una clave foránea nullable mutuamente excluyente con otra clave foránea en la misma fila es un defecto de diseño reconocido — PostgreSQL no puede garantizar que exactamente una de las dos tenga valor, esa validación recae en la aplicación y es propensa a errores. Adicionalmente, las consultas requieren lógica condicional para determinar a qué contexto pertenece cada registro.
- **Opción B — Tablas independientes `diagnoses` y `referral_diagnoses` (elegida):** Cada tabla tiene su propia clave foránea hacia su entidad propietaria. La estructura de columnas es similar hoy, pero cada tabla puede evolucionar independientemente si los requisitos de cada contexto divergen en versiones futuras. Las consultas son directas y sin ambigüedad — no requieren condiciones adicionales para determinar el contexto del diagnóstico.

## Consecuencias

**Positivas:**
- Cada tabla tiene una única responsabilidad y una única relación de pertenencia — no existen columnas nullable mutuamente excluyentes.
- Las consultas sobre diagnósticos de atenciones y sobre diagnósticos de referencias son independientes, directas y sin lógica condicional de contexto.
- Las tablas pueden evolucionar independientemente si los requisitos de cada contexto divergen en el futuro — por ejemplo, agregar un campo `urgency_level` en `referral_diagnoses` sin afectar `diagnoses`.
- La integridad referencial es garantizada completamente por la base de datos sin necesidad de validaciones adicionales en la capa de aplicación.

**Negativas:**
- El modelo tiene dos tablas con estructura similar, lo que puede generar la percepción inicial de duplicación innecesaria en revisiones de código o de modelo. Esta decisión debe estar documentada explícitamente — como lo hace este ADR — para que cualquier desarrollador futuro entienda que la separación es intencional y justificada por dominio, no un error de diseño.
- Cualquier cambio estructural que aplique a ambos tipos de diagnóstico — como agregar un nuevo campo compartido — debe aplicarse en dos tablas en lugar de una, requiriendo dos migraciones Flyway coordinadas.