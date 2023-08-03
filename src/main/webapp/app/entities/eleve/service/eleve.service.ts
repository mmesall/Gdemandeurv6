import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEleve, NewEleve } from '../eleve.model';

export type PartialUpdateEleve = Partial<IEleve> & Pick<IEleve, 'id'>;

type RestOf<T extends IEleve | NewEleve> = Omit<T, 'dateNaiss'> & {
  dateNaiss?: string | null;
};

export type RestEleve = RestOf<IEleve>;

export type NewRestEleve = RestOf<NewEleve>;

export type PartialUpdateRestEleve = RestOf<PartialUpdateEleve>;

export type EntityResponseType = HttpResponse<IEleve>;
export type EntityArrayResponseType = HttpResponse<IEleve[]>;

@Injectable({ providedIn: 'root' })
export class EleveService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/eleves');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(eleve: NewEleve): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eleve);
    return this.http.post<RestEleve>(this.resourceUrl, copy, { observe: 'response' }).pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(eleve: IEleve): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eleve);
    return this.http
      .put<RestEleve>(`${this.resourceUrl}/${this.getEleveIdentifier(eleve)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(eleve: PartialUpdateEleve): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(eleve);
    return this.http
      .patch<RestEleve>(`${this.resourceUrl}/${this.getEleveIdentifier(eleve)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEleve>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEleve[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getEleveIdentifier(eleve: Pick<IEleve, 'id'>): number {
    return eleve.id;
  }

  compareEleve(o1: Pick<IEleve, 'id'> | null, o2: Pick<IEleve, 'id'> | null): boolean {
    return o1 && o2 ? this.getEleveIdentifier(o1) === this.getEleveIdentifier(o2) : o1 === o2;
  }

  addEleveToCollectionIfMissing<Type extends Pick<IEleve, 'id'>>(
    eleveCollection: Type[],
    ...elevesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const eleves: Type[] = elevesToCheck.filter(isPresent);
    if (eleves.length > 0) {
      const eleveCollectionIdentifiers = eleveCollection.map(eleveItem => this.getEleveIdentifier(eleveItem)!);
      const elevesToAdd = eleves.filter(eleveItem => {
        const eleveIdentifier = this.getEleveIdentifier(eleveItem);
        if (eleveCollectionIdentifiers.includes(eleveIdentifier)) {
          return false;
        }
        eleveCollectionIdentifiers.push(eleveIdentifier);
        return true;
      });
      return [...elevesToAdd, ...eleveCollection];
    }
    return eleveCollection;
  }

  protected convertDateFromClient<T extends IEleve | NewEleve | PartialUpdateEleve>(eleve: T): RestOf<T> {
    return {
      ...eleve,
      dateNaiss: eleve.dateNaiss?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restEleve: RestEleve): IEleve {
    return {
      ...restEleve,
      dateNaiss: restEleve.dateNaiss ? dayjs(restEleve.dateNaiss) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEleve>): HttpResponse<IEleve> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEleve[]>): HttpResponse<IEleve[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
