import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICandidatureElev, CandidatureElev } from '../candidature-elev.model';
import { CandidatureElevService } from '../service/candidature-elev.service';

import { CandidatureElevRoutingResolveService } from './candidature-elev-routing-resolve.service';

describe('CandidatureElev routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CandidatureElevRoutingResolveService;
  let service: CandidatureElevService;
  let resultCandidatureElev: ICandidatureElev | undefined;

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
    routingResolveService = TestBed.inject(CandidatureElevRoutingResolveService);
    service = TestBed.inject(CandidatureElevService);
    resultCandidatureElev = undefined;
  });

  describe('resolve', () => {
    it('should return ICandidatureElev returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCandidatureElev = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCandidatureElev).toEqual({ id: 123 });
    });

    it('should return new ICandidatureElev if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCandidatureElev = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCandidatureElev).toEqual(new CandidatureElev());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CandidatureElev })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCandidatureElev = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCandidatureElev).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
