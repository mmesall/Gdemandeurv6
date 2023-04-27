import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IServiceMFPAI, ServiceMFPAI } from '../service-mfpai.model';

import { ServiceMFPAIService } from './service-mfpai.service';

describe('ServiceMFPAI Service', () => {
  let service: ServiceMFPAIService;
  let httpMock: HttpTestingController;
  let elemDefault: IServiceMFPAI;
  let expectedResult: IServiceMFPAI | IServiceMFPAI[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ServiceMFPAIService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      imageServiceContentType: 'image/png',
      imageService: 'AAAAAAA',
      nomService: 'AAAAAAA',
      chefService: 'AAAAAAA',
      description: 'AAAAAAA',
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

    it('should create a ServiceMFPAI', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ServiceMFPAI()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ServiceMFPAI', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          imageService: 'BBBBBB',
          nomService: 'BBBBBB',
          chefService: 'BBBBBB',
          description: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ServiceMFPAI', () => {
      const patchObject = Object.assign(
        {
          imageService: 'BBBBBB',
          nomService: 'BBBBBB',
          chefService: 'BBBBBB',
        },
        new ServiceMFPAI()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ServiceMFPAI', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          imageService: 'BBBBBB',
          nomService: 'BBBBBB',
          chefService: 'BBBBBB',
          description: 'BBBBBB',
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

    it('should delete a ServiceMFPAI', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addServiceMFPAIToCollectionIfMissing', () => {
      it('should add a ServiceMFPAI to an empty array', () => {
        const serviceMFPAI: IServiceMFPAI = { id: 123 };
        expectedResult = service.addServiceMFPAIToCollectionIfMissing([], serviceMFPAI);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceMFPAI);
      });

      it('should not add a ServiceMFPAI to an array that contains it', () => {
        const serviceMFPAI: IServiceMFPAI = { id: 123 };
        const serviceMFPAICollection: IServiceMFPAI[] = [
          {
            ...serviceMFPAI,
          },
          { id: 456 },
        ];
        expectedResult = service.addServiceMFPAIToCollectionIfMissing(serviceMFPAICollection, serviceMFPAI);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ServiceMFPAI to an array that doesn't contain it", () => {
        const serviceMFPAI: IServiceMFPAI = { id: 123 };
        const serviceMFPAICollection: IServiceMFPAI[] = [{ id: 456 }];
        expectedResult = service.addServiceMFPAIToCollectionIfMissing(serviceMFPAICollection, serviceMFPAI);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceMFPAI);
      });

      it('should add only unique ServiceMFPAI to an array', () => {
        const serviceMFPAIArray: IServiceMFPAI[] = [{ id: 123 }, { id: 456 }, { id: 74984 }];
        const serviceMFPAICollection: IServiceMFPAI[] = [{ id: 123 }];
        expectedResult = service.addServiceMFPAIToCollectionIfMissing(serviceMFPAICollection, ...serviceMFPAIArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const serviceMFPAI: IServiceMFPAI = { id: 123 };
        const serviceMFPAI2: IServiceMFPAI = { id: 456 };
        expectedResult = service.addServiceMFPAIToCollectionIfMissing([], serviceMFPAI, serviceMFPAI2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(serviceMFPAI);
        expect(expectedResult).toContain(serviceMFPAI2);
      });

      it('should accept null and undefined values', () => {
        const serviceMFPAI: IServiceMFPAI = { id: 123 };
        expectedResult = service.addServiceMFPAIToCollectionIfMissing([], null, serviceMFPAI, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(serviceMFPAI);
      });

      it('should return initial array if no ServiceMFPAI is added', () => {
        const serviceMFPAICollection: IServiceMFPAI[] = [{ id: 123 }];
        expectedResult = service.addServiceMFPAIToCollectionIfMissing(serviceMFPAICollection, undefined, null);
        expect(expectedResult).toEqual(serviceMFPAICollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
