import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IEtablissement, Etablissement } from '../etablissement.model';
import { EtablissementService } from '../service/etablissement.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { NomEtablissement } from 'app/entities/enumerations/nom-etablissement.model';
import { NomRegion } from 'app/entities/enumerations/nom-region.model';
import { NomDepartement } from 'app/entities/enumerations/nom-departement.model';
import { TypeEtablissement } from 'app/entities/enumerations/type-etablissement.model';
import { StatutEtab } from 'app/entities/enumerations/statut-etab.model';
import { CFP } from 'app/entities/enumerations/cfp.model';
import { LYCEE } from 'app/entities/enumerations/lycee.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { NomSerie } from 'app/entities/enumerations/nom-serie.model';

@Component({
  selector: 'jhi-etablissement-update',
  templateUrl: './etablissement-update.component.html',
})
export class EtablissementUpdateComponent implements OnInit {
  isSaving = false;
  nomEtablissementValues = Object.keys(NomEtablissement);
  nomRegionValues = Object.keys(NomRegion);
  nomDepartementValues = Object.keys(NomDepartement);
  typeEtablissementValues = Object.keys(TypeEtablissement);
  statutEtabValues = Object.keys(StatutEtab);
  cFPValues = Object.keys(CFP);
  lYCEEValues = Object.keys(LYCEE);
  nomFiliereValues = Object.keys(NomFiliere);
  nomSerieValues = Object.keys(NomSerie);

  editForm = this.fb.group({
    id: [],
    nomEtablissement: [null, []],
    photo: [],
    photoContentType: [],
    region: [null, [Validators.required]],
    departement: [null, [Validators.required]],
    email: [],
    telephone: [],
    typeEtablissement: [],
    statut: [null, [Validators.required]],
    autreRegion: [],
    autreDepartement: [],
    cfp: [],
    lycee: [],
    filiere: [],
    serie: [],
    autreFiliere: [],
    autreSerie: [],
    autreNomEtablissement: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected etablissementService: EtablissementService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ etablissement }) => {
      this.updateForm(etablissement);
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
    const etablissement = this.createFromForm();
    if (etablissement.id !== undefined) {
      this.subscribeToSaveResponse(this.etablissementService.update(etablissement));
    } else {
      this.subscribeToSaveResponse(this.etablissementService.create(etablissement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEtablissement>>): void {
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

  protected updateForm(etablissement: IEtablissement): void {
    this.editForm.patchValue({
      id: etablissement.id,
      nomEtablissement: etablissement.nomEtablissement,
      photo: etablissement.photo,
      photoContentType: etablissement.photoContentType,
      region: etablissement.region,
      departement: etablissement.departement,
      email: etablissement.email,
      telephone: etablissement.telephone,
      typeEtablissement: etablissement.typeEtablissement,
      statut: etablissement.statut,
      autreRegion: etablissement.autreRegion,
      autreDepartement: etablissement.autreDepartement,
      cfp: etablissement.cfp,
      lycee: etablissement.lycee,
      filiere: etablissement.filiere,
      serie: etablissement.serie,
      autreFiliere: etablissement.autreFiliere,
      autreSerie: etablissement.autreSerie,
      autreNomEtablissement: etablissement.autreNomEtablissement,
    });
  }

  protected createFromForm(): IEtablissement {
    return {
      ...new Etablissement(),
      id: this.editForm.get(['id'])!.value,
      nomEtablissement: this.editForm.get(['nomEtablissement'])!.value,
      photoContentType: this.editForm.get(['photoContentType'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      region: this.editForm.get(['region'])!.value,
      departement: this.editForm.get(['departement'])!.value,
      email: this.editForm.get(['email'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      typeEtablissement: this.editForm.get(['typeEtablissement'])!.value,
      statut: this.editForm.get(['statut'])!.value,
      autreRegion: this.editForm.get(['autreRegion'])!.value,
      autreDepartement: this.editForm.get(['autreDepartement'])!.value,
      cfp: this.editForm.get(['cfp'])!.value,
      lycee: this.editForm.get(['lycee'])!.value,
      filiere: this.editForm.get(['filiere'])!.value,
      serie: this.editForm.get(['serie'])!.value,
      autreFiliere: this.editForm.get(['autreFiliere'])!.value,
      autreSerie: this.editForm.get(['autreSerie'])!.value,
      autreNomEtablissement: this.editForm.get(['autreNomEtablissement'])!.value,
    };
  }
}
