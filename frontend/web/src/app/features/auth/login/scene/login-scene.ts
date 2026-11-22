// =============================================================================
// <app-login-scene> — el fondo animado del login
// -----------------------------------------------------------------------------
// Es solo decoración: aria-hidden hace que el lector de pantalla la ignore.
// Vive en su propio componente para que login.scss quede enfocado en la
// tarjeta y el formulario. Todo el movimiento está en login-scene.scss.
// =============================================================================
import {ChangeDetectionStrategy, Component} from '@angular/core';

@Component({
    selector: 'app-login-scene',
  changeDetection: ChangeDetectionStrategy.OnPush,
  host: {'aria-hidden': 'true'},
  template: `
    <div class="edge"></div>
    <div class="frame">
      <div class="photo"></div>
      <div class="grain"></div>
    </div>

  `,
  styleUrl: './login-scene.scss'
})

export class LoginSceneComponent {}
