import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ServiceMFPAIComponent } from '../list/service-mfpai.component';
import { ServiceMFPAIDetailComponent } from '../detail/service-mfpai-detail.component';
import { ServiceMFPAIUpdateComponent } from '../update/service-mfpai-update.component';
import { ServiceMFPAIRoutingResolveService } from './service-mfpai-routing-resolve.service';

const serviceMFPAIRoute: Routes = [
  {
    path: '',
    component: ServiceMFPAIComponent,
    // canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ServiceMFPAIDetailComponent,
    resolve: {
      serviceMFPAI: ServiceMFPAIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ServiceMFPAIUpdateComponent,
    resolve: {
      serviceMFPAI: ServiceMFPAIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ServiceMFPAIUpdateComponent,
    resolve: {
      serviceMFPAI: ServiceMFPAIRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(serviceMFPAIRoute)],
  exports: [RouterModule],
})
export class ServiceMFPAIRoutingModule {}
