import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CandidatureEtudiantComponent } from './list/candidature-etudiant.component';
import { CandidatureEtudiantDetailComponent } from './detail/candidature-etudiant-detail.component';
import { CandidatureEtudiantUpdateComponent } from './update/candidature-etudiant-update.component';
import { CandidatureEtudiantDeleteDialogComponent } from './delete/candidature-etudiant-delete-dialog.component';
import { CandidatureEtudiantRoutingModule } from './route/candidature-etudiant-routing.module';

@NgModule({
  imports: [SharedModule, CandidatureEtudiantRoutingModule],
  declarations: [
    CandidatureEtudiantComponent,
    CandidatureEtudiantDetailComponent,
    CandidatureEtudiantUpdateComponent,
    CandidatureEtudiantDeleteDialogComponent,
  ],
  entryComponents: [CandidatureEtudiantDeleteDialogComponent],
})
export class CandidatureEtudiantModule {}
