import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MenuItemService } from '../service/menu-item.service';
import { IMenuItem, MenuItem } from '../menu-item.model';
import { IItem } from 'app/entities/item/item.model';
import { ItemService } from 'app/entities/item/service/item.service';
import { IMenu } from 'app/entities/menu/menu.model';
import { MenuService } from 'app/entities/menu/service/menu.service';

import { MenuItemUpdateComponent } from './menu-item-update.component';

describe('MenuItem Management Update Component', () => {
  let comp: MenuItemUpdateComponent;
  let fixture: ComponentFixture<MenuItemUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let menuItemService: MenuItemService;
  let itemService: ItemService;
  let menuService: MenuService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MenuItemUpdateComponent],
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
      .overrideTemplate(MenuItemUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MenuItemUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    menuItemService = TestBed.inject(MenuItemService);
    itemService = TestBed.inject(ItemService);
    menuService = TestBed.inject(MenuService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Item query and add missing value', () => {
      const menuItem: IMenuItem = { id: 456 };
      const item: IItem = { id: 54872 };
      menuItem.item = item;

      const itemCollection: IItem[] = [{ id: 63320 }];
      jest.spyOn(itemService, 'query').mockReturnValue(of(new HttpResponse({ body: itemCollection })));
      const additionalItems = [item];
      const expectedCollection: IItem[] = [...additionalItems, ...itemCollection];
      jest.spyOn(itemService, 'addItemToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ menuItem });
      comp.ngOnInit();

      expect(itemService.query).toHaveBeenCalled();
      expect(itemService.addItemToCollectionIfMissing).toHaveBeenCalledWith(itemCollection, ...additionalItems);
      expect(comp.itemsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Menu query and add missing value', () => {
      const menuItem: IMenuItem = { id: 456 };
      const menu: IMenu = { id: 23584 };
      menuItem.menu = menu;

      const menuCollection: IMenu[] = [{ id: 4346 }];
      jest.spyOn(menuService, 'query').mockReturnValue(of(new HttpResponse({ body: menuCollection })));
      const additionalMenus = [menu];
      const expectedCollection: IMenu[] = [...additionalMenus, ...menuCollection];
      jest.spyOn(menuService, 'addMenuToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ menuItem });
      comp.ngOnInit();

      expect(menuService.query).toHaveBeenCalled();
      expect(menuService.addMenuToCollectionIfMissing).toHaveBeenCalledWith(menuCollection, ...additionalMenus);
      expect(comp.menusSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const menuItem: IMenuItem = { id: 456 };
      const item: IItem = { id: 14044 };
      menuItem.item = item;
      const menu: IMenu = { id: 34567 };
      menuItem.menu = menu;

      activatedRoute.data = of({ menuItem });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(menuItem));
      expect(comp.itemsSharedCollection).toContain(item);
      expect(comp.menusSharedCollection).toContain(menu);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MenuItem>>();
      const menuItem = { id: 123 };
      jest.spyOn(menuItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ menuItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: menuItem }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(menuItemService.update).toHaveBeenCalledWith(menuItem);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MenuItem>>();
      const menuItem = new MenuItem();
      jest.spyOn(menuItemService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ menuItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: menuItem }));
      saveSubject.complete();

      // THEN
      expect(menuItemService.create).toHaveBeenCalledWith(menuItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<MenuItem>>();
      const menuItem = { id: 123 };
      jest.spyOn(menuItemService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ menuItem });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(menuItemService.update).toHaveBeenCalledWith(menuItem);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackItemById', () => {
      it('Should return tracked Item primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackItemById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackMenuById', () => {
      it('Should return tracked Menu primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackMenuById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
