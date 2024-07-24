import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ISanction } from 'app/entities/sanction/sanction.model';
import { SanctionService } from 'app/entities/sanction/service/sanction.service';
import { IDemandeDexplication } from 'app/entities/demande-dexplication/demande-dexplication.model';
import { DemandeDexplicationService } from 'app/entities/demande-dexplication/service/demande-dexplication.service';
import { ISanctionner } from '../sanctionner.model';
import { SanctionnerService } from '../service/sanctionner.service';
import { SanctionnerFormService } from './sanctionner-form.service';

import { SanctionnerUpdateComponent } from './sanctionner-update.component';

describe('Sanctionner Management Update Component', () => {
  let comp: SanctionnerUpdateComponent;
  let fixture: ComponentFixture<SanctionnerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sanctionnerFormService: SanctionnerFormService;
  let sanctionnerService: SanctionnerService;
  let sanctionService: SanctionService;
  let demandeDexplicationService: DemandeDexplicationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), SanctionnerUpdateComponent],
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
      .overrideTemplate(SanctionnerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SanctionnerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sanctionnerFormService = TestBed.inject(SanctionnerFormService);
    sanctionnerService = TestBed.inject(SanctionnerService);
    sanctionService = TestBed.inject(SanctionService);
    demandeDexplicationService = TestBed.inject(DemandeDexplicationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Sanction query and add missing value', () => {
      const sanctionner: ISanctionner = { id: 456 };
      const sanction: ISanction = { id: 15172 };
      sanctionner.sanction = sanction;

      const sanctionCollection: ISanction[] = [{ id: 6001 }];
      jest.spyOn(sanctionService, 'query').mockReturnValue(of(new HttpResponse({ body: sanctionCollection })));
      const additionalSanctions = [sanction];
      const expectedCollection: ISanction[] = [...additionalSanctions, ...sanctionCollection];
      jest.spyOn(sanctionService, 'addSanctionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sanctionner });
      comp.ngOnInit();

      expect(sanctionService.query).toHaveBeenCalled();
      expect(sanctionService.addSanctionToCollectionIfMissing).toHaveBeenCalledWith(
        sanctionCollection,
        ...additionalSanctions.map(expect.objectContaining),
      );
      expect(comp.sanctionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call DemandeDexplication query and add missing value', () => {
      const sanctionner: ISanctionner = { id: 456 };
      const demandeDexplication: IDemandeDexplication = { id: 1952 };
      sanctionner.demandeDexplication = demandeDexplication;

      const demandeDexplicationCollection: IDemandeDexplication[] = [{ id: 9212 }];
      jest.spyOn(demandeDexplicationService, 'query').mockReturnValue(of(new HttpResponse({ body: demandeDexplicationCollection })));
      const additionalDemandeDexplications = [demandeDexplication];
      const expectedCollection: IDemandeDexplication[] = [...additionalDemandeDexplications, ...demandeDexplicationCollection];
      jest.spyOn(demandeDexplicationService, 'addDemandeDexplicationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sanctionner });
      comp.ngOnInit();

      expect(demandeDexplicationService.query).toHaveBeenCalled();
      expect(demandeDexplicationService.addDemandeDexplicationToCollectionIfMissing).toHaveBeenCalledWith(
        demandeDexplicationCollection,
        ...additionalDemandeDexplications.map(expect.objectContaining),
      );
      expect(comp.demandeDexplicationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sanctionner: ISanctionner = { id: 456 };
      const sanction: ISanction = { id: 10714 };
      sanctionner.sanction = sanction;
      const demandeDexplication: IDemandeDexplication = { id: 5447 };
      sanctionner.demandeDexplication = demandeDexplication;

      activatedRoute.data = of({ sanctionner });
      comp.ngOnInit();

      expect(comp.sanctionsSharedCollection).toContain(sanction);
      expect(comp.demandeDexplicationsSharedCollection).toContain(demandeDexplication);
      expect(comp.sanctionner).toEqual(sanctionner);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISanctionner>>();
      const sanctionner = { id: 123 };
      jest.spyOn(sanctionnerFormService, 'getSanctionner').mockReturnValue(sanctionner);
      jest.spyOn(sanctionnerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sanctionner });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sanctionner }));
      saveSubject.complete();

      // THEN
      expect(sanctionnerFormService.getSanctionner).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sanctionnerService.update).toHaveBeenCalledWith(expect.objectContaining(sanctionner));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISanctionner>>();
      const sanctionner = { id: 123 };
      jest.spyOn(sanctionnerFormService, 'getSanctionner').mockReturnValue({ id: null });
      jest.spyOn(sanctionnerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sanctionner: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sanctionner }));
      saveSubject.complete();

      // THEN
      expect(sanctionnerFormService.getSanctionner).toHaveBeenCalled();
      expect(sanctionnerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISanctionner>>();
      const sanctionner = { id: 123 };
      jest.spyOn(sanctionnerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sanctionner });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sanctionnerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSanction', () => {
      it('Should forward to sanctionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sanctionService, 'compareSanction');
        comp.compareSanction(entity, entity2);
        expect(sanctionService.compareSanction).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDemandeDexplication', () => {
      it('Should forward to demandeDexplicationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(demandeDexplicationService, 'compareDemandeDexplication');
        comp.compareDemandeDexplication(entity, entity2);
        expect(demandeDexplicationService.compareDemandeDexplication).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
