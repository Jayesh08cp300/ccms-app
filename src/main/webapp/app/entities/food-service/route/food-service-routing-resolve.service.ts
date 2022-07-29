import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFoodService, FoodService } from '../food-service.model';
import { FoodServiceService } from '../service/food-service.service';

@Injectable({ providedIn: 'root' })
export class FoodServiceRoutingResolveService implements Resolve<IFoodService> {
  constructor(protected service: FoodServiceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFoodService> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((foodService: HttpResponse<FoodService>) => {
          if (foodService.body) {
            return of(foodService.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FoodService());
  }
}
