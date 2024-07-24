import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IDegre } from 'app/entities/degre/degre.model';
import { DegreService } from 'app/entities/degre/service/degre.service';
import { SanctionService } from '../service/sanction.service';
import { ISanction } from '../sanction.model';
import { SanctionFormService } from './sanction-form.service';

import { SanctionUpdateComponent } from './sanction-update.component';

describe('Sanction Management Update Component', () => {
  let comp: SanctionUpdateComponent;
  let fixture: ComponentFixture<SanctionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sanctionFormService: SanctionFormService;
  let sanctionService: SanctionService;
  let degreService: DegreService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), SanctionUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SanctionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SanctionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sanctionFormService = TestBed.inject(SanctionFormService);
    sanctionService = TestBed.inject(SanctionService);
    degreService = TestBed.inject(DegreService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Degre query and add missing value', () => {
      const sanction: ISanction = { id: 456 };
      const degre: IDegre = { id: 8881 };
      sanction.degre = degre;

      const degreCollection: IDegre[] = [{ id: 5303 }];
      jest.spyOn(degreService, 'query').mockReturnValue(of(new HttpResponse({ body: degreCollection })));
      const additionalDegres = [degre];
      const expectedCollection: IDegre[] = [...additionalDegres, ...degreCollection];
      jest.spyOn(degreService, 'addDegreToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sanction });
      comp.ngOnInit();

      expect(degreService.query).toHaveBeenCalled();
      expect(degreService.addDegreToCollectionIfMissing).toHaveBeenCalledWith(
        degreCollection,
        ...additionalDegres.map(expect.objectContaining),
      );
      expect(comp.degresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sanction: ISanction = { id: 456 };
      const degre: IDegre = { id: 2865 };
      sanction.degre = degre;

      activatedRoute.data = of({ sanction });
      comp.ngOnInit();

      expect(comp.degresSharedCollection).toContain(degre);
      expect(comp.sanction).toEqual(sanction);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISanction>>();
      const sanction = { id: 123 };
      jest.spyOn(sanctionFormService, 'getSanction').mockReturnValue(sanction);
      jest.spyOn(sanctionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sanction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sanction }));
      saveSubject.complete();

      // THEN
      expect(sanctionFormService.getSanction).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sanctionService.update).toHaveBeenCalledWith(expect.objectContaining(sanction));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISanction>>();
      const sanction = { id: 123 };
      jest.spyOn(sanctionFormService, 'getSanction').mockReturnValue({ id: null });
      jest.spyOn(sanctionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sanction: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sanction }));
      saveSubject.complete();

      // THEN
      expect(sanctionFormService.getSanction).toHaveBeenCalled();
      expect(sanctionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISanction>>();
      const sanction = { id: 123 };
      jest.spyOn(sanctionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sanction });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sanctionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDegre', () => {
      it('Should forward to degreService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(degreService, 'compareDegre');
        comp.compareDegre(entity, entity2);
        expect(degreService.compareDegre).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
