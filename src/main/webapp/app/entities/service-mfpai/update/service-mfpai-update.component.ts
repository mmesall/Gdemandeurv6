import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ServiceMFPAIFormService, ServiceMFPAIFormGroup } from './service-mfpai-form.service';
import { IServiceMFPAI } from '../service-mfpai.model';
import { ServiceMFPAIService } from '../service/service-mfpai.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  standalone: true,
  selector: 'jhi-service-mfpai-update',
  templateUrl: './service-mfpai-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ServiceMFPAIUpdateComponent implements OnInit {
  isSaving = false;
  serviceMFPAI: IServiceMFPAI | null = null;

  editForm: ServiceMFPAIFormGroup = this.serviceMFPAIFormService.createServiceMFPAIFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected serviceMFPAIService: ServiceMFPAIService,
    protected serviceMFPAIFormService: ServiceMFPAIFormService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceMFPAI }) => {
      this.serviceMFPAI = serviceMFPAI;
      if (serviceMFPAI) {
        this.updateForm(serviceMFPAI);
      }
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
    const serviceMFPAI = this.serviceMFPAIFormService.getServiceMFPAI(this.editForm);
    if (serviceMFPAI.id !== null) {
      this.subscribeToSaveResponse(this.serviceMFPAIService.update(serviceMFPAI));
    } else {
      this.subscribeToSaveResponse(this.serviceMFPAIService.create(serviceMFPAI));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IServiceMFPAI>>): void {
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

  protected updateForm(serviceMFPAI: IServiceMFPAI): void {
    this.serviceMFPAI = serviceMFPAI;
    this.serviceMFPAIFormService.resetForm(this.editForm, serviceMFPAI);
  }
}
