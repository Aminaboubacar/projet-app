import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDegre } from '../degre.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../degre.test-samples';

import { DegreService } from './degre.service';

const requireRestSample: IDegre = {
  ...sampleWithRequiredData,
};

describe('Degre Service', () => {
  let service: DegreService;
  let httpMock: HttpTestingController;
  let expectedResult: IDegre | IDegre[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DegreService);
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

    it('should create a Degre', () => {
      const degre = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(degre).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Degre', () => {
      const degre = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(degre).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Degre', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Degre', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Degre', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDegreToCollectionIfMissing', () => {
      it('should add a Degre to an empty array', () => {
        const degre: IDegre = sampleWithRequiredData;
        expectedResult = service.addDegreToCollectionIfMissing([], degre);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(degre);
      });

      it('should not add a Degre to an array that contains it', () => {
        const degre: IDegre = sampleWithRequiredData;
        const degreCollection: IDegre[] = [
          {
            ...degre,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDegreToCollectionIfMissing(degreCollection, degre);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Degre to an array that doesn't contain it", () => {
        const degre: IDegre = sampleWithRequiredData;
        const degreCollection: IDegre[] = [sampleWithPartialData];
        expectedResult = service.addDegreToCollectionIfMissing(degreCollection, degre);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(degre);
      });

      it('should add only unique Degre to an array', () => {
        const degreArray: IDegre[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const degreCollection: IDegre[] = [sampleWithRequiredData];
        expectedResult = service.addDegreToCollectionIfMissing(degreCollection, ...degreArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const degre: IDegre = sampleWithRequiredData;
        const degre2: IDegre = sampleWithPartialData;
        expectedResult = service.addDegreToCollectionIfMissing([], degre, degre2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(degre);
        expect(expectedResult).toContain(degre2);
      });

      it('should accept null and undefined values', () => {
        const degre: IDegre = sampleWithRequiredData;
        expectedResult = service.addDegreToCollectionIfMissing([], null, degre, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(degre);
      });

      it('should return initial array if no Degre is added', () => {
        const degreCollection: IDegre[] = [sampleWithRequiredData];
        expectedResult = service.addDegreToCollectionIfMissing(degreCollection, undefined, null);
        expect(expectedResult).toEqual(degreCollection);
      });
    });

    describe('compareDegre', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDegre(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDegre(entity1, entity2);
        const compareResult2 = service.compareDegre(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDegre(entity1, entity2);
        const compareResult2 = service.compareDegre(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDegre(entity1, entity2);
        const compareResult2 = service.compareDegre(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
