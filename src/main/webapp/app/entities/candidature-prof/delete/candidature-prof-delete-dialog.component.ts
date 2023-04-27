import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICandidatureProf } from '../candidature-prof.model';
import { CandidatureProfService } from '../service/candidature-prof.service';

@Component({
  templateUrl: './candidature-prof-delete-dialog.component.html',
})
export class CandidatureProfDeleteDialogComponent {
  candidatureProf?: ICandidatureProf;

  constructor(protected candidatureProfService: CandidatureProfService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.candidatureProfService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
