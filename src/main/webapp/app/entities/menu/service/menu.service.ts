import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMenu, getMenuIdentifier } from '../menu.model';

export type EntityResponseType = HttpResponse<IMenu>;
export type EntityArrayResponseType = HttpResponse<IMenu[]>;

@Injectable({ providedIn: 'root' })
export class MenuService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/menus');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(menu: IMenu): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(menu);
    return this.http
      .post<IMenu>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(menu: IMenu): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(menu);
    return this.http
      .put<IMenu>(`${this.resourceUrl}/${getMenuIdentifier(menu) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(menu: IMenu): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(menu);
    return this.http
      .patch<IMenu>(`${this.resourceUrl}/${getMenuIdentifier(menu) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IMenu>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IMenu[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addMenuToCollectionIfMissing(menuCollection: IMenu[], ...menusToCheck: (IMenu | null | undefined)[]): IMenu[] {
    const menus: IMenu[] = menusToCheck.filter(isPresent);
    if (menus.length > 0) {
      const menuCollectionIdentifiers = menuCollection.map(menuItem => getMenuIdentifier(menuItem)!);
      const menusToAdd = menus.filter(menuItem => {
        const menuIdentifier = getMenuIdentifier(menuItem);
        if (menuIdentifier == null || menuCollectionIdentifiers.includes(menuIdentifier)) {
          return false;
        }
        menuCollectionIdentifiers.push(menuIdentifier);
        return true;
      });
      return [...menusToAdd, ...menuCollection];
    }
    return menuCollection;
  }

  protected convertDateFromClient(menu: IMenu): IMenu {
    return Object.assign({}, menu, {
      serveDate: menu.serveDate?.isValid() ? menu.serveDate.format(DATE_FORMAT) : undefined,
      bookBeforeDate: menu.bookBeforeDate?.isValid() ? menu.bookBeforeDate.format(DATE_FORMAT) : undefined,
      cancelBeforeDate: menu.cancelBeforeDate?.isValid() ? menu.cancelBeforeDate.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.serveDate = res.body.serveDate ? dayjs(res.body.serveDate) : undefined;
      res.body.bookBeforeDate = res.body.bookBeforeDate ? dayjs(res.body.bookBeforeDate) : undefined;
      res.body.cancelBeforeDate = res.body.cancelBeforeDate ? dayjs(res.body.cancelBeforeDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((menu: IMenu) => {
        menu.serveDate = menu.serveDate ? dayjs(menu.serveDate) : undefined;
        menu.bookBeforeDate = menu.bookBeforeDate ? dayjs(menu.bookBeforeDate) : undefined;
        menu.cancelBeforeDate = menu.cancelBeforeDate ? dayjs(menu.cancelBeforeDate) : undefined;
      });
    }
    return res;
  }
}
