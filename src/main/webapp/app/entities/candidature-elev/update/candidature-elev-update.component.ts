import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICandidatureElev, CandidatureElev } from '../candidature-elev.model';
import { CandidatureElevService } from '../service/candidature-elev.service';
import { IEleve } from 'app/entities/eleve/eleve.model';
import { EleveService } from 'app/entities/eleve/service/eleve.service';
import { IFormationInitiale } from 'app/entities/formation-initiale/formation-initiale.model';
import { FormationInitialeService } from 'app/entities/formation-initiale/service/formation-initiale.service';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { Resultat } from 'app/entities/enumerations/resultat.model';

@Component({
  selector: 'jhi-candidature-elev-update',
  templateUrl: './candidature-elev-update.component.html',
})
export class CandidatureElevUpdateComponent implements OnInit {
  isSaving = false;
  nomFiliereValues = Object.keys(NomFiliere);
  resultatValues = Object.keys(Resultat);

  elevesSharedCollection: IEleve[] = [];
  formationInitialesSharedCollection: IFormationInitiale[] = [];

  editForm = this.fb.group({
    id: [],
    offreFormation: [],
    dateDebutOffre: [],
    dateFinOffre: [],
    dateDepot: [],
    resultat: [],
    eleve: [],
    formationInitiale: [],
  });

  constructor(
    protected candidatureElevService: CandidatureElevService,
    protected eleveService: EleveService,
    protected formationInitialeService: FormationInitialeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ candidatureElev }) => {
      this.updateForm(candidatureElev);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const candidatureElev = this.createFromForm();
    if (candidatureElev.id !== undefined) {
      this.subscribeToSaveResponse(this.candidatureElevService.update(candidatureElev));
    } else {
      this.subscribeToSaveResponse(this.candidatureElevService.create(candidatureElev));
    }
  }

  trackEleveById(index: number, item: IEleve): number {
    return item.id!;
  }

  trackFormationInitialeById(index: number, item: IFormationInitiale): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICandidatureElev>>): void {
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

  protected updateForm(candidatureElev: ICandidatureElev): void {
    this.editForm.patchValue({
      id: candidatureElev.id,
      offreFormation: candidatureElev.offreFormation,
      dateDebutOffre: candidatureElev.dateDebutOffre,
      dateFinOffre: candidatureElev.dateFinOffre,
      dateDepot: candidatureElev.dateDepot,
      resultat: candidatureElev.resultat,
      eleve: candidatureElev.eleve,
      formationInitiale: candidatureElev.formationInitiale,
    });

    this.elevesSharedCollection = this.eleveService.addEleveToCollectionIfMissing(this.elevesSharedCollection, candidatureElev.eleve);
    this.formationInitialesSharedCollection = this.formationInitialeService.addFormationInitialeToCollectionIfMissing(
      this.formationInitialesSharedCollection,
      candidatureElev.formationInitiale
    );
  }

  protected loadRelationshipsOptions(): void {
    this.eleveService
      .query()
      .pipe(map((res: HttpResponse<IEleve[]>) => res.body ?? []))
      .pipe(map((eleves: IEleve[]) => this.eleveService.addEleveToCollectionIfMissing(eleves, this.editForm.get('eleve')!.value)))
      .subscribe((eleves: IEleve[]) => (this.elevesSharedCollection = eleves));

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
  }

  protected createFromForm(): ICandidatureElev {
    return {
      ...new CandidatureElev(),
      id: this.editForm.get(['id'])!.value,
      offreFormation: this.editForm.get(['offreFormation'])!.value,
      dateDebutOffre: this.editForm.get(['dateDebutOffre'])!.value,
      dateFinOffre: this.editForm.get(['dateFinOffre'])!.value,
      dateDepot: this.editForm.get(['dateDepot'])!.value,
      resultat: this.editForm.get(['resultat'])!.value,
      eleve: this.editForm.get(['eleve'])!.value,
      formationInitiale: this.editForm.get(['formationInitiale'])!.value,
    };
  }
}
