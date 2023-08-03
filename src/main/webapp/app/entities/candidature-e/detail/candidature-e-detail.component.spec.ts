import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { CandidatureEDetailComponent } from './candidature-e-detail.component';

describe('CandidatureE Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CandidatureEDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: CandidatureEDetailComponent,
              resolve: { candidatureE: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(CandidatureEDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load candidatureE on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', CandidatureEDetailComponent);

      // THEN
      expect(instance.candidatureE).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
