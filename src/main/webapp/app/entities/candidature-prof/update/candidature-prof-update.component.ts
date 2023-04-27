import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICandidatureProf, CandidatureProf } from '../candidature-prof.model';
import { CandidatureProfService } from '../service/candidature-prof.service';
import { IProfessionnel } from 'app/entities/professionnel/professionnel.model';
import { ProfessionnelService } from 'app/entities/professionnel/service/professionnel.service';
import { IFormationContinue } from 'app/entities/formation-continue/formation-continue.model';
import { FormationContinueService } from 'app/entities/formation-continue/service/formation-continue.service';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { Resultat } from 'app/entities/enumerations/resultat.model';

@Component({
  selector: 'jhi-candidature-prof-update',
  templateUrl: './candidature-prof-update.component.html',
})
export class CandidatureProfUpdateComponent implements OnInit {
  isSaving = false;
  nomFiliereValues = Object.keys(NomFiliere);
  resultatValues = Object.keys(Resultat);

  professionnelsSharedCollection: IProfessionnel[] = [];
  formationContinuesSharedCollection: IFormationContinue[] = [];

  editForm = this.fb.group({
    id: [],
    offreFormation: [],
    dateDebutOffre: [],
    dateFinOffre: [],
    dateDepot: [],
    resultat: [],
    professionnel: [],
    formationContinue: [],
  });

  constructor(
    protected candidatureProfService: CandidatureProfService,
    protected professionnelService: ProfessionnelService,
    protected formationContinueService: FormationContinueService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ candidatureProf }) => {
      this.updateForm(candidatureProf);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const candidatureProf = this.createFromForm();
    if (candidatureProf.id !== undefined) {
      this.subscribeToSaveResponse(this.candidatureProfService.update(candidatureProf));
    } else {
      this.subscribeToSaveResponse(this.candidatureProfService.create(candidatureProf));
    }
  }

  trackProfessionnelById(index: number, item: IProfessionnel): number {
    return item.id!;
  }

  trackFormationContinueById(index: number, item: IFormationContinue): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICandidatureProf>>): void {
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

  protected updateForm(candidatureProf: ICandidatureProf): void {
    this.editForm.patchValue({
      id: candidatureProf.id,
      offreFormation: candidatureProf.offreFormation,
      dateDebutOffre: candidatureProf.dateDebutOffre,
      dateFinOffre: candidatureProf.dateFinOffre,
      dateDepot: candidatureProf.dateDepot,
      resultat: candidatureProf.resultat,
      professionnel: candidatureProf.professionnel,
      formationContinue: candidatureProf.formationContinue,
    });

    this.professionnelsSharedCollection = this.professionnelService.addProfessionnelToCollectionIfMissing(
      this.professionnelsSharedCollection,
      candidatureProf.professionnel
    );
    this.formationContinuesSharedCollection = this.formationContinueService.addFormationContinueToCollectionIfMissing(
      this.formationContinuesSharedCollection,
      candidatureProf.formationContinue
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
  }

  protected createFromForm(): ICandidatureProf {
    return {
      ...new CandidatureProf(),
      id: this.editForm.get(['id'])!.value,
      offreFormation: this.editForm.get(['offreFormation'])!.value,
      dateDebutOffre: this.editForm.get(['dateDebutOffre'])!.value,
      dateFinOffre: this.editForm.get(['dateFinOffre'])!.value,
      dateDepot: this.editForm.get(['dateDepot'])!.value,
      resultat: this.editForm.get(['resultat'])!.value,
      professionnel: this.editForm.get(['professionnel'])!.value,
      formationContinue: this.editForm.get(['formationContinue'])!.value,
    };
  }
}
