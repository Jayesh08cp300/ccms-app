import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FoodServiceService } from '../service/food-service.service';
import { IFoodService, FoodService } from '../food-service.model';
import { IFoodServiceProvider } from 'app/entities/food-service-provider/food-service-provider.model';
import { FoodServiceProviderService } from 'app/entities/food-service-provider/service/food-service-provider.service';

import { FoodServiceUpdateComponent } from './food-service-update.component';

describe('FoodService Management Update Component', () => {
  let comp: FoodServiceUpdateComponent;
  let fixture: ComponentFixture<FoodServiceUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let foodServiceService: FoodServiceService;
  let foodServiceProviderService: FoodServiceProviderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FoodServiceUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(FoodServiceUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FoodServiceUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    foodServiceService = TestBed.inject(FoodServiceService);
    foodServiceProviderService = TestBed.inject(FoodServiceProviderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call FoodServiceProvider query and add missing value', () => {
      const foodService: IFoodService = { id: 456 };
      const foodServiceProvider: IFoodServiceProvider = { id: 60747 };
      foodService.foodServiceProvider = foodServiceProvider;

      const foodServiceProviderCollection: IFoodServiceProvider[] = [{ id: 74454 }];
      jest.spyOn(foodServiceProviderService, 'query').mockReturnValue(of(new HttpResponse({ body: foodServiceProviderCollection })));
      const additionalFoodServiceProviders = [foodServiceProvider];
      const expectedCollection: IFoodServiceProvider[] = [...additionalFoodServiceProviders, ...foodServiceProviderCollection];
      jest.spyOn(foodServiceProviderService, 'addFoodServiceProviderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ foodService });
      comp.ngOnInit();

      expect(foodServiceProviderService.query).toHaveBeenCalled();
      expect(foodServiceProviderService.addFoodServiceProviderToCollectionIfMissing).toHaveBeenCalledWith(
        foodServiceProviderCollection,
        ...additionalFoodServiceProviders
      );
      expect(comp.foodServiceProvidersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const foodService: IFoodService = { id: 456 };
      const foodServiceProvider: IFoodServiceProvider = { id: 92892 };
      foodService.foodServiceProvider = foodServiceProvider;

      activatedRoute.data = of({ foodService });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(foodService));
      expect(comp.foodServiceProvidersSharedCollection).toContain(foodServiceProvider);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FoodService>>();
      const foodService = { id: 123 };
      jest.spyOn(foodServiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ foodService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: foodService }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(foodServiceService.update).toHaveBeenCalledWith(foodService);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FoodService>>();
      const foodService = new FoodService();
      jest.spyOn(foodServiceService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ foodService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: foodService }));
      saveSubject.complete();

      // THEN
      expect(foodServiceService.create).toHaveBeenCalledWith(foodService);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FoodService>>();
      const foodService = { id: 123 };
      jest.spyOn(foodServiceService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ foodService });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(foodServiceService.update).toHaveBeenCalledWith(foodService);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFoodServiceProviderById', () => {
      it('Should return tracked FoodServiceProvider primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFoodServiceProviderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
