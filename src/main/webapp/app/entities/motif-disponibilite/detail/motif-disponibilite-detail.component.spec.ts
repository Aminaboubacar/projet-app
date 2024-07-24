import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MotifDisponibiliteDetailComponent } from './motif-disponibilite-detail.component';

describe('MotifDisponibilite Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MotifDisponibiliteDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MotifDisponibiliteDetailComponent,
              resolve: { motifDisponibilite: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MotifDisponibiliteDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load motifDisponibilite on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MotifDisponibiliteDetailComponent);

      // THEN
      expect(instance.motifDisponibilite).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
