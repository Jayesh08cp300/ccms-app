import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFoodService } from '../food-service.model';

@Component({
  selector: 'jhi-food-service-detail',
  templateUrl: './food-service-detail.component.html',
})
export class FoodServiceDetailComponent implements OnInit {
  foodService: IFoodService | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ foodService }) => {
      this.foodService = foodService;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
