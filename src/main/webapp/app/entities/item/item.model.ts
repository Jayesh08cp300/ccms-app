import { IMenuItem } from 'app/entities/menu-item/menu-item.model';

export interface IItem {
  id?: number;
  name?: string | null;
  veg?: boolean | null;
  menuItems?: IMenuItem[] | null;
}

export class Item implements IItem {
  constructor(public id?: number, public name?: string | null, public veg?: boolean | null, public menuItems?: IMenuItem[] | null) {
    this.veg = this.veg ?? false;
  }
}

export function getItemIdentifier(item: IItem): number | undefined {
  return item.id;
}
