import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEleve } from '../eleve.model';
import { EleveService } from '../service/eleve.service';

export const eleveResolve = (route: ActivatedRouteSnapshot): Observable<null | IEleve> => {
  const id = route.params['id'];
  if (id) {
    return inject(EleveService)
      .find(id)
      .pipe(
        mergeMap((eleve: HttpResponse<IEleve>) => {
          if (eleve.body) {
            return of(eleve.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default eleveResolve;
