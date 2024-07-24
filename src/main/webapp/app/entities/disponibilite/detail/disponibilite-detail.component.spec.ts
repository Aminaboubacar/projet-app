import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DisponibiliteDetailComponent } from './disponibilite-detail.component';

describe('Disponibilite Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DisponibiliteDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DisponibiliteDetailComponent,
              resolve: { disponibilite: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DisponibiliteDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load disponibilite on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DisponibiliteDetailComponent);

      // THEN
      expect(instance.disponibilite).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
