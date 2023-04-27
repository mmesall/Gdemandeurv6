import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CandidatureEtudiantService } from '../service/candidature-etudiant.service';
import { ICandidatureEtudiant, CandidatureEtudiant } from '../candidature-etudiant.model';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { IFormationInitiale } from 'app/entities/formation-initiale/formation-initiale.model';
import { FormationInitialeService } from 'app/entities/formation-initiale/service/formation-initiale.service';

import { CandidatureEtudiantUpdateComponent } from './candidature-etudiant-update.component';

describe('CandidatureEtudiant Management Update Component', () => {
  let comp: CandidatureEtudiantUpdateComponent;
  let fixture: ComponentFixture<CandidatureEtudiantUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let candidatureEtudiantService: CandidatureEtudiantService;
  let etudiantService: EtudiantService;
  let formationInitialeService: FormationInitialeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CandidatureEtudiantUpdateComponent],
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
      .overrideTemplate(CandidatureEtudiantUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CandidatureEtudiantUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    candidatureEtudiantService = TestBed.inject(CandidatureEtudiantService);
    etudiantService = TestBed.inject(EtudiantService);
    formationInitialeService = TestBed.inject(FormationInitialeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Etudiant query and add missing value', () => {
      const candidatureEtudiant: ICandidatureEtudiant = { id: 456 };
      const etudiant: IEtudiant = { id: 18351 };
      candidatureEtudiant.etudiant = etudiant;

      const etudiantCollection: IEtudiant[] = [{ id: 97999 }];
      jest.spyOn(etudiantService, 'query').mockReturnValue(of(new HttpResponse({ body: etudiantCollection })));
      const additionalEtudiants = [etudiant];
      const expectedCollection: IEtudiant[] = [...additionalEtudiants, ...etudiantCollection];
      jest.spyOn(etudiantService, 'addEtudiantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidatureEtudiant });
      comp.ngOnInit();

      expect(etudiantService.query).toHaveBeenCalled();
      expect(etudiantService.addEtudiantToCollectionIfMissing).toHaveBeenCalledWith(etudiantCollection, ...additionalEtudiants);
      expect(comp.etudiantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FormationInitiale query and add missing value', () => {
      const candidatureEtudiant: ICandidatureEtudiant = { id: 456 };
      const formationInitiale: IFormationInitiale = { id: 51238 };
      candidatureEtudiant.formationInitiale = formationInitiale;

      const formationInitialeCollection: IFormationInitiale[] = [{ id: 38658 }];
      jest.spyOn(formationInitialeService, 'query').mockReturnValue(of(new HttpResponse({ body: formationInitialeCollection })));
      const additionalFormationInitiales = [formationInitiale];
      const expectedCollection: IFormationInitiale[] = [...additionalFormationInitiales, ...formationInitialeCollection];
      jest.spyOn(formationInitialeService, 'addFormationInitialeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidatureEtudiant });
      comp.ngOnInit();

      expect(formationInitialeService.query).toHaveBeenCalled();
      expect(formationInitialeService.addFormationInitialeToCollectionIfMissing).toHaveBeenCalledWith(
        formationInitialeCollection,
        ...additionalFormationInitiales
      );
      expect(comp.formationInitialesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const candidatureEtudiant: ICandidatureEtudiant = { id: 456 };
      const etudiant: IEtudiant = { id: 84454 };
      candidatureEtudiant.etudiant = etudiant;
      const formationInitiale: IFormationInitiale = { id: 14137 };
      candidatureEtudiant.formationInitiale = formationInitiale;

      activatedRoute.data = of({ candidatureEtudiant });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(candidatureEtudiant));
      expect(comp.etudiantsSharedCollection).toContain(etudiant);
      expect(comp.formationInitialesSharedCollection).toContain(formationInitiale);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CandidatureEtudiant>>();
      const candidatureEtudiant = { id: 123 };
      jest.spyOn(candidatureEtudiantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidatureEtudiant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: candidatureEtudiant }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(candidatureEtudiantService.update).toHaveBeenCalledWith(candidatureEtudiant);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CandidatureEtudiant>>();
      const candidatureEtudiant = new CandidatureEtudiant();
      jest.spyOn(candidatureEtudiantService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidatureEtudiant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: candidatureEtudiant }));
      saveSubject.complete();

      // THEN
      expect(candidatureEtudiantService.create).toHaveBeenCalledWith(candidatureEtudiant);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CandidatureEtudiant>>();
      const candidatureEtudiant = { id: 123 };
      jest.spyOn(candidatureEtudiantService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidatureEtudiant });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(candidatureEtudiantService.update).toHaveBeenCalledWith(candidatureEtudiant);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackEtudiantById', () => {
      it('Should return tracked Etudiant primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEtudiantById(0, entity);
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
