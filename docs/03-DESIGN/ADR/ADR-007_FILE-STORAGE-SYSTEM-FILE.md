# ADR-007 — Almacenamiento de archivos en el sistema de ficheros, no en la base de datos

**Estado:** Aceptada  
**Fecha:** 20 Abril 2026  
**Autor:** Miguel Angel Zhunio Remache  

---

## Contexto

El módulo de Atención Médica permite adjuntar archivos a los exámenes complementarios — resultados de laboratorio, imágenes diagnósticas y otros documentos en formato PDF, JPG o PNG. Estos archivos son contenido binario de tamaño variable que el sistema debe almacenar y entregar al médico cuando los solicite.
 
Surge la necesidad de decidir dónde reside físicamente este contenido binario: dentro de la base de datos PostgreSQL como datos BLOB, o en el sistema de ficheros del servidor con solo los metadatos de referencia en la base de datos.
 
Almacenar archivos directamente en PostgreSQL es técnicamente posible mediante el tipo `BYTEA` o la API de Large Objects. Sin embargo, esta aproximación tiene implicaciones directas sobre el rendimiento de la base de datos, el tamaño de los respaldos y la complejidad de la entrega del contenido al cliente. Una base de datos que mezcla datos relacionales estructurados con contenido binario pesado crece desproporcionadamente, sus respaldos se vuelven lentos y costosos, y la entrega de archivos al navegador o a la app móvil requiere pasar el contenido binario a través de la capa de aplicación en lugar de servirlo directamente.


## Decisión

Se decidió almacenar los archivos adjuntos en el sistema de ficheros del servidor bajo una estructura de directorios organizada. La base de datos almacena únicamente los metadatos del archivo — nombre original, ruta relativa en el sistema de ficheros y tipo MIME. La entrega del archivo al cliente se realiza a través del backend, que resuelve la ruta desde la base de datos y sirve el contenido desde el disco.

## Opciones Consideradas

- **Opción A — Almacenamiento en PostgreSQL como BLOB:** El archivo se convierte a un arreglo de bytes y se almacena directamente en una columna `BYTEA` de la tabla `complementary_exams`. Descartada porque incrementa el tamaño de la base de datos de forma desproporcionada con cada archivo adjunto, degrada el rendimiento de los respaldos — que deben copiar todo el contenido binario junto con los datos relacionales — y complica la entrega en streaming del archivo al cliente al obligar a cargar el contenido completo en memoria antes de enviarlo.
- **Opción B — Almacenamiento en sistema de ficheros con metadatos en base de datos (elegida):** El archivo se guarda en el disco del servidor bajo una ruta estructurada. La base de datos almacena el nombre original, la ruta relativa y el tipo MIME. El backend resuelve la ruta consultando la base de datos y entrega el archivo directamente desde el disco al cliente. Esta es la aproximación estándar de la industria para sistemas que manejan archivos adjuntos de tamaño variable.

## Consecuencias

**Positivas:**
- La base de datos mantiene un tamaño manejable — solo almacena datos estructurados, sin contenido binario pesado.
- Los respaldos de base de datos son rápidos y ligeros. Los archivos del sistema de ficheros se respaldan de forma independiente mediante estrategias de copia de directorio, lo que permite políticas de respaldo diferenciadas para datos relacionales y archivos.
- La entrega de archivos al cliente es eficiente — el backend puede servir el contenido directamente desde el disco sin cargarlo en memoria.

**Negativas:**
- La consistencia entre la base de datos y el sistema de ficheros debe gestionarse explícitamente en la aplicación. Si una operación de guardado falla después de escribir el archivo pero antes de confirmar los metadatos en la base de datos, el archivo queda huérfano en el disco. Esto requiere una estrategia de limpieza periódica de archivos sin referencia en la base de datos.
- La portabilidad del sistema entre servidores requiere migrar tanto el volcado de base de datos como el directorio de archivos adjuntos. Un respaldo incompleto que incluya solo la base de datos dejará referencias a archivos que no existen en el nuevo servidor.
 
