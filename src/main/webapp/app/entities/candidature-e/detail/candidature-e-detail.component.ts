import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICandidatureE } from '../candidature-e.model';

@Component({
  selector: 'jhi-candidature-e-detail',
  templateUrl: './candidature-e-detail.component.html',
})
export class CandidatureEDetailComponent implements OnInit {
  candidatureE: ICandidatureE | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ candidatureE }) => {
      this.candidatureE = candidatureE;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
