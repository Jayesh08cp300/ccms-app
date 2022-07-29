import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { StaffOrderComponent } from './list/staff-order.component';
import { StaffOrderDetailComponent } from './detail/staff-order-detail.component';
import { StaffOrderUpdateComponent } from './update/staff-order-update.component';
import { StaffOrderDeleteDialogComponent } from './delete/staff-order-delete-dialog.component';
import { StaffOrderRoutingModule } from './route/staff-order-routing.module';

@NgModule({
  imports: [SharedModule, StaffOrderRoutingModule],
  declarations: [StaffOrderComponent, StaffOrderDetailComponent, StaffOrderUpdateComponent, StaffOrderDeleteDialogComponent],
  entryComponents: [StaffOrderDeleteDialogComponent],
})
export class StaffOrderModule {}
