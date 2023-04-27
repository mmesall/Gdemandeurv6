import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICandidatureElev } from '../candidature-elev.model';
import { CandidatureElevService } from '../service/candidature-elev.service';

@Component({
  templateUrl: './candidature-elev-delete-dialog.component.html',
})
export class CandidatureElevDeleteDialogComponent {
  candidatureElev?: ICandidatureElev;

  constructor(protected candidatureElevService: CandidatureElevService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.candidatureElevService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
