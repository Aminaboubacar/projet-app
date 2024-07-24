import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISanctionner } from '../sanctionner.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../sanctionner.test-samples';

import { SanctionnerService, RestSanctionner } from './sanctionner.service';

const requireRestSample: RestSanctionner = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.toJSON(),
};

describe('Sanctionner Service', () => {
  let service: SanctionnerService;
  let httpMock: HttpTestingController;
  let expectedResult: ISanctionner | ISanctionner[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(SanctionnerService);
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

    it('should create a Sanctionner', () => {
      const sanctionner = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(sanctionner).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Sanctionner', () => {
      const sanctionner = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(sanctionner).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Sanctionner', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Sanctionner', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Sanctionner', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSanctionnerToCollectionIfMissing', () => {
      it('should add a Sanctionner to an empty array', () => {
        const sanctionner: ISanctionner = sampleWithRequiredData;
        expectedResult = service.addSanctionnerToCollectionIfMissing([], sanctionner);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sanctionner);
      });

      it('should not add a Sanctionner to an array that contains it', () => {
        const sanctionner: ISanctionner = sampleWithRequiredData;
        const sanctionnerCollection: ISanctionner[] = [
          {
            ...sanctionner,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSanctionnerToCollectionIfMissing(sanctionnerCollection, sanctionner);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Sanctionner to an array that doesn't contain it", () => {
        const sanctionner: ISanctionner = sampleWithRequiredData;
        const sanctionnerCollection: ISanctionner[] = [sampleWithPartialData];
        expectedResult = service.addSanctionnerToCollectionIfMissing(sanctionnerCollection, sanctionner);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sanctionner);
      });

      it('should add only unique Sanctionner to an array', () => {
        const sanctionnerArray: ISanctionner[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const sanctionnerCollection: ISanctionner[] = [sampleWithRequiredData];
        expectedResult = service.addSanctionnerToCollectionIfMissing(sanctionnerCollection, ...sanctionnerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const sanctionner: ISanctionner = sampleWithRequiredData;
        const sanctionner2: ISanctionner = sampleWithPartialData;
        expectedResult = service.addSanctionnerToCollectionIfMissing([], sanctionner, sanctionner2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(sanctionner);
        expect(expectedResult).toContain(sanctionner2);
      });

      it('should accept null and undefined values', () => {
        const sanctionner: ISanctionner = sampleWithRequiredData;
        expectedResult = service.addSanctionnerToCollectionIfMissing([], null, sanctionner, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(sanctionner);
      });

      it('should return initial array if no Sanctionner is added', () => {
        const sanctionnerCollection: ISanctionner[] = [sampleWithRequiredData];
        expectedResult = service.addSanctionnerToCollectionIfMissing(sanctionnerCollection, undefined, null);
        expect(expectedResult).toEqual(sanctionnerCollection);
      });
    });

    describe('compareSanctionner', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSanctionner(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSanctionner(entity1, entity2);
        const compareResult2 = service.compareSanctionner(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSanctionner(entity1, entity2);
        const compareResult2 = service.compareSanctionner(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSanctionner(entity1, entity2);
        const compareResult2 = service.compareSanctionner(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
