import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMotifDisponibilite } from 'app/entities/motif-disponibilite/motif-disponibilite.model';
import { MotifDisponibiliteService } from 'app/entities/motif-disponibilite/service/motif-disponibilite.service';
import { IAgent } from 'app/entities/agent/agent.model';
import { AgentService } from 'app/entities/agent/service/agent.service';
import { DisponibiliteService } from '../service/disponibilite.service';
import { IDisponibilite } from '../disponibilite.model';
import { DisponibiliteFormService, DisponibiliteFormGroup } from './disponibilite-form.service';

@Component({
  standalone: true,
  selector: 'jhi-disponibilite-update',
  templateUrl: './disponibilite-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DisponibiliteUpdateComponent implements OnInit {
  isSaving = false;
  disponibilite: IDisponibilite | null = null;

  motifDisponibilitesSharedCollection: IMotifDisponibilite[] = [];
  agentsSharedCollection: IAgent[] = [];

  editForm: DisponibiliteFormGroup = this.disponibiliteFormService.createDisponibiliteFormGroup();

  constructor(
    protected disponibiliteService: DisponibiliteService,
    protected disponibiliteFormService: DisponibiliteFormService,
    protected motifDisponibiliteService: MotifDisponibiliteService,
    protected agentService: AgentService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareMotifDisponibilite = (o1: IMotifDisponibilite | null, o2: IMotifDisponibilite | null): boolean =>
    this.motifDisponibiliteService.compareMotifDisponibilite(o1, o2);

  compareAgent = (o1: IAgent | null, o2: IAgent | null): boolean => this.agentService.compareAgent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ disponibilite }) => {
      this.disponibilite = disponibilite;
      if (disponibilite) {
        this.updateForm(disponibilite);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const disponibilite = this.disponibiliteFormService.getDisponibilite(this.editForm);
    if (disponibilite.id !== null) {
      this.subscribeToSaveResponse(this.disponibiliteService.update(disponibilite));
    } else {
      this.subscribeToSaveResponse(this.disponibiliteService.create(disponibilite));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDisponibilite>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(disponibilite: IDisponibilite): void {
    this.disponibilite = disponibilite;
    this.disponibiliteFormService.resetForm(this.editForm, disponibilite);

    this.motifDisponibilitesSharedCollection =
      this.motifDisponibiliteService.addMotifDisponibiliteToCollectionIfMissing<IMotifDisponibilite>(
        this.motifDisponibilitesSharedCollection,
        disponibilite.motifDisponibilite,
      );
    this.agentsSharedCollection = this.agentService.addAgentToCollectionIfMissing<IAgent>(this.agentsSharedCollection, disponibilite.agent);
  }

  protected loadRelationshipsOptions(): void {
    this.motifDisponibiliteService
      .query()
      .pipe(map((res: HttpResponse<IMotifDisponibilite[]>) => res.body ?? []))
      .pipe(
        map((motifDisponibilites: IMotifDisponibilite[]) =>
          this.motifDisponibiliteService.addMotifDisponibiliteToCollectionIfMissing<IMotifDisponibilite>(
            motifDisponibilites,
            this.disponibilite?.motifDisponibilite,
          ),
        ),
      )
      .subscribe((motifDisponibilites: IMotifDisponibilite[]) => (this.motifDisponibilitesSharedCollection = motifDisponibilites));

    this.agentService
      .query()
      .pipe(map((res: HttpResponse<IAgent[]>) => res.body ?? []))
      .pipe(map((agents: IAgent[]) => this.agentService.addAgentToCollectionIfMissing<IAgent>(agents, this.disponibilite?.agent)))
      .subscribe((agents: IAgent[]) => (this.agentsSharedCollection = agents));
  }
}
