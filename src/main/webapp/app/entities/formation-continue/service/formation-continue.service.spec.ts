import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Admission } from 'app/entities/enumerations/admission.model';
import { DiplomeRequis } from 'app/entities/enumerations/diplome-requis.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { NomSerie } from 'app/entities/enumerations/nom-serie.model';
import { CFP } from 'app/entities/enumerations/cfp.model';
import { LYCEE } from 'app/entities/enumerations/lycee.model';
import { DiplomeObtenu } from 'app/entities/enumerations/diplome-obtenu.model';
import { IFormationContinue, FormationContinue } from '../formation-continue.model';

import { FormationContinueService } from './formation-continue.service';

describe('FormationContinue Service', () => {
  let service: FormationContinueService;
  let httpMock: HttpTestingController;
  let elemDefault: IFormationContinue;
  let expectedResult: IFormationContinue | IFormationContinue[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FormationContinueService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      nomFormationC: 'AAAAAAA',
      duree: 'AAAAAAA',
      admission: Admission.CONCOURS,
      diplomeRequis: DiplomeRequis.ATTESTATION,
      niveauEtude: NiveauEtude.Cinquieme,
      filiere: NomFiliere.AGRI_ELEVAGE,
      serie: NomSerie.STEG,
      cfp: CFP.CEDT_G15,
      lycee: LYCEE.LTID_DAKAR,
      ficheFormationContentType: 'image/png',
      ficheFormation: 'AAAAAAA',
      libellePC: 'AAAAAAA',
      montantPriseEnCharge: 0,
      coutFormation: 0,
      detailPC: 'AAAAAAA',
      nomDiplome: DiplomeObtenu.CPS,
      autreDiplome: 'AAAAAAA',
      nomDebouche: 'AAAAAAA',
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

    it('should create a FormationContinue', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new FormationContinue()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FormationContinue', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomFormationC: 'BBBBBB',
          duree: 'BBBBBB',
          admission: 'BBBBBB',
          diplomeRequis: 'BBBBBB',
          niveauEtude: 'BBBBBB',
          filiere: 'BBBBBB',
          serie: 'BBBBBB',
          cfp: 'BBBBBB',
          lycee: 'BBBBBB',
          ficheFormation: 'BBBBBB',
          libellePC: 'BBBBBB',
          montantPriseEnCharge: 1,
          coutFormation: 1,
          detailPC: 'BBBBBB',
          nomDiplome: 'BBBBBB',
          autreDiplome: 'BBBBBB',
          nomDebouche: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FormationContinue', () => {
      const patchObject = Object.assign(
        {
          nomFormationC: 'BBBBBB',
          duree: 'BBBBBB',
          diplomeRequis: 'BBBBBB',
          niveauEtude: 'BBBBBB',
          ficheFormation: 'BBBBBB',
          libellePC: 'BBBBBB',
          montantPriseEnCharge: 1,
          detailPC: 'BBBBBB',
          nomDiplome: 'BBBBBB',
        },
        new FormationContinue()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FormationContinue', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          nomFormationC: 'BBBBBB',
          duree: 'BBBBBB',
          admission: 'BBBBBB',
          diplomeRequis: 'BBBBBB',
          niveauEtude: 'BBBBBB',
          filiere: 'BBBBBB',
          serie: 'BBBBBB',
          cfp: 'BBBBBB',
          lycee: 'BBBBBB',
          ficheFormation: 'BBBBBB',
          libellePC: 'BBBBBB',
          montantPriseEnCharge: 1,
          coutFormation: 1,
          detailPC: 'BBBBBB',
          nomDiplome: 'BBBBBB',
          autreDiplome: 'BBBBBB',
          nomDebouche: 'BBBBBB',
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

    it('should delete a FormationContinue', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFormationContinueToCollectionIfMissing', () => {
      it('should add a FormationContinue to an empty array', () => {
        const formationContinue: IFormationContinue = { id: 123 };
        expectedResult = service.addFormationContinueToCollectionIfMissing([], formationContinue);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(formationContinue);
      });

      it('should not add a FormationContinue to an array that contains it', () => {
        const formationContinue: IFormationContinue = { id: 123 };
        const formationContinueCollection: IFormationContinue[] = [
          {
            ...formationContinue,
          },
          { id: 456 },
        ];
        expectedResult = service.addFormationContinueToCollectionIfMissing(formationContinueCollection, formationContinue);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FormationContinue to an array that doesn't contain it", () => {
        const formationContinue: IFormationContinue = { id: 123 };
        const formationContinueCollection: IFormationContinue[] = [{ id: 456 }];
        expectedResult = service.addFormationContinueToCollectionIfMissing(formationContinueCollection, formationContinue);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(formationContinue);
      });

      it('should add only unique FormationContinue to an array', () => {
        const formationContinueArray: IFormationContinue[] = [{ id: 123 }, { id: 456 }, { id: 70920 }];
        const formationContinueCollection: IFormationContinue[] = [{ id: 123 }];
        expectedResult = service.addFormationContinueToCollectionIfMissing(formationContinueCollection, ...formationContinueArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const formationContinue: IFormationContinue = { id: 123 };
        const formationContinue2: IFormationContinue = { id: 456 };
        expectedResult = service.addFormationContinueToCollectionIfMissing([], formationContinue, formationContinue2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(formationContinue);
        expect(expectedResult).toContain(formationContinue2);
      });

      it('should accept null and undefined values', () => {
        const formationContinue: IFormationContinue = { id: 123 };
        expectedResult = service.addFormationContinueToCollectionIfMissing([], null, formationContinue, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(formationContinue);
      });

      it('should return initial array if no FormationContinue is added', () => {
        const formationContinueCollection: IFormationContinue[] = [{ id: 123 }];
        expectedResult = service.addFormationContinueToCollectionIfMissing(formationContinueCollection, undefined, null);
        expect(expectedResult).toEqual(formationContinueCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
