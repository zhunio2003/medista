# ADR-005 — Cálculo automático y persistencia de campos derivados (IMC y Total Glasgow)

**Estado:** Aceptada  
**Fecha:** 20 Abril 2026  
**Autor:** Miguel Angel Zhunio Remache  

---

## Contexto

El formulario de atención médica incluye dos campos cuyo valor no es ingresado directamente por el médico sino que se deriva matemáticamente de otros campos registrados en la misma atención.
 
El **Índice de Masa Corporal (IMC)** se calcula dividiendo el peso en kilogramos entre el cuadrado de la talla en metros. El **Total de Glasgow** se obtiene sumando las tres subescalas de la Escala de Coma de Glasgow: apertura ocular (1–4), respuesta verbal (1–5) y respuesta motora (1–6).
 
Exigir que el médico calcule y escriba manualmente estos valores introduce fricción operativa innecesaria, aumenta la probabilidad de errores aritméticos en el registro clínico y ralentiza el flujo de trabajo durante la consulta. Adicionalmente, surge la decisión de si estos valores calculados deben persistirse en la base de datos o recalcularse dinámicamente cada vez que se consulten.

## Decisión

Se decidió que el sistema calcule automáticamente el IMC y el Total de Glasgow en tiempo real a medida que el médico ingresa los valores de los que dependen, sin permitir la entrada manual de estos campos. Los valores calculados se persisten como columnas en la tabla `medical_attendances` en el momento de guardar la atención.

## Opciones Consideradas

- **Opción A — Ingreso manual por el médico:** El médico calcula por su cuenta el IMC y el Total de Glasgow e ingresa los resultados directamente en los campos correspondientes. Descartada porque introduce errores aritméticos evitables en registros clínicos oficiales, agrega carga cognitiva innecesaria al único usuario del sistema durante una consulta médica, y contradice el principio de que el sistema debe simplificar el proceso, no replicarlo tal como existe en papel.
- **Opción B — Cálculo automático sin persistencia (recálculo dinámico):** El sistema calcula los valores automáticamente pero no los almacena — los recalcula en cada consulta a partir de los campos base. Descartada porque si las fórmulas de cálculo cambian en una versión futura del sistema, todos los registros históricos mostrarían valores distintos a los que la médico vio en el momento de la consulta, alterando retroactivamente documentos clínicos oficiales.
- **Opción C — Cálculo automático con persistencia (elegida):** El sistema calcula los valores automáticamente y los persiste como columnas en `medical_attendances`. El médico nunca ingresa estos campos manualmente. Una vez guardada la atención, los valores persisten de forma inmutable junto con el resto del registro clínico, independientemente de cambios futuros en las fórmulas.

## Consecuencias

**Positivas:**
- El médico no realiza cálculos manuales durante la consulta — el sistema los resuelve en tiempo real al ingresar los valores base, reduciendo errores y agilizando el registro.
- Los valores históricos son estables e inmutables — reflejan exactamente lo que el sistema calculó en el momento de la consulta, sin riesgo de alteración retroactiva por cambios futuros en las fórmulas.
- El registro clínico es coherente con el formulario físico original, donde estos campos también aparecen como resultados calculados.

**Negativas:**
- Añade lógica de cálculo en tiempo real a la capa de aplicación — el frontend debe actualizar los campos derivados reactivamente cada vez que el médico modifica un valor base, lo que requiere manejo de estado adicional en Angular.
- Persiste redundancia controlada en la base de datos — el IMC podría derivarse de `weight_kg` y `height_cm` en cualquier momento, pero se almacena de forma independiente por razones de inmutabilidad histórica.
