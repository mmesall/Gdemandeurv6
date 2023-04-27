import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFormationInitiale } from '../formation-initiale.model';
import { FormationInitialeService } from '../service/formation-initiale.service';

@Component({
  templateUrl: './formation-initiale-delete-dialog.component.html',
})
export class FormationInitialeDeleteDialogComponent {
  formationInitiale?: IFormationInitiale;

  constructor(protected formationInitialeService: FormationInitialeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formationInitialeService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
