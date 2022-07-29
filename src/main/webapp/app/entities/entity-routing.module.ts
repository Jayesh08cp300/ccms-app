import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'food-service-provider',
        data: { pageTitle: 'ccmsApp.foodServiceProvider.home.title' },
        loadChildren: () => import('./food-service-provider/food-service-provider.module').then(m => m.FoodServiceProviderModule),
      },
      {
        path: 'food-service',
        data: { pageTitle: 'ccmsApp.foodService.home.title' },
        loadChildren: () => import('./food-service/food-service.module').then(m => m.FoodServiceModule),
      },
      {
        path: 'menu',
        data: { pageTitle: 'ccmsApp.menu.home.title' },
        loadChildren: () => import('./menu/menu.module').then(m => m.MenuModule),
      },
      {
        path: 'item',
        data: { pageTitle: 'ccmsApp.item.home.title' },
        loadChildren: () => import('./item/item.module').then(m => m.ItemModule),
      },
      {
        path: 'menu-item',
        data: { pageTitle: 'ccmsApp.menuItem.home.title' },
        loadChildren: () => import('./menu-item/menu-item.module').then(m => m.MenuItemModule),
      },
      {
        path: 'feedback',
        data: { pageTitle: 'ccmsApp.feedback.home.title' },
        loadChildren: () => import('./feedback/feedback.module').then(m => m.FeedbackModule),
      },
      {
        path: 'staff-order',
        data: { pageTitle: 'ccmsApp.staffOrder.home.title' },
        loadChildren: () => import('./staff-order/staff-order.module').then(m => m.StaffOrderModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
