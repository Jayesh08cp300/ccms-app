import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMenuItem, MenuItem } from '../menu-item.model';
import { MenuItemService } from '../service/menu-item.service';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { IMenu } from 'app/entities/menu/menu.model';
import { MenuService } from 'app/entities/menu/service/menu.service';

@Component({
  selector: 'jhi-menu-item-update',
  templateUrl: './menu-item-update.component.html',
})
export class MenuItemUpdateComponent implements OnInit {
  isSaving = false;

  itemsSharedCollection: IItem[] = [];
  menusSharedCollection: IMenu[] = [];

  editForm = this.fb.group({
    id: [],
    limited: [],
    item: [],
    menu: [],
  });

  constructor(
    protected menuItemService: MenuItemService,
    protected itemService: ItemService,
    protected menuService: MenuService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ menuItem }) => {
      this.updateForm(menuItem);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const menuItem = this.createFromForm();
    if (menuItem.id !== undefined) {
      this.subscribeToSaveResponse(this.menuItemService.update(menuItem));
    } else {
      this.subscribeToSaveResponse(this.menuItemService.create(menuItem));
    }
  }

  trackItemById(_index: number, item: IItem): number {
    return item.id!;
  }

  trackMenuById(_index: number, item: IMenu): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMenuItem>>): void {
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

  protected updateForm(menuItem: IMenuItem): void {
    this.editForm.patchValue({
      id: menuItem.id,
      limited: menuItem.limited,
      item: menuItem.item,
      menu: menuItem.menu,
    });

    this.itemsSharedCollection = this.itemService.addItemToCollectionIfMissing(this.itemsSharedCollection, menuItem.item);
    this.menusSharedCollection = this.menuService.addMenuToCollectionIfMissing(this.menusSharedCollection, menuItem.menu);
  }

  protected loadRelationshipsOptions(): void {
    this.itemService
      .query()
      .pipe(map((res: HttpResponse<IItem[]>) => res.body ?? []))
      .pipe(map((items: IItem[]) => this.itemService.addItemToCollectionIfMissing(items, this.editForm.get('item')!.value)))
      .subscribe((items: IItem[]) => (this.itemsSharedCollection = items));

    this.menuService
      .query()
      .pipe(map((res: HttpResponse<IMenu[]>) => res.body ?? []))
      .pipe(map((menus: IMenu[]) => this.menuService.addMenuToCollectionIfMissing(menus, this.editForm.get('menu')!.value)))
      .subscribe((menus: IMenu[]) => (this.menusSharedCollection = menus));
  }

  protected createFromForm(): IMenuItem {
    return {
      ...new MenuItem(),
      id: this.editForm.get(['id'])!.value,
      limited: this.editForm.get(['limited'])!.value,
      item: this.editForm.get(['item'])!.value,
      menu: this.editForm.get(['menu'])!.value,
    };
  }
}
