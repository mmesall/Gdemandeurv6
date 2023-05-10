import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICandidatureP, CandidatureP } from '../candidature-p.model';
import { CandidaturePService } from '../service/candidature-p.service';

import { CandidaturePRoutingResolveService } from './candidature-p-routing-resolve.service';

describe('CandidatureP routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CandidaturePRoutingResolveService;
  let service: CandidaturePService;
  let resultCandidatureP: ICandidatureP | undefined;

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
    routingResolveService = TestBed.inject(CandidaturePRoutingResolveService);
    service = TestBed.inject(CandidaturePService);
    resultCandidatureP = undefined;
  });

  describe('resolve', () => {
    it('should return ICandidatureP returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCandidatureP = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCandidatureP).toEqual({ id: 123 });
    });

    it('should return new ICandidatureP if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCandidatureP = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCandidatureP).toEqual(new CandidatureP());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CandidatureP })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCandidatureP = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCandidatureP).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
