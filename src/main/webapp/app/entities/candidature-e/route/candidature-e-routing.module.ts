import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CandidatureEComponent } from '../list/candidature-e.component';
import { CandidatureEDetailComponent } from '../detail/candidature-e-detail.component';
import { CandidatureEUpdateComponent } from '../update/candidature-e-update.component';
import { CandidatureERoutingResolveService } from './candidature-e-routing-resolve.service';

const candidatureERoute: Routes = [
  {
    path: '',
    component: CandidatureEComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CandidatureEDetailComponent,
    resolve: {
      candidatureE: CandidatureERoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CandidatureEUpdateComponent,
    resolve: {
      candidatureE: CandidatureERoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CandidatureEUpdateComponent,
    resolve: {
      candidatureE: CandidatureERoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(candidatureERoute)],
  exports: [RouterModule],
})
export class CandidatureERoutingModule {}
