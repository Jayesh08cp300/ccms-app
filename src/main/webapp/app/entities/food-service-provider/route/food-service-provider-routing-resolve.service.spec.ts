import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, ActivatedRoute, Router, convertToParamMap } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { IFoodServiceProvider, FoodServiceProvider } from '../food-service-provider.model';
import { FoodServiceProviderService } from '../service/food-service-provider.service';

import { FoodServiceProviderRoutingResolveService } from './food-service-provider-routing-resolve.service';

describe('FoodServiceProvider routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: FoodServiceProviderRoutingResolveService;
  let service: FoodServiceProviderService;
  let resultFoodServiceProvider: IFoodServiceProvider | undefined;

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
    routingResolveService = TestBed.inject(FoodServiceProviderRoutingResolveService);
    service = TestBed.inject(FoodServiceProviderService);
    resultFoodServiceProvider = undefined;
  });

  describe('resolve', () => {
    it('should return IFoodServiceProvider returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFoodServiceProvider = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFoodServiceProvider).toEqual({ id: 123 });
    });

    it('should return new IFoodServiceProvider if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFoodServiceProvider = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultFoodServiceProvider).toEqual(new FoodServiceProvider());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as FoodServiceProvider })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultFoodServiceProvider = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultFoodServiceProvider).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
