import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DemandeurDetailComponent } from './demandeur-detail.component';

describe('Demandeur Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DemandeurDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DemandeurDetailComponent,
              resolve: { demandeur: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(DemandeurDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load demandeur on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DemandeurDetailComponent);

      // THEN
      expect(instance.demandeur).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
