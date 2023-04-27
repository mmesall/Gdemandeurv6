import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICandidatureProf, CandidatureProf } from '../candidature-prof.model';
import { CandidatureProfService } from '../service/candidature-prof.service';

@Injectable({ providedIn: 'root' })
export class CandidatureProfRoutingResolveService implements Resolve<ICandidatureProf> {
  constructor(protected service: CandidatureProfService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICandidatureProf> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((candidatureProf: HttpResponse<CandidatureProf>) => {
          if (candidatureProf.body) {
            return of(candidatureProf.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CandidatureProf());
  }
}
