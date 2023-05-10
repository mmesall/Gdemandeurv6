import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICandidatureP, CandidatureP } from '../candidature-p.model';
import { CandidaturePService } from '../service/candidature-p.service';
import { IProfessionnel } from 'app/entities/professionnel/professionnel.model';
import { ProfessionnelService } from 'app/entities/professionnel/service/professionnel.service';
import { IFormationContinue } from 'app/entities/formation-continue/formation-continue.model';
import { FormationContinueService } from 'app/entities/formation-continue/service/formation-continue.service';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { Resultat } from 'app/entities/enumerations/resultat.model';

@Component({
  selector: 'jhi-candidature-p-update',
  templateUrl: './candidature-p-update.component.html',
})
export class CandidaturePUpdateComponent implements OnInit {
  isSaving = false;
  nomFiliereValues = Object.keys(NomFiliere);
  resultatValues = Object.keys(Resultat);

  professionnelsSharedCollection: IProfessionnel[] = [];
  formationContinuesSharedCollection: IFormationContinue[] = [];
  etablissementsSharedCollection: IEtablissement[] = [];

  editForm = this.fb.group({
    id: [],
    offreFormation: [],
    dateDebutOffre: [],
    dateFinOffre: [],
    dateDepot: [],
    resultat: [],
    professionnel: [],
    formationContinue: [],
    etablissement: [],
  });

  constructor(
    protected candidaturePService: CandidaturePService,
    protected professionnelService: ProfessionnelService,
    protected formationContinueService: FormationContinueService,
    protected etablissementService: EtablissementService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ candidatureP }) => {
      this.updateForm(candidatureP);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const candidatureP = this.createFromForm();
    if (candidatureP.id !== undefined) {
      this.subscribeToSaveResponse(this.candidaturePService.update(candidatureP));
    } else {
      this.subscribeToSaveResponse(this.candidaturePService.create(candidatureP));
    }
  }

  trackProfessionnelById(index: number, item: IProfessionnel): number {
    return item.id!;
  }

  trackFormationContinueById(index: number, item: IFormationContinue): number {
    return item.id!;
  }

  trackEtablissementById(index: number, item: IEtablissement): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICandidatureP>>): void {
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

  protected updateForm(candidatureP: ICandidatureP): void {
    this.editForm.patchValue({
      id: candidatureP.id,
      offreFormation: candidatureP.offreFormation,
      dateDebutOffre: candidatureP.dateDebutOffre,
      dateFinOffre: candidatureP.dateFinOffre,
      dateDepot: candidatureP.dateDepot,
      resultat: candidatureP.resultat,
      professionnel: candidatureP.professionnel,
      formationContinue: candidatureP.formationContinue,
      etablissement: candidatureP.etablissement,
    });

    this.professionnelsSharedCollection = this.professionnelService.addProfessionnelToCollectionIfMissing(
      this.professionnelsSharedCollection,
      candidatureP.professionnel
    );
    this.formationContinuesSharedCollection = this.formationContinueService.addFormationContinueToCollectionIfMissing(
      this.formationContinuesSharedCollection,
      candidatureP.formationContinue
    );
    this.etablissementsSharedCollection = this.etablissementService.addEtablissementToCollectionIfMissing(
      this.etablissementsSharedCollection,
      candidatureP.etablissement
    );
  }

  protected loadRelationshipsOptions(): void {
    this.professionnelService
      .query()
      .pipe(map((res: HttpResponse<IProfessionnel[]>) => res.body ?? []))
      .pipe(
        map((professionnels: IProfessionnel[]) =>
          this.professionnelService.addProfessionnelToCollectionIfMissing(professionnels, this.editForm.get('professionnel')!.value)
        )
      )
      .subscribe((professionnels: IProfessionnel[]) => (this.professionnelsSharedCollection = professionnels));

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

  protected createFromForm(): ICandidatureP {
    return {
      ...new CandidatureP(),
      id: this.editForm.get(['id'])!.value,
      offreFormation: this.editForm.get(['offreFormation'])!.value,
      dateDebutOffre: this.editForm.get(['dateDebutOffre'])!.value,
      dateFinOffre: this.editForm.get(['dateFinOffre'])!.value,
      dateDepot: this.editForm.get(['dateDepot'])!.value,
      resultat: this.editForm.get(['resultat'])!.value,
      professionnel: this.editForm.get(['professionnel'])!.value,
      formationContinue: this.editForm.get(['formationContinue'])!.value,
      etablissement: this.editForm.get(['etablissement'])!.value,
    };
  }
}
