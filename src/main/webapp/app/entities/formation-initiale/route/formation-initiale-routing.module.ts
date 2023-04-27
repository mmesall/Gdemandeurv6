import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FormationInitialeComponent } from '../list/formation-initiale.component';
import { FormationInitialeDetailComponent } from '../detail/formation-initiale-detail.component';
import { FormationInitialeUpdateComponent } from '../update/formation-initiale-update.component';
import { FormationInitialeRoutingResolveService } from './formation-initiale-routing-resolve.service';

const formationInitialeRoute: Routes = [
  {
    path: '',
    component: FormationInitialeComponent,
    // canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormationInitialeDetailComponent,
    resolve: {
      formationInitiale: FormationInitialeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormationInitialeUpdateComponent,
    resolve: {
      formationInitiale: FormationInitialeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormationInitialeUpdateComponent,
    resolve: {
      formationInitiale: FormationInitialeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(formationInitialeRoute)],
  exports: [RouterModule],
})
export class FormationInitialeRoutingModule {}
