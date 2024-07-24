import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { SanctionDetailComponent } from './sanction-detail.component';

describe('Sanction Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SanctionDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: SanctionDetailComponent,
              resolve: { sanction: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SanctionDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load sanction on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SanctionDetailComponent);

      // THEN
      expect(instance.sanction).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
