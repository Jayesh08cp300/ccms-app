import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFoodServiceProvider } from '../food-service-provider.model';
import { FoodServiceProviderService } from '../service/food-service-provider.service';

@Component({
  templateUrl: './food-service-provider-delete-dialog.component.html',
})
export class FoodServiceProviderDeleteDialogComponent {
  foodServiceProvider?: IFoodServiceProvider;

  constructor(protected foodServiceProviderService: FoodServiceProviderService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.foodServiceProviderService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
