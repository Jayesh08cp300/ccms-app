import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FoodServiceProviderService } from '../service/food-service-provider.service';
import { IFoodServiceProvider, FoodServiceProvider } from '../food-service-provider.model';

import { FoodServiceProviderUpdateComponent } from './food-service-provider-update.component';

describe('FoodServiceProvider Management Update Component', () => {
  let comp: FoodServiceProviderUpdateComponent;
  let fixture: ComponentFixture<FoodServiceProviderUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let foodServiceProviderService: FoodServiceProviderService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FoodServiceProviderUpdateComponent],
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
      .overrideTemplate(FoodServiceProviderUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FoodServiceProviderUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    foodServiceProviderService = TestBed.inject(FoodServiceProviderService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const foodServiceProvider: IFoodServiceProvider = { id: 456 };

      activatedRoute.data = of({ foodServiceProvider });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(foodServiceProvider));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FoodServiceProvider>>();
      const foodServiceProvider = { id: 123 };
      jest.spyOn(foodServiceProviderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ foodServiceProvider });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: foodServiceProvider }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(foodServiceProviderService.update).toHaveBeenCalledWith(foodServiceProvider);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FoodServiceProvider>>();
      const foodServiceProvider = new FoodServiceProvider();
      jest.spyOn(foodServiceProviderService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ foodServiceProvider });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: foodServiceProvider }));
      saveSubject.complete();

      // THEN
      expect(foodServiceProviderService.create).toHaveBeenCalledWith(foodServiceProvider);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<FoodServiceProvider>>();
      const foodServiceProvider = { id: 123 };
      jest.spyOn(foodServiceProviderService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ foodServiceProvider });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(foodServiceProviderService.update).toHaveBeenCalledWith(foodServiceProvider);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
