import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAgent } from 'app/entities/agent/agent.model';
import { AgentService } from 'app/entities/agent/service/agent.service';
import { IMiseadisposition } from '../miseadisposition.model';
import { MiseadispositionService } from '../service/miseadisposition.service';
import { MiseadispositionFormService, MiseadispositionFormGroup } from './miseadisposition-form.service';

@Component({
  standalone: true,
  selector: 'jhi-miseadisposition-update',
  templateUrl: './miseadisposition-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MiseadispositionUpdateComponent implements OnInit {
  isSaving = false;
  miseadisposition: IMiseadisposition | null = null;

  agentsSharedCollection: IAgent[] = [];

  editForm: MiseadispositionFormGroup = this.miseadispositionFormService.createMiseadispositionFormGroup();

  constructor(
    protected miseadispositionService: MiseadispositionService,
    protected miseadispositionFormService: MiseadispositionFormService,
    protected agentService: AgentService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareAgent = (o1: IAgent | null, o2: IAgent | null): boolean => this.agentService.compareAgent(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ miseadisposition }) => {
      this.miseadisposition = miseadisposition;
      if (miseadisposition) {
        this.updateForm(miseadisposition);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const miseadisposition = this.miseadispositionFormService.getMiseadisposition(this.editForm);
    if (miseadisposition.id !== null) {
      this.subscribeToSaveResponse(this.miseadispositionService.update(miseadisposition));
    } else {
      this.subscribeToSaveResponse(this.miseadispositionService.create(miseadisposition));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMiseadisposition>>): void {
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

  protected updateForm(miseadisposition: IMiseadisposition): void {
    this.miseadisposition = miseadisposition;
    this.miseadispositionFormService.resetForm(this.editForm, miseadisposition);

    this.agentsSharedCollection = this.agentService.addAgentToCollectionIfMissing<IAgent>(
      this.agentsSharedCollection,
      miseadisposition.agent,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.agentService
      .query()
      .pipe(map((res: HttpResponse<IAgent[]>) => res.body ?? []))
      .pipe(map((agents: IAgent[]) => this.agentService.addAgentToCollectionIfMissing<IAgent>(agents, this.miseadisposition?.agent)))
      .subscribe((agents: IAgent[]) => (this.agentsSharedCollection = agents));
  }
}
