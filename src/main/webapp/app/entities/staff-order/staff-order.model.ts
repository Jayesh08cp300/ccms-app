import { IMenu } from 'app/entities/menu/menu.model';
import { IUser } from 'app/entities/user/user.model';

export interface IStaffOrder {
  id?: number;
  menus?: IMenu[] | null;
  users?: IUser[] | null;
}

export class StaffOrder implements IStaffOrder {
  constructor(public id?: number, public menus?: IMenu[] | null, public users?: IUser[] | null) {}
}

export function getStaffOrderIdentifier(staffOrder: IStaffOrder): number | undefined {
  return staffOrder.id;
}
