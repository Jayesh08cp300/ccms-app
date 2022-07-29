import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStaffOrder, getStaffOrderIdentifier } from '../staff-order.model';

export type EntityResponseType = HttpResponse<IStaffOrder>;
export type EntityArrayResponseType = HttpResponse<IStaffOrder[]>;

@Injectable({ providedIn: 'root' })
export class StaffOrderService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/staff-orders');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(staffOrder: IStaffOrder): Observable<EntityResponseType> {
    return this.http.post<IStaffOrder>(this.resourceUrl, staffOrder, { observe: 'response' });
  }

  update(staffOrder: IStaffOrder): Observable<EntityResponseType> {
    return this.http.put<IStaffOrder>(`${this.resourceUrl}/${getStaffOrderIdentifier(staffOrder) as number}`, staffOrder, {
      observe: 'response',
    });
  }

  partialUpdate(staffOrder: IStaffOrder): Observable<EntityResponseType> {
    return this.http.patch<IStaffOrder>(`${this.resourceUrl}/${getStaffOrderIdentifier(staffOrder) as number}`, staffOrder, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStaffOrder>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStaffOrder[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addStaffOrderToCollectionIfMissing(
    staffOrderCollection: IStaffOrder[],
    ...staffOrdersToCheck: (IStaffOrder | null | undefined)[]
  ): IStaffOrder[] {
    const staffOrders: IStaffOrder[] = staffOrdersToCheck.filter(isPresent);
    if (staffOrders.length > 0) {
      const staffOrderCollectionIdentifiers = staffOrderCollection.map(staffOrderItem => getStaffOrderIdentifier(staffOrderItem)!);
      const staffOrdersToAdd = staffOrders.filter(staffOrderItem => {
        const staffOrderIdentifier = getStaffOrderIdentifier(staffOrderItem);
        if (staffOrderIdentifier == null || staffOrderCollectionIdentifiers.includes(staffOrderIdentifier)) {
          return false;
        }
        staffOrderCollectionIdentifiers.push(staffOrderIdentifier);
        return true;
      });
      return [...staffOrdersToAdd, ...staffOrderCollection];
    }
    return staffOrderCollection;
  }
}
