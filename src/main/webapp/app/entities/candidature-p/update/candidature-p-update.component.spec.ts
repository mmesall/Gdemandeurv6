import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CandidaturePService } from '../service/candidature-p.service';
import { ICandidatureP, CandidatureP } from '../candidature-p.model';
import { IProfessionnel } from 'app/entities/professionnel/professionnel.model';
import { ProfessionnelService } from 'app/entities/professionnel/service/professionnel.service';
import { IFormationContinue } from 'app/entities/formation-continue/formation-continue.model';
import { FormationContinueService } from 'app/entities/formation-continue/service/formation-continue.service';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';

import { CandidaturePUpdateComponent } from './candidature-p-update.component';

describe('CandidatureP Management Update Component', () => {
  let comp: CandidaturePUpdateComponent;
  let fixture: ComponentFixture<CandidaturePUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let candidaturePService: CandidaturePService;
  let professionnelService: ProfessionnelService;
  let formationContinueService: FormationContinueService;
  let etablissementService: EtablissementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CandidaturePUpdateComponent],
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
      .overrideTemplate(CandidaturePUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CandidaturePUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    candidaturePService = TestBed.inject(CandidaturePService);
    professionnelService = TestBed.inject(ProfessionnelService);
    formationContinueService = TestBed.inject(FormationContinueService);
    etablissementService = TestBed.inject(EtablissementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Professionnel query and add missing value', () => {
      const candidatureP: ICandidatureP = { id: 456 };
      const professionnel: IProfessionnel = { id: 58929 };
      candidatureP.professionnel = professionnel;

      const professionnelCollection: IProfessionnel[] = [{ id: 63369 }];
      jest.spyOn(professionnelService, 'query').mockReturnValue(of(new HttpResponse({ body: professionnelCollection })));
      const additionalProfessionnels = [professionnel];
      const expectedCollection: IProfessionnel[] = [...additionalProfessionnels, ...professionnelCollection];
      jest.spyOn(professionnelService, 'addProfessionnelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidatureP });
      comp.ngOnInit();

      expect(professionnelService.query).toHaveBeenCalled();
      expect(professionnelService.addProfessionnelToCollectionIfMissing).toHaveBeenCalledWith(
        professionnelCollection,
        ...additionalProfessionnels
      );
      expect(comp.professionnelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FormationContinue query and add missing value', () => {
      const candidatureP: ICandidatureP = { id: 456 };
      const formationContinue: IFormationContinue = { id: 36273 };
      candidatureP.formationContinue = formationContinue;

      const formationContinueCollection: IFormationContinue[] = [{ id: 57972 }];
      jest.spyOn(formationContinueService, 'query').mockReturnValue(of(new HttpResponse({ body: formationContinueCollection })));
      const additionalFormationContinues = [formationContinue];
      const expectedCollection: IFormationContinue[] = [...additionalFormationContinues, ...formationContinueCollection];
      jest.spyOn(formationContinueService, 'addFormationContinueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidatureP });
      comp.ngOnInit();

      expect(formationContinueService.query).toHaveBeenCalled();
      expect(formationContinueService.addFormationContinueToCollectionIfMissing).toHaveBeenCalledWith(
        formationContinueCollection,
        ...additionalFormationContinues
      );
      expect(comp.formationContinuesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Etablissement query and add missing value', () => {
      const candidatureP: ICandidatureP = { id: 456 };
      const etablissement: IEtablissement = { id: 95358 };
      candidatureP.etablissement = etablissement;

      const etablissementCollection: IEtablissement[] = [{ id: 22483 }];
      jest.spyOn(etablissementService, 'query').mockReturnValue(of(new HttpResponse({ body: etablissementCollection })));
      const additionalEtablissements = [etablissement];
      const expectedCollection: IEtablissement[] = [...additionalEtablissements, ...etablissementCollection];
      jest.spyOn(etablissementService, 'addEtablissementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidatureP });
      comp.ngOnInit();

      expect(etablissementService.query).toHaveBeenCalled();
      expect(etablissementService.addEtablissementToCollectionIfMissing).toHaveBeenCalledWith(
        etablissementCollection,
        ...additionalEtablissements
      );
      expect(comp.etablissementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const candidatureP: ICandidatureP = { id: 456 };
      const professionnel: IProfessionnel = { id: 39426 };
      candidatureP.professionnel = professionnel;
      const formationContinue: IFormationContinue = { id: 17202 };
      candidatureP.formationContinue = formationContinue;
      const etablissement: IEtablissement = { id: 79977 };
      candidatureP.etablissement = etablissement;

      activatedRoute.data = of({ candidatureP });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(candidatureP));
      expect(comp.professionnelsSharedCollection).toContain(professionnel);
      expect(comp.formationContinuesSharedCollection).toContain(formationContinue);
      expect(comp.etablissementsSharedCollection).toContain(etablissement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CandidatureP>>();
      const candidatureP = { id: 123 };
      jest.spyOn(candidaturePService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidatureP });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: candidatureP }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(candidaturePService.update).toHaveBeenCalledWith(candidatureP);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CandidatureP>>();
      const candidatureP = new CandidatureP();
      jest.spyOn(candidaturePService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidatureP });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: candidatureP }));
      saveSubject.complete();

      // THEN
      expect(candidaturePService.create).toHaveBeenCalledWith(candidatureP);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CandidatureP>>();
      const candidatureP = { id: 123 };
      jest.spyOn(candidaturePService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidatureP });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(candidaturePService.update).toHaveBeenCalledWith(candidatureP);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackProfessionnelById', () => {
      it('Should return tracked Professionnel primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackProfessionnelById(0, entity);
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

    describe('trackEtablissementById', () => {
      it('Should return tracked Etablissement primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEtablissementById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
