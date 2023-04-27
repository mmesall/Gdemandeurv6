import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICandidatureProf } from '../candidature-prof.model';

@Component({
  selector: 'jhi-candidature-prof-detail',
  templateUrl: './candidature-prof-detail.component.html',
})
export class CandidatureProfDetailComponent implements OnInit {
  candidatureProf: ICandidatureProf | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ candidatureProf }) => {
      this.candidatureProf = candidatureProf;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
