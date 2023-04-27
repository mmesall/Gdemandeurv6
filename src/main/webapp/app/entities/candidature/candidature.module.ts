import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CandidatureComponent } from './list/candidature.component';
import { CandidatureDetailComponent } from './detail/candidature-detail.component';
import { CandidatureUpdateComponent } from './update/candidature-update.component';
import { CandidatureDeleteDialogComponent } from './delete/candidature-delete-dialog.component';
import { CandidatureRoutingModule } from './route/candidature-routing.module';

@NgModule({
  imports: [SharedModule, CandidatureRoutingModule],
  declarations: [CandidatureComponent, CandidatureDetailComponent, CandidatureUpdateComponent, CandidatureDeleteDialogComponent],
  entryComponents: [CandidatureDeleteDialogComponent],
})
export class CandidatureModule {}
