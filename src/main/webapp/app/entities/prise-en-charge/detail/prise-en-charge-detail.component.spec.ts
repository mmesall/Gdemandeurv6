import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PriseEnChargeDetailComponent } from './prise-en-charge-detail.component';

describe('PriseEnCharge Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PriseEnChargeDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PriseEnChargeDetailComponent,
              resolve: { priseEnCharge: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(PriseEnChargeDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load priseEnCharge on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PriseEnChargeDetailComponent);

      // THEN
      expect(instance.priseEnCharge).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
