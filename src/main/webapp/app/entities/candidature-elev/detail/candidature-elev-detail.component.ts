import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICandidatureElev } from '../candidature-elev.model';

@Component({
  selector: 'jhi-candidature-elev-detail',
  templateUrl: './candidature-elev-detail.component.html',
})
export class CandidatureElevDetailComponent implements OnInit {
  candidatureElev: ICandidatureElev | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ candidatureElev }) => {
      this.candidatureElev = candidatureElev;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
