import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFoodService } from '../food-service.model';
import { FoodServiceService } from '../service/food-service.service';

@Component({
  templateUrl: './food-service-delete-dialog.component.html',
})
export class FoodServiceDeleteDialogComponent {
  foodService?: IFoodService;

  constructor(protected foodServiceService: FoodServiceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.foodServiceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
