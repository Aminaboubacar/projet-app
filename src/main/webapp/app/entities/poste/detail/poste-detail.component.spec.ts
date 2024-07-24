import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PosteDetailComponent } from './poste-detail.component';

describe('Poste Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PosteDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PosteDetailComponent,
              resolve: { poste: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(PosteDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load poste on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PosteDetailComponent);

      // THEN
      expect(instance.poste).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
