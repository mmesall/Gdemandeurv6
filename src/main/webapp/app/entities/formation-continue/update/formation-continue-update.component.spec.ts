import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FormationContinueService } from '../service/formation-continue.service';
import { IFormationContinue, FormationContinue } from '../formation-continue.model';
import { IFormation } from 'app/entities/formation/formation.model';
import { FormationService } from 'app/entities/formation/service/formation.service';

import { FormationContinueUpdateComponent } from './formation-continue-update.component';

describe('FormationContinue Management Update Component', () => {
  let comp: FormationContinueUpdateComponent;
  let fixture: ComponentFixture<FormationContinueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let formationContinueService: FormationContinueService;
  let formationService: FormationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FormationContinueUpdateComponent],
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
      .overrideTemplate(FormationContinueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FormationContinueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    formationContinueService = TestBed.inject(FormationContinueService);
    formationService = TestBed.inject(FormationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call formation query and add missing value', () => {
      const formationContinue: IFormationContinue = { id: 456 };
      const formation: IFormation = { id: 30467 };
      formationContinue.formation = formation;

      const formationCollection: IFormation[] = [{ id: 59193 }];
      jest.spyOn(formationService, 'query').mockReturnValue(of(new HttpResponse({ body: formationCollection })));
      const expectedCollection: IFormation[] = [formation, ...formationCollection];
      jest.spyOn(formationService, 'addFormationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ formationContinue });
      comp.ngOnInit();

      expect(formationService.query).toHaveBeenCalled();
      expect(formationService.addFormationToCollectionIfMissing).toHaveBeenCalledWith(formationCollection, formation);
      expect(comp.formationsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const formationContinue: IFormationContinue = { id: 456 };
      const formation: IFormation = { id: 68488 };
      formationContinue.formation = formation;

      activatedRoute.data = of({ formationContinue });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(formationContinue));
      expect(comp.formationsCollection).toContain(formation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FormationContinue>>();
      const formationContinue = { id: 123 };
      jest.spyOn(formationContinueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formationContinue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formationContinue }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(formationContinueService.update).toHaveBeenCalledWith(formationContinue);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FormationContinue>>();
      const formationContinue = new FormationContinue();
      jest.spyOn(formationContinueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formationContinue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formationContinue }));
      saveSubject.complete();

      // THEN
      expect(formationContinueService.create).toHaveBeenCalledWith(formationContinue);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FormationContinue>>();
      const formationContinue = { id: 123 };
      jest.spyOn(formationContinueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formationContinue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(formationContinueService.update).toHaveBeenCalledWith(formationContinue);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFormationById', () => {
      it('Should return tracked Formation primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFormationById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
