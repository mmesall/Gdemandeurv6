import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Admission } from 'app/entities/enumerations/admission.model';
import { DiplomeRequis } from 'app/entities/enumerations/diplome-requis.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { NomSerie } from 'app/entities/enumerations/nom-serie.model';
import { CFP } from 'app/entities/enumerations/cfp.model';
import { LYCEE } from 'app/entities/enumerations/lycee.model';
import { DiplomeObtenu } from 'app/entities/enumerations/diplome-obtenu.model';
import { IFormationInitiale, FormationInitiale } from '../formation-initiale.model';

import { FormationInitialeService } from './formation-initiale.service';

describe('FormationInitiale Service', () => {
  let service: FormationInitialeService;
  let httpMock: HttpTestingController;
  let elemDefault: IFormationInitiale;
  let expectedResult: IFormationInitiale | IFormationInitiale[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FormationInitialeService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      nomFormationI: 'AAAAAAA',
      duree: 'AAAAAAA',
      admission: Admission.CONCOURS,
      diplomeRequis: DiplomeRequis.ATTESTATION,
      niveauEtude: NiveauEtude.Cinquieme,
      ficheFormationContentType: 'image/png',
      ficheFormation: 'AAAAAAA',
      filiere: NomFiliere.AGRI_ELEVAGE,
      serie: NomSerie.STEG,
      cfp: CFP.CEDT_G15,
      lycee: LYCEE.LTID_DAKAR,
      nomConcours: 'AAAAAAA',
      dateOuverture: currentDate,
      dateCloture: currentDate,
      dateConcours: currentDate,
      nomDiplome: DiplomeObtenu.CPS,
      nomDebouche: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateOuverture: currentDate.format(DATE_FORMAT),
          dateCloture: currentDate.format(DATE_FORMAT),
          dateConcours: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a FormationInitiale', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateOuverture: currentDate.format(DATE_FORMAT),
          dateCloture: currentDate.format(DATE_FORMAT),
          dateConcours: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateOuverture: currentDate,
          dateCloture: currentDate,
          dateConcours: currentDate,
        },
        returnedFromService
      );

      service.create(new FormationInitiale()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FormationInitiale', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomFormationI: 'BBBBBB',
          duree: 'BBBBBB',
          admission: 'BBBBBB',
          diplomeRequis: 'BBBBBB',
          niveauEtude: 'BBBBBB',
          ficheFormation: 'BBBBBB',
          filiere: 'BBBBBB',
          serie: 'BBBBBB',
          cfp: 'BBBBBB',
          lycee: 'BBBBBB',
          nomConcours: 'BBBBBB',
          dateOuverture: currentDate.format(DATE_FORMAT),
          dateCloture: currentDate.format(DATE_FORMAT),
          dateConcours: currentDate.format(DATE_FORMAT),
          nomDiplome: 'BBBBBB',
          nomDebouche: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateOuverture: currentDate,
          dateCloture: currentDate,
          dateConcours: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FormationInitiale', () => {
      const patchObject = Object.assign(
        {
          niveauEtude: 'BBBBBB',
          serie: 'BBBBBB',
          dateCloture: currentDate.format(DATE_FORMAT),
        },
        new FormationInitiale()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateOuverture: currentDate,
          dateCloture: currentDate,
          dateConcours: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FormationInitiale', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomFormationI: 'BBBBBB',
          duree: 'BBBBBB',
          admission: 'BBBBBB',
          diplomeRequis: 'BBBBBB',
          niveauEtude: 'BBBBBB',
          ficheFormation: 'BBBBBB',
          filiere: 'BBBBBB',
          serie: 'BBBBBB',
          cfp: 'BBBBBB',
          lycee: 'BBBBBB',
          nomConcours: 'BBBBBB',
          dateOuverture: currentDate.format(DATE_FORMAT),
          dateCloture: currentDate.format(DATE_FORMAT),
          dateConcours: currentDate.format(DATE_FORMAT),
          nomDiplome: 'BBBBBB',
          nomDebouche: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateOuverture: currentDate,
          dateCloture: currentDate,
          dateConcours: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a FormationInitiale', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFormationInitialeToCollectionIfMissing', () => {
      it('should add a FormationInitiale to an empty array', () => {
        const formationInitiale: IFormationInitiale = { id: 123 };
        expectedResult = service.addFormationInitialeToCollectionIfMissing([], formationInitiale);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(formationInitiale);
      });

      it('should not add a FormationInitiale to an array that contains it', () => {
        const formationInitiale: IFormationInitiale = { id: 123 };
        const formationInitialeCollection: IFormationInitiale[] = [
          {
            ...formationInitiale,
          },
          { id: 456 },
        ];
        expectedResult = service.addFormationInitialeToCollectionIfMissing(formationInitialeCollection, formationInitiale);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FormationInitiale to an array that doesn't contain it", () => {
        const formationInitiale: IFormationInitiale = { id: 123 };
        const formationInitialeCollection: IFormationInitiale[] = [{ id: 456 }];
        expectedResult = service.addFormationInitialeToCollectionIfMissing(formationInitialeCollection, formationInitiale);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(formationInitiale);
      });

      it('should add only unique FormationInitiale to an array', () => {
        const formationInitialeArray: IFormationInitiale[] = [{ id: 123 }, { id: 456 }, { id: 25039 }];
        const formationInitialeCollection: IFormationInitiale[] = [{ id: 123 }];
        expectedResult = service.addFormationInitialeToCollectionIfMissing(formationInitialeCollection, ...formationInitialeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const formationInitiale: IFormationInitiale = { id: 123 };
        const formationInitiale2: IFormationInitiale = { id: 456 };
        expectedResult = service.addFormationInitialeToCollectionIfMissing([], formationInitiale, formationInitiale2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(formationInitiale);
        expect(expectedResult).toContain(formationInitiale2);
      });

      it('should accept null and undefined values', () => {
        const formationInitiale: IFormationInitiale = { id: 123 };
        expectedResult = service.addFormationInitialeToCollectionIfMissing([], null, formationInitiale, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(formationInitiale);
      });

      it('should return initial array if no FormationInitiale is added', () => {
        const formationInitialeCollection: IFormationInitiale[] = [{ id: 123 }];
        expectedResult = service.addFormationInitialeToCollectionIfMissing(formationInitialeCollection, undefined, null);
        expect(expectedResult).toEqual(formationInitialeCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
