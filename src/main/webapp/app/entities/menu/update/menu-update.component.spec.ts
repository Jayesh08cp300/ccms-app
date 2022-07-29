import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MenuService } from '../service/menu.service';
import { IMenu, Menu } from '../menu.model';
import { IFoodService } from 'app/entities/food-service/food-service.model';
import { FoodServiceService } from 'app/entities/food-service/service/food-service.service';
import { IStaffOrder } from 'app/entities/staff-order/staff-order.model';
import { StaffOrderService } from 'app/entities/staff-order/service/staff-order.service';

import { MenuUpdateComponent } from './menu-update.component';

describe('Menu Management Update Component', () => {
  let comp: MenuUpdateComponent;
  let fixture: ComponentFixture<MenuUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let menuService: MenuService;
  let foodServiceService: FoodServiceService;
  let staffOrderService: StaffOrderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MenuUpdateComponent],
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
      .overrideTemplate(MenuUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MenuUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    menuService = TestBed.inject(MenuService);
    foodServiceService = TestBed.inject(FoodServiceService);
    staffOrderService = TestBed.inject(StaffOrderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call FoodService query and add missing value', () => {
      const menu: IMenu = { id: 456 };
      const foodService: IFoodService = { id: 65855 };
      menu.foodService = foodService;

      const foodServiceCollection: IFoodService[] = [{ id: 83465 }];
      jest.spyOn(foodServiceService, 'query').mockReturnValue(of(new HttpResponse({ body: foodServiceCollection })));
      const additionalFoodServices = [foodService];
      const expectedCollection: IFoodService[] = [...additionalFoodServices, ...foodServiceCollection];
      jest.spyOn(foodServiceService, 'addFoodServiceToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ menu });
      comp.ngOnInit();

      expect(foodServiceService.query).toHaveBeenCalled();
      expect(foodServiceService.addFoodServiceToCollectionIfMissing).toHaveBeenCalledWith(foodServiceCollection, ...additionalFoodServices);
      expect(comp.foodServicesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call StaffOrder query and add missing value', () => {
      const menu: IMenu = { id: 456 };
      const staffOrder: IStaffOrder = { id: 14380 };
      menu.staffOrder = staffOrder;

      const staffOrderCollection: IStaffOrder[] = [{ id: 86296 }];
      jest.spyOn(staffOrderService, 'query').mockReturnValue(of(new HttpResponse({ body: staffOrderCollection })));
      const additionalStaffOrders = [staffOrder];
      const expectedCollection: IStaffOrder[] = [...additionalStaffOrders, ...staffOrderCollection];
      jest.spyOn(staffOrderService, 'addStaffOrderToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ menu });
      comp.ngOnInit();

      expect(staffOrderService.query).toHaveBeenCalled();
      expect(staffOrderService.addStaffOrderToCollectionIfMissing).toHaveBeenCalledWith(staffOrderCollection, ...additionalStaffOrders);
      expect(comp.staffOrdersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const menu: IMenu = { id: 456 };
      const foodService: IFoodService = { id: 5641 };
      menu.foodService = foodService;
      const staffOrder: IStaffOrder = { id: 33415 };
      menu.staffOrder = staffOrder;

      activatedRoute.data = of({ menu });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(menu));
      expect(comp.foodServicesSharedCollection).toContain(foodService);
      expect(comp.staffOrdersSharedCollection).toContain(staffOrder);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Menu>>();
      const menu = { id: 123 };
      jest.spyOn(menuService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ menu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: menu }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(menuService.update).toHaveBeenCalledWith(menu);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Menu>>();
      const menu = new Menu();
      jest.spyOn(menuService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ menu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: menu }));
      saveSubject.complete();

      // THEN
      expect(menuService.create).toHaveBeenCalledWith(menu);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Menu>>();
      const menu = { id: 123 };
      jest.spyOn(menuService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ menu });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(menuService.update).toHaveBeenCalledWith(menu);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackFoodServiceById', () => {
      it('Should return tracked FoodService primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackFoodServiceById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackStaffOrderById', () => {
      it('Should return tracked StaffOrder primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackStaffOrderById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
