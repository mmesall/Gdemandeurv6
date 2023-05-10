import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICandidatureE } from '../candidature-e.model';
import { CandidatureEService } from '../service/candidature-e.service';

@Component({
  templateUrl: './candidature-e-delete-dialog.component.html',
})
export class CandidatureEDeleteDialogComponent {
  candidatureE?: ICandidatureE;

  constructor(protected candidatureEService: CandidatureEService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.candidatureEService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
