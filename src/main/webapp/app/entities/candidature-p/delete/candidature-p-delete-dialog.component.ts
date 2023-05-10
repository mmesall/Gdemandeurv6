import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICandidatureP } from '../candidature-p.model';
import { CandidaturePService } from '../service/candidature-p.service';

@Component({
  templateUrl: './candidature-p-delete-dialog.component.html',
})
export class CandidaturePDeleteDialogComponent {
  candidatureP?: ICandidatureP;

  constructor(protected candidaturePService: CandidaturePService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.candidaturePService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
