import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IExperience, Experience } from '../experience.model';

import { ExperienceService } from './experience.service';

describe('Experience Service', () => {
  let service: ExperienceService;
  let httpMock: HttpTestingController;
  let elemDefault: IExperience;
  let expectedResult: IExperience | IExperience[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ExperienceService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      dateDebut: currentDate,
      dateFin: currentDate,
      nomEntreprise: 'AAAAAAA',
      posteOccupe: 'AAAAAAA',
      mission: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dateDebut: currentDate.format(DATE_FORMAT),
          dateFin: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Experience', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dateDebut: currentDate.format(DATE_FORMAT),
          dateFin: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateDebut: currentDate,
          dateFin: currentDate,
        },
        returnedFromService
      );

      service.create(new Experience()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Experience', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dateDebut: currentDate.format(DATE_FORMAT),
          dateFin: currentDate.format(DATE_FORMAT),
          nomEntreprise: 'BBBBBB',
          posteOccupe: 'BBBBBB',
          mission: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateDebut: currentDate,
          dateFin: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Experience', () => {
      const patchObject = Object.assign(
        {
          nomEntreprise: 'BBBBBB',
          mission: 'BBBBBB',
        },
        new Experience()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dateDebut: currentDate,
          dateFin: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Experience', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dateDebut: currentDate.format(DATE_FORMAT),
          dateFin: currentDate.format(DATE_FORMAT),
          nomEntreprise: 'BBBBBB',
          posteOccupe: 'BBBBBB',
          mission: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dateDebut: currentDate,
          dateFin: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Experience', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addExperienceToCollectionIfMissing', () => {
      it('should add a Experience to an empty array', () => {
        const experience: IExperience = { id: 123 };
        expectedResult = service.addExperienceToCollectionIfMissing([], experience);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(experience);
      });

      it('should not add a Experience to an array that contains it', () => {
        const experience: IExperience = { id: 123 };
        const experienceCollection: IExperience[] = [
          {
            ...experience,
          },
          { id: 456 },
        ];
        expectedResult = service.addExperienceToCollectionIfMissing(experienceCollection, experience);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Experience to an array that doesn't contain it", () => {
        const experience: IExperience = { id: 123 };
        const experienceCollection: IExperience[] = [{ id: 456 }];
        expectedResult = service.addExperienceToCollectionIfMissing(experienceCollection, experience);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(experience);
      });

      it('should add only unique Experience to an array', () => {
        const experienceArray: IExperience[] = [{ id: 123 }, { id: 456 }, { id: 50795 }];
        const experienceCollection: IExperience[] = [{ id: 123 }];
        expectedResult = service.addExperienceToCollectionIfMissing(experienceCollection, ...experienceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const experience: IExperience = { id: 123 };
        const experience2: IExperience = { id: 456 };
        expectedResult = service.addExperienceToCollectionIfMissing([], experience, experience2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(experience);
        expect(expectedResult).toContain(experience2);
      });

      it('should accept null and undefined values', () => {
        const experience: IExperience = { id: 123 };
        expectedResult = service.addExperienceToCollectionIfMissing([], null, experience, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(experience);
      });

      it('should return initial array if no Experience is added', () => {
        const experienceCollection: IExperience[] = [{ id: 123 }];
        expectedResult = service.addExperienceToCollectionIfMissing(experienceCollection, undefined, null);
        expect(expectedResult).toEqual(experienceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
