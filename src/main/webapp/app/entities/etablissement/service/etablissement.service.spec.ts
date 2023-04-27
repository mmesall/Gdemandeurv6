import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { NomEtablissement } from 'app/entities/enumerations/nom-etablissement.model';
import { NomRegion } from 'app/entities/enumerations/nom-region.model';
import { NomDepartement } from 'app/entities/enumerations/nom-departement.model';
import { TypeEtablissement } from 'app/entities/enumerations/type-etablissement.model';
import { StatutEtab } from 'app/entities/enumerations/statut-etab.model';
import { CFP } from 'app/entities/enumerations/cfp.model';
import { LYCEE } from 'app/entities/enumerations/lycee.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { NomSerie } from 'app/entities/enumerations/nom-serie.model';
import { IEtablissement, Etablissement } from '../etablissement.model';

import { EtablissementService } from './etablissement.service';

describe('Etablissement Service', () => {
  let service: EtablissementService;
  let httpMock: HttpTestingController;
  let elemDefault: IEtablissement;
  let expectedResult: IEtablissement | IEtablissement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(EtablissementService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomEtablissement: NomEtablissement.CEDT_G15,
      photoContentType: 'image/png',
      photo: 'AAAAAAA',
      region: NomRegion.DAKAR,
      departement: NomDepartement.DAKAR,
      email: 'AAAAAAA',
      telephone: 0,
      typeEtablissement: TypeEtablissement.CFP,
      statut: StatutEtab.PRIVE,
      autreRegion: 'AAAAAAA',
      autreDepartement: 'AAAAAAA',
      cfp: CFP.CEDT_G15,
      lycee: LYCEE.LTID_DAKAR,
      filiere: NomFiliere.AGRI_ELEVAGE,
      serie: NomSerie.STEG,
      autreFiliere: 'AAAAAAA',
      autreSerie: 'AAAAAAA',
      autreNomEtablissement: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Etablissement', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Etablissement()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Etablissement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomEtablissement: 'BBBBBB',
          photo: 'BBBBBB',
          region: 'BBBBBB',
          departement: 'BBBBBB',
          email: 'BBBBBB',
          telephone: 1,
          typeEtablissement: 'BBBBBB',
          statut: 'BBBBBB',
          autreRegion: 'BBBBBB',
          autreDepartement: 'BBBBBB',
          cfp: 'BBBBBB',
          lycee: 'BBBBBB',
          filiere: 'BBBBBB',
          serie: 'BBBBBB',
          autreFiliere: 'BBBBBB',
          autreSerie: 'BBBBBB',
          autreNomEtablissement: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Etablissement', () => {
      const patchObject = Object.assign(
        {
          nomEtablissement: 'BBBBBB',
          photo: 'BBBBBB',
          telephone: 1,
          statut: 'BBBBBB',
          lycee: 'BBBBBB',
          filiere: 'BBBBBB',
          serie: 'BBBBBB',
          autreSerie: 'BBBBBB',
        },
        new Etablissement()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Etablissement', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomEtablissement: 'BBBBBB',
          photo: 'BBBBBB',
          region: 'BBBBBB',
          departement: 'BBBBBB',
          email: 'BBBBBB',
          telephone: 1,
          typeEtablissement: 'BBBBBB',
          statut: 'BBBBBB',
          autreRegion: 'BBBBBB',
          autreDepartement: 'BBBBBB',
          cfp: 'BBBBBB',
          lycee: 'BBBBBB',
          filiere: 'BBBBBB',
          serie: 'BBBBBB',
          autreFiliere: 'BBBBBB',
          autreSerie: 'BBBBBB',
          autreNomEtablissement: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Etablissement', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addEtablissementToCollectionIfMissing', () => {
      it('should add a Etablissement to an empty array', () => {
        const etablissement: IEtablissement = { id: 123 };
        expectedResult = service.addEtablissementToCollectionIfMissing([], etablissement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(etablissement);
      });

      it('should not add a Etablissement to an array that contains it', () => {
        const etablissement: IEtablissement = { id: 123 };
        const etablissementCollection: IEtablissement[] = [
          {
            ...etablissement,
          },
          { id: 456 },
        ];
        expectedResult = service.addEtablissementToCollectionIfMissing(etablissementCollection, etablissement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Etablissement to an array that doesn't contain it", () => {
        const etablissement: IEtablissement = { id: 123 };
        const etablissementCollection: IEtablissement[] = [{ id: 456 }];
        expectedResult = service.addEtablissementToCollectionIfMissing(etablissementCollection, etablissement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(etablissement);
      });

      it('should add only unique Etablissement to an array', () => {
        const etablissementArray: IEtablissement[] = [{ id: 123 }, { id: 456 }, { id: 70445 }];
        const etablissementCollection: IEtablissement[] = [{ id: 123 }];
        expectedResult = service.addEtablissementToCollectionIfMissing(etablissementCollection, ...etablissementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const etablissement: IEtablissement = { id: 123 };
        const etablissement2: IEtablissement = { id: 456 };
        expectedResult = service.addEtablissementToCollectionIfMissing([], etablissement, etablissement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(etablissement);
        expect(expectedResult).toContain(etablissement2);
      });

      it('should accept null and undefined values', () => {
        const etablissement: IEtablissement = { id: 123 };
        expectedResult = service.addEtablissementToCollectionIfMissing([], null, etablissement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(etablissement);
      });

      it('should return initial array if no Etablissement is added', () => {
        const etablissementCollection: IEtablissement[] = [{ id: 123 }];
        expectedResult = service.addEtablissementToCollectionIfMissing(etablissementCollection, undefined, null);
        expect(expectedResult).toEqual(etablissementCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
