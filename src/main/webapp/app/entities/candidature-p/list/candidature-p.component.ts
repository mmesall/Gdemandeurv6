import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICandidatureP } from '../candidature-p.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { CandidaturePService } from '../service/candidature-p.service';
import { CandidaturePDeleteDialogComponent } from '../delete/candidature-p-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-candidature-p',
  templateUrl: './candidature-p.component.html',
})
export class CandidaturePComponent implements OnInit {
  candidaturePS: ICandidatureP[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected candidaturePService: CandidaturePService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.candidaturePS = [];
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

    this.candidaturePService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<ICandidatureP[]>) => {
          this.isLoading = false;
          this.paginateCandidaturePS(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.candidaturePS = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICandidatureP): number {
    return item.id!;
  }

  delete(candidatureP: ICandidatureP): void {
    const modalRef = this.modalService.open(CandidaturePDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.candidatureP = candidatureP;
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

  protected paginateCandidaturePS(data: ICandidatureP[] | null, headers: HttpHeaders): void {
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
        this.candidaturePS.push(d);
      }
    }
  }
}
