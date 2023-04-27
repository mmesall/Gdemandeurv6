import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CandidatureEtudiantDetailComponent } from './candidature-etudiant-detail.component';

describe('CandidatureEtudiant Management Detail Component', () => {
  let comp: CandidatureEtudiantDetailComponent;
  let fixture: ComponentFixture<CandidatureEtudiantDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CandidatureEtudiantDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ candidatureEtudiant: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CandidatureEtudiantDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CandidatureEtudiantDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load candidatureEtudiant on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.candidatureEtudiant).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
