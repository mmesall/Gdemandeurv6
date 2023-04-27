import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICandidatureEtudiant, CandidatureEtudiant } from '../candidature-etudiant.model';
import { CandidatureEtudiantService } from '../service/candidature-etudiant.service';

@Injectable({ providedIn: 'root' })
export class CandidatureEtudiantRoutingResolveService implements Resolve<ICandidatureEtudiant> {
  constructor(protected service: CandidatureEtudiantService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICandidatureEtudiant> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((candidatureEtudiant: HttpResponse<CandidatureEtudiant>) => {
          if (candidatureEtudiant.body) {
            return of(candidatureEtudiant.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CandidatureEtudiant());
  }
}
