import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CandidaturePComponent } from './list/candidature-p.component';
import { CandidaturePDetailComponent } from './detail/candidature-p-detail.component';
import { CandidaturePUpdateComponent } from './update/candidature-p-update.component';
import { CandidaturePDeleteDialogComponent } from './delete/candidature-p-delete-dialog.component';
import { CandidaturePRoutingModule } from './route/candidature-p-routing.module';

@NgModule({
  imports: [SharedModule, CandidaturePRoutingModule],
  declarations: [CandidaturePComponent, CandidaturePDetailComponent, CandidaturePUpdateComponent, CandidaturePDeleteDialogComponent],
  entryComponents: [CandidaturePDeleteDialogComponent],
})
export class CandidaturePModule {}
