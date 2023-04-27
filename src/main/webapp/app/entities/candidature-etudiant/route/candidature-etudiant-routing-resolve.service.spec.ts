import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ICandidatureEtudiant, CandidatureEtudiant } from '../candidature-etudiant.model';
import { CandidatureEtudiantService } from '../service/candidature-etudiant.service';

import { CandidatureEtudiantRoutingResolveService } from './candidature-etudiant-routing-resolve.service';

describe('CandidatureEtudiant routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CandidatureEtudiantRoutingResolveService;
  let service: CandidatureEtudiantService;
  let resultCandidatureEtudiant: ICandidatureEtudiant | undefined;

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
    routingResolveService = TestBed.inject(CandidatureEtudiantRoutingResolveService);
    service = TestBed.inject(CandidatureEtudiantService);
    resultCandidatureEtudiant = undefined;
  });

  describe('resolve', () => {
    it('should return ICandidatureEtudiant returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCandidatureEtudiant = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCandidatureEtudiant).toEqual({ id: 123 });
    });

    it('should return new ICandidatureEtudiant if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCandidatureEtudiant = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCandidatureEtudiant).toEqual(new CandidatureEtudiant());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CandidatureEtudiant })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCandidatureEtudiant = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCandidatureEtudiant).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
