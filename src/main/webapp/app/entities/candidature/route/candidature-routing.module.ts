import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CandidatureComponent } from '../list/candidature.component';
import { CandidatureDetailComponent } from '../detail/candidature-detail.component';
import { CandidatureUpdateComponent } from '../update/candidature-update.component';
import { CandidatureRoutingResolveService } from './candidature-routing-resolve.service';

const candidatureRoute: Routes = [
  {
    path: '',
    component: CandidatureComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CandidatureDetailComponent,
    resolve: {
      candidature: CandidatureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CandidatureUpdateComponent,
    resolve: {
      candidature: CandidatureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CandidatureUpdateComponent,
    resolve: {
      candidature: CandidatureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(candidatureRoute)],
  exports: [RouterModule],
})
export class CandidatureRoutingModule {}
