import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CandidatureElevDetailComponent } from './candidature-elev-detail.component';

describe('CandidatureElev Management Detail Component', () => {
  let comp: CandidatureElevDetailComponent;
  let fixture: ComponentFixture<CandidatureElevDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CandidatureElevDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ candidatureElev: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(CandidatureElevDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(CandidatureElevDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load candidatureElev on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.candidatureElev).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
