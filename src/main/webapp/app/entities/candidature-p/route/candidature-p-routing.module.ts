import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CandidaturePComponent } from '../list/candidature-p.component';
import { CandidaturePDetailComponent } from '../detail/candidature-p-detail.component';
import { CandidaturePUpdateComponent } from '../update/candidature-p-update.component';
import { CandidaturePRoutingResolveService } from './candidature-p-routing-resolve.service';

const candidaturePRoute: Routes = [
  {
    path: '',
    component: CandidaturePComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CandidaturePDetailComponent,
    resolve: {
      candidatureP: CandidaturePRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CandidaturePUpdateComponent,
    resolve: {
      candidatureP: CandidaturePRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CandidaturePUpdateComponent,
    resolve: {
      candidatureP: CandidaturePRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(candidaturePRoute)],
  exports: [RouterModule],
})
export class CandidaturePRoutingModule {}
