import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICandidature, Candidature } from '../candidature.model';
import { CandidatureService } from '../service/candidature.service';
import { IFormationInitiale } from 'app/entities/formation-initiale/formation-initiale.model';
import { FormationInitialeService } from 'app/entities/formation-initiale/service/formation-initiale.service';
import { IFormationContinue } from 'app/entities/formation-continue/formation-continue.model';
import { FormationContinueService } from 'app/entities/formation-continue/service/formation-continue.service';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { Resultat } from 'app/entities/enumerations/resultat.model';

@Component({
  selector: 'jhi-candidature-update',
  templateUrl: './candidature-update.component.html',
})
export class CandidatureUpdateComponent implements OnInit {
  isSaving = false;
  nomFiliereValues = Object.keys(NomFiliere);
  resultatValues = Object.keys(Resultat);

  formationInitialesSharedCollection: IFormationInitiale[] = [];
  formationContinuesSharedCollection: IFormationContinue[] = [];

  editForm = this.fb.group({
    id: [],
    offreFormation: [],
    dateDebutOffre: [],
    dateFinOffre: [],
    dateDepot: [],
    resultat: [],
    formationInitiale: [],
    formationContinue: [],
  });

  constructor(
    protected candidatureService: CandidatureService,
    protected formationInitialeService: FormationInitialeService,
    protected formationContinueService: FormationContinueService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ candidature }) => {
      this.updateForm(candidature);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const candidature = this.createFromForm();
    if (candidature.id !== undefined) {
      this.subscribeToSaveResponse(this.candidatureService.update(candidature));
    } else {
      this.subscribeToSaveResponse(this.candidatureService.create(candidature));
    }
  }

  trackFormationInitialeById(index: number, item: IFormationInitiale): number {
    return item.id!;
  }

  trackFormationContinueById(index: number, item: IFormationContinue): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICandidature>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(candidature: ICandidature): void {
    this.editForm.patchValue({
      id: candidature.id,
      offreFormation: candidature.offreFormation,
      dateDebutOffre: candidature.dateDebutOffre,
      dateFinOffre: candidature.dateFinOffre,
      dateDepot: candidature.dateDepot,
      resultat: candidature.resultat,
      formationInitiale: candidature.formationInitiale,
      formationContinue: candidature.formationContinue,
    });

    this.formationInitialesSharedCollection = this.formationInitialeService.addFormationInitialeToCollectionIfMissing(
      this.formationInitialesSharedCollection,
      candidature.formationInitiale
    );
    this.formationContinuesSharedCollection = this.formationContinueService.addFormationContinueToCollectionIfMissing(
      this.formationContinuesSharedCollection,
      candidature.formationContinue
    );
  }

  protected loadRelationshipsOptions(): void {
    this.formationInitialeService
      .query()
      .pipe(map((res: HttpResponse<IFormationInitiale[]>) => res.body ?? []))
      .pipe(
        map((formationInitiales: IFormationInitiale[]) =>
          this.formationInitialeService.addFormationInitialeToCollectionIfMissing(
            formationInitiales,
            this.editForm.get('formationInitiale')!.value
          )
        )
      )
      .subscribe((formationInitiales: IFormationInitiale[]) => (this.formationInitialesSharedCollection = formationInitiales));

    this.formationContinueService
      .query()
      .pipe(map((res: HttpResponse<IFormationContinue[]>) => res.body ?? []))
      .pipe(
        map((formationContinues: IFormationContinue[]) =>
          this.formationContinueService.addFormationContinueToCollectionIfMissing(
            formationContinues,
            this.editForm.get('formationContinue')!.value
          )
        )
      )
      .subscribe((formationContinues: IFormationContinue[]) => (this.formationContinuesSharedCollection = formationContinues));
  }

  protected createFromForm(): ICandidature {
    return {
      ...new Candidature(),
      id: this.editForm.get(['id'])!.value,
      offreFormation: this.editForm.get(['offreFormation'])!.value,
      dateDebutOffre: this.editForm.get(['dateDebutOffre'])!.value,
      dateFinOffre: this.editForm.get(['dateFinOffre'])!.value,
      dateDepot: this.editForm.get(['dateDepot'])!.value,
      resultat: this.editForm.get(['resultat'])!.value,
      formationInitiale: this.editForm.get(['formationInitiale'])!.value,
      formationContinue: this.editForm.get(['formationContinue'])!.value,
    };
  }
}
