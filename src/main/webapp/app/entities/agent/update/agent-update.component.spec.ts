import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IPoste } from 'app/entities/poste/poste.model';
import { PosteService } from 'app/entities/poste/service/poste.service';
import { AgentService } from '../service/agent.service';
import { IAgent } from '../agent.model';
import { AgentFormService } from './agent-form.service';

import { AgentUpdateComponent } from './agent-update.component';

describe('Agent Management Update Component', () => {
  let comp: AgentUpdateComponent;
  let fixture: ComponentFixture<AgentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let agentFormService: AgentFormService;
  let agentService: AgentService;
  let posteService: PosteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AgentUpdateComponent],
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
      .overrideTemplate(AgentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AgentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    agentFormService = TestBed.inject(AgentFormService);
    agentService = TestBed.inject(AgentService);
    posteService = TestBed.inject(PosteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Poste query and add missing value', () => {
      const agent: IAgent = { id: 456 };
      const poste: IPoste = { id: 27683 };
      agent.poste = poste;

      const posteCollection: IPoste[] = [{ id: 22309 }];
      jest.spyOn(posteService, 'query').mockReturnValue(of(new HttpResponse({ body: posteCollection })));
      const additionalPostes = [poste];
      const expectedCollection: IPoste[] = [...additionalPostes, ...posteCollection];
      jest.spyOn(posteService, 'addPosteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ agent });
      comp.ngOnInit();

      expect(posteService.query).toHaveBeenCalled();
      expect(posteService.addPosteToCollectionIfMissing).toHaveBeenCalledWith(
        posteCollection,
        ...additionalPostes.map(expect.objectContaining),
      );
      expect(comp.postesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const agent: IAgent = { id: 456 };
      const poste: IPoste = { id: 25414 };
      agent.poste = poste;

      activatedRoute.data = of({ agent });
      comp.ngOnInit();

      expect(comp.postesSharedCollection).toContain(poste);
      expect(comp.agent).toEqual(agent);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgent>>();
      const agent = { id: 123 };
      jest.spyOn(agentFormService, 'getAgent').mockReturnValue(agent);
      jest.spyOn(agentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agent }));
      saveSubject.complete();

      // THEN
      expect(agentFormService.getAgent).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(agentService.update).toHaveBeenCalledWith(expect.objectContaining(agent));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgent>>();
      const agent = { id: 123 };
      jest.spyOn(agentFormService, 'getAgent').mockReturnValue({ id: null });
      jest.spyOn(agentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agent: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agent }));
      saveSubject.complete();

      // THEN
      expect(agentFormService.getAgent).toHaveBeenCalled();
      expect(agentService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAgent>>();
      const agent = { id: 123 };
      jest.spyOn(agentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(agentService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePoste', () => {
      it('Should forward to posteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(posteService, 'comparePoste');
        comp.comparePoste(entity, entity2);
        expect(posteService.comparePoste).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
