import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CandidatureProfService } from '../service/candidature-prof.service';
import { ICandidatureProf, CandidatureProf } from '../candidature-prof.model';
import { IProfessionnel } from 'app/entities/professionnel/professionnel.model';
import { ProfessionnelService } from 'app/entities/professionnel/service/professionnel.service';
import { IFormationContinue } from 'app/entities/formation-continue/formation-continue.model';
import { FormationContinueService } from 'app/entities/formation-continue/service/formation-continue.service';

import { CandidatureProfUpdateComponent } from './candidature-prof-update.component';

describe('CandidatureProf Management Update Component', () => {
  let comp: CandidatureProfUpdateComponent;
  let fixture: ComponentFixture<CandidatureProfUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let candidatureProfService: CandidatureProfService;
  let professionnelService: ProfessionnelService;
  let formationContinueService: FormationContinueService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CandidatureProfUpdateComponent],
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
      .overrideTemplate(CandidatureProfUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CandidatureProfUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    candidatureProfService = TestBed.inject(CandidatureProfService);
    professionnelService = TestBed.inject(ProfessionnelService);
    formationContinueService = TestBed.inject(FormationContinueService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Professionnel query and add missing value', () => {
      const candidatureProf: ICandidatureProf = { id: 456 };
      const professionnel: IProfessionnel = { id: 69621 };
      candidatureProf.professionnel = professionnel;

      const professionnelCollection: IProfessionnel[] = [{ id: 92614 }];
      jest.spyOn(professionnelService, 'query').mockReturnValue(of(new HttpResponse({ body: professionnelCollection })));
      const additionalProfessionnels = [professionnel];
      const expectedCollection: IProfessionnel[] = [...additionalProfessionnels, ...professionnelCollection];
      jest.spyOn(professionnelService, 'addProfessionnelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidatureProf });
      comp.ngOnInit();

      expect(professionnelService.query).toHaveBeenCalled();
      expect(professionnelService.addProfessionnelToCollectionIfMissing).toHaveBeenCalledWith(
        professionnelCollection,
        ...additionalProfessionnels
      );
      expect(comp.professionnelsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FormationContinue query and add missing value', () => {
      const candidatureProf: ICandidatureProf = { id: 456 };
      const formationContinue: IFormationContinue = { id: 94034 };
      candidatureProf.formationContinue = formationContinue;

      const formationContinueCollection: IFormationContinue[] = [{ id: 98021 }];
      jest.spyOn(formationContinueService, 'query').mockReturnValue(of(new HttpResponse({ body: formationContinueCollection })));
      const additionalFormationContinues = [formationContinue];
      const expectedCollection: IFormationContinue[] = [...additionalFormationContinues, ...formationContinueCollection];
      jest.spyOn(formationContinueService, 'addFormationContinueToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidatureProf });
      comp.ngOnInit();

      expect(formationContinueService.query).toHaveBeenCalled();
      expect(formationContinueService.addFormationContinueToCollectionIfMissing).toHaveBeenCalledWith(
        formationContinueCollection,
        ...additionalFormationContinues
      );
      expect(comp.formationContinuesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const candidatureProf: ICandidatureProf = { id: 456 };
      const professionnel: IProfessionnel = { id: 92179 };
      candidatureProf.professionnel = professionnel;
      const formationContinue: IFormationContinue = { id: 26408 };
      candidatureProf.formationContinue = formationContinue;

      activatedRoute.data = of({ candidatureProf });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(candidatureProf));
      expect(comp.professionnelsSharedCollection).toContain(professionnel);
      expect(comp.formationContinuesSharedCollection).toContain(formationContinue);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CandidatureProf>>();
      const candidatureProf = { id: 123 };
      jest.spyOn(candidatureProfService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidatureProf });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: candidatureProf }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(candidatureProfService.update).toHaveBeenCalledWith(candidatureProf);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CandidatureProf>>();
      const candidatureProf = new CandidatureProf();
      jest.spyOn(candidatureProfService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidatureProf });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: candidatureProf }));
      saveSubject.complete();

      // THEN
      expect(candidatureProfService.create).toHaveBeenCalledWith(candidatureProf);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CandidatureProf>>();
      const candidatureProf = { id: 123 };
      jest.spyOn(candidatureProfService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidatureProf });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(candidatureProfService.update).toHaveBeenCalledWith(candidatureProf);
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
  });
});
