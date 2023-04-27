import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFormationContinue } from '../formation-continue.model';
import { FormationContinueService } from '../service/formation-continue.service';

@Component({
  templateUrl: './formation-continue-delete-dialog.component.html',
})
export class FormationContinueDeleteDialogComponent {
  formationContinue?: IFormationContinue;

  constructor(protected formationContinueService: FormationContinueService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.formationContinueService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
