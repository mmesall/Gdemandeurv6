import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { NomRegion } from 'app/entities/enumerations/nom-region.model';
import { NomDepartement } from 'app/entities/enumerations/nom-departement.model';
import { TypePiece } from 'app/entities/enumerations/type-piece.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { NomSerie } from 'app/entities/enumerations/nom-serie.model';
import { DiplomeRequis } from 'app/entities/enumerations/diplome-requis.model';
import { IDossier, Dossier } from '../dossier.model';

import { DossierService } from './dossier.service';

describe('Dossier Service', () => {
  let service: DossierService;
  let httpMock: HttpTestingController;
  let elemDefault: IDossier;
  let expectedResult: IDossier | IDossier[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DossierService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      numDossier: 'AAAAAAA',
      dateNaiss: currentDate,
      prenom: 'AAAAAAA',
      nom: 'AAAAAAA',
      nomUtilisateur: 'AAAAAAA',
      regionNaiss: NomRegion.DAKAR,
      departementNaiss: NomDepartement.DAKAR,
      lieuNaiss: 'AAAAAAA',
      typePiece: TypePiece.CNI,
      numeroPiece: 0,
      sexe: Sexe.HOMME,
      regionResidence: NomRegion.DAKAR,
      depResidence: NomDepartement.DAKAR,
      adresseResidence: 'AAAAAAA',
      telephone1: 'AAAAAAA',
      telephone2: 'AAAAAAA',
      email: 'AAAAAAA',
      niveauFormation: NiveauEtude.Cinquieme,
      specialite1: NomFiliere.AGRI_ELEVAGE,
      specialite2: NomSerie.STEG,
      diplomeRequis: DiplomeRequis.ATTESTATION,
      cvContentType: 'image/png',
      cv: 'AAAAAAA',
      lettreMotivation: 'AAAAAAA',
      profession: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateNaiss: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Dossier', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateNaiss: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateNaiss: currentDate,
        },
        returnedFromService
      );

      service.create(new Dossier()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Dossier', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numDossier: 'BBBBBB',
          dateNaiss: currentDate.format(DATE_FORMAT),
          prenom: 'BBBBBB',
          nom: 'BBBBBB',
          nomUtilisateur: 'BBBBBB',
          regionNaiss: 'BBBBBB',
          departementNaiss: 'BBBBBB',
          lieuNaiss: 'BBBBBB',
          typePiece: 'BBBBBB',
          numeroPiece: 1,
          sexe: 'BBBBBB',
          regionResidence: 'BBBBBB',
          depResidence: 'BBBBBB',
          adresseResidence: 'BBBBBB',
          telephone1: 'BBBBBB',
          telephone2: 'BBBBBB',
          email: 'BBBBBB',
          niveauFormation: 'BBBBBB',
          specialite1: 'BBBBBB',
          specialite2: 'BBBBBB',
          diplomeRequis: 'BBBBBB',
          cv: 'BBBBBB',
          lettreMotivation: 'BBBBBB',
          profession: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateNaiss: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Dossier', () => {
      const patchObject = Object.assign(
        {
          numDossier: 'BBBBBB',
          prenom: 'BBBBBB',
          departementNaiss: 'BBBBBB',
          adresseResidence: 'BBBBBB',
          telephone1: 'BBBBBB',
          telephone2: 'BBBBBB',
          email: 'BBBBBB',
          specialite1: 'BBBBBB',
          diplomeRequis: 'BBBBBB',
          profession: 'BBBBBB',
        },
        new Dossier()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateNaiss: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Dossier', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          numDossier: 'BBBBBB',
          dateNaiss: currentDate.format(DATE_FORMAT),
          prenom: 'BBBBBB',
          nom: 'BBBBBB',
          nomUtilisateur: 'BBBBBB',
          regionNaiss: 'BBBBBB',
          departementNaiss: 'BBBBBB',
          lieuNaiss: 'BBBBBB',
          typePiece: 'BBBBBB',
          numeroPiece: 1,
          sexe: 'BBBBBB',
          regionResidence: 'BBBBBB',
          depResidence: 'BBBBBB',
          adresseResidence: 'BBBBBB',
          telephone1: 'BBBBBB',
          telephone2: 'BBBBBB',
          email: 'BBBBBB',
          niveauFormation: 'BBBBBB',
          specialite1: 'BBBBBB',
          specialite2: 'BBBBBB',
          diplomeRequis: 'BBBBBB',
          cv: 'BBBBBB',
          lettreMotivation: 'BBBBBB',
          profession: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateNaiss: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Dossier', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDossierToCollectionIfMissing', () => {
      it('should add a Dossier to an empty array', () => {
        const dossier: IDossier = { id: 123 };
        expectedResult = service.addDossierToCollectionIfMissing([], dossier);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dossier);
      });

      it('should not add a Dossier to an array that contains it', () => {
        const dossier: IDossier = { id: 123 };
        const dossierCollection: IDossier[] = [
          {
            ...dossier,
          },
          { id: 456 },
        ];
        expectedResult = service.addDossierToCollectionIfMissing(dossierCollection, dossier);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Dossier to an array that doesn't contain it", () => {
        const dossier: IDossier = { id: 123 };
        const dossierCollection: IDossier[] = [{ id: 456 }];
        expectedResult = service.addDossierToCollectionIfMissing(dossierCollection, dossier);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dossier);
      });

      it('should add only unique Dossier to an array', () => {
        const dossierArray: IDossier[] = [{ id: 123 }, { id: 456 }, { id: 12914 }];
        const dossierCollection: IDossier[] = [{ id: 123 }];
        expectedResult = service.addDossierToCollectionIfMissing(dossierCollection, ...dossierArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dossier: IDossier = { id: 123 };
        const dossier2: IDossier = { id: 456 };
        expectedResult = service.addDossierToCollectionIfMissing([], dossier, dossier2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dossier);
        expect(expectedResult).toContain(dossier2);
      });

      it('should accept null and undefined values', () => {
        const dossier: IDossier = { id: 123 };
        expectedResult = service.addDossierToCollectionIfMissing([], null, dossier, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dossier);
      });

      it('should return initial array if no Dossier is added', () => {
        const dossierCollection: IDossier[] = [{ id: 123 }];
        expectedResult = service.addDossierToCollectionIfMissing(dossierCollection, undefined, null);
        expect(expectedResult).toEqual(dossierCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
