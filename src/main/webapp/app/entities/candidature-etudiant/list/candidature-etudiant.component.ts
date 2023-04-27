import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICandidatureEtudiant } from '../candidature-etudiant.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { CandidatureEtudiantService } from '../service/candidature-etudiant.service';
import { CandidatureEtudiantDeleteDialogComponent } from '../delete/candidature-etudiant-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-candidature-etudiant',
  templateUrl: './candidature-etudiant.component.html',
})
export class CandidatureEtudiantComponent implements OnInit {
  candidatureEtudiants: ICandidatureEtudiant[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected candidatureEtudiantService: CandidatureEtudiantService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.candidatureEtudiants = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.isLoading = true;

    this.candidatureEtudiantService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<ICandidatureEtudiant[]>) => {
          this.isLoading = false;
          this.paginateCandidatureEtudiants(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.candidatureEtudiants = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICandidatureEtudiant): number {
    return item.id!;
  }

  delete(candidatureEtudiant: ICandidatureEtudiant): void {
    const modalRef = this.modalService.open(CandidatureEtudiantDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.candidatureEtudiant = candidatureEtudiant;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCandidatureEtudiants(data: ICandidatureEtudiant[] | null, headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
    if (data) {
      for (const d of data) {
        this.candidatureEtudiants.push(d);
      }
    }
  }
}
