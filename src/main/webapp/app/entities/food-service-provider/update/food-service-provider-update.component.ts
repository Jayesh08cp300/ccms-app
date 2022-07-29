import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IFoodServiceProvider, FoodServiceProvider } from '../food-service-provider.model';
import { FoodServiceProviderService } from '../service/food-service-provider.service';

@Component({
  selector: 'jhi-food-service-provider-update',
  templateUrl: './food-service-provider-update.component.html',
})
export class FoodServiceProviderUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    fullName: [],
    docProofName: [],
    docProofNo: [],
    address: [],
    contactNo: [],
    emailAddress: [],
  });

  constructor(
    protected foodServiceProviderService: FoodServiceProviderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ foodServiceProvider }) => {
      this.updateForm(foodServiceProvider);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const foodServiceProvider = this.createFromForm();
    if (foodServiceProvider.id !== undefined) {
      this.subscribeToSaveResponse(this.foodServiceProviderService.update(foodServiceProvider));
    } else {
      this.subscribeToSaveResponse(this.foodServiceProviderService.create(foodServiceProvider));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFoodServiceProvider>>): void {
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

  protected updateForm(foodServiceProvider: IFoodServiceProvider): void {
    this.editForm.patchValue({
      id: foodServiceProvider.id,
      fullName: foodServiceProvider.fullName,
      docProofName: foodServiceProvider.docProofName,
      docProofNo: foodServiceProvider.docProofNo,
      address: foodServiceProvider.address,
      contactNo: foodServiceProvider.contactNo,
      emailAddress: foodServiceProvider.emailAddress,
    });
  }

  protected createFromForm(): IFoodServiceProvider {
    return {
      ...new FoodServiceProvider(),
      id: this.editForm.get(['id'])!.value,
      fullName: this.editForm.get(['fullName'])!.value,
      docProofName: this.editForm.get(['docProofName'])!.value,
      docProofNo: this.editForm.get(['docProofNo'])!.value,
      address: this.editForm.get(['address'])!.value,
      contactNo: this.editForm.get(['contactNo'])!.value,
      emailAddress: this.editForm.get(['emailAddress'])!.value,
    };
  }
}
