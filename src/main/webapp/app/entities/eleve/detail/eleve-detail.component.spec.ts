import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { EleveDetailComponent } from './eleve-detail.component';

describe('Eleve Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EleveDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: EleveDetailComponent,
              resolve: { eleve: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(EleveDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load eleve on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', EleveDetailComponent);

      // THEN
      expect(instance.eleve).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
