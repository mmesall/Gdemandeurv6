import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICandidatureElev, getCandidatureElevIdentifier } from '../candidature-elev.model';

export type EntityResponseType = HttpResponse<ICandidatureElev>;
export type EntityArrayResponseType = HttpResponse<ICandidatureElev[]>;

@Injectable({ providedIn: 'root' })
export class CandidatureElevService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/candidature-elevs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(candidatureElev: ICandidatureElev): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidatureElev);
    return this.http
      .post<ICandidatureElev>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(candidatureElev: ICandidatureElev): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidatureElev);
    return this.http
      .put<ICandidatureElev>(`${this.resourceUrl}/${getCandidatureElevIdentifier(candidatureElev) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(candidatureElev: ICandidatureElev): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidatureElev);
    return this.http
      .patch<ICandidatureElev>(`${this.resourceUrl}/${getCandidatureElevIdentifier(candidatureElev) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICandidatureElev>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICandidatureElev[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCandidatureElevToCollectionIfMissing(
    candidatureElevCollection: ICandidatureElev[],
    ...candidatureElevsToCheck: (ICandidatureElev | null | undefined)[]
  ): ICandidatureElev[] {
    const candidatureElevs: ICandidatureElev[] = candidatureElevsToCheck.filter(isPresent);
    if (candidatureElevs.length > 0) {
      const candidatureElevCollectionIdentifiers = candidatureElevCollection.map(
        candidatureElevItem => getCandidatureElevIdentifier(candidatureElevItem)!
      );
      const candidatureElevsToAdd = candidatureElevs.filter(candidatureElevItem => {
        const candidatureElevIdentifier = getCandidatureElevIdentifier(candidatureElevItem);
        if (candidatureElevIdentifier == null || candidatureElevCollectionIdentifiers.includes(candidatureElevIdentifier)) {
          return false;
        }
        candidatureElevCollectionIdentifiers.push(candidatureElevIdentifier);
        return true;
      });
      return [...candidatureElevsToAdd, ...candidatureElevCollection];
    }
    return candidatureElevCollection;
  }

  protected convertDateFromClient(candidatureElev: ICandidatureElev): ICandidatureElev {
    return Object.assign({}, candidatureElev, {
      dateDebutOffre: candidatureElev.dateDebutOffre?.isValid() ? candidatureElev.dateDebutOffre.format(DATE_FORMAT) : undefined,
      dateFinOffre: candidatureElev.dateFinOffre?.isValid() ? candidatureElev.dateFinOffre.format(DATE_FORMAT) : undefined,
      dateDepot: candidatureElev.dateDepot?.isValid() ? candidatureElev.dateDepot.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((candidatureElev: ICandidatureElev) => {
        candidatureElev.dateDebutOffre = candidatureElev.dateDebutOffre ? dayjs(candidatureElev.dateDebutOffre) : undefined;
        candidatureElev.dateFinOffre = candidatureElev.dateFinOffre ? dayjs(candidatureElev.dateFinOffre) : undefined;
        candidatureElev.dateDepot = candidatureElev.dateDepot ? dayjs(candidatureElev.dateDepot) : undefined;
      });
    }
    return res;
  }
}
