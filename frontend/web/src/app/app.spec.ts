import { TestBed } from '@angular/core/testing';
import { App } from './app';
import {provideRouter} from '@angular/router';

// Prueba mínima del componente raíz: que se cree y que tenga el
// <router-outlet> donde el router pinta cada página.
describe('App', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [App],
      // provideRouter([]) = router sin rutas; basta para que exista <router-outlet>.
      providers: [provideRouter([])]
    }).compileComponents();
  });

  it('se crea', () => {
    const fixture = TestBed.createComponent(App);
    expect(fixture.componentInstance).toBeTrue();
  });

  it('tiene un router-outlet', () => {
    const fixture = TestBed.createComponent(App);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('router-outlet')).not.toBeNull();
  });
});
