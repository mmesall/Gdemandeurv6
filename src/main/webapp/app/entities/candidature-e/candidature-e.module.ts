import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CandidatureEComponent } from './list/candidature-e.component';
import { CandidatureEDetailComponent } from './detail/candidature-e-detail.component';
import { CandidatureEUpdateComponent } from './update/candidature-e-update.component';
import { CandidatureEDeleteDialogComponent } from './delete/candidature-e-delete-dialog.component';
import { CandidatureERoutingModule } from './route/candidature-e-routing.module';

@NgModule({
  imports: [SharedModule, CandidatureERoutingModule],
  declarations: [CandidatureEComponent, CandidatureEDetailComponent, CandidatureEUpdateComponent, CandidatureEDeleteDialogComponent],
  entryComponents: [CandidatureEDeleteDialogComponent],
})
export class CandidatureEModule {}
