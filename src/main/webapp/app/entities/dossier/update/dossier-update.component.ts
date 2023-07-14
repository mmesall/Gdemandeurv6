import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDossier, Dossier } from '../dossier.model';
import { DossierService } from '../service/dossier.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IEleve } from 'app/entities/eleve/eleve.model';
import { EleveService } from 'app/entities/eleve/service/eleve.service';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { EtudiantService } from 'app/entities/etudiant/service/etudiant.service';
import { IProfessionnel } from 'app/entities/professionnel/professionnel.model';
import { ProfessionnelService } from 'app/entities/professionnel/service/professionnel.service';
import { NomRegion } from 'app/entities/enumerations/nom-region.model';
import { NomDepartement } from 'app/entities/enumerations/nom-departement.model';
import { TypePiece } from 'app/entities/enumerations/type-piece.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { NomSerie } from 'app/entities/enumerations/nom-serie.model';
import { DiplomeRequis } from 'app/entities/enumerations/diplome-requis.model';
import { Account } from 'app/core/auth/account.model';

@Component({
  selector: 'jhi-dossier-update',
  templateUrl: './dossier-update.component.html',
})
export class DossierUpdateComponent implements OnInit {
  account!: Account;
  isSaving = false;
  nomRegionValues = Object.keys(NomRegion);
  nomDepartementValues = Object.keys(NomDepartement);
  typePieceValues = Object.keys(TypePiece);
  sexeValues = Object.keys(Sexe);
  niveauEtudeValues = Object.keys(NiveauEtude);
  nomFiliereValues = Object.keys(NomFiliere);
  nomSerieValues = Object.keys(NomSerie);
  diplomeRequisValues = Object.keys(DiplomeRequis);

  elevesCollection: IEleve[] = [];
  etudiantsCollection: IEtudiant[] = [];
  professionnelsCollection: IProfessionnel[] = [];

  editForm = this.fb.group({
    id: [],
    numDossier: [null, []],
    dateNaiss: [],
    prenom: [null, [Validators.required]],
    nom: [null, [Validators.required]],
    nomUtilisateur: [],
    regionNaiss: [],
    departementNaiss: [],
    lieuNaiss: [],
    typePiece: [],
    numeroPiece: [null, []],
    sexe: [],
    regionResidence: [],
    depResidence: [],
    adresseResidence: [],
    telephone1: [],
    telephone2: [],
    email: [],
    niveauFormation: [],
    specialite1: [],
    specialite2: [],
    diplomeRequis: [],
    cv: [],
    cvContentType: [],
    lettreMotivation: [],
    profession: [],
    eleve: [],
    etudiant: [],
    professionnel: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected dossierService: DossierService,
    protected eleveService: EleveService,
    protected etudiantService: EtudiantService,
    protected professionnelService: ProfessionnelService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dossier }) => {
      this.updateForm(dossier);

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
    const dossier = this.createFromForm();
    if (dossier.id !== undefined) {
      this.subscribeToSaveResponse(this.dossierService.update(dossier));
    } else {
      this.subscribeToSaveResponse(this.dossierService.create(dossier));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDossier>>): void {
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

  protected updateForm(dossier: IDossier): void {
    this.editForm.patchValue({
      id: dossier.id,
      numDossier: dossier.numDossier,
      dateNaiss: dossier.dateNaiss,
      prenom: dossier.prenom,
      nom: dossier.nom,
      nomUtilisateur: dossier.nomUtilisateur,
      regionNaiss: dossier.regionNaiss,
      departementNaiss: dossier.departementNaiss,
      lieuNaiss: dossier.lieuNaiss,
      typePiece: dossier.typePiece,
      numeroPiece: dossier.numeroPiece,
      sexe: dossier.sexe,
      regionResidence: dossier.regionResidence,
      depResidence: dossier.depResidence,
      adresseResidence: dossier.adresseResidence,
      telephone1: dossier.telephone1,
      telephone2: dossier.telephone2,
      email: dossier.email,
      niveauFormation: dossier.niveauFormation,
      specialite1: dossier.specialite1,
      specialite2: dossier.specialite2,
      diplomeRequis: dossier.diplomeRequis,
      cv: dossier.cv,
      cvContentType: dossier.cvContentType,
      lettreMotivation: dossier.lettreMotivation,
      profession: dossier.profession,
      eleve: dossier.eleve,
      etudiant: dossier.etudiant,
      professionnel: dossier.professionnel,
    });

    this.elevesCollection = this.eleveService.addEleveToCollectionIfMissing(this.elevesCollection, dossier.eleve);
    this.etudiantsCollection = this.etudiantService.addEtudiantToCollectionIfMissing(this.etudiantsCollection, dossier.etudiant);
    this.professionnelsCollection = this.professionnelService.addProfessionnelToCollectionIfMissing(
      this.professionnelsCollection,
      dossier.professionnel
    );
  }

  protected loadRelationshipsOptions(): void {
    this.eleveService
      .query({ filter: 'dossier-is-null' })
      .pipe(map((res: HttpResponse<IEleve[]>) => res.body ?? []))
      .pipe(map((eleves: IEleve[]) => this.eleveService.addEleveToCollectionIfMissing(eleves, this.editForm.get('eleve')!.value)))
      .subscribe((eleves: IEleve[]) => (this.elevesCollection = eleves));

    this.etudiantService
      .query({ filter: 'dossier-is-null' })
      .pipe(map((res: HttpResponse<IEtudiant[]>) => res.body ?? []))
      .pipe(
        map((etudiants: IEtudiant[]) =>
          this.etudiantService.addEtudiantToCollectionIfMissing(etudiants, this.editForm.get('etudiant')!.value)
        )
      )
      .subscribe((etudiants: IEtudiant[]) => (this.etudiantsCollection = etudiants));

    this.professionnelService
      .query({ filter: 'dossier-is-null' })
      .pipe(map((res: HttpResponse<IProfessionnel[]>) => res.body ?? []))
      .pipe(
        map((professionnels: IProfessionnel[]) =>
          this.professionnelService.addProfessionnelToCollectionIfMissing(professionnels, this.editForm.get('professionnel')!.value)
        )
      )
      .subscribe((professionnels: IProfessionnel[]) => (this.professionnelsCollection = professionnels));
  }

  protected createFromForm(): IDossier {
    return {
      ...new Dossier(),
      id: this.editForm.get(['id'])!.value,
      numDossier: this.editForm.get(['numDossier'])!.value,
      dateNaiss: this.editForm.get(['dateNaiss'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      nomUtilisateur: this.editForm.get(['nomUtilisateur'])!.value,
      regionNaiss: this.editForm.get(['regionNaiss'])!.value,
      departementNaiss: this.editForm.get(['departementNaiss'])!.value,
      lieuNaiss: this.editForm.get(['lieuNaiss'])!.value,
      typePiece: this.editForm.get(['typePiece'])!.value,
      numeroPiece: this.editForm.get(['numeroPiece'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      regionResidence: this.editForm.get(['regionResidence'])!.value,
      depResidence: this.editForm.get(['depResidence'])!.value,
      adresseResidence: this.editForm.get(['adresseResidence'])!.value,
      telephone1: this.editForm.get(['telephone1'])!.value,
      telephone2: this.editForm.get(['telephone2'])!.value,
      email: this.editForm.get(['email'])!.value,
      niveauFormation: this.editForm.get(['niveauFormation'])!.value,
      specialite1: this.editForm.get(['specialite1'])!.value,
      specialite2: this.editForm.get(['specialite2'])!.value,
      diplomeRequis: this.editForm.get(['diplomeRequis'])!.value,
      cvContentType: this.editForm.get(['cvContentType'])!.value,
      cv: this.editForm.get(['cv'])!.value,
      lettreMotivation: this.editForm.get(['lettreMotivation'])!.value,
      profession: this.editForm.get(['profession'])!.value,
      eleve: this.editForm.get(['eleve'])!.value,
      etudiant: this.editForm.get(['etudiant'])!.value,
      professionnel: this.editForm.get(['professionnel'])!.value,
    };
  }
}
