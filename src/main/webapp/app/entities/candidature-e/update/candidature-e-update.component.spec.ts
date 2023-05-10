import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { CandidatureEService } from '../service/candidature-e.service';
import { ICandidatureE, CandidatureE } from '../candidature-e.model';
import { IEleve } from 'app/entities/eleve/eleve.model';
import { EleveService } from 'app/entities/eleve/service/eleve.service';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { IFormationInitiale } from 'app/entities/formation-initiale/formation-initiale.model';
import { FormationInitialeService } from 'app/entities/formation-initiale/service/formation-initiale.service';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';

import { CandidatureEUpdateComponent } from './candidature-e-update.component';

describe('CandidatureE Management Update Component', () => {
  let comp: CandidatureEUpdateComponent;
  let fixture: ComponentFixture<CandidatureEUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let candidatureEService: CandidatureEService;
  let eleveService: EleveService;
  let etudiantService: EtudiantService;
  let formationInitialeService: FormationInitialeService;
  let etablissementService: EtablissementService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [CandidatureEUpdateComponent],
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
      .overrideTemplate(CandidatureEUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CandidatureEUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    candidatureEService = TestBed.inject(CandidatureEService);
    eleveService = TestBed.inject(EleveService);
    etudiantService = TestBed.inject(EtudiantService);
    formationInitialeService = TestBed.inject(FormationInitialeService);
    etablissementService = TestBed.inject(EtablissementService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Eleve query and add missing value', () => {
      const candidatureE: ICandidatureE = { id: 456 };
      const eleve: IEleve = { id: 54332 };
      candidatureE.eleve = eleve;

      const eleveCollection: IEleve[] = [{ id: 42931 }];
      jest.spyOn(eleveService, 'query').mockReturnValue(of(new HttpResponse({ body: eleveCollection })));
      const additionalEleves = [eleve];
      const expectedCollection: IEleve[] = [...additionalEleves, ...eleveCollection];
      jest.spyOn(eleveService, 'addEleveToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidatureE });
      comp.ngOnInit();

      expect(eleveService.query).toHaveBeenCalled();
      expect(eleveService.addEleveToCollectionIfMissing).toHaveBeenCalledWith(eleveCollection, ...additionalEleves);
      expect(comp.elevesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Etudiant query and add missing value', () => {
      const candidatureE: ICandidatureE = { id: 456 };
      const etudiant: IEtudiant = { id: 60081 };
      candidatureE.etudiant = etudiant;

      const etudiantCollection: IEtudiant[] = [{ id: 70174 }];
      jest.spyOn(etudiantService, 'query').mockReturnValue(of(new HttpResponse({ body: etudiantCollection })));
      const additionalEtudiants = [etudiant];
      const expectedCollection: IEtudiant[] = [...additionalEtudiants, ...etudiantCollection];
      jest.spyOn(etudiantService, 'addEtudiantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidatureE });
      comp.ngOnInit();

      expect(etudiantService.query).toHaveBeenCalled();
      expect(etudiantService.addEtudiantToCollectionIfMissing).toHaveBeenCalledWith(etudiantCollection, ...additionalEtudiants);
      expect(comp.etudiantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call FormationInitiale query and add missing value', () => {
      const candidatureE: ICandidatureE = { id: 456 };
      const formationInitiale: IFormationInitiale = { id: 18749 };
      candidatureE.formationInitiale = formationInitiale;

      const formationInitialeCollection: IFormationInitiale[] = [{ id: 62715 }];
      jest.spyOn(formationInitialeService, 'query').mockReturnValue(of(new HttpResponse({ body: formationInitialeCollection })));
      const additionalFormationInitiales = [formationInitiale];
      const expectedCollection: IFormationInitiale[] = [...additionalFormationInitiales, ...formationInitialeCollection];
      jest.spyOn(formationInitialeService, 'addFormationInitialeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidatureE });
      comp.ngOnInit();

      expect(formationInitialeService.query).toHaveBeenCalled();
      expect(formationInitialeService.addFormationInitialeToCollectionIfMissing).toHaveBeenCalledWith(
        formationInitialeCollection,
        ...additionalFormationInitiales
      );
      expect(comp.formationInitialesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Etablissement query and add missing value', () => {
      const candidatureE: ICandidatureE = { id: 456 };
      const etablissement: IEtablissement = { id: 27875 };
      candidatureE.etablissement = etablissement;

      const etablissementCollection: IEtablissement[] = [{ id: 54563 }];
      jest.spyOn(etablissementService, 'query').mockReturnValue(of(new HttpResponse({ body: etablissementCollection })));
      const additionalEtablissements = [etablissement];
      const expectedCollection: IEtablissement[] = [...additionalEtablissements, ...etablissementCollection];
      jest.spyOn(etablissementService, 'addEtablissementToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ candidatureE });
      comp.ngOnInit();

      expect(etablissementService.query).toHaveBeenCalled();
      expect(etablissementService.addEtablissementToCollectionIfMissing).toHaveBeenCalledWith(
        etablissementCollection,
        ...additionalEtablissements
      );
      expect(comp.etablissementsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const candidatureE: ICandidatureE = { id: 456 };
      const eleve: IEleve = { id: 78785 };
      candidatureE.eleve = eleve;
      const etudiant: IEtudiant = { id: 5368 };
      candidatureE.etudiant = etudiant;
      const formationInitiale: IFormationInitiale = { id: 48755 };
      candidatureE.formationInitiale = formationInitiale;
      const etablissement: IEtablissement = { id: 68289 };
      candidatureE.etablissement = etablissement;

      activatedRoute.data = of({ candidatureE });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(candidatureE));
      expect(comp.elevesSharedCollection).toContain(eleve);
      expect(comp.etudiantsSharedCollection).toContain(etudiant);
      expect(comp.formationInitialesSharedCollection).toContain(formationInitiale);
      expect(comp.etablissementsSharedCollection).toContain(etablissement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CandidatureE>>();
      const candidatureE = { id: 123 };
      jest.spyOn(candidatureEService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidatureE });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: candidatureE }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(candidatureEService.update).toHaveBeenCalledWith(candidatureE);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CandidatureE>>();
      const candidatureE = new CandidatureE();
      jest.spyOn(candidatureEService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidatureE });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: candidatureE }));
      saveSubject.complete();

      // THEN
      expect(candidatureEService.create).toHaveBeenCalledWith(candidatureE);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<CandidatureE>>();
      const candidatureE = { id: 123 };
      jest.spyOn(candidatureEService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ candidatureE });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(candidatureEService.update).toHaveBeenCalledWith(candidatureE);
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

    describe('trackEtablissementById', () => {
      it('Should return tracked Etablissement primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackEtablissementById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
