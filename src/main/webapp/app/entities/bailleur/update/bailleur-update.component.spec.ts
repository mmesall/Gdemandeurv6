import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { BailleurService } from '../service/bailleur.service';
import { IBailleur, Bailleur } from '../bailleur.model';

import { BailleurUpdateComponent } from './bailleur-update.component';

describe('Bailleur Management Update Component', () => {
  let comp: BailleurUpdateComponent;
  let fixture: ComponentFixture<BailleurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let bailleurService: BailleurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [BailleurUpdateComponent],
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
      .overrideTemplate(BailleurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BailleurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    bailleurService = TestBed.inject(BailleurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const bailleur: IBailleur = { id: 456 };

      activatedRoute.data = of({ bailleur });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(bailleur));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bailleur>>();
      const bailleur = { id: 123 };
      jest.spyOn(bailleurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bailleur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bailleur }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(bailleurService.update).toHaveBeenCalledWith(bailleur);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bailleur>>();
      const bailleur = new Bailleur();
      jest.spyOn(bailleurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bailleur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: bailleur }));
      saveSubject.complete();

      // THEN
      expect(bailleurService.create).toHaveBeenCalledWith(bailleur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Bailleur>>();
      const bailleur = { id: 123 };
      jest.spyOn(bailleurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ bailleur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(bailleurService.update).toHaveBeenCalledWith(bailleur);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
