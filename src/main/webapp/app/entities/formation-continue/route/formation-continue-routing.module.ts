import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FormationContinueComponent } from '../list/formation-continue.component';
import { FormationContinueDetailComponent } from '../detail/formation-continue-detail.component';
import { FormationContinueUpdateComponent } from '../update/formation-continue-update.component';
import { FormationContinueRoutingResolveService } from './formation-continue-routing-resolve.service';

const formationContinueRoute: Routes = [
  {
    path: '',
    component: FormationContinueComponent,
    // canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormationContinueDetailComponent,
    resolve: {
      formationContinue: FormationContinueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormationContinueUpdateComponent,
    resolve: {
      formationContinue: FormationContinueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormationContinueUpdateComponent,
    resolve: {
      formationContinue: FormationContinueRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(formationContinueRoute)],
  exports: [RouterModule],
})
export class FormationContinueRoutingModule {}
