import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IMotifDisponibilite } from '../motif-disponibilite.model';

@Component({
  standalone: true,
  selector: 'jhi-motif-disponibilite-detail',
  templateUrl: './motif-disponibilite-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MotifDisponibiliteDetailComponent {
  @Input() motifDisponibilite: IMotifDisponibilite | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
