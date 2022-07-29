import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IStaffOrder } from '../staff-order.model';
import { StaffOrderService } from '../service/staff-order.service';

@Component({
  templateUrl: './staff-order-delete-dialog.component.html',
})
export class StaffOrderDeleteDialogComponent {
  staffOrder?: IStaffOrder;

  constructor(protected staffOrderService: StaffOrderService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.staffOrderService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
