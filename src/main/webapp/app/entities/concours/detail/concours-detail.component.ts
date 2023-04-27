import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IConcours } from '../concours.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-concours-detail',
  templateUrl: './concours-detail.component.html',
})
export class ConcoursDetailComponent implements OnInit {
  concours: IConcours | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ concours }) => {
      this.concours = concours;
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
