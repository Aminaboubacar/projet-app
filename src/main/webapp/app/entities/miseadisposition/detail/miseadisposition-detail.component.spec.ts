import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { MiseadispositionDetailComponent } from './miseadisposition-detail.component';

describe('Miseadisposition Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MiseadispositionDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: MiseadispositionDetailComponent,
              resolve: { miseadisposition: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(MiseadispositionDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load miseadisposition on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', MiseadispositionDetailComponent);

      // THEN
      expect(instance.miseadisposition).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
