import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFoodServiceProvider, FoodServiceProvider } from '../food-service-provider.model';
import { FoodServiceProviderService } from '../service/food-service-provider.service';

@Injectable({ providedIn: 'root' })
export class FoodServiceProviderRoutingResolveService implements Resolve<IFoodServiceProvider> {
  constructor(protected service: FoodServiceProviderService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFoodServiceProvider> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((foodServiceProvider: HttpResponse<FoodServiceProvider>) => {
          if (foodServiceProvider.body) {
            return of(foodServiceProvider.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FoodServiceProvider());
  }
}
