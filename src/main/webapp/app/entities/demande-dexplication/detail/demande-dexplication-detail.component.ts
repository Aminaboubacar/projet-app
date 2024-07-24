import { Component, Input } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe } from 'app/shared/date';
import { IDemandeDexplication } from '../demande-dexplication.model';

@Component({
  standalone: true,
  selector: 'jhi-demande-dexplication-detail',
  templateUrl: './demande-dexplication-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class DemandeDexplicationDetailComponent {
  @Input() demandeDexplication: IDemandeDexplication | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  previousState(): void {
    window.history.back();
  }
}
