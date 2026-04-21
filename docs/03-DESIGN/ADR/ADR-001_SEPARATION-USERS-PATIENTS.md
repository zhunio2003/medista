# ADR-001 — Separación de users y patients

**Estado:** Aceptada  
**Fecha:** 20 Abril 2026  
**Autor:** Miguel Angel Zhunio Remache  

---

## Contexto

El sistema maneja cuatro tipos de usuarios: Médico, Decano, Administrador del Sistema y Estudiante. Durante el modelado de datos surgió la necesidad de decidir cómo representar a estas personas en la base de datos.  
La tentación natural es centralizar todo en una única tabla `users` que contenga tanto los datos de autenticación como los datos clínicos del paciente. Esto simplifica el diagrama y reduce el número de tablas. Sin embargo, los datos clínicos — cédula, carrera, ciclo, dirección, fecha de nacimiento, género, tipo de sangre — son atributos exclusivos de los estudiantes en su rol de pacientes. Un médico, un decano o un administrador del sistema también son usuarios, pero nunca son pacientes y nunca tendrán esos atributos.  
Adicionalmente, se evaluó el patrón de herencia de tablas, donde `patients` heredaría de `users` como una especialización. Este patrón es válido en dominios donde todas las entidades hijo son variantes del mismo concepto. En MEDISTA, sin embargo, `users` y `patients` representan conceptos de dominio distintos: el primero responde a la pregunta *¿quién se autentica?* y el segundo a *¿de quién es este expediente clínico?*. La herencia mezclaría responsabilidades que deben permanecer separadas.

## Decisión

Se decidió separar la identidad del sistema y los datos clínicos en dos tablas independientes: `users` para autenticación y autorización, y `patients` para datos de filiación y contexto clínico. Ambas tablas se vinculan mediante una clave foránea nullable `user_id` en `patients`, que se completa cuando el estudiante activa su cuenta en el sistema.

## Opciones Consideradas

- **Opción A — Tabla única `users` con todos los campos:** Una sola tabla contendría tanto los datos de autenticación como los datos clínicos. Descartada porque obligaría a tener columnas clínicas con valor nulo en todos los usuarios no estudiantes, violando el principio de que cada columna debe tener significado para todos los registros de la tabla.
- **Opción B — Herencia de tablas (`patients` hereda de `users`):** `patients` sería una especialización de `users`, heredando sus columnas base y añadiendo las clínicas. Descartada porque `users` y `patients` no son el mismo concepto especializado — un médico es un usuario pero nunca un paciente, lo que invalida la relación de herencia.
- **Opción C — Tablas separadas con FK nullable (elegida):** `users` gestiona exclusivamente autenticación y rol. `patients` gestiona exclusivamente datos clínicos. Un estudiante tiene ambos registros vinculados por `user_id`. Un médico tiene solo `users`. La FK es nullable para permitir que un paciente exista en el sistema antes de que el estudiante active su cuenta.

## Consecuencias

**Positivas:**
- Cada tabla tiene una única responsabilidad: `users` gestiona identidad, `patients` gestiona datos clínicos. Ninguna columna es irrelevante para ningún registro de su tabla.
- Los datos clínicos quedan aislados del mecanismo de autenticación, lo que simplifica la aplicación del cifrado selectivo requerido por la LOPDP sobre datos sensibles.
- El modelo refleja con precisión la realidad del dominio: no todo usuario es paciente.
- Las consultas clínicas no necesitan filtrar por rol para evitar mezclar datos de usuarios no clínicos.
**Negativas:**
- Incrementa el número de tablas del modelo y requiere un JOIN adicional en las consultas que necesitan combinar datos de identidad con datos clínicos.
- La lógica de creación de un estudiante requiere dos operaciones de escritura coordinadas — una en `users` y otra en `patients` — que deben ejecutarse dentro de una transacción para garantizar consistencia.