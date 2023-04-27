import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CandidatureElevService } from '../service/candidature-elev.service';
import { ICandidatureElev, CandidatureElev } from '../candidature-elev.model';
import { IEleve } from 'app/entities/eleve/eleve.model';
import { EleveService } from 'app/entities/eleve/service/eleve.service';
import { IFormationInitiale } from 'app/entities/formation-initiale/formation-initiale.model';
import { FormationInitialeService } from 'app/entities/formation-initiale/service/formation-initiale.service';

import { CandidatureElevUpdateComponent } from './candidature-elev-update.component';

describe('CandidatureElev Management Update Component', () => {
  let comp: CandidatureElevUpdateComponent;
  let fixture: ComponentFixture<CandidatureElevUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let candidatureElevService: CandidatureElevService;
  let eleveService: EleveService;
  let formationInitialeService: FormationInitialeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CandidatureElevUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(CandidatureElevUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CandidatureElevUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    candidatureElevService = TestBed.inject(CandidatureElevService);
    eleveService = TestBed.inject(EleveService);
    formationInitialeService = TestBed.inject(FormationInitialeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Eleve query and add missing value', () => {
      const candidatureElev: ICandidatureElev = { id: 456 };
      const eleve: IEleve = { id: 76074 };
      candidatureElev.eleve = eleve;

      const eleveCollection: IEleve[] = [{ id: 26509 }];
      jest.spyOn(eleveService, 'query').mockReturnValue(of(new HttpResponse({ body: eleveCollection })));
      const additionalEleves = [eleve];
      const expectedCollection: IEleve[] = [...additionalEleves, ...eleveCollection];
      jest.spyOn(eleveService, 'addEleveToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidatureElev });
      comp.ngOnInit();

      expect(eleveService.query).toHaveBeenCalled();
      expect(eleveService.addEleveToCollectionIfMissing).toHaveBeenCalledWith(eleveCollection, ...additionalEleves);
      expect(comp.elevesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FormationInitiale query and add missing value', () => {
      const candidatureElev: ICandidatureElev = { id: 456 };
      const formationInitiale: IFormationInitiale = { id: 49846 };
      candidatureElev.formationInitiale = formationInitiale;

      const formationInitialeCollection: IFormationInitiale[] = [{ id: 87733 }];
      jest.spyOn(formationInitialeService, 'query').mockReturnValue(of(new HttpResponse({ body: formationInitialeCollection })));
      const additionalFormationInitiales = [formationInitiale];
      const expectedCollection: IFormationInitiale[] = [...additionalFormationInitiales, ...formationInitialeCollection];
      jest.spyOn(formationInitialeService, 'addFormationInitialeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidatureElev });
      comp.ngOnInit();

      expect(formationInitialeService.query).toHaveBeenCalled();
      expect(formationInitialeService.addFormationInitialeToCollectionIfMissing).toHaveBeenCalledWith(
        formationInitialeCollection,
        ...additionalFormationInitiales
      );
      expect(comp.formationInitialesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const candidatureElev: ICandidatureElev = { id: 456 };
      const eleve: IEleve = { id: 53228 };
      candidatureElev.eleve = eleve;
      const formationInitiale: IFormationInitiale = { id: 82782 };
      candidatureElev.formationInitiale = formationInitiale;

      activatedRoute.data = of({ candidatureElev });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(candidatureElev));
      expect(comp.elevesSharedCollection).toContain(eleve);
      expect(comp.formationInitialesSharedCollection).toContain(formationInitiale);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CandidatureElev>>();
      const candidatureElev = { id: 123 };
      jest.spyOn(candidatureElevService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidatureElev });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: candidatureElev }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(candidatureElevService.update).toHaveBeenCalledWith(candidatureElev);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CandidatureElev>>();
      const candidatureElev = new CandidatureElev();
      jest.spyOn(candidatureElevService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidatureElev });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: candidatureElev }));
      saveSubject.complete();

      // THEN
      expect(candidatureElevService.create).toHaveBeenCalledWith(candidatureElev);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CandidatureElev>>();
      const candidatureElev = { id: 123 };
      jest.spyOn(candidatureElevService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidatureElev });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(candidatureElevService.update).toHaveBeenCalledWith(candidatureElev);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEleveById', () => {
      it('Should return tracked Eleve primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEleveById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFormationInitialeById', () => {
      it('Should return tracked FormationInitiale primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFormationInitialeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
