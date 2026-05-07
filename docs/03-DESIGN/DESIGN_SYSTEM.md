# Design System — MEDISTA

**Proyecto:** MEDISTA — Sistema de Gestión de Atención Médica  
**Versión:** 1.0  
**Autor:** Miguel Angel Zhunio Remache  
**Institución:** Instituto Superior Universitario TEC Azuay · Cuenca, Ecuador  
**Fecha:** Mayo 2026  

---

## 1. Principios de Diseño

MEDISTA es un sistema de gestión médica institucional dirigido a tres perfiles distintos: la médica del departamento (usuaria principal), el decano (acceso estadístico) y los estudiantes (consulta de historial). El sistema visual debe reflejar los valores de un entorno clínico real: **confianza, claridad y seriedad profesional**.

- **Light mode como base:** Un entorno médico requiere máxima legibilidad. El fondo claro reduce la fatiga visual en jornadas largas y transmite limpieza e higiene, valores inherentes al contexto de salud.
- **Azul marino institucional como primario:** Tomado directamente del logo de TEC Azuay. Transmite autoridad, confianza y seriedad. Es el color que el usuario ya asocia con la institución.
- **Amarillo dorado como acento:** También extraído del logo institucional. Se usa exclusivamente en badges, indicadores de estado y elementos de énfasis puntual. Nunca como fondo de componentes grandes.
- **Tipografía Inter:** Sans-serif diseñada específicamente para interfaces digitales. Excelente legibilidad en tamaños pequeños, ideal para formularios médicos con mucha densidad de información.
- **Accesibilidad primero:** Todos los colores de texto sobre fondo cumplen mínimo WCAG AA (4.5:1). Los colores semánticos (éxito, error, advertencia) son reconocibles para personas con daltonismo de tipo común por usar variaciones de matiz además de luminosidad.

---

## 2. Paleta de Colores

### 2.1 Colores de Marca

#### `primary` — Azul Marino Institucional
- **Hex:** `#1E3A8A`
- **Uso:** Sidebar de navegación, botones de acción principal, encabezados de sección, íconos activos, links destacados.
- **Contraste vs `background`:** 10.2:1 — WCAG AAA ✅

#### `primary-hover` — Azul Marino Oscuro
- **Hex:** `#1E3580`
- **Uso:** Estado hover y pressed de botones primarios y elementos interactivos con fondo `primary`.

#### `primary-light` — Azul Hielo
- **Hex:** `#EEF2FF`
- **Uso:** Fondo de ítems activos en el sidebar, fondo de badges de estado "activo", highlights de fila en tablas.

#### `accent` — Amarillo Dorado Institucional
- **Hex:** `#F5C518`
- **Uso:** Badges de alerta, indicadores de notificación pendiente, marcadores de énfasis puntual. Usar con moderación — máximo en el 5% de la interfaz visible.
- **Restricción:** Nunca usar como fondo de botones ni como color de texto sobre fondo blanco (contraste insuficiente).

---

### 2.2 Colores de Fondo y Superficie

#### `background` — Gris Hielo
- **Hex:** `#F8FAFC`
- **Uso:** Fondo general de todas las pantallas de la aplicación web.

#### `surface` — Blanco Puro
- **Hex:** `#FFFFFF`
- **Uso:** Fondo de cards, modales, drawers, paneles laterales, tablas. Crea la percepción visual de capas flotando sobre el fondo.

#### `surface-secondary` — Gris Muy Claro
- **Hex:** `#F1F5F9`
- **Uso:** Fondo de filas alternadas en tablas (zebra striping), fondo de campos de formulario deshabilitados, área de arrastre en uploads.

---

### 2.3 Colores de Texto

#### `text-primary` — Casi Negro
- **Hex:** `#1A202C`
- **Uso:** Texto de contenido principal, labels de formulario, títulos de cards, datos clínicos. Es el color de lectura por defecto.
- **Contraste vs `surface`:** 16.8:1 — WCAG AAA ✅

#### `text-secondary` — Gris Azulado
- **Hex:** `#64748B`
- **Uso:** Subtítulos, metadatos, timestamps, texto de ayuda (helper text), placeholders descriptivos, labels de menor jerarquía.
- **Contraste vs `surface`:** 5.9:1 — WCAG AA ✅

#### `text-on-primary` — Blanco Puro
- **Hex:** `#FFFFFF`
- **Uso:** Texto e íconos sobre fondos con color `primary` (#1E3A8A). Por ejemplo, el label del botón "Guardar" o los ítems del sidebar.
- **Contraste vs `primary`:** 10.2:1 — WCAG AAA ✅

#### `text-on-accent` — Negro
- **Hex:** `#1A202C`
- **Uso:** Texto sobre elementos con color `accent` (#F5C518). El amarillo es claro y requiere texto oscuro para cumplir contraste.

---

### 2.4 Colores de Borde

#### `border-default`
- **Hex:** `#E2E8F0`
- **Uso:** Bordes de cards, inputs en estado rest, divisores entre secciones, líneas de tabla.

#### `border-focus`
- **Hex:** `#1E3A8A`
- **Uso:** Borde de inputs y selects en estado focus. Siempre acompañado de un ring de `3px` con `rgba(30,58,138,0.08)`.

#### `border-error`
- **Hex:** `#DC2626`
- **Uso:** Borde de inputs con validación fallida.

---

### 2.5 Colores Semánticos

#### `success` — Verde Salud
- **Hex:** `#16A34A`
- **Uso:** Mensajes de guardado exitoso, badges de paciente "Alta", indicadores de signos vitales dentro de rango normal, checkmarks de completado.
- **Contraste vs `surface`:** 5.1:1 — WCAG AA ✅

#### `success-light` — Verde Claro
- **Hex:** `#F0FDF4`
- **Uso:** Fondo de alertas de éxito, fondo de badges de estado positivo.

#### `error` — Rojo Clínico
- **Hex:** `#DC2626`
- **Uso:** Mensajes de error, validaciones fallidas, badges de paciente con alerta crítica, signos vitales fuera de rango peligroso, confirmaciones de eliminación.
- **Contraste vs `surface`:** 5.6:1 — WCAG AA ✅
- **Nota:** La intensidad del rojo es intencional — en un contexto médico, los errores y alertas críticas deben capturar la atención de forma inmediata.

#### `error-light` — Rojo Claro
- **Hex:** `#FEF2F2`
- **Uso:** Fondo de alertas de error, fondo de inputs con error de validación.

#### `warning` — Naranja Ámbar
- **Hex:** `#D97706`
- **Uso:** Advertencias de proceso incompleto, badges de paciente "En seguimiento", alertas de signos vitales en límite, notificaciones de acción pendiente.
- **Contraste vs `surface`:** 4.6:1 — WCAG AA ✅

#### `warning-light` — Ámbar Claro
- **Hex:** `#FFFBEB`
- **Uso:** Fondo de alertas de advertencia, fondo de badges de estado de seguimiento.

#### `info` — Azul Informativo
- **Hex:** `#2563EB`
- **Uso:** Mensajes informativos, tooltips de ayuda, indicadores de datos de referencia, badges de "Nueva referencia médica".
- **Contraste vs `surface`:** 5.9:1 — WCAG AA ✅

#### `info-light` — Azul Claro
- **Hex:** `#EFF6FF`
- **Uso:** Fondo de alertas informativas, fondo de badges informativos.

---

### 2.6 Resumen de la Paleta

| Token | Hex | Uso principal | Contraste vs #FFFFFF | WCAG |
|---|---|---|---|---|
| `primary` | `#1E3A8A` | Sidebar, botones primarios | 10.2:1 | AAA ✅ |
| `primary-hover` | `#1E3580` | Estado hover primario | 11.4:1 | AAA ✅ |
| `primary-light` | `#EEF2FF` | Ítem activo sidebar, highlights | — | — |
| `accent` | `#F5C518` | Badges, énfasis puntual | — | Solo con texto oscuro |
| `background` | `#F8FAFC` | Fondo de pantallas | — | — |
| `surface` | `#FFFFFF` | Cards, modales, tablas | — | — |
| `surface-secondary` | `#F1F5F9` | Filas alternas, inputs disabled | — | — |
| `text-primary` | `#1A202C` | Texto principal | 16.8:1 | AAA ✅ |
| `text-secondary` | `#64748B` | Texto secundario, metadatos | 5.9:1 | AA ✅ |
| `text-on-primary` | `#FFFFFF` | Texto sobre fondo primary | 10.2:1 (vs primary) | AAA ✅ |
| `border-default` | `#E2E8F0` | Bordes de componentes | — | — |
| `success` | `#16A34A` | Estados positivos, alta | 5.1:1 | AA ✅ |
| `success-light` | `#F0FDF4` | Fondo alertas éxito | — | — |
| `error` | `#DC2626` | Errores, alertas críticas | 5.6:1 | AA ✅ |
| `error-light` | `#FEF2F2` | Fondo alertas error | — | — |
| `warning` | `#D97706` | Advertencias, seguimiento | 4.6:1 | AA ✅ |
| `warning-light` | `#FFFBEB` | Fondo alertas advertencia | — | — |
| `info` | `#2563EB` | Mensajes informativos | 5.9:1 | AA ✅ |
| `info-light` | `#EFF6FF` | Fondo alertas informativas | — | — |

---

## 3. Tipografía

### 3.1 Familia Tipográfica

| Propiedad | Valor |
|---|---|
| **Fuente** | Inter |
| **Tipo** | Sans-serif humanista |
| **Origen** | Google Fonts |
| **Justificación** | Diseñada específicamente para interfaces digitales. Excelente legibilidad en tamaños pequeños (ideal para formularios médicos de alta densidad), métricas equilibradas en pantalla, y amplia disponibilidad como fuente del sistema en Windows, macOS y Android. Es el estándar de facto en aplicaciones SaaS modernas y transmite neutralidad profesional. |

---

### 3.2 Escala Tipográfica

| Elemento | Tamaño | Weight | Uso |
|---|---|---|---|
| `display` | 28px | 600 (SemiBold) | Títulos de páginas principales (Dashboard, Historial Clínico) |
| `h1` | 24px | 600 (SemiBold) | Títulos de sección mayor dentro de una pantalla |
| `h2` | 20px | 600 (SemiBold) | Títulos de cards y modales |
| `h3` | 16px | 500 (Medium) | Subtítulos de sección, encabezados de grupos de formulario |
| `body-lg` | 15px | 400 (Regular) | Texto de lectura extensa (historial clínico, notas médicas) |
| `body` | 14px | 400 (Regular) | Texto de contenido general, valores de campos |
| `label` | 13px | 500 (Medium) | Labels de campos de formulario, encabezados de columna en tablas |
| `caption` | 12px | 400 (Regular) | Texto auxiliar, timestamps, notas de ayuda, unidades de medida |
| `button` | 14px | 500 (Medium) | Labels de botones y acciones |
| `badge` | 11px | 500 (Medium) | Texto dentro de badges y chips de estado |

---

### 3.3 Reglas de Uso Tipográfico

- Nunca usar más de 3 niveles de jerarquía tipográfica en una misma pantalla visible.
- El peso `Regular (400)` es exclusivo para contenido de lectura. Nunca para labels de acciones ni encabezados.
- El peso `Medium (500)` es el mínimo para cualquier elemento interactivo (botones, links, tabs).
- El tamaño mínimo de texto visible en la aplicación es `12px` (caption). Los textos de menos de 12px violan las guías de accesibilidad en contextos médicos.
- Los valores numéricos clínicos (signos vitales, dosis, códigos CIE-10) usan siempre `font-variant-numeric: tabular-nums` para alineación vertical en tablas.
- Los nombres de pacientes en tablas y encabezados usan `text-transform: capitalize` para normalización visual, no como hardcoding — el dato real se mantiene tal como fue ingresado.

---

## 4. Sistema de Espaciado

### 4.1 Escala de Espaciado

El sistema usa una base de **4px** con incrementos consistentes. Todos los márgenes, paddings y gaps deben usar exclusivamente estos valores.

| Token | Valor | Uso principal |
|---|---|---|
| `spacing-1` | 4px | Separación mínima entre icono y texto dentro del mismo elemento |
| `spacing-2` | 8px | Padding interno de badges, chips y tags. Gap entre íconos relacionados. |
| `spacing-3` | 12px | Padding interno de inputs compactos. Gap entre elementos de un mismo grupo. |
| `spacing-4` | 16px | Padding interno de cards, botones y modales. Unidad base de layout. |
| `spacing-5` | 20px | Separación entre grupos de campos de formulario relacionados. |
| `spacing-6` | 24px | Separación entre secciones dentro de una pantalla. Gap entre cards en grid. |
| `spacing-8` | 32px | Padding horizontal de la pantalla (margen lateral estándar). |
| `spacing-10` | 40px | Separación entre secciones mayores con divider visual. |
| `spacing-12` | 48px | Espaciado vertical entre secciones en formularios largos (atención médica). |

---

### 4.2 Reglas de Uso del Espaciado

- El padding lateral estándar del contenido principal es `spacing-8` (32px).
- La separación mínima entre dos botones adyacentes es `spacing-4` (16px) para evitar clics accidentales.
- El gap entre cards en un grid es siempre `spacing-6` (24px).
- El padding interno estándar de una card es `spacing-4` (16px) en todos los lados.
- En formularios médicos de alta densidad (como el formulario de atención), el gap entre secciones es `spacing-12` (48px) con un divider de `1px solid #E2E8F0` entre ellas.

---

## 5. Bordes y Radio de Esquinas

| Token | Valor | Uso |
|---|---|---|
| `radius-sm` | 4px | Badges, chips, tags, checkboxes, radio buttons |
| `radius-md` | 8px | Inputs, selects, textareas, buttons estándar |
| `radius-lg` | 12px | Cards, panels, dropdowns, tooltips |
| `radius-xl` | 16px | Modales, drawers, bottom sheets |
| `radius-full` | 9999px | Pills de estado, avatares circulares, toggles |

**Regla:** No usar bordes redondeados en elementos que tienen borde solo en un lado (border-left accent de sección, por ejemplo). Las esquinas redondeadas aplican únicamente cuando los 4 lados tienen borde o fondo visible.

---

## 6. Elevación y Sombras

MEDISTA es un sistema de diseño plano (flat). Las sombras se usan únicamente para crear jerarquía funcional — no decorativa.

| Nivel | Valor CSS | Uso |
|---|---|---|
| `shadow-none` | `none` | Cards en estado rest, inputs, botones |
| `shadow-sm` | `0 1px 3px rgba(0,0,0,0.06)` | Cards con hover, dropdowns, tooltips |
| `shadow-md` | `0 4px 12px rgba(0,0,0,0.08)` | Modales, drawers, sidebars flotantes |
| `shadow-focus` | `0 0 0 3px rgba(30,58,138,0.08)` | Ring de foco en inputs y botones (accesibilidad) |

**Regla:** Nunca apilar más de un nivel de sombra sobre el mismo elemento. No usar box-shadow con múltiples capas — aumenta la complejidad visual sin beneficio funcional en un entorno clínico.

---

## 7. Iconografía

### 7.1 Librería

| Propiedad | Valor |
|---|---|
| **Librería** | Tabler Icons (outline) |
| **Versión** | 3.x |
| **Estilo** | Outline exclusivamente — nunca filled |
| **Tamaño estándar** | 20px en navegación y acciones / 16px inline en texto |
| **Tamaño máximo** | 24px (elementos decorativos o destacados) |
| **Color** | Hereda de `color` del elemento padre — nunca hardcodeado |

### 7.2 Íconos por Módulo

| Módulo | Ícono | Clase Tabler |
|---|---|---|
| Dashboard | Tablero | `ti-dashboard` |
| Pacientes | Usuarios | `ti-users` |
| Atención médica | Estetoscopio | `ti-stethoscope` |
| Historial clínico | Archivo clínico | `ti-file-text` |
| Referencia médica | Envío | `ti-send` |
| Estadísticas | Gráfica de barras | `ti-chart-bar` |
| Notificaciones | Campana | `ti-bell` |
| Administración | Configuración | `ti-settings` |
| Seguridad / Auditoría | Escudo | `ti-shield-lock` |
| Estudiante / Historial personal | Persona círculo | `ti-user-circle` |

### 7.3 Reglas de Uso

- Los íconos decorativos (sin función interactiva) llevan siempre `aria-hidden="true"`.
- Los íconos que actúan como botón sin texto visible llevan `aria-label` descriptivo.
- Nunca combinar dos íconos diferentes dentro del mismo elemento interactivo sin separación visual de al menos `spacing-2` (8px).

---

## 8. Componentes Base

### 8.1 Botones

| Variante | Fondo | Texto | Borde | Uso |
|---|---|---|---|---|
| `primary` | `#1E3A8A` | `#FFFFFF` | Ninguno | Acción principal de la pantalla (Guardar, Registrar, Generar PDF) |
| `secondary` | `#FFFFFF` | `#1E3A8A` | `1px solid #1E3A8A` | Acciones secundarias (Cancelar, Volver, Exportar) |
| `danger` | `#DC2626` | `#FFFFFF` | Ninguno | Acciones destructivas (Eliminar, Corregir con nota) — siempre con modal de confirmación |
| `ghost` | `transparent` | `#64748B` | Ninguno | Acciones terciarias, links de navegación, acciones en tablas |

**Dimensiones estándar:** height `42px`, padding horizontal `spacing-4` (16px), `border-radius: radius-md` (8px), `font-size: 14px`, `font-weight: 500`.

**Estados:** `hover` oscurece el fondo un 8%; `focus` agrega `shadow-focus`; `disabled` reduce opacidad a `0.5` y cambia cursor a `not-allowed`.

---

### 8.2 Inputs de Formulario

- **Height estándar:** 42px
- **Padding interno:** 12px horizontal, con ícono a la izquierda: 38px de padding-left
- **Fondo en rest:** `#F8FAFC`
- **Fondo en focus:** `#FFFFFF`
- **Borde en rest:** `1px solid #CBD5E1`
- **Borde en focus:** `1px solid #1E3A8A` + `shadow-focus`
- **Borde en error:** `1px solid #DC2626`
- **Font-size:** 14px, color `text-primary`
- **Placeholder:** color `#94A3B8`

Los campos de solo lectura (datos de paciente en formulario de atención) usan fondo `#F1F5F9` y cursor `default` para diferenciarse visualmente de los campos editables.

---

### 8.3 Badges de Estado

Los badges comunican el estado clínico de una atención, paciente o proceso.

| Estado | Fondo | Texto | Uso |
|---|---|---|---|
| Activo | `#EEF2FF` | `#1E3A8A` | Paciente con atención activa |
| Alta | `#F0FDF4` | `#15803D` | Paciente dado de alta |
| Seguimiento | `#FFFBEB` | `#92400E` | Paciente requiere seguimiento |
| Referido | `#EFF6FF` | `#1D4ED8` | Paciente derivado a especialista |
| Crítico | `#FEF2F2` | `#991B1B` | Alerta crítica activa |
| Pendiente | `#F8FAFC` | `#64748B` | Atención pendiente de completar |

**Dimensiones:** `font-size: 11px`, `font-weight: 500`, `padding: 3px 10px`, `border-radius: radius-full` (9999px).

---

### 8.4 Sidebar de Navegación

- **Fondo:** `#1E3A8A` (primary)
- **Ancho:** 240px en estado expandido, 64px en estado colapsado
- **Ítem inactivo:** texto `rgba(255,255,255,0.7)`, ícono `rgba(255,255,255,0.6)`
- **Ítem activo:** fondo `rgba(255,255,255,0.12)`, texto `#FFFFFF`, ícono `#F5C518` (accent)
- **Ítem hover:** fondo `rgba(255,255,255,0.08)`
- **Separador de sección:** `1px solid rgba(255,255,255,0.1)`
- **Logo MEDISTA:** en la parte superior, fondo `rgba(255,255,255,0.08)`, padding `spacing-4`

---

### 8.5 Cards

```
background:     #FFFFFF
border:         0.5px solid #E2E8F0
border-radius:  12px (radius-lg)
padding:        16px 20px (spacing-4 vertical, spacing-5 horizontal)
```

Los cards con hover interactivo agregan `shadow-sm` y cambian el borde a `border-color: #CBD5E1` en el estado hover.

---

### 8.6 Tablas

- **Encabezado:** fondo `#F8FAFC`, texto `text-secondary` 13px 500, `border-bottom: 1px solid #E2E8F0`
- **Fila par:** fondo `#FFFFFF`
- **Fila impar (zebra):** fondo `#F8FAFC`
- **Fila hover:** fondo `#EEF2FF`
- **Fila seleccionada:** fondo `#EEF2FF`, borde-izquierdo `3px solid #1E3A8A`
- **Celda:** padding `12px 16px`, font-size `14px`, color `text-primary`
- **Separador de fila:** `1px solid #F1F5F9`

---

## 9. Notificaciones y Alertas

### 9.1 Alertas Inline (Toasts)

Las alertas aparecen en la esquina inferior derecha, con duración de 4 segundos para informativas y 8 segundos para errores (descartables manualmente).

| Tipo | Borde izquierdo | Fondo | Ícono |
|---|---|---|---|
| Éxito | `#16A34A` | `#F0FDF4` | `ti-circle-check` |
| Error | `#DC2626` | `#FEF2F2` | `ti-circle-x` |
| Advertencia | `#D97706` | `#FFFBEB` | `ti-alert-triangle` |
| Información | `#2563EB` | `#EFF6FF` | `ti-info-circle` |

### 9.2 Notificaciones del Sistema (WebSocket)

Las notificaciones push del sistema (nueva referencia recibida, resultado de laboratorio, turno próximo) aparecen como badges numéricos sobre el ícono de campana en el sidebar. El badge usa fondo `#F5C518` (accent) con texto `#1A202C`.

---

## 10. Formulario de Atención Médica — Reglas Especiales

El formulario de atención médica es la pantalla más crítica del sistema. Su diseño sigue reglas adicionales dada la complejidad del formulario físico original:

- Cada sección numerada del formulario físico (1. Datos de Filiación, 2. Motivo de Consulta, etc.) se renderiza como un card separado con encabezado de sección numerado en `primary`.
- Los campos que en el formulario físico son casillas de verificación (Estado Civil: S/C/V/D/UL) se implementan como `radio buttons` en línea horizontal, manteniendo la misma disposición visual que el papel.
- La sección de Signos Vitales usa un grid de 8 columnas para replicar la distribución del formulario físico. Los valores fuera de rango se marcan automáticamente con `error` o `warning` según el grado de desviación.
- El modo de solo lectura (historial clínico) renderiza el formulario con todos los campos en estado `disabled` con fondo `#F1F5F9`. Los campos vacíos muestran un guion largo (—) en lugar de quedar en blanco.
- La sección de Emergencias Obstétricas tiene visibilidad condicional — se muestra únicamente cuando el género registrado del paciente es femenino.

---

## 11. App Android (Kotlin) — Consideraciones

La app móvil comparte los tokens de color del sistema web, adaptados a Material Design 3:

- `colorPrimary` → `#1E3A8A`
- `colorSecondary` → `#F5C518`
- `colorSurface` → `#FFFFFF`
- `colorBackground` → `#F8FAFC`
- `colorOnPrimary` → `#FFFFFF`
- `colorOnSurface` → `#1A202C`

La app es de solo lectura para estudiantes. El patrón de pantalla principal sigue Bottom Navigation con 3 ítems: Historial, Notificaciones, Perfil. Las listas de atenciones usan `RecyclerView` con cards según las especificaciones del punto 8.5.

---

## 12. Variables CSS (Implementación Angular)

```css
:root {
  /* Marca */
  --color-primary:          #1E3A8A;
  --color-primary-hover:    #1E3580;
  --color-primary-light:    #EEF2FF;
  --color-accent:           #F5C518;

  /* Fondos */
  --color-background:       #F8FAFC;
  --color-surface:          #FFFFFF;
  --color-surface-secondary:#F1F5F9;

  /* Texto */
  --color-text-primary:     #1A202C;
  --color-text-secondary:   #64748B;
  --color-text-disabled:    #94A3B8;
  --color-text-on-primary:  #FFFFFF;
  --color-text-on-accent:   #1A202C;

  /* Bordes */
  --color-border:           #E2E8F0;
  --color-border-focus:     #1E3A8A;
  --color-border-error:     #DC2626;

  /* Semánticos */
  --color-success:          #16A34A;
  --color-success-light:    #F0FDF4;
  --color-error:            #DC2626;
  --color-error-light:      #FEF2F2;
  --color-warning:          #D97706;
  --color-warning-light:    #FFFBEB;
  --color-info:             #2563EB;
  --color-info-light:       #EFF6FF;

  /* Espaciado */
  --spacing-1:  4px;
  --spacing-2:  8px;
  --spacing-3:  12px;
  --spacing-4:  16px;
  --spacing-5:  20px;
  --spacing-6:  24px;
  --spacing-8:  32px;
  --spacing-10: 40px;
  --spacing-12: 48px;

  /* Radio */
  --radius-sm:   4px;
  --radius-md:   8px;
  --radius-lg:   12px;
  --radius-xl:   16px;
  --radius-full: 9999px;

  /* Sombras */
  --shadow-sm:    0 1px 3px rgba(0,0,0,0.06);
  --shadow-md:    0 4px 12px rgba(0,0,0,0.08);
  --shadow-focus: 0 0 0 3px rgba(30,58,138,0.08);

  /* Tipografía */
  --font-family: 'Inter', sans-serif;
  --font-size-display: 28px;
  --font-size-h1:      24px;
  --font-size-h2:      20px;
  --font-size-h3:      16px;
  --font-size-body-lg: 15px;
  --font-size-body:    14px;
  --font-size-label:   13px;
  --font-size-caption: 12px;
  --font-size-badge:   11px;
}
```

---

*MEDISTA — Design System v1.0*  
*Instituto Superior Universitario TEC Azuay — Mayo 2026*
