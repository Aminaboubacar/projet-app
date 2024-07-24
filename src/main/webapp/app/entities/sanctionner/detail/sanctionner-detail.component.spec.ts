import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { SanctionnerDetailComponent } from './sanctionner-detail.component';

describe('Sanctionner Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SanctionnerDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SanctionnerDetailComponent,
              resolve: { sanctionner: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SanctionnerDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load sanctionner on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SanctionnerDetailComponent);

      // THEN
      expect(instance.sanctionner).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
