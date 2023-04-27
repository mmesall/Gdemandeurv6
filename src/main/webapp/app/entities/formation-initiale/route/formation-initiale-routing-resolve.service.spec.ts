import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IFormationInitiale, FormationInitiale } from '../formation-initiale.model';
import { FormationInitialeService } from '../service/formation-initiale.service';

import { FormationInitialeRoutingResolveService } from './formation-initiale-routing-resolve.service';

describe('FormationInitiale routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FormationInitialeRoutingResolveService;
  let service: FormationInitialeService;
  let resultFormationInitiale: IFormationInitiale | undefined;

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
    routingResolveService = TestBed.inject(FormationInitialeRoutingResolveService);
    service = TestBed.inject(FormationInitialeService);
    resultFormationInitiale = undefined;
  });

  describe('resolve', () => {
    it('should return IFormationInitiale returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFormationInitiale = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFormationInitiale).toEqual({ id: 123 });
    });

    it('should return new IFormationInitiale if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFormationInitiale = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFormationInitiale).toEqual(new FormationInitiale());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FormationInitiale })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFormationInitiale = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFormationInitiale).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
