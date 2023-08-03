import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ConcoursFormService, ConcoursFormGroup } from './concours-form.service';
import { IConcours } from '../concours.model';
import { ConcoursService } from '../service/concours.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IFormation } from 'app/entities/formation/formation.model';
import { FormationService } from 'app/entities/formation/service/formation.service';
import { NomEtablissement } from 'app/entities/enumerations/nom-etablissement.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';

@Component({
  standalone: true,
  selector: 'jhi-concours-update',
  templateUrl: './concours-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ConcoursUpdateComponent implements OnInit {
  isSaving = false;
  concours: IConcours | null = null;
  nomEtablissementValues = Object.keys(NomEtablissement);
  niveauEtudeValues = Object.keys(NiveauEtude);

  formationsCollection: IFormation[] = [];

  editForm: ConcoursFormGroup = this.concoursFormService.createConcoursFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected concoursService: ConcoursService,
    protected concoursFormService: ConcoursFormService,
    protected formationService: FormationService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareFormation = (o1: IFormation | null, o2: IFormation | null): boolean => this.formationService.compareFormation(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ concours }) => {
      this.concours = concours;
      if (concours) {
        this.updateForm(concours);
      }

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
    const concours = this.concoursFormService.getConcours(this.editForm);
    if (concours.id !== null) {
      this.subscribeToSaveResponse(this.concoursService.update(concours));
    } else {
      this.subscribeToSaveResponse(this.concoursService.create(concours));
    }
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
    this.concours = concours;
    this.concoursFormService.resetForm(this.editForm, concours);

    this.formationsCollection = this.formationService.addFormationToCollectionIfMissing<IFormation>(
      this.formationsCollection,
      concours.formation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.formationService
      .query({ filter: 'concours-is-null' })
      .pipe(map((res: HttpResponse<IFormation[]>) => res.body ?? []))
      .pipe(
        map((formations: IFormation[]) =>
          this.formationService.addFormationToCollectionIfMissing<IFormation>(formations, this.concours?.formation)
        )
      )
      .subscribe((formations: IFormation[]) => (this.formationsCollection = formations));
  }
}
