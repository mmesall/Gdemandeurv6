import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICandidatureE, CandidatureE } from '../candidature-e.model';
import { CandidatureEService } from '../service/candidature-e.service';

import { CandidatureERoutingResolveService } from './candidature-e-routing-resolve.service';

describe('CandidatureE routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CandidatureERoutingResolveService;
  let service: CandidatureEService;
  let resultCandidatureE: ICandidatureE | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(CandidatureERoutingResolveService);
    service = TestBed.inject(CandidatureEService);
    resultCandidatureE = undefined;
  });

  describe('resolve', () => {
    it('should return ICandidatureE returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCandidatureE = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCandidatureE).toEqual({ id: 123 });
    });

    it('should return new ICandidatureE if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCandidatureE = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCandidatureE).toEqual(new CandidatureE());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CandidatureE })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCandidatureE = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCandidatureE).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
