import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CandidatureEtudiantComponent } from '../list/candidature-etudiant.component';
import { CandidatureEtudiantDetailComponent } from '../detail/candidature-etudiant-detail.component';
import { CandidatureEtudiantUpdateComponent } from '../update/candidature-etudiant-update.component';
import { CandidatureEtudiantRoutingResolveService } from './candidature-etudiant-routing-resolve.service';

const candidatureEtudiantRoute: Routes = [
  {
    path: '',
    component: CandidatureEtudiantComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CandidatureEtudiantDetailComponent,
    resolve: {
      candidatureEtudiant: CandidatureEtudiantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CandidatureEtudiantUpdateComponent,
    resolve: {
      candidatureEtudiant: CandidatureEtudiantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CandidatureEtudiantUpdateComponent,
    resolve: {
      candidatureEtudiant: CandidatureEtudiantRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(candidatureEtudiantRoute)],
  exports: [RouterModule],
})
export class CandidatureEtudiantRoutingModule {}
