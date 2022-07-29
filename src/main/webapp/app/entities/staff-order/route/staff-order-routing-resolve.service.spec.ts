import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IStaffOrder, StaffOrder } from '../staff-order.model';
import { StaffOrderService } from '../service/staff-order.service';

import { StaffOrderRoutingResolveService } from './staff-order-routing-resolve.service';

describe('StaffOrder routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: StaffOrderRoutingResolveService;
  let service: StaffOrderService;
  let resultStaffOrder: IStaffOrder | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: convertToParamMap({}),
            },
          },
        },
      ],
    });
    mockRouter = TestBed.inject(Router);
    jest.spyOn(mockRouter, 'navigate').mockImplementation(() => Promise.resolve(true));
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRoute).snapshot;
    routingResolveService = TestBed.inject(StaffOrderRoutingResolveService);
    service = TestBed.inject(StaffOrderService);
    resultStaffOrder = undefined;
  });

  describe('resolve', () => {
    it('should return IStaffOrder returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultStaffOrder = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultStaffOrder).toEqual({ id: 123 });
    });

    it('should return new IStaffOrder if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultStaffOrder = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultStaffOrder).toEqual(new StaffOrder());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as StaffOrder })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultStaffOrder = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultStaffOrder).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
