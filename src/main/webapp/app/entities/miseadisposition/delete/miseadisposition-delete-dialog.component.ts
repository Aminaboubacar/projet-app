import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMiseadisposition } from '../miseadisposition.model';
import { MiseadispositionService } from '../service/miseadisposition.service';

@Component({
  standalone: true,
  templateUrl: './miseadisposition-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MiseadispositionDeleteDialogComponent {
  miseadisposition?: IMiseadisposition;

  constructor(
    protected miseadispositionService: MiseadispositionService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.miseadispositionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
