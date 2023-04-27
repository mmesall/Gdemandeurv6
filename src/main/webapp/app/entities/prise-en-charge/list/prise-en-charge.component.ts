import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPriseEnCharge } from '../prise-en-charge.model';

import { ASC, DESC, ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { PriseEnChargeService } from '../service/prise-en-charge.service';
import { PriseEnChargeDeleteDialogComponent } from '../delete/prise-en-charge-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-prise-en-charge',
  templateUrl: './prise-en-charge.component.html',
})
export class PriseEnChargeComponent implements OnInit {
  priseEnCharges: IPriseEnCharge[];
  isLoading = false;
  itemsPerPage: number;
  links: { [key: string]: number };
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(protected priseEnChargeService: PriseEnChargeService, protected modalService: NgbModal, protected parseLinks: ParseLinks) {
    this.priseEnCharges = [];
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

    this.priseEnChargeService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe({
        next: (res: HttpResponse<IPriseEnCharge[]>) => {
          this.isLoading = false;
          this.paginatePriseEnCharges(res.body, res.headers);
        },
        error: () => {
          this.isLoading = false;
        },
      });
  }

  reset(): void {
    this.page = 0;
    this.priseEnCharges = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPriseEnCharge): number {
    return item.id!;
  }

  delete(priseEnCharge: IPriseEnCharge): void {
    const modalRef = this.modalService.open(PriseEnChargeDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.priseEnCharge = priseEnCharge;
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

  protected paginatePriseEnCharges(data: IPriseEnCharge[] | null, headers: HttpHeaders): void {
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
        this.priseEnCharges.push(d);
      }
    }
  }
}
