import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IBailleur, Bailleur } from '../bailleur.model';
import { BailleurService } from '../service/bailleur.service';

@Component({
  selector: 'jhi-bailleur-update',
  templateUrl: './bailleur-update.component.html',
})
export class BailleurUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nomBailleur: [null, [Validators.required]],
    budgetPrevu: [],
    budgetDepense: [],
    budgetRestant: [],
    nbrePC: [],
  });

  constructor(protected bailleurService: BailleurService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bailleur }) => {
      this.updateForm(bailleur);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bailleur = this.createFromForm();
    if (bailleur.id !== undefined) {
      this.subscribeToSaveResponse(this.bailleurService.update(bailleur));
    } else {
      this.subscribeToSaveResponse(this.bailleurService.create(bailleur));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBailleur>>): void {
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

  protected updateForm(bailleur: IBailleur): void {
    this.editForm.patchValue({
      id: bailleur.id,
      nomBailleur: bailleur.nomBailleur,
      budgetPrevu: bailleur.budgetPrevu,
      budgetDepense: bailleur.budgetDepense,
      budgetRestant: bailleur.budgetRestant,
      nbrePC: bailleur.nbrePC,
    });
  }

  protected createFromForm(): IBailleur {
    return {
      ...new Bailleur(),
      id: this.editForm.get(['id'])!.value,
      nomBailleur: this.editForm.get(['nomBailleur'])!.value,
      budgetPrevu: this.editForm.get(['budgetPrevu'])!.value,
      budgetDepense: this.editForm.get(['budgetDepense'])!.value,
      budgetRestant: this.editForm.get(['budgetRestant'])!.value,
      nbrePC: this.editForm.get(['nbrePC'])!.value,
    };
  }
}
