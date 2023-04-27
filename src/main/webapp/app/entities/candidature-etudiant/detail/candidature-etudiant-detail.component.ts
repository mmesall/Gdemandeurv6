import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICandidatureEtudiant } from '../candidature-etudiant.model';

@Component({
  selector: 'jhi-candidature-etudiant-detail',
  templateUrl: './candidature-etudiant-detail.component.html',
})
export class CandidatureEtudiantDetailComponent implements OnInit {
  candidatureEtudiant: ICandidatureEtudiant | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ candidatureEtudiant }) => {
      this.candidatureEtudiant = candidatureEtudiant;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
