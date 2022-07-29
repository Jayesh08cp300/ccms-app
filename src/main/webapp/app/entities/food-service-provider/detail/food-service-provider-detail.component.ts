import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFoodServiceProvider } from '../food-service-provider.model';

@Component({
  selector: 'jhi-food-service-provider-detail',
  templateUrl: './food-service-provider-detail.component.html',
})
export class FoodServiceProviderDetailComponent implements OnInit {
  foodServiceProvider: IFoodServiceProvider | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ foodServiceProvider }) => {
      this.foodServiceProvider = foodServiceProvider;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
