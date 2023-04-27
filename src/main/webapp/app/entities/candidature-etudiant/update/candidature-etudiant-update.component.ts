import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICandidatureEtudiant, CandidatureEtudiant } from '../candidature-etudiant.model';
import { CandidatureEtudiantService } from '../service/candidature-etudiant.service';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { IFormationInitiale } from 'app/entities/formation-initiale/formation-initiale.model';
import { FormationInitialeService } from 'app/entities/formation-initiale/service/formation-initiale.service';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { Resultat } from 'app/entities/enumerations/resultat.model';

@Component({
  selector: 'jhi-candidature-etudiant-update',
  templateUrl: './candidature-etudiant-update.component.html',
})
export class CandidatureEtudiantUpdateComponent implements OnInit {
  isSaving = false;
  nomFiliereValues = Object.keys(NomFiliere);
  resultatValues = Object.keys(Resultat);

  etudiantsSharedCollection: IEtudiant[] = [];
  formationInitialesSharedCollection: IFormationInitiale[] = [];

  editForm = this.fb.group({
    id: [],
    offreFormation: [],
    dateDebutOffre: [],
    dateFinOffre: [],
    dateDepot: [],
    resultat: [],
    etudiant: [],
    formationInitiale: [],
  });

  constructor(
    protected candidatureEtudiantService: CandidatureEtudiantService,
    protected etudiantService: EtudiantService,
    protected formationInitialeService: FormationInitialeService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ candidatureEtudiant }) => {
      this.updateForm(candidatureEtudiant);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const candidatureEtudiant = this.createFromForm();
    if (candidatureEtudiant.id !== undefined) {
      this.subscribeToSaveResponse(this.candidatureEtudiantService.update(candidatureEtudiant));
    } else {
      this.subscribeToSaveResponse(this.candidatureEtudiantService.create(candidatureEtudiant));
    }
  }

  trackEtudiantById(index: number, item: IEtudiant): number {
    return item.id!;
  }

  trackFormationInitialeById(index: number, item: IFormationInitiale): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICandidatureEtudiant>>): void {
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

  protected updateForm(candidatureEtudiant: ICandidatureEtudiant): void {
    this.editForm.patchValue({
      id: candidatureEtudiant.id,
      offreFormation: candidatureEtudiant.offreFormation,
      dateDebutOffre: candidatureEtudiant.dateDebutOffre,
      dateFinOffre: candidatureEtudiant.dateFinOffre,
      dateDepot: candidatureEtudiant.dateDepot,
      resultat: candidatureEtudiant.resultat,
      etudiant: candidatureEtudiant.etudiant,
      formationInitiale: candidatureEtudiant.formationInitiale,
    });

    this.etudiantsSharedCollection = this.etudiantService.addEtudiantToCollectionIfMissing(
      this.etudiantsSharedCollection,
      candidatureEtudiant.etudiant
    );
    this.formationInitialesSharedCollection = this.formationInitialeService.addFormationInitialeToCollectionIfMissing(
      this.formationInitialesSharedCollection,
      candidatureEtudiant.formationInitiale
    );
  }

  protected loadRelationshipsOptions(): void {
    this.etudiantService
      .query()
      .pipe(map((res: HttpResponse<IEtudiant[]>) => res.body ?? []))
      .pipe(
        map((etudiants: IEtudiant[]) =>
          this.etudiantService.addEtudiantToCollectionIfMissing(etudiants, this.editForm.get('etudiant')!.value)
        )
      )
      .subscribe((etudiants: IEtudiant[]) => (this.etudiantsSharedCollection = etudiants));

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

  protected createFromForm(): ICandidatureEtudiant {
    return {
      ...new CandidatureEtudiant(),
      id: this.editForm.get(['id'])!.value,
      offreFormation: this.editForm.get(['offreFormation'])!.value,
      dateDebutOffre: this.editForm.get(['dateDebutOffre'])!.value,
      dateFinOffre: this.editForm.get(['dateFinOffre'])!.value,
      dateDepot: this.editForm.get(['dateDepot'])!.value,
      resultat: this.editForm.get(['resultat'])!.value,
      etudiant: this.editForm.get(['etudiant'])!.value,
      formationInitiale: this.editForm.get(['formationInitiale'])!.value,
    };
  }
}
