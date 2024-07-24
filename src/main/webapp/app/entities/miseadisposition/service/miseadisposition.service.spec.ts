import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMiseadisposition } from '../miseadisposition.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../miseadisposition.test-samples';

import { MiseadispositionService, RestMiseadisposition } from './miseadisposition.service';

const requireRestSample: RestMiseadisposition = {
  ...sampleWithRequiredData,
  dateDebut: sampleWithRequiredData.dateDebut?.toJSON(),
  dateFin: sampleWithRequiredData.dateFin?.toJSON(),
  dateRetour: sampleWithRequiredData.dateRetour?.toJSON(),
};

describe('Miseadisposition Service', () => {
  let service: MiseadispositionService;
  let httpMock: HttpTestingController;
  let expectedResult: IMiseadisposition | IMiseadisposition[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MiseadispositionService);
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

    it('should create a Miseadisposition', () => {
      const miseadisposition = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(miseadisposition).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Miseadisposition', () => {
      const miseadisposition = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(miseadisposition).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Miseadisposition', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Miseadisposition', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Miseadisposition', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMiseadispositionToCollectionIfMissing', () => {
      it('should add a Miseadisposition to an empty array', () => {
        const miseadisposition: IMiseadisposition = sampleWithRequiredData;
        expectedResult = service.addMiseadispositionToCollectionIfMissing([], miseadisposition);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(miseadisposition);
      });

      it('should not add a Miseadisposition to an array that contains it', () => {
        const miseadisposition: IMiseadisposition = sampleWithRequiredData;
        const miseadispositionCollection: IMiseadisposition[] = [
          {
            ...miseadisposition,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMiseadispositionToCollectionIfMissing(miseadispositionCollection, miseadisposition);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Miseadisposition to an array that doesn't contain it", () => {
        const miseadisposition: IMiseadisposition = sampleWithRequiredData;
        const miseadispositionCollection: IMiseadisposition[] = [sampleWithPartialData];
        expectedResult = service.addMiseadispositionToCollectionIfMissing(miseadispositionCollection, miseadisposition);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(miseadisposition);
      });

      it('should add only unique Miseadisposition to an array', () => {
        const miseadispositionArray: IMiseadisposition[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const miseadispositionCollection: IMiseadisposition[] = [sampleWithRequiredData];
        expectedResult = service.addMiseadispositionToCollectionIfMissing(miseadispositionCollection, ...miseadispositionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const miseadisposition: IMiseadisposition = sampleWithRequiredData;
        const miseadisposition2: IMiseadisposition = sampleWithPartialData;
        expectedResult = service.addMiseadispositionToCollectionIfMissing([], miseadisposition, miseadisposition2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(miseadisposition);
        expect(expectedResult).toContain(miseadisposition2);
      });

      it('should accept null and undefined values', () => {
        const miseadisposition: IMiseadisposition = sampleWithRequiredData;
        expectedResult = service.addMiseadispositionToCollectionIfMissing([], null, miseadisposition, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(miseadisposition);
      });

      it('should return initial array if no Miseadisposition is added', () => {
        const miseadispositionCollection: IMiseadisposition[] = [sampleWithRequiredData];
        expectedResult = service.addMiseadispositionToCollectionIfMissing(miseadispositionCollection, undefined, null);
        expect(expectedResult).toEqual(miseadispositionCollection);
      });
    });

    describe('compareMiseadisposition', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMiseadisposition(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMiseadisposition(entity1, entity2);
        const compareResult2 = service.compareMiseadisposition(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMiseadisposition(entity1, entity2);
        const compareResult2 = service.compareMiseadisposition(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMiseadisposition(entity1, entity2);
        const compareResult2 = service.compareMiseadisposition(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
