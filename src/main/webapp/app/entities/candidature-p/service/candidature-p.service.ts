import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICandidatureP, getCandidaturePIdentifier } from '../candidature-p.model';

export type EntityResponseType = HttpResponse<ICandidatureP>;
export type EntityArrayResponseType = HttpResponse<ICandidatureP[]>;

@Injectable({ providedIn: 'root' })
export class CandidaturePService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/candidature-ps');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(candidatureP: ICandidatureP): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidatureP);
    return this.http
      .post<ICandidatureP>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(candidatureP: ICandidatureP): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidatureP);
    return this.http
      .put<ICandidatureP>(`${this.resourceUrl}/${getCandidaturePIdentifier(candidatureP) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(candidatureP: ICandidatureP): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidatureP);
    return this.http
      .patch<ICandidatureP>(`${this.resourceUrl}/${getCandidaturePIdentifier(candidatureP) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICandidatureP>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICandidatureP[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCandidaturePToCollectionIfMissing(
    candidaturePCollection: ICandidatureP[],
    ...candidaturePSToCheck: (ICandidatureP | null | undefined)[]
  ): ICandidatureP[] {
    const candidaturePS: ICandidatureP[] = candidaturePSToCheck.filter(isPresent);
    if (candidaturePS.length > 0) {
      const candidaturePCollectionIdentifiers = candidaturePCollection.map(
        candidaturePItem => getCandidaturePIdentifier(candidaturePItem)!
      );
      const candidaturePSToAdd = candidaturePS.filter(candidaturePItem => {
        const candidaturePIdentifier = getCandidaturePIdentifier(candidaturePItem);
        if (candidaturePIdentifier == null || candidaturePCollectionIdentifiers.includes(candidaturePIdentifier)) {
          return false;
        }
        candidaturePCollectionIdentifiers.push(candidaturePIdentifier);
        return true;
      });
      return [...candidaturePSToAdd, ...candidaturePCollection];
    }
    return candidaturePCollection;
  }

  protected convertDateFromClient(candidatureP: ICandidatureP): ICandidatureP {
    return Object.assign({}, candidatureP, {
      dateDebutOffre: candidatureP.dateDebutOffre?.isValid() ? candidatureP.dateDebutOffre.format(DATE_FORMAT) : undefined,
      dateFinOffre: candidatureP.dateFinOffre?.isValid() ? candidatureP.dateFinOffre.format(DATE_FORMAT) : undefined,
      dateDepot: candidatureP.dateDepot?.isValid() ? candidatureP.dateDepot.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateDebutOffre = res.body.dateDebutOffre ? dayjs(res.body.dateDebutOffre) : undefined;
      res.body.dateFinOffre = res.body.dateFinOffre ? dayjs(res.body.dateFinOffre) : undefined;
      res.body.dateDepot = res.body.dateDepot ? dayjs(res.body.dateDepot) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((candidatureP: ICandidatureP) => {
        candidatureP.dateDebutOffre = candidatureP.dateDebutOffre ? dayjs(candidatureP.dateDebutOffre) : undefined;
        candidatureP.dateFinOffre = candidatureP.dateFinOffre ? dayjs(candidatureP.dateFinOffre) : undefined;
        candidatureP.dateDepot = candidatureP.dateDepot ? dayjs(candidatureP.dateDepot) : undefined;
      });
    }
    return res;
  }
}
