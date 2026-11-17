import {ChangeDetectionStrategy, Component} from '@angular/core';

// =============================================================================
// <app-caduceus> — el caduceo animado de MEDISTA
// -----------------------------------------------------------------------------
// Al cargar se "arma" solo, como si cobrara vida:
//   1. la vara crece desde abajo,
//   2. las serpientes suben enroscándose tramo a tramo,
//   3. aparece el pomo y las alas se despliegan con un aleteo,
//   4. después, cada pocos segundos, las alas aletean suave.
// Toda la animación es CSS (caduceus.scss): no hace falta lógica en TS.
// El tamaño lo decide quien lo usa (ancho del host); el SVG se adapta.
// =============================================================================

@Component({
  selector: 'app-caduceus',
  changeDetection: ChangeDetectionStrategy.OnPush,
  templateUrl: './caduceus.html',
  styleUrl: './caduceus.scss',
})

export class CaduceusComponent {}
