import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ServiceMFPAIComponent } from './list/service-mfpai.component';
import { ServiceMFPAIDetailComponent } from './detail/service-mfpai-detail.component';
import { ServiceMFPAIUpdateComponent } from './update/service-mfpai-update.component';
import { ServiceMFPAIDeleteDialogComponent } from './delete/service-mfpai-delete-dialog.component';
import { ServiceMFPAIRoutingModule } from './route/service-mfpai-routing.module';

@NgModule({
  imports: [SharedModule, ServiceMFPAIRoutingModule],
  declarations: [ServiceMFPAIComponent, ServiceMFPAIDetailComponent, ServiceMFPAIUpdateComponent, ServiceMFPAIDeleteDialogComponent],
  entryComponents: [ServiceMFPAIDeleteDialogComponent],
})
export class ServiceMFPAIModule {}
