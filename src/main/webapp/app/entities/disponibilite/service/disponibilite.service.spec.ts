import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDisponibilite } from '../disponibilite.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../disponibilite.test-samples';

import { DisponibiliteService, RestDisponibilite } from './disponibilite.service';

const requireRestSample: RestDisponibilite = {
  ...sampleWithRequiredData,
  dateDebut: sampleWithRequiredData.dateDebut?.toJSON(),
  dateFin: sampleWithRequiredData.dateFin?.toJSON(),
  dateRetour: sampleWithRequiredData.dateRetour?.toJSON(),
};

describe('Disponibilite Service', () => {
  let service: DisponibiliteService;
  let httpMock: HttpTestingController;
  let expectedResult: IDisponibilite | IDisponibilite[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DisponibiliteService);
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

    it('should create a Disponibilite', () => {
      const disponibilite = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(disponibilite).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Disponibilite', () => {
      const disponibilite = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(disponibilite).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Disponibilite', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Disponibilite', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Disponibilite', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDisponibiliteToCollectionIfMissing', () => {
      it('should add a Disponibilite to an empty array', () => {
        const disponibilite: IDisponibilite = sampleWithRequiredData;
        expectedResult = service.addDisponibiliteToCollectionIfMissing([], disponibilite);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(disponibilite);
      });

      it('should not add a Disponibilite to an array that contains it', () => {
        const disponibilite: IDisponibilite = sampleWithRequiredData;
        const disponibiliteCollection: IDisponibilite[] = [
          {
            ...disponibilite,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDisponibiliteToCollectionIfMissing(disponibiliteCollection, disponibilite);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Disponibilite to an array that doesn't contain it", () => {
        const disponibilite: IDisponibilite = sampleWithRequiredData;
        const disponibiliteCollection: IDisponibilite[] = [sampleWithPartialData];
        expectedResult = service.addDisponibiliteToCollectionIfMissing(disponibiliteCollection, disponibilite);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(disponibilite);
      });

      it('should add only unique Disponibilite to an array', () => {
        const disponibiliteArray: IDisponibilite[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const disponibiliteCollection: IDisponibilite[] = [sampleWithRequiredData];
        expectedResult = service.addDisponibiliteToCollectionIfMissing(disponibiliteCollection, ...disponibiliteArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const disponibilite: IDisponibilite = sampleWithRequiredData;
        const disponibilite2: IDisponibilite = sampleWithPartialData;
        expectedResult = service.addDisponibiliteToCollectionIfMissing([], disponibilite, disponibilite2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(disponibilite);
        expect(expectedResult).toContain(disponibilite2);
      });

      it('should accept null and undefined values', () => {
        const disponibilite: IDisponibilite = sampleWithRequiredData;
        expectedResult = service.addDisponibiliteToCollectionIfMissing([], null, disponibilite, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(disponibilite);
      });

      it('should return initial array if no Disponibilite is added', () => {
        const disponibiliteCollection: IDisponibilite[] = [sampleWithRequiredData];
        expectedResult = service.addDisponibiliteToCollectionIfMissing(disponibiliteCollection, undefined, null);
        expect(expectedResult).toEqual(disponibiliteCollection);
      });
    });

    describe('compareDisponibilite', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDisponibilite(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDisponibilite(entity1, entity2);
        const compareResult2 = service.compareDisponibilite(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDisponibilite(entity1, entity2);
        const compareResult2 = service.compareDisponibilite(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDisponibilite(entity1, entity2);
        const compareResult2 = service.compareDisponibilite(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
