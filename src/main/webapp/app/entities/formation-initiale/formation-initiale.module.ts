import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FormationInitialeComponent } from './list/formation-initiale.component';
import { FormationInitialeDetailComponent } from './detail/formation-initiale-detail.component';
import { FormationInitialeUpdateComponent } from './update/formation-initiale-update.component';
import { FormationInitialeDeleteDialogComponent } from './delete/formation-initiale-delete-dialog.component';
import { FormationInitialeRoutingModule } from './route/formation-initiale-routing.module';

@NgModule({
  imports: [SharedModule, FormationInitialeRoutingModule],
  declarations: [
    FormationInitialeComponent,
    FormationInitialeDetailComponent,
    FormationInitialeUpdateComponent,
    FormationInitialeDeleteDialogComponent,
  ],
  entryComponents: [FormationInitialeDeleteDialogComponent],
})
export class FormationInitialeModule {}
