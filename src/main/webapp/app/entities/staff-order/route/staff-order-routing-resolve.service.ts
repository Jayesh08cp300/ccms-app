import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStaffOrder, StaffOrder } from '../staff-order.model';
import { StaffOrderService } from '../service/staff-order.service';

@Injectable({ providedIn: 'root' })
export class StaffOrderRoutingResolveService implements Resolve<IStaffOrder> {
  constructor(protected service: StaffOrderService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IStaffOrder> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((staffOrder: HttpResponse<StaffOrder>) => {
          if (staffOrder.body) {
            return of(staffOrder.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new StaffOrder());
  }
}
