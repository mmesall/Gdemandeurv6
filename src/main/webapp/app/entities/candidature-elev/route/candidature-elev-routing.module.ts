import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CandidatureElevComponent } from '../list/candidature-elev.component';
import { CandidatureElevDetailComponent } from '../detail/candidature-elev-detail.component';
import { CandidatureElevUpdateComponent } from '../update/candidature-elev-update.component';
import { CandidatureElevRoutingResolveService } from './candidature-elev-routing-resolve.service';

const candidatureElevRoute: Routes = [
  {
    path: '',
    component: CandidatureElevComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CandidatureElevDetailComponent,
    resolve: {
      candidatureElev: CandidatureElevRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CandidatureElevUpdateComponent,
    resolve: {
      candidatureElev: CandidatureElevRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CandidatureElevUpdateComponent,
    resolve: {
      candidatureElev: CandidatureElevRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(candidatureElevRoute)],
  exports: [RouterModule],
})
export class CandidatureElevRoutingModule {}
