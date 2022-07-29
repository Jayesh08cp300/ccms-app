import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFoodService, FoodService } from '../food-service.model';
import { FoodServiceService } from '../service/food-service.service';
import { IFoodServiceProvider } from 'app/entities/food-service-provider/food-service-provider.model';
import { FoodServiceProviderService } from 'app/entities/food-service-provider/service/food-service-provider.service';

@Component({
  selector: 'jhi-food-service-update',
  templateUrl: './food-service-update.component.html',
})
export class FoodServiceUpdateComponent implements OnInit {
  isSaving = false;

  foodServiceProvidersSharedCollection: IFoodServiceProvider[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    rate: [],
    startDate: [],
    endDate: [],
    foodServiceProvider: [],
  });

  constructor(
    protected foodServiceService: FoodServiceService,
    protected foodServiceProviderService: FoodServiceProviderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ foodService }) => {
      this.updateForm(foodService);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const foodService = this.createFromForm();
    if (foodService.id !== undefined) {
      this.subscribeToSaveResponse(this.foodServiceService.update(foodService));
    } else {
      this.subscribeToSaveResponse(this.foodServiceService.create(foodService));
    }
  }

  trackFoodServiceProviderById(_index: number, item: IFoodServiceProvider): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFoodService>>): void {
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

  protected updateForm(foodService: IFoodService): void {
    this.editForm.patchValue({
      id: foodService.id,
      name: foodService.name,
      rate: foodService.rate,
      startDate: foodService.startDate,
      endDate: foodService.endDate,
      foodServiceProvider: foodService.foodServiceProvider,
    });

    this.foodServiceProvidersSharedCollection = this.foodServiceProviderService.addFoodServiceProviderToCollectionIfMissing(
      this.foodServiceProvidersSharedCollection,
      foodService.foodServiceProvider
    );
  }

  protected loadRelationshipsOptions(): void {
    this.foodServiceProviderService
      .query()
      .pipe(map((res: HttpResponse<IFoodServiceProvider[]>) => res.body ?? []))
      .pipe(
        map((foodServiceProviders: IFoodServiceProvider[]) =>
          this.foodServiceProviderService.addFoodServiceProviderToCollectionIfMissing(
            foodServiceProviders,
            this.editForm.get('foodServiceProvider')!.value
          )
        )
      )
      .subscribe((foodServiceProviders: IFoodServiceProvider[]) => (this.foodServiceProvidersSharedCollection = foodServiceProviders));
  }

  protected createFromForm(): IFoodService {
    return {
      ...new FoodService(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      rate: this.editForm.get(['rate'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      foodServiceProvider: this.editForm.get(['foodServiceProvider'])!.value,
    };
  }
}
