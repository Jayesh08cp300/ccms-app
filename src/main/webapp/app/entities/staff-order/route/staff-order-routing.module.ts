import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StaffOrderComponent } from '../list/staff-order.component';
import { StaffOrderDetailComponent } from '../detail/staff-order-detail.component';
import { StaffOrderUpdateComponent } from '../update/staff-order-update.component';
import { StaffOrderRoutingResolveService } from './staff-order-routing-resolve.service';

const staffOrderRoute: Routes = [
  {
    path: '',
    component: StaffOrderComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StaffOrderDetailComponent,
    resolve: {
      staffOrder: StaffOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StaffOrderUpdateComponent,
    resolve: {
      staffOrder: StaffOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StaffOrderUpdateComponent,
    resolve: {
      staffOrder: StaffOrderRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(staffOrderRoute)],
  exports: [RouterModule],
})
export class StaffOrderRoutingModule {}
