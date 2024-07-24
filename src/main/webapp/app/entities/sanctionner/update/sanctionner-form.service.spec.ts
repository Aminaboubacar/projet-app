import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sanctionner.test-samples';

import { SanctionnerFormService } from './sanctionner-form.service';

describe('Sanctionner Form Service', () => {
  let service: SanctionnerFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SanctionnerFormService);
  });

  describe('Service methods', () => {
    describe('createSanctionnerFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSanctionnerFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            sanction: expect.any(Object),
            demandeDexplication: expect.any(Object),
          }),
        );
      });

      it('passing ISanctionner should create a new form with FormGroup', () => {
        const formGroup = service.createSanctionnerFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            date: expect.any(Object),
            sanction: expect.any(Object),
            demandeDexplication: expect.any(Object),
          }),
        );
      });
    });

    describe('getSanctionner', () => {
      it('should return NewSanctionner for default Sanctionner initial value', () => {
        const formGroup = service.createSanctionnerFormGroup(sampleWithNewData);

        const sanctionner = service.getSanctionner(formGroup) as any;

        expect(sanctionner).toMatchObject(sampleWithNewData);
      });

      it('should return NewSanctionner for empty Sanctionner initial value', () => {
        const formGroup = service.createSanctionnerFormGroup();

        const sanctionner = service.getSanctionner(formGroup) as any;

        expect(sanctionner).toMatchObject({});
      });

      it('should return ISanctionner', () => {
        const formGroup = service.createSanctionnerFormGroup(sampleWithRequiredData);

        const sanctionner = service.getSanctionner(formGroup) as any;

        expect(sanctionner).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISanctionner should not enable id FormControl', () => {
        const formGroup = service.createSanctionnerFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSanctionner should disable id FormControl', () => {
        const formGroup = service.createSanctionnerFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
