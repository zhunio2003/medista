import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';

// Componente raíz: solo contiene el <router-outlet>.
// Cada página la decide app.routes.ts.
@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
}
