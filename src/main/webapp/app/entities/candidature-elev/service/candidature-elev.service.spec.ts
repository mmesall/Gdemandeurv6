import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { Resultat } from 'app/entities/enumerations/resultat.model';
import { ICandidatureElev, CandidatureElev } from '../candidature-elev.model';

import { CandidatureElevService } from './candidature-elev.service';

describe('CandidatureElev Service', () => {
  let service: CandidatureElevService;
  let httpMock: HttpTestingController;
  let elemDefault: ICandidatureElev;
  let expectedResult: ICandidatureElev | ICandidatureElev[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CandidatureElevService);
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

    it('should create a CandidatureElev', () => {
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

      service.create(new CandidatureElev()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CandidatureElev', () => {
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

    it('should partial update a CandidatureElev', () => {
      const patchObject = Object.assign(
        {
          offreFormation: 'BBBBBB',
          dateDebutOffre: currentDate.format(DATE_FORMAT),
          resultat: 'BBBBBB',
        },
        new CandidatureElev()
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

    it('should return a list of CandidatureElev', () => {
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

    it('should delete a CandidatureElev', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCandidatureElevToCollectionIfMissing', () => {
      it('should add a CandidatureElev to an empty array', () => {
        const candidatureElev: ICandidatureElev = { id: 123 };
        expectedResult = service.addCandidatureElevToCollectionIfMissing([], candidatureElev);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(candidatureElev);
      });

      it('should not add a CandidatureElev to an array that contains it', () => {
        const candidatureElev: ICandidatureElev = { id: 123 };
        const candidatureElevCollection: ICandidatureElev[] = [
          {
            ...candidatureElev,
          },
          { id: 456 },
        ];
        expectedResult = service.addCandidatureElevToCollectionIfMissing(candidatureElevCollection, candidatureElev);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CandidatureElev to an array that doesn't contain it", () => {
        const candidatureElev: ICandidatureElev = { id: 123 };
        const candidatureElevCollection: ICandidatureElev[] = [{ id: 456 }];
        expectedResult = service.addCandidatureElevToCollectionIfMissing(candidatureElevCollection, candidatureElev);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(candidatureElev);
      });

      it('should add only unique CandidatureElev to an array', () => {
        const candidatureElevArray: ICandidatureElev[] = [{ id: 123 }, { id: 456 }, { id: 92136 }];
        const candidatureElevCollection: ICandidatureElev[] = [{ id: 123 }];
        expectedResult = service.addCandidatureElevToCollectionIfMissing(candidatureElevCollection, ...candidatureElevArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const candidatureElev: ICandidatureElev = { id: 123 };
        const candidatureElev2: ICandidatureElev = { id: 456 };
        expectedResult = service.addCandidatureElevToCollectionIfMissing([], candidatureElev, candidatureElev2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(candidatureElev);
        expect(expectedResult).toContain(candidatureElev2);
      });

      it('should accept null and undefined values', () => {
        const candidatureElev: ICandidatureElev = { id: 123 };
        expectedResult = service.addCandidatureElevToCollectionIfMissing([], null, candidatureElev, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(candidatureElev);
      });

      it('should return initial array if no CandidatureElev is added', () => {
        const candidatureElevCollection: ICandidatureElev[] = [{ id: 123 }];
        expectedResult = service.addCandidatureElevToCollectionIfMissing(candidatureElevCollection, undefined, null);
        expect(expectedResult).toEqual(candidatureElevCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
