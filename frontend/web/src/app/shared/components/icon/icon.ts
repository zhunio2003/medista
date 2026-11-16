// =============================================================================
// <app-icon> — iconos del sistema
// -----------------------------------------------------------------------------
// El design system (§7) pide Tabler Icons en estilo outline. En vez de
// instalar la librería completa (miles de iconos) copiamos a mano solo los
// trazos que usamos. Tabler es MIT: se puede copiar libremente.
//
// Cómo agregar un icono nuevo:
//   1. Búscalo en https://tabler.io/icons y copia sus <path>.
//   2. Agrega su nombre al tipo IconName.
//   3. Agrega un @case con sus paths en el template.
//
// Reglas del DS que este componente ya cumple:
//   - stroke="currentColor": el icono hereda el color del texto del padre.
//   - aria-hidden="true": es decorativo; quien lo usa pone el texto o el
//     aria-label en el botón/campo que lo contiene.
// =============================================================================

import {ChangeDetectionStrategy, Component, input} from '@angular/core';

/** Nombres válidos. Si escribes uno que no existe, TypeScript te avisa. */
export type IconName = 'email' | 'lock' | 'eye' | 'eye-off';

@Component({
  selector: 'app-icon',
  // OnPush: el icono solo se vuelve a dibujar si cambian sus inputs.
  changeDetection: ChangeDetectionStrategy.OnPush,
  // El host (<app-icon>) se comporta como una caja en línea del tamaño del SVG.
  host: {style: 'display:inline-flew; line-height:0'},
  template: `
  <svg
      xmlns="http://www.w3.org/2000/svg"
      viewBox="0 0 24 24"
      [attr.width]="size()"
      [attr.height]="size()"
      fill="none"
      stroke="currentColor"
      stroke-width="1.75"
      stroke-linecap="round"
      stroke-linejoin="round"
      aria-hidden="true"
      focusable="false"
    >
      @switch (name()) {
        @case ('mail') {
          <path d="M3 7a2 2 0 0 1 2 -2h14a2 2 0 0 1 2 2v10a2 2 0 0 1 -2 2h-14a2 2 0 0 1 -2 -2v-10z" />
          <path d="M3 7l9 6l9 -6" />
        }
        @case ('lock') {
          <path d="M5 13a2 2 0 0 1 2 -2h10a2 2 0 0 1 2 2v6a2 2 0 0 1 -2 2h-10a2 2 0 0 1 -2 -2v-6z" />
          <path d="M11 16a1 1 0 1 0 2 0a1 1 0 0 0 -2 0" />
          <path d="M8 11v-4a4 4 0 1 1 8 0v4" />
        }
        @case ('eye') {
          <path d="M10 12a2 2 0 1 0 4 0a2 2 0 0 0 -4 0" />
          <path d="M21 12c-2.4 4 -5.4 6 -9 6c-3.6 0 -6.6 -2 -9 -6c2.4 -4 5.4 -6 9 -6c3.6 0 6.6 2 9 6" />
        }
        @case ('eye-off') {
          <path d="M10.585 10.587a2 2 0 0 0 2.829 2.828" />
          <path
            d="M16.681 16.673a8.717 8.717 0 0 1 -4.681 1.327c-3.6 0 -6.6 -2 -9 -6c1.272 -2.12 2.712 -3.678 4.32 -4.674m2.86 -1.146a9.055 9.055 0 0 1 1.82 -.18c3.6 0 6.6 2 9 6c-.666 1.11 -1.379 2.067 -2.138 2.87"
          />
          <path d="M3 3l18 18" />
        }
      }
    </svg>
  `,
})

export class IconCamponent {
  /** Qué icono dibujar. Obligatorio. */
  readonly name= input.required<IconName>();

  /** Tamaño en px. 20 = navegación y acciones (DS §7.1); 16 = dentro de texto. */
  readonly size = input(20);
}
