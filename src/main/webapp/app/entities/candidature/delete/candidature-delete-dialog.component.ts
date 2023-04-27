import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICandidature } from '../candidature.model';
import { CandidatureService } from '../service/candidature.service';

@Component({
  templateUrl: './candidature-delete-dialog.component.html',
})
export class CandidatureDeleteDialogComponent {
  candidature?: ICandidature;

  constructor(protected candidatureService: CandidatureService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.candidatureService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
