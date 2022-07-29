import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStaffOrder } from '../staff-order.model';

@Component({
  selector: 'jhi-staff-order-detail',
  templateUrl: './staff-order-detail.component.html',
})
export class StaffOrderDetailComponent implements OnInit {
  staffOrder: IStaffOrder | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ staffOrder }) => {
      this.staffOrder = staffOrder;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
