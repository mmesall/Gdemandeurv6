import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IProfessionnel } from '../professionnel.model';
import { ProfessionnelService } from '../service/professionnel.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './professionnel-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ProfessionnelDeleteDialogComponent {
  professionnel?: IProfessionnel;

  constructor(protected professionnelService: ProfessionnelService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.professionnelService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
