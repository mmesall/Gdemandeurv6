import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { ProfessionnelDetailComponent } from './professionnel-detail.component';

describe('Professionnel Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfessionnelDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: ProfessionnelDetailComponent,
              resolve: { professionnel: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(ProfessionnelDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load professionnel on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', ProfessionnelDetailComponent);

      // THEN
      expect(instance.professionnel).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
