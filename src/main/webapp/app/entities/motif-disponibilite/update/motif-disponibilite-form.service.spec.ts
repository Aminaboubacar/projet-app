import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../motif-disponibilite.test-samples';

import { MotifDisponibiliteFormService } from './motif-disponibilite-form.service';

describe('MotifDisponibilite Form Service', () => {
  let service: MotifDisponibiliteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MotifDisponibiliteFormService);
  });

  describe('Service methods', () => {
    describe('createMotifDisponibiliteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMotifDisponibiliteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });

      it('passing IMotifDisponibilite should create a new form with FormGroup', () => {
        const formGroup = service.createMotifDisponibiliteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            libelle: expect.any(Object),
          }),
        );
      });
    });

    describe('getMotifDisponibilite', () => {
      it('should return NewMotifDisponibilite for default MotifDisponibilite initial value', () => {
        const formGroup = service.createMotifDisponibiliteFormGroup(sampleWithNewData);

        const motifDisponibilite = service.getMotifDisponibilite(formGroup) as any;

        expect(motifDisponibilite).toMatchObject(sampleWithNewData);
      });

      it('should return NewMotifDisponibilite for empty MotifDisponibilite initial value', () => {
        const formGroup = service.createMotifDisponibiliteFormGroup();

        const motifDisponibilite = service.getMotifDisponibilite(formGroup) as any;

        expect(motifDisponibilite).toMatchObject({});
      });

      it('should return IMotifDisponibilite', () => {
        const formGroup = service.createMotifDisponibiliteFormGroup(sampleWithRequiredData);

        const motifDisponibilite = service.getMotifDisponibilite(formGroup) as any;

        expect(motifDisponibilite).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMotifDisponibilite should not enable id FormControl', () => {
        const formGroup = service.createMotifDisponibiliteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMotifDisponibilite should disable id FormControl', () => {
        const formGroup = service.createMotifDisponibiliteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
