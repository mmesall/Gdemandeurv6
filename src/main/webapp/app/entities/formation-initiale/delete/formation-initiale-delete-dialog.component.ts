import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IFormationInitiale } from '../formation-initiale.model';
import { FormationInitialeService } from '../service/formation-initiale.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './formation-initiale-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class FormationInitialeDeleteDialogComponent {
  formationInitiale?: IFormationInitiale;

  constructor(protected formationInitialeService: FormationInitialeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formationInitialeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
