import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IServiceMFPAI, getServiceMFPAIIdentifier } from '../service-mfpai.model';

export type EntityResponseType = HttpResponse<IServiceMFPAI>;
export type EntityArrayResponseType = HttpResponse<IServiceMFPAI[]>;

@Injectable({ providedIn: 'root' })
export class ServiceMFPAIService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/service-mfpais');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(serviceMFPAI: IServiceMFPAI): Observable<EntityResponseType> {
    return this.http.post<IServiceMFPAI>(this.resourceUrl, serviceMFPAI, { observe: 'response' });
  }

  update(serviceMFPAI: IServiceMFPAI): Observable<EntityResponseType> {
    return this.http.put<IServiceMFPAI>(`${this.resourceUrl}/${getServiceMFPAIIdentifier(serviceMFPAI) as number}`, serviceMFPAI, {
      observe: 'response',
    });
  }

  partialUpdate(serviceMFPAI: IServiceMFPAI): Observable<EntityResponseType> {
    return this.http.patch<IServiceMFPAI>(`${this.resourceUrl}/${getServiceMFPAIIdentifier(serviceMFPAI) as number}`, serviceMFPAI, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IServiceMFPAI>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IServiceMFPAI[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addServiceMFPAIToCollectionIfMissing(
    serviceMFPAICollection: IServiceMFPAI[],
    ...serviceMFPAISToCheck: (IServiceMFPAI | null | undefined)[]
  ): IServiceMFPAI[] {
    const serviceMFPAIS: IServiceMFPAI[] = serviceMFPAISToCheck.filter(isPresent);
    if (serviceMFPAIS.length > 0) {
      const serviceMFPAICollectionIdentifiers = serviceMFPAICollection.map(
        serviceMFPAIItem => getServiceMFPAIIdentifier(serviceMFPAIItem)!
      );
      const serviceMFPAISToAdd = serviceMFPAIS.filter(serviceMFPAIItem => {
        const serviceMFPAIIdentifier = getServiceMFPAIIdentifier(serviceMFPAIItem);
        if (serviceMFPAIIdentifier == null || serviceMFPAICollectionIdentifiers.includes(serviceMFPAIIdentifier)) {
          return false;
        }
        serviceMFPAICollectionIdentifiers.push(serviceMFPAIIdentifier);
        return true;
      });
      return [...serviceMFPAISToAdd, ...serviceMFPAICollection];
    }
    return serviceMFPAICollection;
  }
}
