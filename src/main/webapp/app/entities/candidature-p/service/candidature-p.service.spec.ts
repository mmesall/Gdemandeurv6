import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { Resultat } from 'app/entities/enumerations/resultat.model';
import { ICandidatureP, CandidatureP } from '../candidature-p.model';

import { CandidaturePService } from './candidature-p.service';

describe('CandidatureP Service', () => {
  let service: CandidaturePService;
  let httpMock: HttpTestingController;
  let elemDefault: ICandidatureP;
  let expectedResult: ICandidatureP | ICandidatureP[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CandidaturePService);
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

    it('should create a CandidatureP', () => {
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

      service.create(new CandidatureP()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CandidatureP', () => {
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

    it('should partial update a CandidatureP', () => {
      const patchObject = Object.assign(
        {
          offreFormation: 'BBBBBB',
          dateDepot: currentDate.format(DATE_FORMAT),
          resultat: 'BBBBBB',
        },
        new CandidatureP()
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

    it('should return a list of CandidatureP', () => {
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

    it('should delete a CandidatureP', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCandidaturePToCollectionIfMissing', () => {
      it('should add a CandidatureP to an empty array', () => {
        const candidatureP: ICandidatureP = { id: 123 };
        expectedResult = service.addCandidaturePToCollectionIfMissing([], candidatureP);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(candidatureP);
      });

      it('should not add a CandidatureP to an array that contains it', () => {
        const candidatureP: ICandidatureP = { id: 123 };
        const candidaturePCollection: ICandidatureP[] = [
          {
            ...candidatureP,
          },
          { id: 456 },
        ];
        expectedResult = service.addCandidaturePToCollectionIfMissing(candidaturePCollection, candidatureP);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CandidatureP to an array that doesn't contain it", () => {
        const candidatureP: ICandidatureP = { id: 123 };
        const candidaturePCollection: ICandidatureP[] = [{ id: 456 }];
        expectedResult = service.addCandidaturePToCollectionIfMissing(candidaturePCollection, candidatureP);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(candidatureP);
      });

      it('should add only unique CandidatureP to an array', () => {
        const candidaturePArray: ICandidatureP[] = [{ id: 123 }, { id: 456 }, { id: 33052 }];
        const candidaturePCollection: ICandidatureP[] = [{ id: 123 }];
        expectedResult = service.addCandidaturePToCollectionIfMissing(candidaturePCollection, ...candidaturePArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const candidatureP: ICandidatureP = { id: 123 };
        const candidatureP2: ICandidatureP = { id: 456 };
        expectedResult = service.addCandidaturePToCollectionIfMissing([], candidatureP, candidatureP2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(candidatureP);
        expect(expectedResult).toContain(candidatureP2);
      });

      it('should accept null and undefined values', () => {
        const candidatureP: ICandidatureP = { id: 123 };
        expectedResult = service.addCandidaturePToCollectionIfMissing([], null, candidatureP, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(candidatureP);
      });

      it('should return initial array if no CandidatureP is added', () => {
        const candidaturePCollection: ICandidatureP[] = [{ id: 123 }];
        expectedResult = service.addCandidaturePToCollectionIfMissing(candidaturePCollection, undefined, null);
        expect(expectedResult).toEqual(candidaturePCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
