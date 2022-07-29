import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FoodServiceComponent } from './list/food-service.component';
import { FoodServiceDetailComponent } from './detail/food-service-detail.component';
import { FoodServiceUpdateComponent } from './update/food-service-update.component';
import { FoodServiceDeleteDialogComponent } from './delete/food-service-delete-dialog.component';
import { FoodServiceRoutingModule } from './route/food-service-routing.module';

@NgModule({
  imports: [SharedModule, FoodServiceRoutingModule],
  declarations: [FoodServiceComponent, FoodServiceDetailComponent, FoodServiceUpdateComponent, FoodServiceDeleteDialogComponent],
  entryComponents: [FoodServiceDeleteDialogComponent],
})
export class FoodServiceModule {}
