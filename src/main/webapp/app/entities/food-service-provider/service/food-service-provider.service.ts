import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFoodServiceProvider, getFoodServiceProviderIdentifier } from '../food-service-provider.model';

export type EntityResponseType = HttpResponse<IFoodServiceProvider>;
export type EntityArrayResponseType = HttpResponse<IFoodServiceProvider[]>;

@Injectable({ providedIn: 'root' })
export class FoodServiceProviderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/food-service-providers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(foodServiceProvider: IFoodServiceProvider): Observable<EntityResponseType> {
    return this.http.post<IFoodServiceProvider>(this.resourceUrl, foodServiceProvider, { observe: 'response' });
  }

  update(foodServiceProvider: IFoodServiceProvider): Observable<EntityResponseType> {
    return this.http.put<IFoodServiceProvider>(
      `${this.resourceUrl}/${getFoodServiceProviderIdentifier(foodServiceProvider) as number}`,
      foodServiceProvider,
      { observe: 'response' }
    );
  }

  partialUpdate(foodServiceProvider: IFoodServiceProvider): Observable<EntityResponseType> {
    return this.http.patch<IFoodServiceProvider>(
      `${this.resourceUrl}/${getFoodServiceProviderIdentifier(foodServiceProvider) as number}`,
      foodServiceProvider,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFoodServiceProvider>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFoodServiceProvider[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFoodServiceProviderToCollectionIfMissing(
    foodServiceProviderCollection: IFoodServiceProvider[],
    ...foodServiceProvidersToCheck: (IFoodServiceProvider | null | undefined)[]
  ): IFoodServiceProvider[] {
    const foodServiceProviders: IFoodServiceProvider[] = foodServiceProvidersToCheck.filter(isPresent);
    if (foodServiceProviders.length > 0) {
      const foodServiceProviderCollectionIdentifiers = foodServiceProviderCollection.map(
        foodServiceProviderItem => getFoodServiceProviderIdentifier(foodServiceProviderItem)!
      );
      const foodServiceProvidersToAdd = foodServiceProviders.filter(foodServiceProviderItem => {
        const foodServiceProviderIdentifier = getFoodServiceProviderIdentifier(foodServiceProviderItem);
        if (foodServiceProviderIdentifier == null || foodServiceProviderCollectionIdentifiers.includes(foodServiceProviderIdentifier)) {
          return false;
        }
        foodServiceProviderCollectionIdentifiers.push(foodServiceProviderIdentifier);
        return true;
      });
      return [...foodServiceProvidersToAdd, ...foodServiceProviderCollection];
    }
    return foodServiceProviderCollection;
  }
}
