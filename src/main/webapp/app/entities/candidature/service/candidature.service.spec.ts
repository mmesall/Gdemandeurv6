import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { Resultat } from 'app/entities/enumerations/resultat.model';
import { ICandidature, Candidature } from '../candidature.model';

import { CandidatureService } from './candidature.service';

describe('Candidature Service', () => {
  let service: CandidatureService;
  let httpMock: HttpTestingController;
  let elemDefault: ICandidature;
  let expectedResult: ICandidature | ICandidature[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CandidatureService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      offreFormation: NomFiliere.AGRI_ELEVAGE,
      dateDebutOffre: currentDate,
      dateFinOffre: currentDate,
      dateDepot: currentDate,
      resultat: Resultat.SOUMIS,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateDebutOffre: currentDate.format(DATE_FORMAT),
          dateFinOffre: currentDate.format(DATE_FORMAT),
          dateDepot: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Candidature', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateDebutOffre: currentDate.format(DATE_FORMAT),
          dateFinOffre: currentDate.format(DATE_FORMAT),
          dateDepot: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateDebutOffre: currentDate,
          dateFinOffre: currentDate,
          dateDepot: currentDate,
        },
        returnedFromService
      );

      service.create(new Candidature()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Candidature', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          offreFormation: 'BBBBBB',
          dateDebutOffre: currentDate.format(DATE_FORMAT),
          dateFinOffre: currentDate.format(DATE_FORMAT),
          dateDepot: currentDate.format(DATE_FORMAT),
          resultat: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateDebutOffre: currentDate,
          dateFinOffre: currentDate,
          dateDepot: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Candidature', () => {
      const patchObject = Object.assign(
        {
          offreFormation: 'BBBBBB',
          dateDebutOffre: currentDate.format(DATE_FORMAT),
          resultat: 'BBBBBB',
        },
        new Candidature()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateDebutOffre: currentDate,
          dateFinOffre: currentDate,
          dateDepot: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Candidature', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          offreFormation: 'BBBBBB',
          dateDebutOffre: currentDate.format(DATE_FORMAT),
          dateFinOffre: currentDate.format(DATE_FORMAT),
          dateDepot: currentDate.format(DATE_FORMAT),
          resultat: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateDebutOffre: currentDate,
          dateFinOffre: currentDate,
          dateDepot: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Candidature', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCandidatureToCollectionIfMissing', () => {
      it('should add a Candidature to an empty array', () => {
        const candidature: ICandidature = { id: 123 };
        expectedResult = service.addCandidatureToCollectionIfMissing([], candidature);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(candidature);
      });

      it('should not add a Candidature to an array that contains it', () => {
        const candidature: ICandidature = { id: 123 };
        const candidatureCollection: ICandidature[] = [
          {
            ...candidature,
          },
          { id: 456 },
        ];
        expectedResult = service.addCandidatureToCollectionIfMissing(candidatureCollection, candidature);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Candidature to an array that doesn't contain it", () => {
        const candidature: ICandidature = { id: 123 };
        const candidatureCollection: ICandidature[] = [{ id: 456 }];
        expectedResult = service.addCandidatureToCollectionIfMissing(candidatureCollection, candidature);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(candidature);
      });

      it('should add only unique Candidature to an array', () => {
        const candidatureArray: ICandidature[] = [{ id: 123 }, { id: 456 }, { id: 77728 }];
        const candidatureCollection: ICandidature[] = [{ id: 123 }];
        expectedResult = service.addCandidatureToCollectionIfMissing(candidatureCollection, ...candidatureArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const candidature: ICandidature = { id: 123 };
        const candidature2: ICandidature = { id: 456 };
        expectedResult = service.addCandidatureToCollectionIfMissing([], candidature, candidature2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(candidature);
        expect(expectedResult).toContain(candidature2);
      });

      it('should accept null and undefined values', () => {
        const candidature: ICandidature = { id: 123 };
        expectedResult = service.addCandidatureToCollectionIfMissing([], null, candidature, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(candidature);
      });

      it('should return initial array if no Candidature is added', () => {
        const candidatureCollection: ICandidature[] = [{ id: 123 }];
        expectedResult = service.addCandidatureToCollectionIfMissing(candidatureCollection, undefined, null);
        expect(expectedResult).toEqual(candidatureCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
