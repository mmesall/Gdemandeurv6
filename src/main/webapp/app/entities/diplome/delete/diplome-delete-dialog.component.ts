import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IDiplome } from '../diplome.model';
import { DiplomeService } from '../service/diplome.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './diplome-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class DiplomeDeleteDialogComponent {
  diplome?: IDiplome;

  constructor(protected diplomeService: DiplomeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.diplomeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
