import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IFoodService, FoodService } from '../food-service.model';

import { FoodServiceService } from './food-service.service';

describe('FoodService Service', () => {
  let service: FoodServiceService;
  let httpMock: HttpTestingController;
  let elemDefault: IFoodService;
  let expectedResult: IFoodService | IFoodService[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(FoodServiceService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      name: 'AAAAAAA',
      rate: 0,
      startDate: currentDate,
      endDate: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          startDate: currentDate.format(DATE_FORMAT),
          endDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a FoodService', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          startDate: currentDate.format(DATE_FORMAT),
          endDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
          endDate: currentDate,
        },
        returnedFromService
      );

      service.create(new FoodService()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a FoodService', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          rate: 1,
          startDate: currentDate.format(DATE_FORMAT),
          endDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
          endDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a FoodService', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          startDate: currentDate.format(DATE_FORMAT),
        },
        new FoodService()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          startDate: currentDate,
          endDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of FoodService', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          name: 'BBBBBB',
          rate: 1,
          startDate: currentDate.format(DATE_FORMAT),
          endDate: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          startDate: currentDate,
          endDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a FoodService', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addFoodServiceToCollectionIfMissing', () => {
      it('should add a FoodService to an empty array', () => {
        const foodService: IFoodService = { id: 123 };
        expectedResult = service.addFoodServiceToCollectionIfMissing([], foodService);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(foodService);
      });

      it('should not add a FoodService to an array that contains it', () => {
        const foodService: IFoodService = { id: 123 };
        const foodServiceCollection: IFoodService[] = [
          {
            ...foodService,
          },
          { id: 456 },
        ];
        expectedResult = service.addFoodServiceToCollectionIfMissing(foodServiceCollection, foodService);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a FoodService to an array that doesn't contain it", () => {
        const foodService: IFoodService = { id: 123 };
        const foodServiceCollection: IFoodService[] = [{ id: 456 }];
        expectedResult = service.addFoodServiceToCollectionIfMissing(foodServiceCollection, foodService);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(foodService);
      });

      it('should add only unique FoodService to an array', () => {
        const foodServiceArray: IFoodService[] = [{ id: 123 }, { id: 456 }, { id: 38358 }];
        const foodServiceCollection: IFoodService[] = [{ id: 123 }];
        expectedResult = service.addFoodServiceToCollectionIfMissing(foodServiceCollection, ...foodServiceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const foodService: IFoodService = { id: 123 };
        const foodService2: IFoodService = { id: 456 };
        expectedResult = service.addFoodServiceToCollectionIfMissing([], foodService, foodService2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(foodService);
        expect(expectedResult).toContain(foodService2);
      });

      it('should accept null and undefined values', () => {
        const foodService: IFoodService = { id: 123 };
        expectedResult = service.addFoodServiceToCollectionIfMissing([], null, foodService, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(foodService);
      });

      it('should return initial array if no FoodService is added', () => {
        const foodServiceCollection: IFoodService[] = [{ id: 123 }];
        expectedResult = service.addFoodServiceToCollectionIfMissing(foodServiceCollection, undefined, null);
        expect(expectedResult).toEqual(foodServiceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
