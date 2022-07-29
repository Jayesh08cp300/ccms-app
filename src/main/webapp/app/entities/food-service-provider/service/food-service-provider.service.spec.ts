import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IFoodServiceProvider, FoodServiceProvider } from '../food-service-provider.model';

import { FoodServiceProviderService } from './food-service-provider.service';

describe('FoodServiceProvider Service', () => {
  let service: FoodServiceProviderService;
  let httpMock: HttpTestingController;
  let elemDefault: IFoodServiceProvider;
  let expectedResult: IFoodServiceProvider | IFoodServiceProvider[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FoodServiceProviderService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      fullName: 'AAAAAAA',
      docProofName: 'AAAAAAA',
      docProofNo: 'AAAAAAA',
      address: 'AAAAAAA',
      contactNo: 'AAAAAAA',
      emailAddress: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a FoodServiceProvider', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new FoodServiceProvider()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FoodServiceProvider', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fullName: 'BBBBBB',
          docProofName: 'BBBBBB',
          docProofNo: 'BBBBBB',
          address: 'BBBBBB',
          contactNo: 'BBBBBB',
          emailAddress: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FoodServiceProvider', () => {
      const patchObject = Object.assign(
        {
          fullName: 'BBBBBB',
          contactNo: 'BBBBBB',
        },
        new FoodServiceProvider()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FoodServiceProvider', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          fullName: 'BBBBBB',
          docProofName: 'BBBBBB',
          docProofNo: 'BBBBBB',
          address: 'BBBBBB',
          contactNo: 'BBBBBB',
          emailAddress: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a FoodServiceProvider', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFoodServiceProviderToCollectionIfMissing', () => {
      it('should add a FoodServiceProvider to an empty array', () => {
        const foodServiceProvider: IFoodServiceProvider = { id: 123 };
        expectedResult = service.addFoodServiceProviderToCollectionIfMissing([], foodServiceProvider);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(foodServiceProvider);
      });

      it('should not add a FoodServiceProvider to an array that contains it', () => {
        const foodServiceProvider: IFoodServiceProvider = { id: 123 };
        const foodServiceProviderCollection: IFoodServiceProvider[] = [
          {
            ...foodServiceProvider,
          },
          { id: 456 },
        ];
        expectedResult = service.addFoodServiceProviderToCollectionIfMissing(foodServiceProviderCollection, foodServiceProvider);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FoodServiceProvider to an array that doesn't contain it", () => {
        const foodServiceProvider: IFoodServiceProvider = { id: 123 };
        const foodServiceProviderCollection: IFoodServiceProvider[] = [{ id: 456 }];
        expectedResult = service.addFoodServiceProviderToCollectionIfMissing(foodServiceProviderCollection, foodServiceProvider);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(foodServiceProvider);
      });

      it('should add only unique FoodServiceProvider to an array', () => {
        const foodServiceProviderArray: IFoodServiceProvider[] = [{ id: 123 }, { id: 456 }, { id: 12198 }];
        const foodServiceProviderCollection: IFoodServiceProvider[] = [{ id: 123 }];
        expectedResult = service.addFoodServiceProviderToCollectionIfMissing(foodServiceProviderCollection, ...foodServiceProviderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const foodServiceProvider: IFoodServiceProvider = { id: 123 };
        const foodServiceProvider2: IFoodServiceProvider = { id: 456 };
        expectedResult = service.addFoodServiceProviderToCollectionIfMissing([], foodServiceProvider, foodServiceProvider2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(foodServiceProvider);
        expect(expectedResult).toContain(foodServiceProvider2);
      });

      it('should accept null and undefined values', () => {
        const foodServiceProvider: IFoodServiceProvider = { id: 123 };
        expectedResult = service.addFoodServiceProviderToCollectionIfMissing([], null, foodServiceProvider, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(foodServiceProvider);
      });

      it('should return initial array if no FoodServiceProvider is added', () => {
        const foodServiceProviderCollection: IFoodServiceProvider[] = [{ id: 123 }];
        expectedResult = service.addFoodServiceProviderToCollectionIfMissing(foodServiceProviderCollection, undefined, null);
        expect(expectedResult).toEqual(foodServiceProviderCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
