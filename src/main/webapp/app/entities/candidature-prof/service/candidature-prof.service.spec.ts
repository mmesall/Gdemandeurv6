import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { Resultat } from 'app/entities/enumerations/resultat.model';
import { ICandidatureProf, CandidatureProf } from '../candidature-prof.model';

import { CandidatureProfService } from './candidature-prof.service';

describe('CandidatureProf Service', () => {
  let service: CandidatureProfService;
  let httpMock: HttpTestingController;
  let elemDefault: ICandidatureProf;
  let expectedResult: ICandidatureProf | ICandidatureProf[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CandidatureProfService);
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

    it('should create a CandidatureProf', () => {
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

      service.create(new CandidatureProf()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CandidatureProf', () => {
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

    it('should partial update a CandidatureProf', () => {
      const patchObject = Object.assign(
        {
          offreFormation: 'BBBBBB',
          dateDebutOffre: currentDate.format(DATE_FORMAT),
          dateDepot: currentDate.format(DATE_FORMAT),
        },
        new CandidatureProf()
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

    it('should return a list of CandidatureProf', () => {
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

    it('should delete a CandidatureProf', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCandidatureProfToCollectionIfMissing', () => {
      it('should add a CandidatureProf to an empty array', () => {
        const candidatureProf: ICandidatureProf = { id: 123 };
        expectedResult = service.addCandidatureProfToCollectionIfMissing([], candidatureProf);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(candidatureProf);
      });

      it('should not add a CandidatureProf to an array that contains it', () => {
        const candidatureProf: ICandidatureProf = { id: 123 };
        const candidatureProfCollection: ICandidatureProf[] = [
          {
            ...candidatureProf,
          },
          { id: 456 },
        ];
        expectedResult = service.addCandidatureProfToCollectionIfMissing(candidatureProfCollection, candidatureProf);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CandidatureProf to an array that doesn't contain it", () => {
        const candidatureProf: ICandidatureProf = { id: 123 };
        const candidatureProfCollection: ICandidatureProf[] = [{ id: 456 }];
        expectedResult = service.addCandidatureProfToCollectionIfMissing(candidatureProfCollection, candidatureProf);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(candidatureProf);
      });

      it('should add only unique CandidatureProf to an array', () => {
        const candidatureProfArray: ICandidatureProf[] = [{ id: 123 }, { id: 456 }, { id: 93177 }];
        const candidatureProfCollection: ICandidatureProf[] = [{ id: 123 }];
        expectedResult = service.addCandidatureProfToCollectionIfMissing(candidatureProfCollection, ...candidatureProfArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const candidatureProf: ICandidatureProf = { id: 123 };
        const candidatureProf2: ICandidatureProf = { id: 456 };
        expectedResult = service.addCandidatureProfToCollectionIfMissing([], candidatureProf, candidatureProf2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(candidatureProf);
        expect(expectedResult).toContain(candidatureProf2);
      });

      it('should accept null and undefined values', () => {
        const candidatureProf: ICandidatureProf = { id: 123 };
        expectedResult = service.addCandidatureProfToCollectionIfMissing([], null, candidatureProf, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(candidatureProf);
      });

      it('should return initial array if no CandidatureProf is added', () => {
        const candidatureProfCollection: ICandidatureProf[] = [{ id: 123 }];
        expectedResult = service.addCandidatureProfToCollectionIfMissing(candidatureProfCollection, undefined, null);
        expect(expectedResult).toEqual(candidatureProfCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
