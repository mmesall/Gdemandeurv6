import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CandidatureProfComponent } from '../list/candidature-prof.component';
import { CandidatureProfDetailComponent } from '../detail/candidature-prof-detail.component';
import { CandidatureProfUpdateComponent } from '../update/candidature-prof-update.component';
import { CandidatureProfRoutingResolveService } from './candidature-prof-routing-resolve.service';

const candidatureProfRoute: Routes = [
  {
    path: '',
    component: CandidatureProfComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CandidatureProfDetailComponent,
    resolve: {
      candidatureProf: CandidatureProfRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CandidatureProfUpdateComponent,
    resolve: {
      candidatureProf: CandidatureProfRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CandidatureProfUpdateComponent,
    resolve: {
      candidatureProf: CandidatureProfRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(candidatureProfRoute)],
  exports: [RouterModule],
})
export class CandidatureProfRoutingModule {}
