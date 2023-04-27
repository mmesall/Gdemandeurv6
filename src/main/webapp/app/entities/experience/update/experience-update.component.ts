import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IExperience, Experience } from '../experience.model';
import { ExperienceService } from '../service/experience.service';
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

@Component({
  selector: 'jhi-experience-update',
  templateUrl: './experience-update.component.html',
})
export class ExperienceUpdateComponent implements OnInit {
  isSaving = false;

  elevesSharedCollection: IEleve[] = [];
  etudiantsSharedCollection: IEtudiant[] = [];
  professionnelsSharedCollection: IProfessionnel[] = [];
  demandeursSharedCollection: IDemandeur[] = [];

  editForm = this.fb.group({
    id: [],
    dateDebut: [null, [Validators.required]],
    dateFin: [null, [Validators.required]],
    nomEntreprise: [null, [Validators.required]],
    posteOccupe: [null, [Validators.required]],
    mission: [],
    eleve: [],
    etudiant: [],
    professionnel: [],
    demandeur: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected experienceService: ExperienceService,
    protected eleveService: EleveService,
    protected etudiantService: EtudiantService,
    protected professionnelService: ProfessionnelService,
    protected demandeurService: DemandeurService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ experience }) => {
      this.updateForm(experience);

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
    const experience = this.createFromForm();
    if (experience.id !== undefined) {
      this.subscribeToSaveResponse(this.experienceService.update(experience));
    } else {
      this.subscribeToSaveResponse(this.experienceService.create(experience));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IExperience>>): void {
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

  protected updateForm(experience: IExperience): void {
    this.editForm.patchValue({
      id: experience.id,
      dateDebut: experience.dateDebut,
      dateFin: experience.dateFin,
      nomEntreprise: experience.nomEntreprise,
      posteOccupe: experience.posteOccupe,
      mission: experience.mission,
      eleve: experience.eleve,
      etudiant: experience.etudiant,
      professionnel: experience.professionnel,
      demandeur: experience.demandeur,
    });

    this.elevesSharedCollection = this.eleveService.addEleveToCollectionIfMissing(this.elevesSharedCollection, experience.eleve);
    this.etudiantsSharedCollection = this.etudiantService.addEtudiantToCollectionIfMissing(
      this.etudiantsSharedCollection,
      experience.etudiant
    );
    this.professionnelsSharedCollection = this.professionnelService.addProfessionnelToCollectionIfMissing(
      this.professionnelsSharedCollection,
      experience.professionnel
    );
    this.demandeursSharedCollection = this.demandeurService.addDemandeurToCollectionIfMissing(
      this.demandeursSharedCollection,
      experience.demandeur
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

  protected createFromForm(): IExperience {
    return {
      ...new Experience(),
      id: this.editForm.get(['id'])!.value,
      dateDebut: this.editForm.get(['dateDebut'])!.value,
      dateFin: this.editForm.get(['dateFin'])!.value,
      nomEntreprise: this.editForm.get(['nomEntreprise'])!.value,
      posteOccupe: this.editForm.get(['posteOccupe'])!.value,
      mission: this.editForm.get(['mission'])!.value,
      eleve: this.editForm.get(['eleve'])!.value,
      etudiant: this.editForm.get(['etudiant'])!.value,
      professionnel: this.editForm.get(['professionnel'])!.value,
      demandeur: this.editForm.get(['demandeur'])!.value,
    };
  }
}
