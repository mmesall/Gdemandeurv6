import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CandidatureService } from '../service/candidature.service';
import { ICandidature, Candidature } from '../candidature.model';
import { IFormationInitiale } from 'app/entities/formation-initiale/formation-initiale.model';
import { FormationInitialeService } from 'app/entities/formation-initiale/service/formation-initiale.service';
import { IFormationContinue } from 'app/entities/formation-continue/formation-continue.model';
import { FormationContinueService } from 'app/entities/formation-continue/service/formation-continue.service';

import { CandidatureUpdateComponent } from './candidature-update.component';

describe('Candidature Management Update Component', () => {
  let comp: CandidatureUpdateComponent;
  let fixture: ComponentFixture<CandidatureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let candidatureService: CandidatureService;
  let formationInitialeService: FormationInitialeService;
  let formationContinueService: FormationContinueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CandidatureUpdateComponent],
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
      .overrideTemplate(CandidatureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CandidatureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    candidatureService = TestBed.inject(CandidatureService);
    formationInitialeService = TestBed.inject(FormationInitialeService);
    formationContinueService = TestBed.inject(FormationContinueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call FormationInitiale query and add missing value', () => {
      const candidature: ICandidature = { id: 456 };
      const formationInitiale: IFormationInitiale = { id: 46021 };
      candidature.formationInitiale = formationInitiale;

      const formationInitialeCollection: IFormationInitiale[] = [{ id: 13015 }];
      jest.spyOn(formationInitialeService, 'query').mockReturnValue(of(new HttpResponse({ body: formationInitialeCollection })));
      const additionalFormationInitiales = [formationInitiale];
      const expectedCollection: IFormationInitiale[] = [...additionalFormationInitiales, ...formationInitialeCollection];
      jest.spyOn(formationInitialeService, 'addFormationInitialeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidature });
      comp.ngOnInit();

      expect(formationInitialeService.query).toHaveBeenCalled();
      expect(formationInitialeService.addFormationInitialeToCollectionIfMissing).toHaveBeenCalledWith(
        formationInitialeCollection,
        ...additionalFormationInitiales
      );
      expect(comp.formationInitialesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FormationContinue query and add missing value', () => {
      const candidature: ICandidature = { id: 456 };
      const formationContinue: IFormationContinue = { id: 74570 };
      candidature.formationContinue = formationContinue;

      const formationContinueCollection: IFormationContinue[] = [{ id: 482 }];
      jest.spyOn(formationContinueService, 'query').mockReturnValue(of(new HttpResponse({ body: formationContinueCollection })));
      const additionalFormationContinues = [formationContinue];
      const expectedCollection: IFormationContinue[] = [...additionalFormationContinues, ...formationContinueCollection];
      jest.spyOn(formationContinueService, 'addFormationContinueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidature });
      comp.ngOnInit();

      expect(formationContinueService.query).toHaveBeenCalled();
      expect(formationContinueService.addFormationContinueToCollectionIfMissing).toHaveBeenCalledWith(
        formationContinueCollection,
        ...additionalFormationContinues
      );
      expect(comp.formationContinuesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const candidature: ICandidature = { id: 456 };
      const formationInitiale: IFormationInitiale = { id: 78642 };
      candidature.formationInitiale = formationInitiale;
      const formationContinue: IFormationContinue = { id: 19930 };
      candidature.formationContinue = formationContinue;

      activatedRoute.data = of({ candidature });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(candidature));
      expect(comp.formationInitialesSharedCollection).toContain(formationInitiale);
      expect(comp.formationContinuesSharedCollection).toContain(formationContinue);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Candidature>>();
      const candidature = { id: 123 };
      jest.spyOn(candidatureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidature });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: candidature }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(candidatureService.update).toHaveBeenCalledWith(candidature);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Candidature>>();
      const candidature = new Candidature();
      jest.spyOn(candidatureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidature });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: candidature }));
      saveSubject.complete();

      // THEN
      expect(candidatureService.create).toHaveBeenCalledWith(candidature);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Candidature>>();
      const candidature = { id: 123 };
      jest.spyOn(candidatureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidature });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(candidatureService.update).toHaveBeenCalledWith(candidature);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFormationInitialeById', () => {
      it('Should return tracked FormationInitiale primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFormationInitialeById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackFormationContinueById', () => {
      it('Should return tracked FormationContinue primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFormationContinueById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
