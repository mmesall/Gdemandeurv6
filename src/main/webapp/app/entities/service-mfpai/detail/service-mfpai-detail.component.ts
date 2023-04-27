import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IServiceMFPAI } from '../service-mfpai.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-service-mfpai-detail',
  templateUrl: './service-mfpai-detail.component.html',
})
export class ServiceMFPAIDetailComponent implements OnInit {
  serviceMFPAI: IServiceMFPAI | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ serviceMFPAI }) => {
      this.serviceMFPAI = serviceMFPAI;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
