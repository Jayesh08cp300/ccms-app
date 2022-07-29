import { IItem } from 'app/entities/item/item.model';
import { IMenu } from 'app/entities/menu/menu.model';

export interface IMenuItem {
  id?: number;
  limited?: boolean | null;
  item?: IItem | null;
  menu?: IMenu | null;
}

export class MenuItem implements IMenuItem {
  constructor(public id?: number, public limited?: boolean | null, public item?: IItem | null, public menu?: IMenu | null) {
    this.limited = this.limited ?? false;
  }
}

export function getMenuItemIdentifier(menuItem: IMenuItem): number | undefined {
  return menuItem.id;
}
