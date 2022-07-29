import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FoodServiceComponent } from '../list/food-service.component';
import { FoodServiceDetailComponent } from '../detail/food-service-detail.component';
import { FoodServiceUpdateComponent } from '../update/food-service-update.component';
import { FoodServiceRoutingResolveService } from './food-service-routing-resolve.service';

const foodServiceRoute: Routes = [
  {
    path: '',
    component: FoodServiceComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FoodServiceDetailComponent,
    resolve: {
      foodService: FoodServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FoodServiceUpdateComponent,
    resolve: {
      foodService: FoodServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FoodServiceUpdateComponent,
    resolve: {
      foodService: FoodServiceRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(foodServiceRoute)],
  exports: [RouterModule],
})
export class FoodServiceRoutingModule {}
