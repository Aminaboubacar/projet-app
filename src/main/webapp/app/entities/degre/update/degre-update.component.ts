import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDegre } from '../degre.model';
import { DegreService } from '../service/degre.service';
import { DegreFormService, DegreFormGroup } from './degre-form.service';

@Component({
  standalone: true,
  selector: 'jhi-degre-update',
  templateUrl: './degre-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class DegreUpdateComponent implements OnInit {
  isSaving = false;
  degre: IDegre | null = null;

  editForm: DegreFormGroup = this.degreFormService.createDegreFormGroup();

  constructor(
    protected degreService: DegreService,
    protected degreFormService: DegreFormService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ degre }) => {
      this.degre = degre;
      if (degre) {
        this.updateForm(degre);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const degre = this.degreFormService.getDegre(this.editForm);
    if (degre.id !== null) {
      this.subscribeToSaveResponse(this.degreService.update(degre));
    } else {
      this.subscribeToSaveResponse(this.degreService.create(degre));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDegre>>): void {
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

  protected updateForm(degre: IDegre): void {
    this.degre = degre;
    this.degreFormService.resetForm(this.editForm, degre);
  }
}
