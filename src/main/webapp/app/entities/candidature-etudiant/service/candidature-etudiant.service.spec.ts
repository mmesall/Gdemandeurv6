import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { Resultat } from 'app/entities/enumerations/resultat.model';
import { ICandidatureEtudiant, CandidatureEtudiant } from '../candidature-etudiant.model';

import { CandidatureEtudiantService } from './candidature-etudiant.service';

describe('CandidatureEtudiant Service', () => {
  let service: CandidatureEtudiantService;
  let httpMock: HttpTestingController;
  let elemDefault: ICandidatureEtudiant;
  let expectedResult: ICandidatureEtudiant | ICandidatureEtudiant[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CandidatureEtudiantService);
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

    it('should create a CandidatureEtudiant', () => {
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

      service.create(new CandidatureEtudiant()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CandidatureEtudiant', () => {
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

    it('should partial update a CandidatureEtudiant', () => {
      const patchObject = Object.assign(
        {
          offreFormation: 'BBBBBB',
          dateDebutOffre: currentDate.format(DATE_FORMAT),
          dateDepot: currentDate.format(DATE_FORMAT),
          resultat: 'BBBBBB',
        },
        new CandidatureEtudiant()
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

    it('should return a list of CandidatureEtudiant', () => {
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

    it('should delete a CandidatureEtudiant', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCandidatureEtudiantToCollectionIfMissing', () => {
      it('should add a CandidatureEtudiant to an empty array', () => {
        const candidatureEtudiant: ICandidatureEtudiant = { id: 123 };
        expectedResult = service.addCandidatureEtudiantToCollectionIfMissing([], candidatureEtudiant);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(candidatureEtudiant);
      });

      it('should not add a CandidatureEtudiant to an array that contains it', () => {
        const candidatureEtudiant: ICandidatureEtudiant = { id: 123 };
        const candidatureEtudiantCollection: ICandidatureEtudiant[] = [
          {
            ...candidatureEtudiant,
          },
          { id: 456 },
        ];
        expectedResult = service.addCandidatureEtudiantToCollectionIfMissing(candidatureEtudiantCollection, candidatureEtudiant);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CandidatureEtudiant to an array that doesn't contain it", () => {
        const candidatureEtudiant: ICandidatureEtudiant = { id: 123 };
        const candidatureEtudiantCollection: ICandidatureEtudiant[] = [{ id: 456 }];
        expectedResult = service.addCandidatureEtudiantToCollectionIfMissing(candidatureEtudiantCollection, candidatureEtudiant);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(candidatureEtudiant);
      });

      it('should add only unique CandidatureEtudiant to an array', () => {
        const candidatureEtudiantArray: ICandidatureEtudiant[] = [{ id: 123 }, { id: 456 }, { id: 21813 }];
        const candidatureEtudiantCollection: ICandidatureEtudiant[] = [{ id: 123 }];
        expectedResult = service.addCandidatureEtudiantToCollectionIfMissing(candidatureEtudiantCollection, ...candidatureEtudiantArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const candidatureEtudiant: ICandidatureEtudiant = { id: 123 };
        const candidatureEtudiant2: ICandidatureEtudiant = { id: 456 };
        expectedResult = service.addCandidatureEtudiantToCollectionIfMissing([], candidatureEtudiant, candidatureEtudiant2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(candidatureEtudiant);
        expect(expectedResult).toContain(candidatureEtudiant2);
      });

      it('should accept null and undefined values', () => {
        const candidatureEtudiant: ICandidatureEtudiant = { id: 123 };
        expectedResult = service.addCandidatureEtudiantToCollectionIfMissing([], null, candidatureEtudiant, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(candidatureEtudiant);
      });

      it('should return initial array if no CandidatureEtudiant is added', () => {
        const candidatureEtudiantCollection: ICandidatureEtudiant[] = [{ id: 123 }];
        expectedResult = service.addCandidatureEtudiantToCollectionIfMissing(candidatureEtudiantCollection, undefined, null);
        expect(expectedResult).toEqual(candidatureEtudiantCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
