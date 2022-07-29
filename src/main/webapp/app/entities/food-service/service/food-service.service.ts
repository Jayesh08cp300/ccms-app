import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFoodService, getFoodServiceIdentifier } from '../food-service.model';

export type EntityResponseType = HttpResponse<IFoodService>;
export type EntityArrayResponseType = HttpResponse<IFoodService[]>;

@Injectable({ providedIn: 'root' })
export class FoodServiceService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/food-services');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(foodService: IFoodService): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(foodService);
    return this.http
      .post<IFoodService>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(foodService: IFoodService): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(foodService);
    return this.http
      .put<IFoodService>(`${this.resourceUrl}/${getFoodServiceIdentifier(foodService) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(foodService: IFoodService): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(foodService);
    return this.http
      .patch<IFoodService>(`${this.resourceUrl}/${getFoodServiceIdentifier(foodService) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IFoodService>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IFoodService[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addFoodServiceToCollectionIfMissing(
    foodServiceCollection: IFoodService[],
    ...foodServicesToCheck: (IFoodService | null | undefined)[]
  ): IFoodService[] {
    const foodServices: IFoodService[] = foodServicesToCheck.filter(isPresent);
    if (foodServices.length > 0) {
      const foodServiceCollectionIdentifiers = foodServiceCollection.map(foodServiceItem => getFoodServiceIdentifier(foodServiceItem)!);
      const foodServicesToAdd = foodServices.filter(foodServiceItem => {
        const foodServiceIdentifier = getFoodServiceIdentifier(foodServiceItem);
        if (foodServiceIdentifier == null || foodServiceCollectionIdentifiers.includes(foodServiceIdentifier)) {
          return false;
        }
        foodServiceCollectionIdentifiers.push(foodServiceIdentifier);
        return true;
      });
      return [...foodServicesToAdd, ...foodServiceCollection];
    }
    return foodServiceCollection;
  }

  protected convertDateFromClient(foodService: IFoodService): IFoodService {
    return Object.assign({}, foodService, {
      startDate: foodService.startDate?.isValid() ? foodService.startDate.format(DATE_FORMAT) : undefined,
      endDate: foodService.endDate?.isValid() ? foodService.endDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.startDate = res.body.startDate ? dayjs(res.body.startDate) : undefined;
      res.body.endDate = res.body.endDate ? dayjs(res.body.endDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((foodService: IFoodService) => {
        foodService.startDate = foodService.startDate ? dayjs(foodService.startDate) : undefined;
        foodService.endDate = foodService.endDate ? dayjs(foodService.endDate) : undefined;
      });
    }
    return res;
  }
}
