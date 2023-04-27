import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICandidatureProf, getCandidatureProfIdentifier } from '../candidature-prof.model';

export type EntityResponseType = HttpResponse<ICandidatureProf>;
export type EntityArrayResponseType = HttpResponse<ICandidatureProf[]>;

@Injectable({ providedIn: 'root' })
export class CandidatureProfService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/candidature-profs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(candidatureProf: ICandidatureProf): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidatureProf);
    return this.http
      .post<ICandidatureProf>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(candidatureProf: ICandidatureProf): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidatureProf);
    return this.http
      .put<ICandidatureProf>(`${this.resourceUrl}/${getCandidatureProfIdentifier(candidatureProf) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(candidatureProf: ICandidatureProf): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidatureProf);
    return this.http
      .patch<ICandidatureProf>(`${this.resourceUrl}/${getCandidatureProfIdentifier(candidatureProf) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICandidatureProf>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICandidatureProf[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCandidatureProfToCollectionIfMissing(
    candidatureProfCollection: ICandidatureProf[],
    ...candidatureProfsToCheck: (ICandidatureProf | null | undefined)[]
  ): ICandidatureProf[] {
    const candidatureProfs: ICandidatureProf[] = candidatureProfsToCheck.filter(isPresent);
    if (candidatureProfs.length > 0) {
      const candidatureProfCollectionIdentifiers = candidatureProfCollection.map(
        candidatureProfItem => getCandidatureProfIdentifier(candidatureProfItem)!
      );
      const candidatureProfsToAdd = candidatureProfs.filter(candidatureProfItem => {
        const candidatureProfIdentifier = getCandidatureProfIdentifier(candidatureProfItem);
        if (candidatureProfIdentifier == null || candidatureProfCollectionIdentifiers.includes(candidatureProfIdentifier)) {
          return false;
        }
        candidatureProfCollectionIdentifiers.push(candidatureProfIdentifier);
        return true;
      });
      return [...candidatureProfsToAdd, ...candidatureProfCollection];
    }
    return candidatureProfCollection;
  }

  protected convertDateFromClient(candidatureProf: ICandidatureProf): ICandidatureProf {
    return Object.assign({}, candidatureProf, {
      dateDebutOffre: candidatureProf.dateDebutOffre?.isValid() ? candidatureProf.dateDebutOffre.format(DATE_FORMAT) : undefined,
      dateFinOffre: candidatureProf.dateFinOffre?.isValid() ? candidatureProf.dateFinOffre.format(DATE_FORMAT) : undefined,
      dateDepot: candidatureProf.dateDepot?.isValid() ? candidatureProf.dateDepot.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((candidatureProf: ICandidatureProf) => {
        candidatureProf.dateDebutOffre = candidatureProf.dateDebutOffre ? dayjs(candidatureProf.dateDebutOffre) : undefined;
        candidatureProf.dateFinOffre = candidatureProf.dateFinOffre ? dayjs(candidatureProf.dateFinOffre) : undefined;
        candidatureProf.dateDepot = candidatureProf.dateDepot ? dayjs(candidatureProf.dateDepot) : undefined;
      });
    }
    return res;
  }
}
