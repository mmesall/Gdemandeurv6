import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IExperience, getExperienceIdentifier } from '../experience.model';

export type EntityResponseType = HttpResponse<IExperience>;
export type EntityArrayResponseType = HttpResponse<IExperience[]>;

@Injectable({ providedIn: 'root' })
export class ExperienceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/experiences');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(experience: IExperience): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(experience);
    return this.http
      .post<IExperience>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(experience: IExperience): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(experience);
    return this.http
      .put<IExperience>(`${this.resourceUrl}/${getExperienceIdentifier(experience) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(experience: IExperience): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(experience);
    return this.http
      .patch<IExperience>(`${this.resourceUrl}/${getExperienceIdentifier(experience) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IExperience>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IExperience[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addExperienceToCollectionIfMissing(
    experienceCollection: IExperience[],
    ...experiencesToCheck: (IExperience | null | undefined)[]
  ): IExperience[] {
    const experiences: IExperience[] = experiencesToCheck.filter(isPresent);
    if (experiences.length > 0) {
      const experienceCollectionIdentifiers = experienceCollection.map(experienceItem => getExperienceIdentifier(experienceItem)!);
      const experiencesToAdd = experiences.filter(experienceItem => {
        const experienceIdentifier = getExperienceIdentifier(experienceItem);
        if (experienceIdentifier == null || experienceCollectionIdentifiers.includes(experienceIdentifier)) {
          return false;
        }
        experienceCollectionIdentifiers.push(experienceIdentifier);
        return true;
      });
      return [...experiencesToAdd, ...experienceCollection];
    }
    return experienceCollection;
  }

  protected convertDateFromClient(experience: IExperience): IExperience {
    return Object.assign({}, experience, {
      dateDebut: experience.dateDebut?.isValid() ? experience.dateDebut.format(DATE_FORMAT) : undefined,
      dateFin: experience.dateFin?.isValid() ? experience.dateFin.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateDebut = res.body.dateDebut ? dayjs(res.body.dateDebut) : undefined;
      res.body.dateFin = res.body.dateFin ? dayjs(res.body.dateFin) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((experience: IExperience) => {
        experience.dateDebut = experience.dateDebut ? dayjs(experience.dateDebut) : undefined;
        experience.dateFin = experience.dateFin ? dayjs(experience.dateFin) : undefined;
      });
    }
    return res;
  }
}
