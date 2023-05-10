import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICandidatureE, CandidatureE } from '../candidature-e.model';
import { CandidatureEService } from '../service/candidature-e.service';

@Injectable({ providedIn: 'root' })
export class CandidatureERoutingResolveService implements Resolve<ICandidatureE> {
  constructor(protected service: CandidatureEService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICandidatureE> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((candidatureE: HttpResponse<CandidatureE>) => {
          if (candidatureE.body) {
            return of(candidatureE.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CandidatureE());
  }
}
