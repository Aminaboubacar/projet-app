import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAgent } from 'app/entities/agent/agent.model';
import { AgentService } from 'app/entities/agent/service/agent.service';
import { IDemandeDexplication } from '../demande-dexplication.model';
import { DemandeDexplicationService } from '../service/demande-dexplication.service';
import { DemandeDexplicationFormService, DemandeDexplicationFormGroup } from './demande-dexplication-form.service';

@Component({
  standalone: true,
  selector: 'jhi-demande-dexplication-update',
  templateUrl: './demande-dexplication-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DemandeDexplicationUpdateComponent implements OnInit {
  isSaving = false;
  demandeDexplication: IDemandeDexplication | null = null;

  agentsSharedCollection: IAgent[] = [];

  editForm: DemandeDexplicationFormGroup = this.demandeDexplicationFormService.createDemandeDexplicationFormGroup();

  constructor(
    protected demandeDexplicationService: DemandeDexplicationService,
    protected demandeDexplicationFormService: DemandeDexplicationFormService,
    protected agentService: AgentService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareAgent = (o1: IAgent | null, o2: IAgent | null): boolean => this.agentService.compareAgent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ demandeDexplication }) => {
      this.demandeDexplication = demandeDexplication;
      if (demandeDexplication) {
        this.updateForm(demandeDexplication);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const demandeDexplication = this.demandeDexplicationFormService.getDemandeDexplication(this.editForm);
    if (demandeDexplication.id !== null) {
      this.subscribeToSaveResponse(this.demandeDexplicationService.update(demandeDexplication));
    } else {
      this.subscribeToSaveResponse(this.demandeDexplicationService.create(demandeDexplication));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDemandeDexplication>>): void {
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

  protected updateForm(demandeDexplication: IDemandeDexplication): void {
    this.demandeDexplication = demandeDexplication;
    this.demandeDexplicationFormService.resetForm(this.editForm, demandeDexplication);

    this.agentsSharedCollection = this.agentService.addAgentToCollectionIfMissing<IAgent>(
      this.agentsSharedCollection,
      demandeDexplication.agent,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.agentService
      .query()
      .pipe(map((res: HttpResponse<IAgent[]>) => res.body ?? []))
      .pipe(map((agents: IAgent[]) => this.agentService.addAgentToCollectionIfMissing<IAgent>(agents, this.demandeDexplication?.agent)))
      .subscribe((agents: IAgent[]) => (this.agentsSharedCollection = agents));
  }
}
