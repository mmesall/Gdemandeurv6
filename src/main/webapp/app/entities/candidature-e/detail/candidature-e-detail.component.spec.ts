import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CandidatureEDetailComponent } from './candidature-e-detail.component';

describe('CandidatureE Management Detail Component', () => {
  let comp: CandidatureEDetailComponent;
  let fixture: ComponentFixture<CandidatureEDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CandidatureEDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ candidatureE: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CandidatureEDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CandidatureEDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load candidatureE on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.candidatureE).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
