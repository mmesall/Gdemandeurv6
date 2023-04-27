import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICandidatureEtudiant } from '../candidature-etudiant.model';
import { CandidatureEtudiantService } from '../service/candidature-etudiant.service';

@Component({
  templateUrl: './candidature-etudiant-delete-dialog.component.html',
})
export class CandidatureEtudiantDeleteDialogComponent {
  candidatureEtudiant?: ICandidatureEtudiant;

  constructor(protected candidatureEtudiantService: CandidatureEtudiantService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.candidatureEtudiantService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
