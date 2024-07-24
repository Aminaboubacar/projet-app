import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDemandeDexplication } from '../demande-dexplication.model';
import { DemandeDexplicationService } from '../service/demande-dexplication.service';

@Component({
  standalone: true,
  templateUrl: './demande-dexplication-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DemandeDexplicationDeleteDialogComponent {
  demandeDexplication?: IDemandeDexplication;

  constructor(
    protected demandeDexplicationService: DemandeDexplicationService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.demandeDexplicationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
