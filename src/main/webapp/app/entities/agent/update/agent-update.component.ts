import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPoste } from 'app/entities/poste/poste.model';
import { PosteService } from 'app/entities/poste/service/poste.service';
import { IAgent } from '../agent.model';
import { AgentService } from '../service/agent.service';
import { AgentFormService, AgentFormGroup } from './agent-form.service';

@Component({
  standalone: true,
  selector: 'jhi-agent-update',
  templateUrl: './agent-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AgentUpdateComponent implements OnInit {
  isSaving = false;
  agent: IAgent | null = null;

  postesSharedCollection: IPoste[] = [];

  editForm: AgentFormGroup = this.agentFormService.createAgentFormGroup();

  constructor(
    protected agentService: AgentService,
    protected agentFormService: AgentFormService,
    protected posteService: PosteService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  comparePoste = (o1: IPoste | null, o2: IPoste | null): boolean => this.posteService.comparePoste(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agent }) => {
      this.agent = agent;
      if (agent) {
        this.updateForm(agent);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const agent = this.agentFormService.getAgent(this.editForm);
    if (agent.id !== null) {
      this.subscribeToSaveResponse(this.agentService.update(agent));
    } else {
      this.subscribeToSaveResponse(this.agentService.create(agent));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAgent>>): void {
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

  protected updateForm(agent: IAgent): void {
    this.agent = agent;
    this.agentFormService.resetForm(this.editForm, agent);

    this.postesSharedCollection = this.posteService.addPosteToCollectionIfMissing<IPoste>(this.postesSharedCollection, agent.poste);
  }

  protected loadRelationshipsOptions(): void {
    this.posteService
      .query()
      .pipe(map((res: HttpResponse<IPoste[]>) => res.body ?? []))
      .pipe(map((postes: IPoste[]) => this.posteService.addPosteToCollectionIfMissing<IPoste>(postes, this.agent?.poste)))
      .subscribe((postes: IPoste[]) => (this.postesSharedCollection = postes));
  }
}
