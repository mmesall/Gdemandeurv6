import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFormationContinue, getFormationContinueIdentifier } from '../formation-continue.model';

export type EntityResponseType = HttpResponse<IFormationContinue>;
export type EntityArrayResponseType = HttpResponse<IFormationContinue[]>;

@Injectable({ providedIn: 'root' })
export class FormationContinueService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/formation-continues');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(formationContinue: IFormationContinue): Observable<EntityResponseType> {
    return this.http.post<IFormationContinue>(this.resourceUrl, formationContinue, { observe: 'response' });
  }

  update(formationContinue: IFormationContinue): Observable<EntityResponseType> {
    return this.http.put<IFormationContinue>(
      `${this.resourceUrl}/${getFormationContinueIdentifier(formationContinue) as number}`,
      formationContinue,
      { observe: 'response' }
    );
  }

  partialUpdate(formationContinue: IFormationContinue): Observable<EntityResponseType> {
    return this.http.patch<IFormationContinue>(
      `${this.resourceUrl}/${getFormationContinueIdentifier(formationContinue) as number}`,
      formationContinue,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFormationContinue>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFormationContinue[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFormationContinueToCollectionIfMissing(
    formationContinueCollection: IFormationContinue[],
    ...formationContinuesToCheck: (IFormationContinue | null | undefined)[]
  ): IFormationContinue[] {
    const formationContinues: IFormationContinue[] = formationContinuesToCheck.filter(isPresent);
    if (formationContinues.length > 0) {
      const formationContinueCollectionIdentifiers = formationContinueCollection.map(
        formationContinueItem => getFormationContinueIdentifier(formationContinueItem)!
      );
      const formationContinuesToAdd = formationContinues.filter(formationContinueItem => {
        const formationContinueIdentifier = getFormationContinueIdentifier(formationContinueItem);
        if (formationContinueIdentifier == null || formationContinueCollectionIdentifiers.includes(formationContinueIdentifier)) {
          return false;
        }
        formationContinueCollectionIdentifiers.push(formationContinueIdentifier);
        return true;
      });
      return [...formationContinuesToAdd, ...formationContinueCollection];
    }
    return formationContinueCollection;
  }
}
