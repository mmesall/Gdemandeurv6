import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { BailleurDetailComponent } from './bailleur-detail.component';

describe('Bailleur Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BailleurDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: BailleurDetailComponent,
              resolve: { bailleur: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(BailleurDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load bailleur on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', BailleurDetailComponent);

      // THEN
      expect(instance.bailleur).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
