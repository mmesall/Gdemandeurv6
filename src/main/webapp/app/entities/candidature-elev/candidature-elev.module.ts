import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CandidatureElevComponent } from './list/candidature-elev.component';
import { CandidatureElevDetailComponent } from './detail/candidature-elev-detail.component';
import { CandidatureElevUpdateComponent } from './update/candidature-elev-update.component';
import { CandidatureElevDeleteDialogComponent } from './delete/candidature-elev-delete-dialog.component';
import { CandidatureElevRoutingModule } from './route/candidature-elev-routing.module';

@NgModule({
  imports: [SharedModule, CandidatureElevRoutingModule],
  declarations: [
    CandidatureElevComponent,
    CandidatureElevDetailComponent,
    CandidatureElevUpdateComponent,
    CandidatureElevDeleteDialogComponent,
  ],
  entryComponents: [CandidatureElevDeleteDialogComponent],
})
export class CandidatureElevModule {}
