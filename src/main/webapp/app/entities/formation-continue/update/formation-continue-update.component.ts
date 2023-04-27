import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFormationContinue, FormationContinue } from '../formation-continue.model';
import { FormationContinueService } from '../service/formation-continue.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IFormation } from 'app/entities/formation/formation.model';
import { FormationService } from 'app/entities/formation/service/formation.service';
import { Admission } from 'app/entities/enumerations/admission.model';
import { DiplomeRequis } from 'app/entities/enumerations/diplome-requis.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { NomSerie } from 'app/entities/enumerations/nom-serie.model';
import { CFP } from 'app/entities/enumerations/cfp.model';
import { LYCEE } from 'app/entities/enumerations/lycee.model';
import { DiplomeObtenu } from 'app/entities/enumerations/diplome-obtenu.model';

@Component({
  selector: 'jhi-formation-continue-update',
  templateUrl: './formation-continue-update.component.html',
})
export class FormationContinueUpdateComponent implements OnInit {
  isSaving = false;
  admissionValues = Object.keys(Admission);
  diplomeRequisValues = Object.keys(DiplomeRequis);
  niveauEtudeValues = Object.keys(NiveauEtude);
  nomFiliereValues = Object.keys(NomFiliere);
  nomSerieValues = Object.keys(NomSerie);
  cFPValues = Object.keys(CFP);
  lYCEEValues = Object.keys(LYCEE);
  diplomeObtenuValues = Object.keys(DiplomeObtenu);

  formationsCollection: IFormation[] = [];

  editForm = this.fb.group({
    id: [],
    nomFormationC: [],
    duree: [],
    admission: [],
    diplomeRequis: [],
    niveauEtude: [],
    filiere: [],
    serie: [],
    cfp: [],
    lycee: [],
    ficheFormation: [],
    ficheFormationContentType: [],
    libellePC: [],
    montantPriseEnCharge: [],
    coutFormation: [],
    detailPC: [],
    nomDiplome: [],
    autreDiplome: [],
    nomDebouche: [],
    formation: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected formationContinueService: FormationContinueService,
    protected formationService: FormationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formationContinue }) => {
      this.updateForm(formationContinue);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('jhipsterProjectBaseJwtApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const formationContinue = this.createFromForm();
    if (formationContinue.id !== undefined) {
      this.subscribeToSaveResponse(this.formationContinueService.update(formationContinue));
    } else {
      this.subscribeToSaveResponse(this.formationContinueService.create(formationContinue));
    }
  }

  trackFormationById(index: number, item: IFormation): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormationContinue>>): void {
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

  protected updateForm(formationContinue: IFormationContinue): void {
    this.editForm.patchValue({
      id: formationContinue.id,
      nomFormationC: formationContinue.nomFormationC,
      duree: formationContinue.duree,
      admission: formationContinue.admission,
      diplomeRequis: formationContinue.diplomeRequis,
      niveauEtude: formationContinue.niveauEtude,
      filiere: formationContinue.filiere,
      serie: formationContinue.serie,
      cfp: formationContinue.cfp,
      lycee: formationContinue.lycee,
      ficheFormation: formationContinue.ficheFormation,
      ficheFormationContentType: formationContinue.ficheFormationContentType,
      libellePC: formationContinue.libellePC,
      montantPriseEnCharge: formationContinue.montantPriseEnCharge,
      coutFormation: formationContinue.coutFormation,
      detailPC: formationContinue.detailPC,
      nomDiplome: formationContinue.nomDiplome,
      autreDiplome: formationContinue.autreDiplome,
      nomDebouche: formationContinue.nomDebouche,
      formation: formationContinue.formation,
    });

    this.formationsCollection = this.formationService.addFormationToCollectionIfMissing(
      this.formationsCollection,
      formationContinue.formation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.formationService
      .query({ filter: 'formationcontinue-is-null' })
      .pipe(map((res: HttpResponse<IFormation[]>) => res.body ?? []))
      .pipe(
        map((formations: IFormation[]) =>
          this.formationService.addFormationToCollectionIfMissing(formations, this.editForm.get('formation')!.value)
        )
      )
      .subscribe((formations: IFormation[]) => (this.formationsCollection = formations));
  }

  protected createFromForm(): IFormationContinue {
    return {
      ...new FormationContinue(),
      id: this.editForm.get(['id'])!.value,
      nomFormationC: this.editForm.get(['nomFormationC'])!.value,
      duree: this.editForm.get(['duree'])!.value,
      admission: this.editForm.get(['admission'])!.value,
      diplomeRequis: this.editForm.get(['diplomeRequis'])!.value,
      niveauEtude: this.editForm.get(['niveauEtude'])!.value,
      filiere: this.editForm.get(['filiere'])!.value,
      serie: this.editForm.get(['serie'])!.value,
      cfp: this.editForm.get(['cfp'])!.value,
      lycee: this.editForm.get(['lycee'])!.value,
      ficheFormationContentType: this.editForm.get(['ficheFormationContentType'])!.value,
      ficheFormation: this.editForm.get(['ficheFormation'])!.value,
      libellePC: this.editForm.get(['libellePC'])!.value,
      montantPriseEnCharge: this.editForm.get(['montantPriseEnCharge'])!.value,
      coutFormation: this.editForm.get(['coutFormation'])!.value,
      detailPC: this.editForm.get(['detailPC'])!.value,
      nomDiplome: this.editForm.get(['nomDiplome'])!.value,
      autreDiplome: this.editForm.get(['autreDiplome'])!.value,
      nomDebouche: this.editForm.get(['nomDebouche'])!.value,
      formation: this.editForm.get(['formation'])!.value,
    };
  }
}
