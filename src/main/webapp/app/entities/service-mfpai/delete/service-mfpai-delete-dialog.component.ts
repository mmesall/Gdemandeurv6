import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IServiceMFPAI } from '../service-mfpai.model';
import { ServiceMFPAIService } from '../service/service-mfpai.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './service-mfpai-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class ServiceMFPAIDeleteDialogComponent {
  serviceMFPAI?: IServiceMFPAI;

  constructor(protected serviceMFPAIService: ServiceMFPAIService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.serviceMFPAIService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
