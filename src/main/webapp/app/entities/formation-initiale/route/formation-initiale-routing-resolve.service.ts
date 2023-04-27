import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFormationInitiale, FormationInitiale } from '../formation-initiale.model';
import { FormationInitialeService } from '../service/formation-initiale.service';

@Injectable({ providedIn: 'root' })
export class FormationInitialeRoutingResolveService implements Resolve<IFormationInitiale> {
  constructor(protected service: FormationInitialeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFormationInitiale> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((formationInitiale: HttpResponse<FormationInitiale>) => {
          if (formationInitiale.body) {
            return of(formationInitiale.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FormationInitiale());
  }
}
