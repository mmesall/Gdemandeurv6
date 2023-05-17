import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICandidatureE } from '../candidature-e.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { CandidatureEService } from '../service/candidature-e.service';
import { CandidatureEDeleteDialogComponent } from '../delete/candidature-e-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-candidature-e',
  templateUrl: './candidature-e.component.html',
})
export class CandidatureEComponent implements OnInit {
  candidatureES: ICandidatureE[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected candidatureEService: CandidatureEService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.candidatureES = [];
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

    this.candidatureEService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<ICandidatureE[]>) => {
          this.isLoading = false;
          this.paginateCandidatureES(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.candidatureES = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ICandidatureE): number {
    return item.id!;
  }

  delete(candidatureE: ICandidatureE): void {
    const modalRef = this.modalService.open(CandidatureEDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.candidatureE = candidatureE;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.reset();
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateCandidatureES(data: ICandidatureE[] | null, headers: HttpHeaders): void {
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
        this.candidatureES.push(d);
      }
    }
  }
}
