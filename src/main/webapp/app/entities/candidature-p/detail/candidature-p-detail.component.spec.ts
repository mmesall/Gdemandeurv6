import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CandidaturePDetailComponent } from './candidature-p-detail.component';

describe('CandidatureP Management Detail Component', () => {
  let comp: CandidaturePDetailComponent;
  let fixture: ComponentFixture<CandidaturePDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CandidaturePDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ candidatureP: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CandidaturePDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CandidaturePDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load candidatureP on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.candidatureP).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
