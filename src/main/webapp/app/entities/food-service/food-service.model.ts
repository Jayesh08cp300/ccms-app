import dayjs from 'dayjs/esm';
import { IMenu } from 'app/entities/menu/menu.model';
import { IFoodServiceProvider } from 'app/entities/food-service-provider/food-service-provider.model';

export interface IFoodService {
  id?: number;
  name?: string | null;
  rate?: number | null;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  menus?: IMenu[] | null;
  foodServiceProvider?: IFoodServiceProvider | null;
}

export class FoodService implements IFoodService {
  constructor(
    public id?: number,
    public name?: string | null,
    public rate?: number | null,
    public startDate?: dayjs.Dayjs | null,
    public endDate?: dayjs.Dayjs | null,
    public menus?: IMenu[] | null,
    public foodServiceProvider?: IFoodServiceProvider | null
  ) {}
}

export function getFoodServiceIdentifier(foodService: IFoodService): number | undefined {
  return foodService.id;
}
