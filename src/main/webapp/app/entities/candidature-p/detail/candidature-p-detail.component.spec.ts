import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CandidaturePDetailComponent } from './candidature-p-detail.component';

describe('CandidatureP Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CandidaturePDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CandidaturePDetailComponent,
              resolve: { candidatureP: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(CandidaturePDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load candidatureP on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CandidaturePDetailComponent);

      // THEN
      expect(instance.candidatureP).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
