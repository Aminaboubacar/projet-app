import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IMotifDisponibilite } from 'app/entities/motif-disponibilite/motif-disponibilite.model';
import { MotifDisponibiliteService } from 'app/entities/motif-disponibilite/service/motif-disponibilite.service';
import { IAgent } from 'app/entities/agent/agent.model';
import { AgentService } from 'app/entities/agent/service/agent.service';
import { IDisponibilite } from '../disponibilite.model';
import { DisponibiliteService } from '../service/disponibilite.service';
import { DisponibiliteFormService } from './disponibilite-form.service';

import { DisponibiliteUpdateComponent } from './disponibilite-update.component';

describe('Disponibilite Management Update Component', () => {
  let comp: DisponibiliteUpdateComponent;
  let fixture: ComponentFixture<DisponibiliteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let disponibiliteFormService: DisponibiliteFormService;
  let disponibiliteService: DisponibiliteService;
  let motifDisponibiliteService: MotifDisponibiliteService;
  let agentService: AgentService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DisponibiliteUpdateComponent],
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
      .overrideTemplate(DisponibiliteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DisponibiliteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    disponibiliteFormService = TestBed.inject(DisponibiliteFormService);
    disponibiliteService = TestBed.inject(DisponibiliteService);
    motifDisponibiliteService = TestBed.inject(MotifDisponibiliteService);
    agentService = TestBed.inject(AgentService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call MotifDisponibilite query and add missing value', () => {
      const disponibilite: IDisponibilite = { id: 456 };
      const motifDisponibilite: IMotifDisponibilite = { id: 21079 };
      disponibilite.motifDisponibilite = motifDisponibilite;

      const motifDisponibiliteCollection: IMotifDisponibilite[] = [{ id: 4517 }];
      jest.spyOn(motifDisponibiliteService, 'query').mockReturnValue(of(new HttpResponse({ body: motifDisponibiliteCollection })));
      const additionalMotifDisponibilites = [motifDisponibilite];
      const expectedCollection: IMotifDisponibilite[] = [...additionalMotifDisponibilites, ...motifDisponibiliteCollection];
      jest.spyOn(motifDisponibiliteService, 'addMotifDisponibiliteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ disponibilite });
      comp.ngOnInit();

      expect(motifDisponibiliteService.query).toHaveBeenCalled();
      expect(motifDisponibiliteService.addMotifDisponibiliteToCollectionIfMissing).toHaveBeenCalledWith(
        motifDisponibiliteCollection,
        ...additionalMotifDisponibilites.map(expect.objectContaining),
      );
      expect(comp.motifDisponibilitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Agent query and add missing value', () => {
      const disponibilite: IDisponibilite = { id: 456 };
      const agent: IAgent = { id: 23312 };
      disponibilite.agent = agent;

      const agentCollection: IAgent[] = [{ id: 27190 }];
      jest.spyOn(agentService, 'query').mockReturnValue(of(new HttpResponse({ body: agentCollection })));
      const additionalAgents = [agent];
      const expectedCollection: IAgent[] = [...additionalAgents, ...agentCollection];
      jest.spyOn(agentService, 'addAgentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ disponibilite });
      comp.ngOnInit();

      expect(agentService.query).toHaveBeenCalled();
      expect(agentService.addAgentToCollectionIfMissing).toHaveBeenCalledWith(
        agentCollection,
        ...additionalAgents.map(expect.objectContaining),
      );
      expect(comp.agentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const disponibilite: IDisponibilite = { id: 456 };
      const motifDisponibilite: IMotifDisponibilite = { id: 3125 };
      disponibilite.motifDisponibilite = motifDisponibilite;
      const agent: IAgent = { id: 22309 };
      disponibilite.agent = agent;

      activatedRoute.data = of({ disponibilite });
      comp.ngOnInit();

      expect(comp.motifDisponibilitesSharedCollection).toContain(motifDisponibilite);
      expect(comp.agentsSharedCollection).toContain(agent);
      expect(comp.disponibilite).toEqual(disponibilite);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisponibilite>>();
      const disponibilite = { id: 123 };
      jest.spyOn(disponibiliteFormService, 'getDisponibilite').mockReturnValue(disponibilite);
      jest.spyOn(disponibiliteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disponibilite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: disponibilite }));
      saveSubject.complete();

      // THEN
      expect(disponibiliteFormService.getDisponibilite).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(disponibiliteService.update).toHaveBeenCalledWith(expect.objectContaining(disponibilite));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisponibilite>>();
      const disponibilite = { id: 123 };
      jest.spyOn(disponibiliteFormService, 'getDisponibilite').mockReturnValue({ id: null });
      jest.spyOn(disponibiliteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disponibilite: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: disponibilite }));
      saveSubject.complete();

      // THEN
      expect(disponibiliteFormService.getDisponibilite).toHaveBeenCalled();
      expect(disponibiliteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDisponibilite>>();
      const disponibilite = { id: 123 };
      jest.spyOn(disponibiliteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ disponibilite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(disponibiliteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMotifDisponibilite', () => {
      it('Should forward to motifDisponibiliteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(motifDisponibiliteService, 'compareMotifDisponibilite');
        comp.compareMotifDisponibilite(entity, entity2);
        expect(motifDisponibiliteService.compareMotifDisponibilite).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
