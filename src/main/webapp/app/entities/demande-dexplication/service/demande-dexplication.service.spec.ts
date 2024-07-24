import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDemandeDexplication } from '../demande-dexplication.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../demande-dexplication.test-samples';

import { DemandeDexplicationService, RestDemandeDexplication } from './demande-dexplication.service';

const requireRestSample: RestDemandeDexplication = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.toJSON(),
  datappDg: sampleWithRequiredData.datappDg?.toJSON(),
};

describe('DemandeDexplication Service', () => {
  let service: DemandeDexplicationService;
  let httpMock: HttpTestingController;
  let expectedResult: IDemandeDexplication | IDemandeDexplication[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DemandeDexplicationService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a DemandeDexplication', () => {
      const demandeDexplication = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(demandeDexplication).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DemandeDexplication', () => {
      const demandeDexplication = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(demandeDexplication).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DemandeDexplication', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DemandeDexplication', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DemandeDexplication', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDemandeDexplicationToCollectionIfMissing', () => {
      it('should add a DemandeDexplication to an empty array', () => {
        const demandeDexplication: IDemandeDexplication = sampleWithRequiredData;
        expectedResult = service.addDemandeDexplicationToCollectionIfMissing([], demandeDexplication);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(demandeDexplication);
      });

      it('should not add a DemandeDexplication to an array that contains it', () => {
        const demandeDexplication: IDemandeDexplication = sampleWithRequiredData;
        const demandeDexplicationCollection: IDemandeDexplication[] = [
          {
            ...demandeDexplication,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDemandeDexplicationToCollectionIfMissing(demandeDexplicationCollection, demandeDexplication);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DemandeDexplication to an array that doesn't contain it", () => {
        const demandeDexplication: IDemandeDexplication = sampleWithRequiredData;
        const demandeDexplicationCollection: IDemandeDexplication[] = [sampleWithPartialData];
        expectedResult = service.addDemandeDexplicationToCollectionIfMissing(demandeDexplicationCollection, demandeDexplication);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(demandeDexplication);
      });

      it('should add only unique DemandeDexplication to an array', () => {
        const demandeDexplicationArray: IDemandeDexplication[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const demandeDexplicationCollection: IDemandeDexplication[] = [sampleWithRequiredData];
        expectedResult = service.addDemandeDexplicationToCollectionIfMissing(demandeDexplicationCollection, ...demandeDexplicationArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const demandeDexplication: IDemandeDexplication = sampleWithRequiredData;
        const demandeDexplication2: IDemandeDexplication = sampleWithPartialData;
        expectedResult = service.addDemandeDexplicationToCollectionIfMissing([], demandeDexplication, demandeDexplication2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(demandeDexplication);
        expect(expectedResult).toContain(demandeDexplication2);
      });

      it('should accept null and undefined values', () => {
        const demandeDexplication: IDemandeDexplication = sampleWithRequiredData;
        expectedResult = service.addDemandeDexplicationToCollectionIfMissing([], null, demandeDexplication, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(demandeDexplication);
      });

      it('should return initial array if no DemandeDexplication is added', () => {
        const demandeDexplicationCollection: IDemandeDexplication[] = [sampleWithRequiredData];
        expectedResult = service.addDemandeDexplicationToCollectionIfMissing(demandeDexplicationCollection, undefined, null);
        expect(expectedResult).toEqual(demandeDexplicationCollection);
      });
    });

    describe('compareDemandeDexplication', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDemandeDexplication(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDemandeDexplication(entity1, entity2);
        const compareResult2 = service.compareDemandeDexplication(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDemandeDexplication(entity1, entity2);
        const compareResult2 = service.compareDemandeDexplication(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDemandeDexplication(entity1, entity2);
        const compareResult2 = service.compareDemandeDexplication(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
