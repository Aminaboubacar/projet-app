import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISanction } from 'app/entities/sanction/sanction.model';
import { SanctionService } from 'app/entities/sanction/service/sanction.service';
import { IDemandeDexplication } from 'app/entities/demande-dexplication/demande-dexplication.model';
import { DemandeDexplicationService } from 'app/entities/demande-dexplication/service/demande-dexplication.service';
import { SanctionnerService } from '../service/sanctionner.service';
import { ISanctionner } from '../sanctionner.model';
import { SanctionnerFormService, SanctionnerFormGroup } from './sanctionner-form.service';

@Component({
  standalone: true,
  selector: 'jhi-sanctionner-update',
  templateUrl: './sanctionner-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SanctionnerUpdateComponent implements OnInit {
  isSaving = false;
  sanctionner: ISanctionner | null = null;

  sanctionsSharedCollection: ISanction[] = [];
  demandeDexplicationsSharedCollection: IDemandeDexplication[] = [];

  editForm: SanctionnerFormGroup = this.sanctionnerFormService.createSanctionnerFormGroup();

  constructor(
    protected sanctionnerService: SanctionnerService,
    protected sanctionnerFormService: SanctionnerFormService,
    protected sanctionService: SanctionService,
    protected demandeDexplicationService: DemandeDexplicationService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareSanction = (o1: ISanction | null, o2: ISanction | null): boolean => this.sanctionService.compareSanction(o1, o2);

  compareDemandeDexplication = (o1: IDemandeDexplication | null, o2: IDemandeDexplication | null): boolean =>
    this.demandeDexplicationService.compareDemandeDexplication(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ sanctionner }) => {
      this.sanctionner = sanctionner;
      if (sanctionner) {
        this.updateForm(sanctionner);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const sanctionner = this.sanctionnerFormService.getSanctionner(this.editForm);
    if (sanctionner.id !== null) {
      this.subscribeToSaveResponse(this.sanctionnerService.update(sanctionner));
    } else {
      this.subscribeToSaveResponse(this.sanctionnerService.create(sanctionner));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISanctionner>>): void {
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

  protected updateForm(sanctionner: ISanctionner): void {
    this.sanctionner = sanctionner;
    this.sanctionnerFormService.resetForm(this.editForm, sanctionner);

    this.sanctionsSharedCollection = this.sanctionService.addSanctionToCollectionIfMissing<ISanction>(
      this.sanctionsSharedCollection,
      sanctionner.sanction,
    );
    this.demandeDexplicationsSharedCollection =
      this.demandeDexplicationService.addDemandeDexplicationToCollectionIfMissing<IDemandeDexplication>(
        this.demandeDexplicationsSharedCollection,
        sanctionner.demandeDexplication,
      );
  }

  protected loadRelationshipsOptions(): void {
    this.sanctionService
      .query()
      .pipe(map((res: HttpResponse<ISanction[]>) => res.body ?? []))
      .pipe(
        map((sanctions: ISanction[]) =>
          this.sanctionService.addSanctionToCollectionIfMissing<ISanction>(sanctions, this.sanctionner?.sanction),
        ),
      )
      .subscribe((sanctions: ISanction[]) => (this.sanctionsSharedCollection = sanctions));

    this.demandeDexplicationService
      .query()
      .pipe(map((res: HttpResponse<IDemandeDexplication[]>) => res.body ?? []))
      .pipe(
        map((demandeDexplications: IDemandeDexplication[]) =>
          this.demandeDexplicationService.addDemandeDexplicationToCollectionIfMissing<IDemandeDexplication>(
            demandeDexplications,
            this.sanctionner?.demandeDexplication,
          ),
        ),
      )
      .subscribe((demandeDexplications: IDemandeDexplication[]) => (this.demandeDexplicationsSharedCollection = demandeDexplications));
  }
}
