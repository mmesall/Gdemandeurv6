import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IServiceMFPAI } from '../service-mfpai.model';
import { ServiceMFPAIService } from '../service/service-mfpai.service';

@Component({
  templateUrl: './service-mfpai-delete-dialog.component.html',
})
export class ServiceMFPAIDeleteDialogComponent {
  serviceMFPAI?: IServiceMFPAI;

  constructor(protected serviceMFPAIService: ServiceMFPAIService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.serviceMFPAIService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
