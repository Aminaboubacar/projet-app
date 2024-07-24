import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IMotifDisponibilite } from '../motif-disponibilite.model';
import { MotifDisponibiliteService } from '../service/motif-disponibilite.service';

@Component({
  standalone: true,
  templateUrl: './motif-disponibilite-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class MotifDisponibiliteDeleteDialogComponent {
  motifDisponibilite?: IMotifDisponibilite;

  constructor(
    protected motifDisponibiliteService: MotifDisponibiliteService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.motifDisponibiliteService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
