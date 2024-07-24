import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IMiseadisposition } from '../miseadisposition.model';

@Component({
  standalone: true,
  selector: 'jhi-miseadisposition-detail',
  templateUrl: './miseadisposition-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class MiseadispositionDetailComponent {
  @Input() miseadisposition: IMiseadisposition | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
