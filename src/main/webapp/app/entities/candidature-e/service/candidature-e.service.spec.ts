import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { Resultat } from 'app/entities/enumerations/resultat.model';
import { ICandidatureE, CandidatureE } from '../candidature-e.model';

import { CandidatureEService } from './candidature-e.service';

describe('CandidatureE Service', () => {
  let service: CandidatureEService;
  let httpMock: HttpTestingController;
  let elemDefault: ICandidatureE;
  let expectedResult: ICandidatureE | ICandidatureE[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CandidatureEService);
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

    it('should create a CandidatureE', () => {
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

      service.create(new CandidatureE()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CandidatureE', () => {
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

    it('should partial update a CandidatureE', () => {
      const patchObject = Object.assign(
        {
          dateDebutOffre: currentDate.format(DATE_FORMAT),
          dateFinOffre: currentDate.format(DATE_FORMAT),
        },
        new CandidatureE()
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

    it('should return a list of CandidatureE', () => {
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

    it('should delete a CandidatureE', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCandidatureEToCollectionIfMissing', () => {
      it('should add a CandidatureE to an empty array', () => {
        const candidatureE: ICandidatureE = { id: 123 };
        expectedResult = service.addCandidatureEToCollectionIfMissing([], candidatureE);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(candidatureE);
      });

      it('should not add a CandidatureE to an array that contains it', () => {
        const candidatureE: ICandidatureE = { id: 123 };
        const candidatureECollection: ICandidatureE[] = [
          {
            ...candidatureE,
          },
          { id: 456 },
        ];
        expectedResult = service.addCandidatureEToCollectionIfMissing(candidatureECollection, candidatureE);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CandidatureE to an array that doesn't contain it", () => {
        const candidatureE: ICandidatureE = { id: 123 };
        const candidatureECollection: ICandidatureE[] = [{ id: 456 }];
        expectedResult = service.addCandidatureEToCollectionIfMissing(candidatureECollection, candidatureE);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(candidatureE);
      });

      it('should add only unique CandidatureE to an array', () => {
        const candidatureEArray: ICandidatureE[] = [{ id: 123 }, { id: 456 }, { id: 51559 }];
        const candidatureECollection: ICandidatureE[] = [{ id: 123 }];
        expectedResult = service.addCandidatureEToCollectionIfMissing(candidatureECollection, ...candidatureEArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const candidatureE: ICandidatureE = { id: 123 };
        const candidatureE2: ICandidatureE = { id: 456 };
        expectedResult = service.addCandidatureEToCollectionIfMissing([], candidatureE, candidatureE2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(candidatureE);
        expect(expectedResult).toContain(candidatureE2);
      });

      it('should accept null and undefined values', () => {
        const candidatureE: ICandidatureE = { id: 123 };
        expectedResult = service.addCandidatureEToCollectionIfMissing([], null, candidatureE, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(candidatureE);
      });

      it('should return initial array if no CandidatureE is added', () => {
        const candidatureECollection: ICandidatureE[] = [{ id: 123 }];
        expectedResult = service.addCandidatureEToCollectionIfMissing(candidatureECollection, undefined, null);
        expect(expectedResult).toEqual(candidatureECollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
