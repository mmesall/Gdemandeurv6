import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICandidatureE, CandidatureE } from '../candidature-e.model';
import { CandidatureEService } from '../service/candidature-e.service';
import { IEleve } from 'app/entities/eleve/eleve.model';
import { EleveService } from 'app/entities/eleve/service/eleve.service';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { IFormationInitiale } from 'app/entities/formation-initiale/formation-initiale.model';
import { FormationInitialeService } from 'app/entities/formation-initiale/service/formation-initiale.service';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { Resultat } from 'app/entities/enumerations/resultat.model';

@Component({
  selector: 'jhi-candidature-e-update',
  templateUrl: './candidature-e-update.component.html',
})
export class CandidatureEUpdateComponent implements OnInit {
  isSaving = false;
  nomFiliereValues = Object.keys(NomFiliere);
  resultatValues = Object.keys(Resultat);

  elevesSharedCollection: IEleve[] = [];
  etudiantsSharedCollection: IEtudiant[] = [];
  formationInitialesSharedCollection: IFormationInitiale[] = [];
  etablissementsSharedCollection: IEtablissement[] = [];

  editForm = this.fb.group({
    id: [],
    offreFormation: [],
    dateDebutOffre: [],
    dateFinOffre: [],
    dateDepot: [],
    resultat: [],
    eleve: [],
    etudiant: [],
    formationInitiale: [],
    etablissement: [],
  });

  constructor(
    protected candidatureEService: CandidatureEService,
    protected eleveService: EleveService,
    protected etudiantService: EtudiantService,
    protected formationInitialeService: FormationInitialeService,
    protected etablissementService: EtablissementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ candidatureE }) => {
      this.updateForm(candidatureE);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const candidatureE = this.createFromForm();
    if (candidatureE.id !== undefined) {
      this.subscribeToSaveResponse(this.candidatureEService.update(candidatureE));
    } else {
      this.subscribeToSaveResponse(this.candidatureEService.create(candidatureE));
    }
  }

  trackEleveById(index: number, item: IEleve): number {
    return item.id!;
  }

  trackEtudiantById(index: number, item: IEtudiant): number {
    return item.id!;
  }

  trackFormationInitialeById(index: number, item: IFormationInitiale): number {
    return item.id!;
  }

  trackEtablissementById(index: number, item: IEtablissement): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICandidatureE>>): void {
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

  protected updateForm(candidatureE: ICandidatureE): void {
    this.editForm.patchValue({
      id: candidatureE.id,
      offreFormation: candidatureE.offreFormation,
      dateDebutOffre: candidatureE.dateDebutOffre,
      dateFinOffre: candidatureE.dateFinOffre,
      dateDepot: candidatureE.dateDepot,
      resultat: candidatureE.resultat,
      eleve: candidatureE.eleve,
      etudiant: candidatureE.etudiant,
      formationInitiale: candidatureE.formationInitiale,
      etablissement: candidatureE.etablissement,
    });

    this.elevesSharedCollection = this.eleveService.addEleveToCollectionIfMissing(this.elevesSharedCollection, candidatureE.eleve);
    this.etudiantsSharedCollection = this.etudiantService.addEtudiantToCollectionIfMissing(
      this.etudiantsSharedCollection,
      candidatureE.etudiant
    );
    this.formationInitialesSharedCollection = this.formationInitialeService.addFormationInitialeToCollectionIfMissing(
      this.formationInitialesSharedCollection,
      candidatureE.formationInitiale
    );
    this.etablissementsSharedCollection = this.etablissementService.addEtablissementToCollectionIfMissing(
      this.etablissementsSharedCollection,
      candidatureE.etablissement
    );
  }

  protected loadRelationshipsOptions(): void {
    this.eleveService
      .query()
      .pipe(map((res: HttpResponse<IEleve[]>) => res.body ?? []))
      .pipe(map((eleves: IEleve[]) => this.eleveService.addEleveToCollectionIfMissing(eleves, this.editForm.get('eleve')!.value)))
      .subscribe((eleves: IEleve[]) => (this.elevesSharedCollection = eleves));

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

    this.etablissementService
      .query()
      .pipe(map((res: HttpResponse<IEtablissement[]>) => res.body ?? []))
      .pipe(
        map((etablissements: IEtablissement[]) =>
          this.etablissementService.addEtablissementToCollectionIfMissing(etablissements, this.editForm.get('etablissement')!.value)
        )
      )
      .subscribe((etablissements: IEtablissement[]) => (this.etablissementsSharedCollection = etablissements));
  }

  protected createFromForm(): ICandidatureE {
    return {
      ...new CandidatureE(),
      id: this.editForm.get(['id'])!.value,
      offreFormation: this.editForm.get(['offreFormation'])!.value,
      dateDebutOffre: this.editForm.get(['dateDebutOffre'])!.value,
      dateFinOffre: this.editForm.get(['dateFinOffre'])!.value,
      dateDepot: this.editForm.get(['dateDepot'])!.value,
      resultat: this.editForm.get(['resultat'])!.value,
      eleve: this.editForm.get(['eleve'])!.value,
      etudiant: this.editForm.get(['etudiant'])!.value,
      formationInitiale: this.editForm.get(['formationInitiale'])!.value,
      etablissement: this.editForm.get(['etablissement'])!.value,
    };
  }
}
