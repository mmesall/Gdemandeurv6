import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICandidatureP, CandidatureP } from '../candidature-p.model';
import { CandidaturePService } from '../service/candidature-p.service';

@Injectable({ providedIn: 'root' })
export class CandidaturePRoutingResolveService implements Resolve<ICandidatureP> {
  constructor(protected service: CandidaturePService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICandidatureP> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((candidatureP: HttpResponse<CandidatureP>) => {
          if (candidatureP.body) {
            return of(candidatureP.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CandidatureP());
  }
}
