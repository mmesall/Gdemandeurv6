import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICandidatureProf } from '../candidature-prof.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { CandidatureProfService } from '../service/candidature-prof.service';
import { CandidatureProfDeleteDialogComponent } from '../delete/candidature-prof-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-candidature-prof',
  templateUrl: './candidature-prof.component.html',
})
export class CandidatureProfComponent implements OnInit {
  candidatureProfs: ICandidatureProf[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected candidatureProfService: CandidatureProfService,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks
  ) {
    this.candidatureProfs = [];
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

    this.candidatureProfService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<ICandidatureProf[]>) => {
          this.isLoading = false;
          this.paginateCandidatureProfs(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.candidatureProfs = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICandidatureProf): number {
    return item.id!;
  }

  delete(candidatureProf: ICandidatureProf): void {
    const modalRef = this.modalService.open(CandidatureProfDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.candidatureProf = candidatureProf;
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

  protected paginateCandidatureProfs(data: ICandidatureProf[] | null, headers: HttpHeaders): void {
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
        this.candidatureProfs.push(d);
      }
    }
  }
}
