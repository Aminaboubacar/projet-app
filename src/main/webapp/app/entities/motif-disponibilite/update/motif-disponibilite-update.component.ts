import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IMotifDisponibilite } from '../motif-disponibilite.model';
import { MotifDisponibiliteService } from '../service/motif-disponibilite.service';
import { MotifDisponibiliteFormService, MotifDisponibiliteFormGroup } from './motif-disponibilite-form.service';

@Component({
  standalone: true,
  selector: 'jhi-motif-disponibilite-update',
  templateUrl: './motif-disponibilite-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class MotifDisponibiliteUpdateComponent implements OnInit {
  isSaving = false;
  motifDisponibilite: IMotifDisponibilite | null = null;

  editForm: MotifDisponibiliteFormGroup = this.motifDisponibiliteFormService.createMotifDisponibiliteFormGroup();

  constructor(
    protected motifDisponibiliteService: MotifDisponibiliteService,
    protected motifDisponibiliteFormService: MotifDisponibiliteFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ motifDisponibilite }) => {
      this.motifDisponibilite = motifDisponibilite;
      if (motifDisponibilite) {
        this.updateForm(motifDisponibilite);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const motifDisponibilite = this.motifDisponibiliteFormService.getMotifDisponibilite(this.editForm);
    if (motifDisponibilite.id !== null) {
      this.subscribeToSaveResponse(this.motifDisponibiliteService.update(motifDisponibilite));
    } else {
      this.subscribeToSaveResponse(this.motifDisponibiliteService.create(motifDisponibilite));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMotifDisponibilite>>): void {
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

  protected updateForm(motifDisponibilite: IMotifDisponibilite): void {
    this.motifDisponibilite = motifDisponibilite;
    this.motifDisponibiliteFormService.resetForm(this.editForm, motifDisponibilite);
  }
}
