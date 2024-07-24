import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../degre.test-samples';

import { DegreFormService } from './degre-form.service';

describe('Degre Form Service', () => {
  let service: DegreFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DegreFormService);
  });

  describe('Service methods', () => {
    describe('createDegreFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDegreFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });

      it('passing IDegre should create a new form with FormGroup', () => {
        const formGroup = service.createDegreFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });
    });

    describe('getDegre', () => {
      it('should return NewDegre for default Degre initial value', () => {
        const formGroup = service.createDegreFormGroup(sampleWithNewData);

        const degre = service.getDegre(formGroup) as any;

        expect(degre).toMatchObject(sampleWithNewData);
      });

      it('should return NewDegre for empty Degre initial value', () => {
        const formGroup = service.createDegreFormGroup();

        const degre = service.getDegre(formGroup) as any;

        expect(degre).toMatchObject({});
      });

      it('should return IDegre', () => {
        const formGroup = service.createDegreFormGroup(sampleWithRequiredData);

        const degre = service.getDegre(formGroup) as any;

        expect(degre).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDegre should not enable id FormControl', () => {
        const formGroup = service.createDegreFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDegre should disable id FormControl', () => {
        const formGroup = service.createDegreFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
