import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IDegre } from '../degre.model';
import { DegreService } from '../service/degre.service';

@Component({
  standalone: true,
  templateUrl: './degre-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DegreDeleteDialogComponent {
  degre?: IDegre;

  constructor(
    protected degreService: DegreService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.degreService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
