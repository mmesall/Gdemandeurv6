import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDiplome, Diplome } from '../diplome.model';
import { DiplomeService } from '../service/diplome.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IEleve } from 'app/entities/eleve/eleve.model';
import { EleveService } from 'app/entities/eleve/service/eleve.service';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { IProfessionnel } from 'app/entities/professionnel/professionnel.model';
import { ProfessionnelService } from 'app/entities/professionnel/service/professionnel.service';
import { IDemandeur } from 'app/entities/demandeur/demandeur.model';
import { DemandeurService } from 'app/entities/demandeur/service/demandeur.service';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';
import { Mention } from 'app/entities/enumerations/mention.model';

@Component({
  selector: 'jhi-diplome-update',
  templateUrl: './diplome-update.component.html',
})
export class DiplomeUpdateComponent implements OnInit {
  isSaving = false;
  nomFiliereValues = Object.keys(NomFiliere);
  niveauEtudeValues = Object.keys(NiveauEtude);
  mentionValues = Object.keys(Mention);

  elevesSharedCollection: IEleve[] = [];
  etudiantsSharedCollection: IEtudiant[] = [];
  professionnelsSharedCollection: IProfessionnel[] = [];
  demandeursSharedCollection: IDemandeur[] = [];

  editForm = this.fb.group({
    id: [],
    intitule: [],
    domaine: [null, [Validators.required]],
    niveau: [],
    mention: [],
    anneeObtention: [],
    etablissement: [],
    document: [null, [Validators.required]],
    documentContentType: [],
    eleve: [],
    etudiant: [],
    professionnel: [],
    demandeur: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected diplomeService: DiplomeService,
    protected eleveService: EleveService,
    protected etudiantService: EtudiantService,
    protected professionnelService: ProfessionnelService,
    protected demandeurService: DemandeurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ diplome }) => {
      this.updateForm(diplome);

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
    const diplome = this.createFromForm();
    if (diplome.id !== undefined) {
      this.subscribeToSaveResponse(this.diplomeService.update(diplome));
    } else {
      this.subscribeToSaveResponse(this.diplomeService.create(diplome));
    }
  }

  trackEleveById(index: number, item: IEleve): number {
    return item.id!;
  }

  trackEtudiantById(index: number, item: IEtudiant): number {
    return item.id!;
  }

  trackProfessionnelById(index: number, item: IProfessionnel): number {
    return item.id!;
  }

  trackDemandeurById(index: number, item: IDemandeur): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDiplome>>): void {
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

  protected updateForm(diplome: IDiplome): void {
    this.editForm.patchValue({
      id: diplome.id,
      intitule: diplome.intitule,
      domaine: diplome.domaine,
      niveau: diplome.niveau,
      mention: diplome.mention,
      anneeObtention: diplome.anneeObtention,
      etablissement: diplome.etablissement,
      document: diplome.document,
      documentContentType: diplome.documentContentType,
      eleve: diplome.eleve,
      etudiant: diplome.etudiant,
      professionnel: diplome.professionnel,
      demandeur: diplome.demandeur,
    });

    this.elevesSharedCollection = this.eleveService.addEleveToCollectionIfMissing(this.elevesSharedCollection, diplome.eleve);
    this.etudiantsSharedCollection = this.etudiantService.addEtudiantToCollectionIfMissing(
      this.etudiantsSharedCollection,
      diplome.etudiant
    );
    this.professionnelsSharedCollection = this.professionnelService.addProfessionnelToCollectionIfMissing(
      this.professionnelsSharedCollection,
      diplome.professionnel
    );
    this.demandeursSharedCollection = this.demandeurService.addDemandeurToCollectionIfMissing(
      this.demandeursSharedCollection,
      diplome.demandeur
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

    this.professionnelService
      .query()
      .pipe(map((res: HttpResponse<IProfessionnel[]>) => res.body ?? []))
      .pipe(
        map((professionnels: IProfessionnel[]) =>
          this.professionnelService.addProfessionnelToCollectionIfMissing(professionnels, this.editForm.get('professionnel')!.value)
        )
      )
      .subscribe((professionnels: IProfessionnel[]) => (this.professionnelsSharedCollection = professionnels));

    this.demandeurService
      .query()
      .pipe(map((res: HttpResponse<IDemandeur[]>) => res.body ?? []))
      .pipe(
        map((demandeurs: IDemandeur[]) =>
          this.demandeurService.addDemandeurToCollectionIfMissing(demandeurs, this.editForm.get('demandeur')!.value)
        )
      )
      .subscribe((demandeurs: IDemandeur[]) => (this.demandeursSharedCollection = demandeurs));
  }

  protected createFromForm(): IDiplome {
    return {
      ...new Diplome(),
      id: this.editForm.get(['id'])!.value,
      intitule: this.editForm.get(['intitule'])!.value,
      domaine: this.editForm.get(['domaine'])!.value,
      niveau: this.editForm.get(['niveau'])!.value,
      mention: this.editForm.get(['mention'])!.value,
      anneeObtention: this.editForm.get(['anneeObtention'])!.value,
      etablissement: this.editForm.get(['etablissement'])!.value,
      documentContentType: this.editForm.get(['documentContentType'])!.value,
      document: this.editForm.get(['document'])!.value,
      eleve: this.editForm.get(['eleve'])!.value,
      etudiant: this.editForm.get(['etudiant'])!.value,
      professionnel: this.editForm.get(['professionnel'])!.value,
      demandeur: this.editForm.get(['demandeur'])!.value,
    };
  }
}
