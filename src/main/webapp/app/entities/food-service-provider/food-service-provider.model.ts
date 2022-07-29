import { IFoodService } from 'app/entities/food-service/food-service.model';

export interface IFoodServiceProvider {
  id?: number;
  fullName?: string | null;
  docProofName?: string | null;
  docProofNo?: string | null;
  address?: string | null;
  contactNo?: string | null;
  emailAddress?: string | null;
  foodServices?: IFoodService[] | null;
}

export class FoodServiceProvider implements IFoodServiceProvider {
  constructor(
    public id?: number,
    public fullName?: string | null,
    public docProofName?: string | null,
    public docProofNo?: string | null,
    public address?: string | null,
    public contactNo?: string | null,
    public emailAddress?: string | null,
    public foodServices?: IFoodService[] | null
  ) {}
}

export function getFoodServiceProviderIdentifier(foodServiceProvider: IFoodServiceProvider): number | undefined {
  return foodServiceProvider.id;
}
