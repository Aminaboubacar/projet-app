import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DegreDetailComponent } from './degre-detail.component';

describe('Degre Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DegreDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DegreDetailComponent,
              resolve: { degre: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DegreDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load degre on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DegreDetailComponent);

      // THEN
      expect(instance.degre).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
