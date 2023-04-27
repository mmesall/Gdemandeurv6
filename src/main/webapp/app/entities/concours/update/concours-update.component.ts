import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IConcours, Concours } from '../concours.model';
import { ConcoursService } from '../service/concours.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IFormation } from 'app/entities/formation/formation.model';
import { FormationService } from 'app/entities/formation/service/formation.service';
import { NomEtablissement } from 'app/entities/enumerations/nom-etablissement.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';

@Component({
  selector: 'jhi-concours-update',
  templateUrl: './concours-update.component.html',
})
export class ConcoursUpdateComponent implements OnInit {
  isSaving = false;
  nomEtablissementValues = Object.keys(NomEtablissement);
  niveauEtudeValues = Object.keys(NiveauEtude);

  formationsCollection: IFormation[] = [];

  editForm = this.fb.group({
    id: [],
    nomConcours: [],
    nomEtablissement: [],
    niveauEtude: [],
    dateOuverture: [],
    dateCloture: [],
    dateConcours: [],
    affiche: [],
    afficheContentType: [],
    formation: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected concoursService: ConcoursService,
    protected formationService: FormationService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ concours }) => {
      this.updateForm(concours);

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

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const concours = this.createFromForm();
    if (concours.id !== undefined) {
      this.subscribeToSaveResponse(this.concoursService.update(concours));
    } else {
      this.subscribeToSaveResponse(this.concoursService.create(concours));
    }
  }

  trackFormationById(index: number, item: IFormation): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IConcours>>): void {
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

  protected updateForm(concours: IConcours): void {
    this.editForm.patchValue({
      id: concours.id,
      nomConcours: concours.nomConcours,
      nomEtablissement: concours.nomEtablissement,
      niveauEtude: concours.niveauEtude,
      dateOuverture: concours.dateOuverture,
      dateCloture: concours.dateCloture,
      dateConcours: concours.dateConcours,
      affiche: concours.affiche,
      afficheContentType: concours.afficheContentType,
      formation: concours.formation,
    });

    this.formationsCollection = this.formationService.addFormationToCollectionIfMissing(this.formationsCollection, concours.formation);
  }

  protected loadRelationshipsOptions(): void {
    this.formationService
      .query({ filter: 'concours-is-null' })
      .pipe(map((res: HttpResponse<IFormation[]>) => res.body ?? []))
      .pipe(
        map((formations: IFormation[]) =>
          this.formationService.addFormationToCollectionIfMissing(formations, this.editForm.get('formation')!.value)
        )
      )
      .subscribe((formations: IFormation[]) => (this.formationsCollection = formations));
  }

  protected createFromForm(): IConcours {
    return {
      ...new Concours(),
      id: this.editForm.get(['id'])!.value,
      nomConcours: this.editForm.get(['nomConcours'])!.value,
      nomEtablissement: this.editForm.get(['nomEtablissement'])!.value,
      niveauEtude: this.editForm.get(['niveauEtude'])!.value,
      dateOuverture: this.editForm.get(['dateOuverture'])!.value,
      dateCloture: this.editForm.get(['dateCloture'])!.value,
      dateConcours: this.editForm.get(['dateConcours'])!.value,
      afficheContentType: this.editForm.get(['afficheContentType'])!.value,
      affiche: this.editForm.get(['affiche'])!.value,
      formation: this.editForm.get(['formation'])!.value,
    };
  }
}
