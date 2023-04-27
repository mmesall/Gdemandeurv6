import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FormationInitialeService } from '../service/formation-initiale.service';
import { IFormationInitiale, FormationInitiale } from '../formation-initiale.model';
import { IFormation } from 'app/entities/formation/formation.model';
import { FormationService } from 'app/entities/formation/service/formation.service';

import { FormationInitialeUpdateComponent } from './formation-initiale-update.component';

describe('FormationInitiale Management Update Component', () => {
  let comp: FormationInitialeUpdateComponent;
  let fixture: ComponentFixture<FormationInitialeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let formationInitialeService: FormationInitialeService;
  let formationService: FormationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FormationInitialeUpdateComponent],
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
      .overrideTemplate(FormationInitialeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FormationInitialeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    formationInitialeService = TestBed.inject(FormationInitialeService);
    formationService = TestBed.inject(FormationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call formation query and add missing value', () => {
      const formationInitiale: IFormationInitiale = { id: 456 };
      const formation: IFormation = { id: 6856 };
      formationInitiale.formation = formation;

      const formationCollection: IFormation[] = [{ id: 20980 }];
      jest.spyOn(formationService, 'query').mockReturnValue(of(new HttpResponse({ body: formationCollection })));
      const expectedCollection: IFormation[] = [formation, ...formationCollection];
      jest.spyOn(formationService, 'addFormationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ formationInitiale });
      comp.ngOnInit();

      expect(formationService.query).toHaveBeenCalled();
      expect(formationService.addFormationToCollectionIfMissing).toHaveBeenCalledWith(formationCollection, formation);
      expect(comp.formationsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const formationInitiale: IFormationInitiale = { id: 456 };
      const formation: IFormation = { id: 39158 };
      formationInitiale.formation = formation;

      activatedRoute.data = of({ formationInitiale });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(formationInitiale));
      expect(comp.formationsCollection).toContain(formation);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FormationInitiale>>();
      const formationInitiale = { id: 123 };
      jest.spyOn(formationInitialeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formationInitiale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formationInitiale }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(formationInitialeService.update).toHaveBeenCalledWith(formationInitiale);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FormationInitiale>>();
      const formationInitiale = new FormationInitiale();
      jest.spyOn(formationInitialeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formationInitiale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: formationInitiale }));
      saveSubject.complete();

      // THEN
      expect(formationInitialeService.create).toHaveBeenCalledWith(formationInitiale);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FormationInitiale>>();
      const formationInitiale = { id: 123 };
      jest.spyOn(formationInitialeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ formationInitiale });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(formationInitialeService.update).toHaveBeenCalledWith(formationInitiale);
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
