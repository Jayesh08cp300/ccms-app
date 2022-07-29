import dayjs from 'dayjs/esm';
import { IMenuItem } from 'app/entities/menu-item/menu-item.model';
import { IFeedback } from 'app/entities/feedback/feedback.model';
import { IFoodService } from 'app/entities/food-service/food-service.model';
import { IStaffOrder } from 'app/entities/staff-order/staff-order.model';

export interface IMenu {
  id?: number;
  name?: string | null;
  serveDate?: dayjs.Dayjs | null;
  bookBeforeDate?: dayjs.Dayjs | null;
  cancelBeforeDate?: dayjs.Dayjs | null;
  menuItems?: IMenuItem[] | null;
  feedbacks?: IFeedback[] | null;
  foodService?: IFoodService | null;
  staffOrder?: IStaffOrder | null;
}

export class Menu implements IMenu {
  constructor(
    public id?: number,
    public name?: string | null,
    public serveDate?: dayjs.Dayjs | null,
    public bookBeforeDate?: dayjs.Dayjs | null,
    public cancelBeforeDate?: dayjs.Dayjs | null,
    public menuItems?: IMenuItem[] | null,
    public feedbacks?: IFeedback[] | null,
    public foodService?: IFoodService | null,
    public staffOrder?: IStaffOrder | null
  ) {}
}

export function getMenuIdentifier(menu: IMenu): number | undefined {
  return menu.id;
}
