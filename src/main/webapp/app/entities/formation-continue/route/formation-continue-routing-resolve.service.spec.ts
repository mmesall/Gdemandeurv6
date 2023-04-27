import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IFormationContinue, FormationContinue } from '../formation-continue.model';
import { FormationContinueService } from '../service/formation-continue.service';

import { FormationContinueRoutingResolveService } from './formation-continue-routing-resolve.service';

describe('FormationContinue routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FormationContinueRoutingResolveService;
  let service: FormationContinueService;
  let resultFormationContinue: IFormationContinue | undefined;

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
    routingResolveService = TestBed.inject(FormationContinueRoutingResolveService);
    service = TestBed.inject(FormationContinueService);
    resultFormationContinue = undefined;
  });

  describe('resolve', () => {
    it('should return IFormationContinue returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFormationContinue = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFormationContinue).toEqual({ id: 123 });
    });

    it('should return new IFormationContinue if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFormationContinue = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFormationContinue).toEqual(new FormationContinue());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FormationContinue })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFormationContinue = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFormationContinue).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
