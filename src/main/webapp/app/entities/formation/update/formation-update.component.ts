import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFormation, Formation } from '../formation.model';
import { FormationService } from '../service/formation.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { EtablissementService } from 'app/entities/etablissement/service/etablissement.service';
import { TypeFormation } from 'app/entities/enumerations/type-formation.model';
import { Admission } from 'app/entities/enumerations/admission.model';
import { DiplomeRequis } from 'app/entities/enumerations/diplome-requis.model';

@Component({
  selector: 'jhi-formation-update',
  templateUrl: './formation-update.component.html',
})
export class FormationUpdateComponent implements OnInit {
  isSaving = false;
  typeFormationValues = Object.keys(TypeFormation);
  admissionValues = Object.keys(Admission);
  diplomeRequisValues = Object.keys(DiplomeRequis);

  etablissementsSharedCollection: IEtablissement[] = [];

  editForm = this.fb.group({
    id: [],
    nomFormation: [],
    imageFormation: [],
    imageFormationContentType: [],
    typeFormation: [],
    duree: [],
    admission: [],
    diplomeRequis: [],
    ficheFormation: [],
    ficheFormationContentType: [],
    etablissements: [null, Validators.required],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected formationService: FormationService,
    protected etablissementService: EtablissementService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ formation }) => {
      this.updateForm(formation);

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
    const formation = this.createFromForm();
    if (formation.id !== undefined) {
      this.subscribeToSaveResponse(this.formationService.update(formation));
    } else {
      this.subscribeToSaveResponse(this.formationService.create(formation));
    }
  }

  trackEtablissementById(index: number, item: IEtablissement): number {
    return item.id!;
  }

  getSelectedEtablissement(option: IEtablissement, selectedVals?: IEtablissement[]): IEtablissement {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFormation>>): void {
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

  protected updateForm(formation: IFormation): void {
    this.editForm.patchValue({
      id: formation.id,
      nomFormation: formation.nomFormation,
      imageFormation: formation.imageFormation,
      imageFormationContentType: formation.imageFormationContentType,
      typeFormation: formation.typeFormation,
      duree: formation.duree,
      admission: formation.admission,
      diplomeRequis: formation.diplomeRequis,
      ficheFormation: formation.ficheFormation,
      ficheFormationContentType: formation.ficheFormationContentType,
      etablissements: formation.etablissements,
    });

    this.etablissementsSharedCollection = this.etablissementService.addEtablissementToCollectionIfMissing(
      this.etablissementsSharedCollection,
      ...(formation.etablissements ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.etablissementService
      .query()
      .pipe(map((res: HttpResponse<IEtablissement[]>) => res.body ?? []))
      .pipe(
        map((etablissements: IEtablissement[]) =>
          this.etablissementService.addEtablissementToCollectionIfMissing(
            etablissements,
            ...(this.editForm.get('etablissements')!.value ?? [])
          )
        )
      )
      .subscribe((etablissements: IEtablissement[]) => (this.etablissementsSharedCollection = etablissements));
  }

  protected createFromForm(): IFormation {
    return {
      ...new Formation(),
      id: this.editForm.get(['id'])!.value,
      nomFormation: this.editForm.get(['nomFormation'])!.value,
      imageFormationContentType: this.editForm.get(['imageFormationContentType'])!.value,
      imageFormation: this.editForm.get(['imageFormation'])!.value,
      typeFormation: this.editForm.get(['typeFormation'])!.value,
      duree: this.editForm.get(['duree'])!.value,
      admission: this.editForm.get(['admission'])!.value,
      diplomeRequis: this.editForm.get(['diplomeRequis'])!.value,
      ficheFormationContentType: this.editForm.get(['ficheFormationContentType'])!.value,
      ficheFormation: this.editForm.get(['ficheFormation'])!.value,
      etablissements: this.editForm.get(['etablissements'])!.value,
    };
  }
}
