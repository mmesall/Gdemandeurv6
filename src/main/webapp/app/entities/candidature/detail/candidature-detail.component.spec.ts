import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CandidatureDetailComponent } from './candidature-detail.component';

describe('Candidature Management Detail Component', () => {
  let comp: CandidatureDetailComponent;
  let fixture: ComponentFixture<CandidatureDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CandidatureDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ candidature: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CandidatureDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CandidatureDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load candidature on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.candidature).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
