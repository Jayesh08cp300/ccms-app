import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FoodServiceProviderComponent } from './list/food-service-provider.component';
import { FoodServiceProviderDetailComponent } from './detail/food-service-provider-detail.component';
import { FoodServiceProviderUpdateComponent } from './update/food-service-provider-update.component';
import { FoodServiceProviderDeleteDialogComponent } from './delete/food-service-provider-delete-dialog.component';
import { FoodServiceProviderRoutingModule } from './route/food-service-provider-routing.module';

@NgModule({
  imports: [SharedModule, FoodServiceProviderRoutingModule],
  declarations: [
    FoodServiceProviderComponent,
    FoodServiceProviderDetailComponent,
    FoodServiceProviderUpdateComponent,
    FoodServiceProviderDeleteDialogComponent,
  ],
  entryComponents: [FoodServiceProviderDeleteDialogComponent],
})
export class FoodServiceProviderModule {}
