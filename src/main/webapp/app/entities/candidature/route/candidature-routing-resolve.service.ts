import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICandidature, Candidature } from '../candidature.model';
import { CandidatureService } from '../service/candidature.service';

@Injectable({ providedIn: 'root' })
export class CandidatureRoutingResolveService implements Resolve<ICandidature> {
  constructor(protected service: CandidatureService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICandidature> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((candidature: HttpResponse<Candidature>) => {
          if (candidature.body) {
            return of(candidature.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Candidature());
  }
}
