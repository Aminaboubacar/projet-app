import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DegreService } from '../service/degre.service';
import { IDegre } from '../degre.model';
import { DegreFormService } from './degre-form.service';

import { DegreUpdateComponent } from './degre-update.component';

describe('Degre Management Update Component', () => {
  let comp: DegreUpdateComponent;
  let fixture: ComponentFixture<DegreUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let degreFormService: DegreFormService;
  let degreService: DegreService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DegreUpdateComponent],
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
      .overrideTemplate(DegreUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DegreUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    degreFormService = TestBed.inject(DegreFormService);
    degreService = TestBed.inject(DegreService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const degre: IDegre = { id: 456 };

      activatedRoute.data = of({ degre });
      comp.ngOnInit();

      expect(comp.degre).toEqual(degre);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDegre>>();
      const degre = { id: 123 };
      jest.spyOn(degreFormService, 'getDegre').mockReturnValue(degre);
      jest.spyOn(degreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ degre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: degre }));
      saveSubject.complete();

      // THEN
      expect(degreFormService.getDegre).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(degreService.update).toHaveBeenCalledWith(expect.objectContaining(degre));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDegre>>();
      const degre = { id: 123 };
      jest.spyOn(degreFormService, 'getDegre').mockReturnValue({ id: null });
      jest.spyOn(degreService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ degre: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: degre }));
      saveSubject.complete();

      // THEN
      expect(degreFormService.getDegre).toHaveBeenCalled();
      expect(degreService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDegre>>();
      const degre = { id: 123 };
      jest.spyOn(degreService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ degre });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(degreService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
