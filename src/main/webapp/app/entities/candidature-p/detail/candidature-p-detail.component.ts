import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICandidatureP } from '../candidature-p.model';

@Component({
  selector: 'jhi-candidature-p-detail',
  templateUrl: './candidature-p-detail.component.html',
})
export class CandidaturePDetailComponent implements OnInit {
  candidatureP: ICandidatureP | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ candidatureP }) => {
      this.candidatureP = candidatureP;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
