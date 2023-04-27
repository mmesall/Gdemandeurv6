import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFormationInitiale, FormationInitiale } from '../formation-initiale.model';
import { FormationInitialeService } from '../service/formation-initiale.service';
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
  selector: 'jhi-formation-initiale-update',
  templateUrl: './formation-initiale-update.component.html',
})
export class FormationInitialeUpdateComponent implements OnInit {
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
    nomFormationI: [],
    duree: [],
    admission: [],
    diplomeRequis: [],
    niveauEtude: [],
    ficheFormation: [],
    ficheFormationContentType: [],
    filiere: [],
    serie: [],
    cfp: [],
    lycee: [],
    nomConcours: [],
    dateOuverture: [],
    dateCloture: [],
    dateConcours: [],
    nomDiplome: [],
    nomDebouche: [],
    formation: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected formationInitialeService: FormationInitialeService,
    protected formationService: FormationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formationInitiale }) => {
      this.updateForm(formationInitiale);

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
    const formationInitiale = this.createFromForm();
    if (formationInitiale.id !== undefined) {
      this.subscribeToSaveResponse(this.formationInitialeService.update(formationInitiale));
    } else {
      this.subscribeToSaveResponse(this.formationInitialeService.create(formationInitiale));
    }
  }

  trackFormationById(index: number, item: IFormation): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormationInitiale>>): void {
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

  protected updateForm(formationInitiale: IFormationInitiale): void {
    this.editForm.patchValue({
      id: formationInitiale.id,
      nomFormationI: formationInitiale.nomFormationI,
      duree: formationInitiale.duree,
      admission: formationInitiale.admission,
      diplomeRequis: formationInitiale.diplomeRequis,
      niveauEtude: formationInitiale.niveauEtude,
      ficheFormation: formationInitiale.ficheFormation,
      ficheFormationContentType: formationInitiale.ficheFormationContentType,
      filiere: formationInitiale.filiere,
      serie: formationInitiale.serie,
      cfp: formationInitiale.cfp,
      lycee: formationInitiale.lycee,
      nomConcours: formationInitiale.nomConcours,
      dateOuverture: formationInitiale.dateOuverture,
      dateCloture: formationInitiale.dateCloture,
      dateConcours: formationInitiale.dateConcours,
      nomDiplome: formationInitiale.nomDiplome,
      nomDebouche: formationInitiale.nomDebouche,
      formation: formationInitiale.formation,
    });

    this.formationsCollection = this.formationService.addFormationToCollectionIfMissing(
      this.formationsCollection,
      formationInitiale.formation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.formationService
      .query({ filter: 'formationinitiale-is-null' })
      .pipe(map((res: HttpResponse<IFormation[]>) => res.body ?? []))
      .pipe(
        map((formations: IFormation[]) =>
          this.formationService.addFormationToCollectionIfMissing(formations, this.editForm.get('formation')!.value)
        )
      )
      .subscribe((formations: IFormation[]) => (this.formationsCollection = formations));
  }

  protected createFromForm(): IFormationInitiale {
    return {
      ...new FormationInitiale(),
      id: this.editForm.get(['id'])!.value,
      nomFormationI: this.editForm.get(['nomFormationI'])!.value,
      duree: this.editForm.get(['duree'])!.value,
      admission: this.editForm.get(['admission'])!.value,
      diplomeRequis: this.editForm.get(['diplomeRequis'])!.value,
      niveauEtude: this.editForm.get(['niveauEtude'])!.value,
      ficheFormationContentType: this.editForm.get(['ficheFormationContentType'])!.value,
      ficheFormation: this.editForm.get(['ficheFormation'])!.value,
      filiere: this.editForm.get(['filiere'])!.value,
      serie: this.editForm.get(['serie'])!.value,
      cfp: this.editForm.get(['cfp'])!.value,
      lycee: this.editForm.get(['lycee'])!.value,
      nomConcours: this.editForm.get(['nomConcours'])!.value,
      dateOuverture: this.editForm.get(['dateOuverture'])!.value,
      dateCloture: this.editForm.get(['dateCloture'])!.value,
      dateConcours: this.editForm.get(['dateConcours'])!.value,
      nomDiplome: this.editForm.get(['nomDiplome'])!.value,
      nomDebouche: this.editForm.get(['nomDebouche'])!.value,
      formation: this.editForm.get(['formation'])!.value,
    };
  }
}
