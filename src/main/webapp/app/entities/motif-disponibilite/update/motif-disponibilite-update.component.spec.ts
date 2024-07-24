import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MotifDisponibiliteService } from '../service/motif-disponibilite.service';
import { IMotifDisponibilite } from '../motif-disponibilite.model';
import { MotifDisponibiliteFormService } from './motif-disponibilite-form.service';

import { MotifDisponibiliteUpdateComponent } from './motif-disponibilite-update.component';

describe('MotifDisponibilite Management Update Component', () => {
  let comp: MotifDisponibiliteUpdateComponent;
  let fixture: ComponentFixture<MotifDisponibiliteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let motifDisponibiliteFormService: MotifDisponibiliteFormService;
  let motifDisponibiliteService: MotifDisponibiliteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MotifDisponibiliteUpdateComponent],
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
      .overrideTemplate(MotifDisponibiliteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MotifDisponibiliteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    motifDisponibiliteFormService = TestBed.inject(MotifDisponibiliteFormService);
    motifDisponibiliteService = TestBed.inject(MotifDisponibiliteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const motifDisponibilite: IMotifDisponibilite = { id: 456 };

      activatedRoute.data = of({ motifDisponibilite });
      comp.ngOnInit();

      expect(comp.motifDisponibilite).toEqual(motifDisponibilite);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMotifDisponibilite>>();
      const motifDisponibilite = { id: 123 };
      jest.spyOn(motifDisponibiliteFormService, 'getMotifDisponibilite').mockReturnValue(motifDisponibilite);
      jest.spyOn(motifDisponibiliteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ motifDisponibilite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: motifDisponibilite }));
      saveSubject.complete();

      // THEN
      expect(motifDisponibiliteFormService.getMotifDisponibilite).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(motifDisponibiliteService.update).toHaveBeenCalledWith(expect.objectContaining(motifDisponibilite));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMotifDisponibilite>>();
      const motifDisponibilite = { id: 123 };
      jest.spyOn(motifDisponibiliteFormService, 'getMotifDisponibilite').mockReturnValue({ id: null });
      jest.spyOn(motifDisponibiliteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ motifDisponibilite: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: motifDisponibilite }));
      saveSubject.complete();

      // THEN
      expect(motifDisponibiliteFormService.getMotifDisponibilite).toHaveBeenCalled();
      expect(motifDisponibiliteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMotifDisponibilite>>();
      const motifDisponibilite = { id: 123 };
      jest.spyOn(motifDisponibiliteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ motifDisponibilite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(motifDisponibiliteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
