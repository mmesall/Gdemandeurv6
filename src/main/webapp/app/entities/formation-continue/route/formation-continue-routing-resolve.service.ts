import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFormationContinue, FormationContinue } from '../formation-continue.model';
import { FormationContinueService } from '../service/formation-continue.service';

@Injectable({ providedIn: 'root' })
export class FormationContinueRoutingResolveService implements Resolve<IFormationContinue> {
  constructor(protected service: FormationContinueService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFormationContinue> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((formationContinue: HttpResponse<FormationContinue>) => {
          if (formationContinue.body) {
            return of(formationContinue.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FormationContinue());
  }
}
