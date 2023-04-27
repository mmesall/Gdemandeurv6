import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CandidatureProfComponent } from './list/candidature-prof.component';
import { CandidatureProfDetailComponent } from './detail/candidature-prof-detail.component';
import { CandidatureProfUpdateComponent } from './update/candidature-prof-update.component';
import { CandidatureProfDeleteDialogComponent } from './delete/candidature-prof-delete-dialog.component';
import { CandidatureProfRoutingModule } from './route/candidature-prof-routing.module';

@NgModule({
  imports: [SharedModule, CandidatureProfRoutingModule],
  declarations: [
    CandidatureProfComponent,
    CandidatureProfDetailComponent,
    CandidatureProfUpdateComponent,
    CandidatureProfDeleteDialogComponent,
  ],
  entryComponents: [CandidatureProfDeleteDialogComponent],
})
export class CandidatureProfModule {}
