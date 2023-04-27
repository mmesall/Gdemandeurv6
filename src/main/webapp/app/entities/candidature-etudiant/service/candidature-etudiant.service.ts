import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICandidatureEtudiant, getCandidatureEtudiantIdentifier } from '../candidature-etudiant.model';

export type EntityResponseType = HttpResponse<ICandidatureEtudiant>;
export type EntityArrayResponseType = HttpResponse<ICandidatureEtudiant[]>;

@Injectable({ providedIn: 'root' })
export class CandidatureEtudiantService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/candidature-etudiants');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(candidatureEtudiant: ICandidatureEtudiant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidatureEtudiant);
    return this.http
      .post<ICandidatureEtudiant>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(candidatureEtudiant: ICandidatureEtudiant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidatureEtudiant);
    return this.http
      .put<ICandidatureEtudiant>(`${this.resourceUrl}/${getCandidatureEtudiantIdentifier(candidatureEtudiant) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(candidatureEtudiant: ICandidatureEtudiant): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(candidatureEtudiant);
    return this.http
      .patch<ICandidatureEtudiant>(`${this.resourceUrl}/${getCandidatureEtudiantIdentifier(candidatureEtudiant) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ICandidatureEtudiant>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ICandidatureEtudiant[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCandidatureEtudiantToCollectionIfMissing(
    candidatureEtudiantCollection: ICandidatureEtudiant[],
    ...candidatureEtudiantsToCheck: (ICandidatureEtudiant | null | undefined)[]
  ): ICandidatureEtudiant[] {
    const candidatureEtudiants: ICandidatureEtudiant[] = candidatureEtudiantsToCheck.filter(isPresent);
    if (candidatureEtudiants.length > 0) {
      const candidatureEtudiantCollectionIdentifiers = candidatureEtudiantCollection.map(
        candidatureEtudiantItem => getCandidatureEtudiantIdentifier(candidatureEtudiantItem)!
      );
      const candidatureEtudiantsToAdd = candidatureEtudiants.filter(candidatureEtudiantItem => {
        const candidatureEtudiantIdentifier = getCandidatureEtudiantIdentifier(candidatureEtudiantItem);
        if (candidatureEtudiantIdentifier == null || candidatureEtudiantCollectionIdentifiers.includes(candidatureEtudiantIdentifier)) {
          return false;
        }
        candidatureEtudiantCollectionIdentifiers.push(candidatureEtudiantIdentifier);
        return true;
      });
      return [...candidatureEtudiantsToAdd, ...candidatureEtudiantCollection];
    }
    return candidatureEtudiantCollection;
  }

  protected convertDateFromClient(candidatureEtudiant: ICandidatureEtudiant): ICandidatureEtudiant {
    return Object.assign({}, candidatureEtudiant, {
      dateDebutOffre: candidatureEtudiant.dateDebutOffre?.isValid() ? candidatureEtudiant.dateDebutOffre.format(DATE_FORMAT) : undefined,
      dateFinOffre: candidatureEtudiant.dateFinOffre?.isValid() ? candidatureEtudiant.dateFinOffre.format(DATE_FORMAT) : undefined,
      dateDepot: candidatureEtudiant.dateDepot?.isValid() ? candidatureEtudiant.dateDepot.format(DATE_FORMAT) : undefined,
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
      res.body.forEach((candidatureEtudiant: ICandidatureEtudiant) => {
        candidatureEtudiant.dateDebutOffre = candidatureEtudiant.dateDebutOffre ? dayjs(candidatureEtudiant.dateDebutOffre) : undefined;
        candidatureEtudiant.dateFinOffre = candidatureEtudiant.dateFinOffre ? dayjs(candidatureEtudiant.dateFinOffre) : undefined;
        candidatureEtudiant.dateDepot = candidatureEtudiant.dateDepot ? dayjs(candidatureEtudiant.dateDepot) : undefined;
      });
    }
    return res;
  }
}
