import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FormationContinueComponent } from './list/formation-continue.component';
import { FormationContinueDetailComponent } from './detail/formation-continue-detail.component';
import { FormationContinueUpdateComponent } from './update/formation-continue-update.component';
import { FormationContinueDeleteDialogComponent } from './delete/formation-continue-delete-dialog.component';
import { FormationContinueRoutingModule } from './route/formation-continue-routing.module';

@NgModule({
  imports: [SharedModule, FormationContinueRoutingModule],
  declarations: [
    FormationContinueComponent,
    FormationContinueDetailComponent,
    FormationContinueUpdateComponent,
    FormationContinueDeleteDialogComponent,
  ],
  entryComponents: [FormationContinueDeleteDialogComponent],
})
export class FormationContinueModule {}
