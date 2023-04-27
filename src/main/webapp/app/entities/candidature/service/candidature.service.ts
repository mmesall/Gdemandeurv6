import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICandidature, getCandidatureIdentifier } from '../candidature.model';

export type EntityResponseType = HttpResponse<ICandidature>;
export type EntityArrayResponseType = HttpResponse<ICandidature[]>;

@Injectable({ providedIn: 'root' })
export class CandidatureService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/candidatures');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(candidature: ICandidature): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidature);
    return this.http
      .post<ICandidature>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(candidature: ICandidature): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidature);
    return this.http
      .put<ICandidature>(`${this.resourceUrl}/${getCandidatureIdentifier(candidature) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(candidature: ICandidature): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidature);
    return this.http
      .patch<ICandidature>(`${this.resourceUrl}/${getCandidatureIdentifier(candidature) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICandidature>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICandidature[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCandidatureToCollectionIfMissing(
    candidatureCollection: ICandidature[],
    ...candidaturesToCheck: (ICandidature | null | undefined)[]
  ): ICandidature[] {
    const candidatures: ICandidature[] = candidaturesToCheck.filter(isPresent);
    if (candidatures.length > 0) {
      const candidatureCollectionIdentifiers = candidatureCollection.map(candidatureItem => getCandidatureIdentifier(candidatureItem)!);
      const candidaturesToAdd = candidatures.filter(candidatureItem => {
        const candidatureIdentifier = getCandidatureIdentifier(candidatureItem);
        if (candidatureIdentifier == null || candidatureCollectionIdentifiers.includes(candidatureIdentifier)) {
          return false;
        }
        candidatureCollectionIdentifiers.push(candidatureIdentifier);
        return true;
      });
      return [...candidaturesToAdd, ...candidatureCollection];
    }
    return candidatureCollection;
  }

  protected convertDateFromClient(candidature: ICandidature): ICandidature {
    return Object.assign({}, candidature, {
      dateDebutOffre: candidature.dateDebutOffre?.isValid() ? candidature.dateDebutOffre.format(DATE_FORMAT) : undefined,
      dateFinOffre: candidature.dateFinOffre?.isValid() ? candidature.dateFinOffre.format(DATE_FORMAT) : undefined,
      dateDepot: candidature.dateDepot?.isValid() ? candidature.dateDepot.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((candidature: ICandidature) => {
        candidature.dateDebutOffre = candidature.dateDebutOffre ? dayjs(candidature.dateDebutOffre) : undefined;
        candidature.dateFinOffre = candidature.dateFinOffre ? dayjs(candidature.dateFinOffre) : undefined;
        candidature.dateDepot = candidature.dateDepot ? dayjs(candidature.dateDepot) : undefined;
      });
    }
    return res;
  }
}
