import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IDegre } from 'app/entities/degre/degre.model';
import { DegreService } from 'app/entities/degre/service/degre.service';
import { ISanction } from '../sanction.model';
import { SanctionService } from '../service/sanction.service';
import { SanctionFormService, SanctionFormGroup } from './sanction-form.service';

@Component({
  standalone: true,
  selector: 'jhi-sanction-update',
  templateUrl: './sanction-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SanctionUpdateComponent implements OnInit {
  isSaving = false;
  sanction: ISanction | null = null;

  degresSharedCollection: IDegre[] = [];

  editForm: SanctionFormGroup = this.sanctionFormService.createSanctionFormGroup();

  constructor(
    protected sanctionService: SanctionService,
    protected sanctionFormService: SanctionFormService,
    protected degreService: DegreService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareDegre = (o1: IDegre | null, o2: IDegre | null): boolean => this.degreService.compareDegre(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sanction }) => {
      this.sanction = sanction;
      if (sanction) {
        this.updateForm(sanction);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sanction = this.sanctionFormService.getSanction(this.editForm);
    if (sanction.id !== null) {
      this.subscribeToSaveResponse(this.sanctionService.update(sanction));
    } else {
      this.subscribeToSaveResponse(this.sanctionService.create(sanction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISanction>>): void {
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

  protected updateForm(sanction: ISanction): void {
    this.sanction = sanction;
    this.sanctionFormService.resetForm(this.editForm, sanction);

    this.degresSharedCollection = this.degreService.addDegreToCollectionIfMissing<IDegre>(this.degresSharedCollection, sanction.degre);
  }

  protected loadRelationshipsOptions(): void {
    this.degreService
      .query()
      .pipe(map((res: HttpResponse<IDegre[]>) => res.body ?? []))
      .pipe(map((degres: IDegre[]) => this.degreService.addDegreToCollectionIfMissing<IDegre>(degres, this.sanction?.degre)))
      .subscribe((degres: IDegre[]) => (this.degresSharedCollection = degres));
  }
}
