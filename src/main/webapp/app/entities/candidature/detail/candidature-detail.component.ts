import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICandidature } from '../candidature.model';

@Component({
  selector: 'jhi-candidature-detail',
  templateUrl: './candidature-detail.component.html',
})
export class CandidatureDetailComponent implements OnInit {
  candidature: ICandidature | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ candidature }) => {
      this.candidature = candidature;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
