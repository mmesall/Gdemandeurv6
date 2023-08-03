import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { DossierFormService } from './dossier-form.service';
import { DossierService } from '../service/dossier.service';
import { IDossier } from '../dossier.model';
import { IEleve } from 'app/entities/eleve/eleve.model';
import { EleveService } from 'app/entities/eleve/service/eleve.service';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { IProfessionnel } from 'app/entities/professionnel/professionnel.model';
import { ProfessionnelService } from 'app/entities/professionnel/service/professionnel.service';

import { DossierUpdateComponent } from './dossier-update.component';

describe('Dossier Management Update Component', () => {
  let comp: DossierUpdateComponent;
  let fixture: ComponentFixture<DossierUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dossierFormService: DossierFormService;
  let dossierService: DossierService;
  let eleveService: EleveService;
  let etudiantService: EtudiantService;
  let professionnelService: ProfessionnelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), DossierUpdateComponent],
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
      .overrideTemplate(DossierUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DossierUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dossierFormService = TestBed.inject(DossierFormService);
    dossierService = TestBed.inject(DossierService);
    eleveService = TestBed.inject(EleveService);
    etudiantService = TestBed.inject(EtudiantService);
    professionnelService = TestBed.inject(ProfessionnelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call eleve query and add missing value', () => {
      const dossier: IDossier = { id: 456 };
      const eleve: IEleve = { id: 8643 };
      dossier.eleve = eleve;

      const eleveCollection: IEleve[] = [{ id: 6793 }];
      jest.spyOn(eleveService, 'query').mockReturnValue(of(new HttpResponse({ body: eleveCollection })));
      const expectedCollection: IEleve[] = [eleve, ...eleveCollection];
      jest.spyOn(eleveService, 'addEleveToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dossier });
      comp.ngOnInit();

      expect(eleveService.query).toHaveBeenCalled();
      expect(eleveService.addEleveToCollectionIfMissing).toHaveBeenCalledWith(eleveCollection, eleve);
      expect(comp.elevesCollection).toEqual(expectedCollection);
    });

    it('Should call etudiant query and add missing value', () => {
      const dossier: IDossier = { id: 456 };
      const etudiant: IEtudiant = { id: 23407 };
      dossier.etudiant = etudiant;

      const etudiantCollection: IEtudiant[] = [{ id: 13404 }];
      jest.spyOn(etudiantService, 'query').mockReturnValue(of(new HttpResponse({ body: etudiantCollection })));
      const expectedCollection: IEtudiant[] = [etudiant, ...etudiantCollection];
      jest.spyOn(etudiantService, 'addEtudiantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dossier });
      comp.ngOnInit();

      expect(etudiantService.query).toHaveBeenCalled();
      expect(etudiantService.addEtudiantToCollectionIfMissing).toHaveBeenCalledWith(etudiantCollection, etudiant);
      expect(comp.etudiantsCollection).toEqual(expectedCollection);
    });

    it('Should call professionnel query and add missing value', () => {
      const dossier: IDossier = { id: 456 };
      const professionnel: IProfessionnel = { id: 16663 };
      dossier.professionnel = professionnel;

      const professionnelCollection: IProfessionnel[] = [{ id: 18977 }];
      jest.spyOn(professionnelService, 'query').mockReturnValue(of(new HttpResponse({ body: professionnelCollection })));
      const expectedCollection: IProfessionnel[] = [professionnel, ...professionnelCollection];
      jest.spyOn(professionnelService, 'addProfessionnelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dossier });
      comp.ngOnInit();

      expect(professionnelService.query).toHaveBeenCalled();
      expect(professionnelService.addProfessionnelToCollectionIfMissing).toHaveBeenCalledWith(professionnelCollection, professionnel);
      expect(comp.professionnelsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const dossier: IDossier = { id: 456 };
      const eleve: IEleve = { id: 9050 };
      dossier.eleve = eleve;
      const etudiant: IEtudiant = { id: 25429 };
      dossier.etudiant = etudiant;
      const professionnel: IProfessionnel = { id: 31302 };
      dossier.professionnel = professionnel;

      activatedRoute.data = of({ dossier });
      comp.ngOnInit();

      expect(comp.elevesCollection).toContain(eleve);
      expect(comp.etudiantsCollection).toContain(etudiant);
      expect(comp.professionnelsCollection).toContain(professionnel);
      expect(comp.dossier).toEqual(dossier);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDossier>>();
      const dossier = { id: 123 };
      jest.spyOn(dossierFormService, 'getDossier').mockReturnValue(dossier);
      jest.spyOn(dossierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dossier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dossier }));
      saveSubject.complete();

      // THEN
      expect(dossierFormService.getDossier).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(dossierService.update).toHaveBeenCalledWith(expect.objectContaining(dossier));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDossier>>();
      const dossier = { id: 123 };
      jest.spyOn(dossierFormService, 'getDossier').mockReturnValue({ id: null });
      jest.spyOn(dossierService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dossier: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dossier }));
      saveSubject.complete();

      // THEN
      expect(dossierFormService.getDossier).toHaveBeenCalled();
      expect(dossierService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IDossier>>();
      const dossier = { id: 123 };
      jest.spyOn(dossierService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dossier });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dossierService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEleve', () => {
      it('Should forward to eleveService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(eleveService, 'compareEleve');
        comp.compareEleve(entity, entity2);
        expect(eleveService.compareEleve).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEtudiant', () => {
      it('Should forward to etudiantService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(etudiantService, 'compareEtudiant');
        comp.compareEtudiant(entity, entity2);
        expect(etudiantService.compareEtudiant).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareProfessionnel', () => {
      it('Should forward to professionnelService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(professionnelService, 'compareProfessionnel');
        comp.compareProfessionnel(entity, entity2);
        expect(professionnelService.compareProfessionnel).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
