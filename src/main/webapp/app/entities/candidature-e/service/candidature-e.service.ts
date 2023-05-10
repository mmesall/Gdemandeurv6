import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICandidatureE, getCandidatureEIdentifier } from '../candidature-e.model';

export type EntityResponseType = HttpResponse<ICandidatureE>;
export type EntityArrayResponseType = HttpResponse<ICandidatureE[]>;

@Injectable({ providedIn: 'root' })
export class CandidatureEService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/candidature-es');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(candidatureE: ICandidatureE): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidatureE);
    return this.http
      .post<ICandidatureE>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(candidatureE: ICandidatureE): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidatureE);
    return this.http
      .put<ICandidatureE>(`${this.resourceUrl}/${getCandidatureEIdentifier(candidatureE) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(candidatureE: ICandidatureE): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidatureE);
    return this.http
      .patch<ICandidatureE>(`${this.resourceUrl}/${getCandidatureEIdentifier(candidatureE) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICandidatureE>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICandidatureE[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCandidatureEToCollectionIfMissing(
    candidatureECollection: ICandidatureE[],
    ...candidatureESToCheck: (ICandidatureE | null | undefined)[]
  ): ICandidatureE[] {
    const candidatureES: ICandidatureE[] = candidatureESToCheck.filter(isPresent);
    if (candidatureES.length > 0) {
      const candidatureECollectionIdentifiers = candidatureECollection.map(
        candidatureEItem => getCandidatureEIdentifier(candidatureEItem)!
      );
      const candidatureESToAdd = candidatureES.filter(candidatureEItem => {
        const candidatureEIdentifier = getCandidatureEIdentifier(candidatureEItem);
        if (candidatureEIdentifier == null || candidatureECollectionIdentifiers.includes(candidatureEIdentifier)) {
          return false;
        }
        candidatureECollectionIdentifiers.push(candidatureEIdentifier);
        return true;
      });
      return [...candidatureESToAdd, ...candidatureECollection];
    }
    return candidatureECollection;
  }

  protected convertDateFromClient(candidatureE: ICandidatureE): ICandidatureE {
    return Object.assign({}, candidatureE, {
      dateDebutOffre: candidatureE.dateDebutOffre?.isValid() ? candidatureE.dateDebutOffre.format(DATE_FORMAT) : undefined,
      dateFinOffre: candidatureE.dateFinOffre?.isValid() ? candidatureE.dateFinOffre.format(DATE_FORMAT) : undefined,
      dateDepot: candidatureE.dateDepot?.isValid() ? candidatureE.dateDepot.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((candidatureE: ICandidatureE) => {
        candidatureE.dateDebutOffre = candidatureE.dateDebutOffre ? dayjs(candidatureE.dateDebutOffre) : undefined;
        candidatureE.dateFinOffre = candidatureE.dateFinOffre ? dayjs(candidatureE.dateFinOffre) : undefined;
        candidatureE.dateDepot = candidatureE.dateDepot ? dayjs(candidatureE.dateDepot) : undefined;
      });
    }
    return res;
  }
}
