import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DemandeDexplicationDetailComponent } from './demande-dexplication-detail.component';

describe('DemandeDexplication Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DemandeDexplicationDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DemandeDexplicationDetailComponent,
              resolve: { demandeDexplication: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(DemandeDexplicationDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load demandeDexplication on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DemandeDexplicationDetailComponent);

      // THEN
      expect(instance.demandeDexplication).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
