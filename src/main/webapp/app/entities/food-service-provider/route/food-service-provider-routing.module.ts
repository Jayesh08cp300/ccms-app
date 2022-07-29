import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FoodServiceProviderComponent } from '../list/food-service-provider.component';
import { FoodServiceProviderDetailComponent } from '../detail/food-service-provider-detail.component';
import { FoodServiceProviderUpdateComponent } from '../update/food-service-provider-update.component';
import { FoodServiceProviderRoutingResolveService } from './food-service-provider-routing-resolve.service';

const foodServiceProviderRoute: Routes = [
  {
    path: '',
    component: FoodServiceProviderComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FoodServiceProviderDetailComponent,
    resolve: {
      foodServiceProvider: FoodServiceProviderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FoodServiceProviderUpdateComponent,
    resolve: {
      foodServiceProvider: FoodServiceProviderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FoodServiceProviderUpdateComponent,
    resolve: {
      foodServiceProvider: FoodServiceProviderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(foodServiceProviderRoute)],
  exports: [RouterModule],
})
export class FoodServiceProviderRoutingModule {}
