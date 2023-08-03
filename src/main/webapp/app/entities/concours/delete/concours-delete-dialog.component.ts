import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IConcours } from '../concours.model';
import { ConcoursService } from '../service/concours.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './concours-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ConcoursDeleteDialogComponent {
  concours?: IConcours;

  constructor(protected concoursService: ConcoursService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.concoursService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
