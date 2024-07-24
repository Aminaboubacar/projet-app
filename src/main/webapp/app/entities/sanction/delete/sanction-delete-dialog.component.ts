import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISanction } from '../sanction.model';
import { SanctionService } from '../service/sanction.service';

@Component({
  standalone: true,
  templateUrl: './sanction-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SanctionDeleteDialogComponent {
  sanction?: ISanction;

  constructor(
    protected sanctionService: SanctionService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.sanctionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
