import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CandidatureProfDetailComponent } from './candidature-prof-detail.component';

describe('CandidatureProf Management Detail Component', () => {
  let comp: CandidatureProfDetailComponent;
  let fixture: ComponentFixture<CandidatureProfDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CandidatureProfDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ candidatureProf: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CandidatureProfDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CandidatureProfDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load candidatureProf on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.candidatureProf).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
