import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProfessionnel, Professionnel } from '../professionnel.model';
import { ProfessionnelService } from '../service/professionnel.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { NomRegion } from 'app/entities/enumerations/nom-region.model';
import { NomDepartement } from 'app/entities/enumerations/nom-departement.model';

@Component({
  selector: 'jhi-professionnel-update',
  templateUrl: './professionnel-update.component.html',
})
export class ProfessionnelUpdateComponent implements OnInit {
  isSaving = false;
  sexeValues = Object.keys(Sexe);
  nomRegionValues = Object.keys(NomRegion);
  nomDepartementValues = Object.keys(NomDepartement);

  usersSharedCollection: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    profession: [null, [Validators.required]],
    nom: [null, [Validators.required]],
    prenom: [null, [Validators.required]],
    dateNaiss: [],
    lieuNaiss: [],
    sexe: [],
    telephone: [],
    adressePhysique: [],
    regionResidence: [],
    departResidence: [],
    email: [null, []],
    cni: [null, [Validators.required]],
    user: [],
  });

  constructor(
    protected professionnelService: ProfessionnelService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ professionnel }) => {
      this.updateForm(professionnel);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const professionnel = this.createFromForm();
    if (professionnel.id !== undefined) {
      this.subscribeToSaveResponse(this.professionnelService.update(professionnel));
    } else {
      this.subscribeToSaveResponse(this.professionnelService.create(professionnel));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProfessionnel>>): void {
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

  protected updateForm(professionnel: IProfessionnel): void {
    this.editForm.patchValue({
      id: professionnel.id,
      profession: professionnel.profession,
      nom: professionnel.nom,
      prenom: professionnel.prenom,
      dateNaiss: professionnel.dateNaiss,
      lieuNaiss: professionnel.lieuNaiss,
      sexe: professionnel.sexe,
      telephone: professionnel.telephone,
      adressePhysique: professionnel.adressePhysique,
      regionResidence: professionnel.regionResidence,
      departResidence: professionnel.departResidence,
      email: professionnel.email,
      cni: professionnel.cni,
      user: professionnel.user,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, professionnel.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  protected createFromForm(): IProfessionnel {
    return {
      ...new Professionnel(),
      id: this.editForm.get(['id'])!.value,
      profession: this.editForm.get(['profession'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      dateNaiss: this.editForm.get(['dateNaiss'])!.value,
      lieuNaiss: this.editForm.get(['lieuNaiss'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      adressePhysique: this.editForm.get(['adressePhysique'])!.value,
      regionResidence: this.editForm.get(['regionResidence'])!.value,
      departResidence: this.editForm.get(['departResidence'])!.value,
      email: this.editForm.get(['email'])!.value,
      cni: this.editForm.get(['cni'])!.value,
      user: this.editForm.get(['user'])!.value,
    };
  }
}
