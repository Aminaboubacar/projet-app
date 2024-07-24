import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IAgent } from 'app/entities/agent/agent.model';
import { AgentService } from 'app/entities/agent/service/agent.service';
import { MiseadispositionService } from '../service/miseadisposition.service';
import { IMiseadisposition } from '../miseadisposition.model';
import { MiseadispositionFormService } from './miseadisposition-form.service';

import { MiseadispositionUpdateComponent } from './miseadisposition-update.component';

describe('Miseadisposition Management Update Component', () => {
  let comp: MiseadispositionUpdateComponent;
  let fixture: ComponentFixture<MiseadispositionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let miseadispositionFormService: MiseadispositionFormService;
  let miseadispositionService: MiseadispositionService;
  let agentService: AgentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MiseadispositionUpdateComponent],
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
      .overrideTemplate(MiseadispositionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MiseadispositionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    miseadispositionFormService = TestBed.inject(MiseadispositionFormService);
    miseadispositionService = TestBed.inject(MiseadispositionService);
    agentService = TestBed.inject(AgentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Agent query and add missing value', () => {
      const miseadisposition: IMiseadisposition = { id: 456 };
      const agent: IAgent = { id: 31923 };
      miseadisposition.agent = agent;

      const agentCollection: IAgent[] = [{ id: 2886 }];
      jest.spyOn(agentService, 'query').mockReturnValue(of(new HttpResponse({ body: agentCollection })));
      const additionalAgents = [agent];
      const expectedCollection: IAgent[] = [...additionalAgents, ...agentCollection];
      jest.spyOn(agentService, 'addAgentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ miseadisposition });
      comp.ngOnInit();

      expect(agentService.query).toHaveBeenCalled();
      expect(agentService.addAgentToCollectionIfMissing).toHaveBeenCalledWith(
        agentCollection,
        ...additionalAgents.map(expect.objectContaining),
      );
      expect(comp.agentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const miseadisposition: IMiseadisposition = { id: 456 };
      const agent: IAgent = { id: 10049 };
      miseadisposition.agent = agent;

      activatedRoute.data = of({ miseadisposition });
      comp.ngOnInit();

      expect(comp.agentsSharedCollection).toContain(agent);
      expect(comp.miseadisposition).toEqual(miseadisposition);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMiseadisposition>>();
      const miseadisposition = { id: 123 };
      jest.spyOn(miseadispositionFormService, 'getMiseadisposition').mockReturnValue(miseadisposition);
      jest.spyOn(miseadispositionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ miseadisposition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: miseadisposition }));
      saveSubject.complete();

      // THEN
      expect(miseadispositionFormService.getMiseadisposition).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(miseadispositionService.update).toHaveBeenCalledWith(expect.objectContaining(miseadisposition));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMiseadisposition>>();
      const miseadisposition = { id: 123 };
      jest.spyOn(miseadispositionFormService, 'getMiseadisposition').mockReturnValue({ id: null });
      jest.spyOn(miseadispositionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ miseadisposition: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: miseadisposition }));
      saveSubject.complete();

      // THEN
      expect(miseadispositionFormService.getMiseadisposition).toHaveBeenCalled();
      expect(miseadispositionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMiseadisposition>>();
      const miseadisposition = { id: 123 };
      jest.spyOn(miseadispositionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ miseadisposition });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(miseadispositionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAgent', () => {
      it('Should forward to agentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(agentService, 'compareAgent');
        comp.compareAgent(entity, entity2);
        expect(agentService.compareAgent).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
