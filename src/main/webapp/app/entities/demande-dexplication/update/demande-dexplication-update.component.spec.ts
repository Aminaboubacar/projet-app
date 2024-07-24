import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IAgent } from 'app/entities/agent/agent.model';
import { AgentService } from 'app/entities/agent/service/agent.service';
import { DemandeDexplicationService } from '../service/demande-dexplication.service';
import { IDemandeDexplication } from '../demande-dexplication.model';
import { DemandeDexplicationFormService } from './demande-dexplication-form.service';

import { DemandeDexplicationUpdateComponent } from './demande-dexplication-update.component';

describe('DemandeDexplication Management Update Component', () => {
  let comp: DemandeDexplicationUpdateComponent;
  let fixture: ComponentFixture<DemandeDexplicationUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let demandeDexplicationFormService: DemandeDexplicationFormService;
  let demandeDexplicationService: DemandeDexplicationService;
  let agentService: AgentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DemandeDexplicationUpdateComponent],
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
      .overrideTemplate(DemandeDexplicationUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DemandeDexplicationUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    demandeDexplicationFormService = TestBed.inject(DemandeDexplicationFormService);
    demandeDexplicationService = TestBed.inject(DemandeDexplicationService);
    agentService = TestBed.inject(AgentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Agent query and add missing value', () => {
      const demandeDexplication: IDemandeDexplication = { id: 456 };
      const agent: IAgent = { id: 20464 };
      demandeDexplication.agent = agent;

      const agentCollection: IAgent[] = [{ id: 15088 }];
      jest.spyOn(agentService, 'query').mockReturnValue(of(new HttpResponse({ body: agentCollection })));
      const additionalAgents = [agent];
      const expectedCollection: IAgent[] = [...additionalAgents, ...agentCollection];
      jest.spyOn(agentService, 'addAgentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ demandeDexplication });
      comp.ngOnInit();

      expect(agentService.query).toHaveBeenCalled();
      expect(agentService.addAgentToCollectionIfMissing).toHaveBeenCalledWith(
        agentCollection,
        ...additionalAgents.map(expect.objectContaining),
      );
      expect(comp.agentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const demandeDexplication: IDemandeDexplication = { id: 456 };
      const agent: IAgent = { id: 31052 };
      demandeDexplication.agent = agent;

      activatedRoute.data = of({ demandeDexplication });
      comp.ngOnInit();

      expect(comp.agentsSharedCollection).toContain(agent);
      expect(comp.demandeDexplication).toEqual(demandeDexplication);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDemandeDexplication>>();
      const demandeDexplication = { id: 123 };
      jest.spyOn(demandeDexplicationFormService, 'getDemandeDexplication').mockReturnValue(demandeDexplication);
      jest.spyOn(demandeDexplicationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeDexplication });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demandeDexplication }));
      saveSubject.complete();

      // THEN
      expect(demandeDexplicationFormService.getDemandeDexplication).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(demandeDexplicationService.update).toHaveBeenCalledWith(expect.objectContaining(demandeDexplication));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDemandeDexplication>>();
      const demandeDexplication = { id: 123 };
      jest.spyOn(demandeDexplicationFormService, 'getDemandeDexplication').mockReturnValue({ id: null });
      jest.spyOn(demandeDexplicationService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeDexplication: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: demandeDexplication }));
      saveSubject.complete();

      // THEN
      expect(demandeDexplicationFormService.getDemandeDexplication).toHaveBeenCalled();
      expect(demandeDexplicationService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDemandeDexplication>>();
      const demandeDexplication = { id: 123 };
      jest.spyOn(demandeDexplicationService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ demandeDexplication });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(demandeDexplicationService.update).toHaveBeenCalled();
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
