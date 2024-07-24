import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMotifDisponibilite } from '../motif-disponibilite.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../motif-disponibilite.test-samples';

import { MotifDisponibiliteService } from './motif-disponibilite.service';

const requireRestSample: IMotifDisponibilite = {
  ...sampleWithRequiredData,
};

describe('MotifDisponibilite Service', () => {
  let service: MotifDisponibiliteService;
  let httpMock: HttpTestingController;
  let expectedResult: IMotifDisponibilite | IMotifDisponibilite[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MotifDisponibiliteService);
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

    it('should create a MotifDisponibilite', () => {
      const motifDisponibilite = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(motifDisponibilite).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MotifDisponibilite', () => {
      const motifDisponibilite = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(motifDisponibilite).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MotifDisponibilite', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MotifDisponibilite', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MotifDisponibilite', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMotifDisponibiliteToCollectionIfMissing', () => {
      it('should add a MotifDisponibilite to an empty array', () => {
        const motifDisponibilite: IMotifDisponibilite = sampleWithRequiredData;
        expectedResult = service.addMotifDisponibiliteToCollectionIfMissing([], motifDisponibilite);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(motifDisponibilite);
      });

      it('should not add a MotifDisponibilite to an array that contains it', () => {
        const motifDisponibilite: IMotifDisponibilite = sampleWithRequiredData;
        const motifDisponibiliteCollection: IMotifDisponibilite[] = [
          {
            ...motifDisponibilite,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMotifDisponibiliteToCollectionIfMissing(motifDisponibiliteCollection, motifDisponibilite);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MotifDisponibilite to an array that doesn't contain it", () => {
        const motifDisponibilite: IMotifDisponibilite = sampleWithRequiredData;
        const motifDisponibiliteCollection: IMotifDisponibilite[] = [sampleWithPartialData];
        expectedResult = service.addMotifDisponibiliteToCollectionIfMissing(motifDisponibiliteCollection, motifDisponibilite);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(motifDisponibilite);
      });

      it('should add only unique MotifDisponibilite to an array', () => {
        const motifDisponibiliteArray: IMotifDisponibilite[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const motifDisponibiliteCollection: IMotifDisponibilite[] = [sampleWithRequiredData];
        expectedResult = service.addMotifDisponibiliteToCollectionIfMissing(motifDisponibiliteCollection, ...motifDisponibiliteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const motifDisponibilite: IMotifDisponibilite = sampleWithRequiredData;
        const motifDisponibilite2: IMotifDisponibilite = sampleWithPartialData;
        expectedResult = service.addMotifDisponibiliteToCollectionIfMissing([], motifDisponibilite, motifDisponibilite2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(motifDisponibilite);
        expect(expectedResult).toContain(motifDisponibilite2);
      });

      it('should accept null and undefined values', () => {
        const motifDisponibilite: IMotifDisponibilite = sampleWithRequiredData;
        expectedResult = service.addMotifDisponibiliteToCollectionIfMissing([], null, motifDisponibilite, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(motifDisponibilite);
      });

      it('should return initial array if no MotifDisponibilite is added', () => {
        const motifDisponibiliteCollection: IMotifDisponibilite[] = [sampleWithRequiredData];
        expectedResult = service.addMotifDisponibiliteToCollectionIfMissing(motifDisponibiliteCollection, undefined, null);
        expect(expectedResult).toEqual(motifDisponibiliteCollection);
      });
    });

    describe('compareMotifDisponibilite', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMotifDisponibilite(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMotifDisponibilite(entity1, entity2);
        const compareResult2 = service.compareMotifDisponibilite(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMotifDisponibilite(entity1, entity2);
        const compareResult2 = service.compareMotifDisponibilite(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMotifDisponibilite(entity1, entity2);
        const compareResult2 = service.compareMotifDisponibilite(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
