import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { EleveFormService, EleveFormGroup } from './eleve-form.service';
import { IEleve } from '../eleve.model';
import { EleveService } from '../service/eleve.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { NomRegion } from 'app/entities/enumerations/nom-region.model';
import { NomDepartement } from 'app/entities/enumerations/nom-departement.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';

@Component({
  standalone: true,
  selector: 'jhi-eleve-update',
  templateUrl: './eleve-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class EleveUpdateComponent implements OnInit {
  isSaving = false;
  eleve: IEleve | null = null;
  sexeValues = Object.keys(Sexe);
  nomRegionValues = Object.keys(NomRegion);
  nomDepartementValues = Object.keys(NomDepartement);
  niveauEtudeValues = Object.keys(NiveauEtude);

  usersSharedCollection: IUser[] = [];

  editForm: EleveFormGroup = this.eleveFormService.createEleveFormGroup();

  constructor(
    protected eleveService: EleveService,
    protected eleveFormService: EleveFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ eleve }) => {
      this.eleve = eleve;
      if (eleve) {
        this.updateForm(eleve);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const eleve = this.eleveFormService.getEleve(this.editForm);
    if (eleve.id !== null) {
      this.subscribeToSaveResponse(this.eleveService.update(eleve));
    } else {
      this.subscribeToSaveResponse(this.eleveService.create(eleve));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEleve>>): void {
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

  protected updateForm(eleve: IEleve): void {
    this.eleve = eleve;
    this.eleveFormService.resetForm(this.editForm, eleve);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, eleve.user);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.eleve?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
