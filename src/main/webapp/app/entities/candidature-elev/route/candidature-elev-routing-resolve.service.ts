import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICandidatureElev, CandidatureElev } from '../candidature-elev.model';
import { CandidatureElevService } from '../service/candidature-elev.service';

@Injectable({ providedIn: 'root' })
export class CandidatureElevRoutingResolveService implements Resolve<ICandidatureElev> {
  constructor(protected service: CandidatureElevService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICandidatureElev> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((candidatureElev: HttpResponse<CandidatureElev>) => {
          if (candidatureElev.body) {
            return of(candidatureElev.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CandidatureElev());
  }
}
