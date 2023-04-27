import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAgent, Agent } from '../agent.model';
import { AgentService } from '../service/agent.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IServiceMFPAI } from 'app/entities/service-mfpai/service-mfpai.model';
import { ServiceMFPAIService } from 'app/entities/service-mfpai/service/service-mfpai.service';
import { Sexe } from 'app/entities/enumerations/sexe.model';

@Component({
  selector: 'jhi-agent-update',
  templateUrl: './agent-update.component.html',
})
export class AgentUpdateComponent implements OnInit {
  isSaving = false;
  sexeValues = Object.keys(Sexe);

  usersSharedCollection: IUser[] = [];
  serviceMFPAISCollection: IServiceMFPAI[] = [];

  editForm = this.fb.group({
    id: [],
    matricule: [null, [Validators.required]],
    nom: [],
    prenom: [],
    dateNaiss: [],
    lieuNaiss: [],
    sexe: [],
    telephone: [],
    email: [null, []],
    user: [],
    serviceMFPAI: [],
  });

  constructor(
    protected agentService: AgentService,
    protected userService: UserService,
    protected serviceMFPAIService: ServiceMFPAIService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ agent }) => {
      this.updateForm(agent);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const agent = this.createFromForm();
    if (agent.id !== undefined) {
      this.subscribeToSaveResponse(this.agentService.update(agent));
    } else {
      this.subscribeToSaveResponse(this.agentService.create(agent));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackServiceMFPAIById(index: number, item: IServiceMFPAI): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAgent>>): void {
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

  protected updateForm(agent: IAgent): void {
    this.editForm.patchValue({
      id: agent.id,
      matricule: agent.matricule,
      nom: agent.nom,
      prenom: agent.prenom,
      dateNaiss: agent.dateNaiss,
      lieuNaiss: agent.lieuNaiss,
      sexe: agent.sexe,
      telephone: agent.telephone,
      email: agent.email,
      user: agent.user,
      serviceMFPAI: agent.serviceMFPAI,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, agent.user);
    this.serviceMFPAISCollection = this.serviceMFPAIService.addServiceMFPAIToCollectionIfMissing(
      this.serviceMFPAISCollection,
      agent.serviceMFPAI
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, this.editForm.get('user')!.value)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.serviceMFPAIService
      .query({ filter: 'agent-is-null' })
      .pipe(map((res: HttpResponse<IServiceMFPAI[]>) => res.body ?? []))
      .pipe(
        map((serviceMFPAIS: IServiceMFPAI[]) =>
          this.serviceMFPAIService.addServiceMFPAIToCollectionIfMissing(serviceMFPAIS, this.editForm.get('serviceMFPAI')!.value)
        )
      )
      .subscribe((serviceMFPAIS: IServiceMFPAI[]) => (this.serviceMFPAISCollection = serviceMFPAIS));
  }

  protected createFromForm(): IAgent {
    return {
      ...new Agent(),
      id: this.editForm.get(['id'])!.value,
      matricule: this.editForm.get(['matricule'])!.value,
      nom: this.editForm.get(['nom'])!.value,
      prenom: this.editForm.get(['prenom'])!.value,
      dateNaiss: this.editForm.get(['dateNaiss'])!.value,
      lieuNaiss: this.editForm.get(['lieuNaiss'])!.value,
      sexe: this.editForm.get(['sexe'])!.value,
      telephone: this.editForm.get(['telephone'])!.value,
      email: this.editForm.get(['email'])!.value,
      user: this.editForm.get(['user'])!.value,
      serviceMFPAI: this.editForm.get(['serviceMFPAI'])!.value,
    };
  }
}
