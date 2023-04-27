import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFormationInitiale, getFormationInitialeIdentifier } from '../formation-initiale.model';

export type EntityResponseType = HttpResponse<IFormationInitiale>;
export type EntityArrayResponseType = HttpResponse<IFormationInitiale[]>;

@Injectable({ providedIn: 'root' })
export class FormationInitialeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/formation-initiales');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(formationInitiale: IFormationInitiale): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formationInitiale);
    return this.http
      .post<IFormationInitiale>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(formationInitiale: IFormationInitiale): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formationInitiale);
    return this.http
      .put<IFormationInitiale>(`${this.resourceUrl}/${getFormationInitialeIdentifier(formationInitiale) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(formationInitiale: IFormationInitiale): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(formationInitiale);
    return this.http
      .patch<IFormationInitiale>(`${this.resourceUrl}/${getFormationInitialeIdentifier(formationInitiale) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFormationInitiale>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFormationInitiale[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFormationInitialeToCollectionIfMissing(
    formationInitialeCollection: IFormationInitiale[],
    ...formationInitialesToCheck: (IFormationInitiale | null | undefined)[]
  ): IFormationInitiale[] {
    const formationInitiales: IFormationInitiale[] = formationInitialesToCheck.filter(isPresent);
    if (formationInitiales.length > 0) {
      const formationInitialeCollectionIdentifiers = formationInitialeCollection.map(
        formationInitialeItem => getFormationInitialeIdentifier(formationInitialeItem)!
      );
      const formationInitialesToAdd = formationInitiales.filter(formationInitialeItem => {
        const formationInitialeIdentifier = getFormationInitialeIdentifier(formationInitialeItem);
        if (formationInitialeIdentifier == null || formationInitialeCollectionIdentifiers.includes(formationInitialeIdentifier)) {
          return false;
        }
        formationInitialeCollectionIdentifiers.push(formationInitialeIdentifier);
        return true;
      });
      return [...formationInitialesToAdd, ...formationInitialeCollection];
    }
    return formationInitialeCollection;
  }

  protected convertDateFromClient(formationInitiale: IFormationInitiale): IFormationInitiale {
    return Object.assign({}, formationInitiale, {
      dateOuverture: formationInitiale.dateOuverture?.isValid() ? formationInitiale.dateOuverture.format(DATE_FORMAT) : undefined,
      dateCloture: formationInitiale.dateCloture?.isValid() ? formationInitiale.dateCloture.format(DATE_FORMAT) : undefined,
      dateConcours: formationInitiale.dateConcours?.isValid() ? formationInitiale.dateConcours.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateOuverture = res.body.dateOuverture ? dayjs(res.body.dateOuverture) : undefined;
      res.body.dateCloture = res.body.dateCloture ? dayjs(res.body.dateCloture) : undefined;
      res.body.dateConcours = res.body.dateConcours ? dayjs(res.body.dateConcours) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((formationInitiale: IFormationInitiale) => {
        formationInitiale.dateOuverture = formationInitiale.dateOuverture ? dayjs(formationInitiale.dateOuverture) : undefined;
        formationInitiale.dateCloture = formationInitiale.dateCloture ? dayjs(formationInitiale.dateCloture) : undefined;
        formationInitiale.dateConcours = formationInitiale.dateConcours ? dayjs(formationInitiale.dateConcours) : undefined;
      });
    }
    return res;
  }
}
