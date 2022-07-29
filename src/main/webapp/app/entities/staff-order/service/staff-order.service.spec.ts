import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IStaffOrder, StaffOrder } from '../staff-order.model';

import { StaffOrderService } from './staff-order.service';

describe('StaffOrder Service', () => {
  let service: StaffOrderService;
  let httpMock: HttpTestingController;
  let elemDefault: IStaffOrder;
  let expectedResult: IStaffOrder | IStaffOrder[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StaffOrderService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
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

    it('should create a StaffOrder', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new StaffOrder()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StaffOrder', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StaffOrder', () => {
      const patchObject = Object.assign({}, new StaffOrder());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StaffOrder', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
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

    it('should delete a StaffOrder', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addStaffOrderToCollectionIfMissing', () => {
      it('should add a StaffOrder to an empty array', () => {
        const staffOrder: IStaffOrder = { id: 123 };
        expectedResult = service.addStaffOrderToCollectionIfMissing([], staffOrder);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(staffOrder);
      });

      it('should not add a StaffOrder to an array that contains it', () => {
        const staffOrder: IStaffOrder = { id: 123 };
        const staffOrderCollection: IStaffOrder[] = [
          {
            ...staffOrder,
          },
          { id: 456 },
        ];
        expectedResult = service.addStaffOrderToCollectionIfMissing(staffOrderCollection, staffOrder);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StaffOrder to an array that doesn't contain it", () => {
        const staffOrder: IStaffOrder = { id: 123 };
        const staffOrderCollection: IStaffOrder[] = [{ id: 456 }];
        expectedResult = service.addStaffOrderToCollectionIfMissing(staffOrderCollection, staffOrder);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(staffOrder);
      });

      it('should add only unique StaffOrder to an array', () => {
        const staffOrderArray: IStaffOrder[] = [{ id: 123 }, { id: 456 }, { id: 86197 }];
        const staffOrderCollection: IStaffOrder[] = [{ id: 123 }];
        expectedResult = service.addStaffOrderToCollectionIfMissing(staffOrderCollection, ...staffOrderArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const staffOrder: IStaffOrder = { id: 123 };
        const staffOrder2: IStaffOrder = { id: 456 };
        expectedResult = service.addStaffOrderToCollectionIfMissing([], staffOrder, staffOrder2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(staffOrder);
        expect(expectedResult).toContain(staffOrder2);
      });

      it('should accept null and undefined values', () => {
        const staffOrder: IStaffOrder = { id: 123 };
        expectedResult = service.addStaffOrderToCollectionIfMissing([], null, staffOrder, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(staffOrder);
      });

      it('should return initial array if no StaffOrder is added', () => {
        const staffOrderCollection: IStaffOrder[] = [{ id: 123 }];
        expectedResult = service.addStaffOrderToCollectionIfMissing(staffOrderCollection, undefined, null);
        expect(expectedResult).toEqual(staffOrderCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
