import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IServiceMFPAI, ServiceMFPAI } from '../service-mfpai.model';
import { ServiceMFPAIService } from '../service/service-mfpai.service';

@Injectable({ providedIn: 'root' })
export class ServiceMFPAIRoutingResolveService implements Resolve<IServiceMFPAI> {
  constructor(protected service: ServiceMFPAIService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IServiceMFPAI> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((serviceMFPAI: HttpResponse<ServiceMFPAI>) => {
          if (serviceMFPAI.body) {
            return of(serviceMFPAI.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ServiceMFPAI());
  }
}
