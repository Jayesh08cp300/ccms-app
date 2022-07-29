import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMenu, Menu } from '../menu.model';
import { MenuService } from '../service/menu.service';
import { IFoodService } from 'app/entities/food-service/food-service.model';
import { FoodServiceService } from 'app/entities/food-service/service/food-service.service';
import { IStaffOrder } from 'app/entities/staff-order/staff-order.model';
import { StaffOrderService } from 'app/entities/staff-order/service/staff-order.service';

@Component({
  selector: 'jhi-menu-update',
  templateUrl: './menu-update.component.html',
})
export class MenuUpdateComponent implements OnInit {
  isSaving = false;

  foodServicesSharedCollection: IFoodService[] = [];
  staffOrdersSharedCollection: IStaffOrder[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    serveDate: [],
    bookBeforeDate: [],
    cancelBeforeDate: [],
    foodService: [],
    staffOrder: [],
  });

  constructor(
    protected menuService: MenuService,
    protected foodServiceService: FoodServiceService,
    protected staffOrderService: StaffOrderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ menu }) => {
      this.updateForm(menu);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const menu = this.createFromForm();
    if (menu.id !== undefined) {
      this.subscribeToSaveResponse(this.menuService.update(menu));
    } else {
      this.subscribeToSaveResponse(this.menuService.create(menu));
    }
  }

  trackFoodServiceById(_index: number, item: IFoodService): number {
    return item.id!;
  }

  trackStaffOrderById(_index: number, item: IStaffOrder): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMenu>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(menu: IMenu): void {
    this.editForm.patchValue({
      id: menu.id,
      name: menu.name,
      serveDate: menu.serveDate,
      bookBeforeDate: menu.bookBeforeDate,
      cancelBeforeDate: menu.cancelBeforeDate,
      foodService: menu.foodService,
      staffOrder: menu.staffOrder,
    });

    this.foodServicesSharedCollection = this.foodServiceService.addFoodServiceToCollectionIfMissing(
      this.foodServicesSharedCollection,
      menu.foodService
    );
    this.staffOrdersSharedCollection = this.staffOrderService.addStaffOrderToCollectionIfMissing(
      this.staffOrdersSharedCollection,
      menu.staffOrder
    );
  }

  protected loadRelationshipsOptions(): void {
    this.foodServiceService
      .query()
      .pipe(map((res: HttpResponse<IFoodService[]>) => res.body ?? []))
      .pipe(
        map((foodServices: IFoodService[]) =>
          this.foodServiceService.addFoodServiceToCollectionIfMissing(foodServices, this.editForm.get('foodService')!.value)
        )
      )
      .subscribe((foodServices: IFoodService[]) => (this.foodServicesSharedCollection = foodServices));

    this.staffOrderService
      .query()
      .pipe(map((res: HttpResponse<IStaffOrder[]>) => res.body ?? []))
      .pipe(
        map((staffOrders: IStaffOrder[]) =>
          this.staffOrderService.addStaffOrderToCollectionIfMissing(staffOrders, this.editForm.get('staffOrder')!.value)
        )
      )
      .subscribe((staffOrders: IStaffOrder[]) => (this.staffOrdersSharedCollection = staffOrders));
  }

  protected createFromForm(): IMenu {
    return {
      ...new Menu(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      serveDate: this.editForm.get(['serveDate'])!.value,
      bookBeforeDate: this.editForm.get(['bookBeforeDate'])!.value,
      cancelBeforeDate: this.editForm.get(['cancelBeforeDate'])!.value,
      foodService: this.editForm.get(['foodService'])!.value,
      staffOrder: this.editForm.get(['staffOrder'])!.value,
    };
  }
}
