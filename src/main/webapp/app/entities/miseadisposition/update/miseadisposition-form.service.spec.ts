import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../miseadisposition.test-samples';

import { MiseadispositionFormService } from './miseadisposition-form.service';

describe('Miseadisposition Form Service', () => {
  let service: MiseadispositionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MiseadispositionFormService);
  });

  describe('Service methods', () => {
    describe('createMiseadispositionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMiseadispositionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            organisme: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            sensMouvement: expect.any(Object),
            dateRetour: expect.any(Object),
            agent: expect.any(Object),
          }),
        );
      });

      it('passing IMiseadisposition should create a new form with FormGroup', () => {
        const formGroup = service.createMiseadispositionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            organisme: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            sensMouvement: expect.any(Object),
            dateRetour: expect.any(Object),
            agent: expect.any(Object),
          }),
        );
      });
    });

    describe('getMiseadisposition', () => {
      it('should return NewMiseadisposition for default Miseadisposition initial value', () => {
        const formGroup = service.createMiseadispositionFormGroup(sampleWithNewData);

        const miseadisposition = service.getMiseadisposition(formGroup) as any;

        expect(miseadisposition).toMatchObject(sampleWithNewData);
      });

      it('should return NewMiseadisposition for empty Miseadisposition initial value', () => {
        const formGroup = service.createMiseadispositionFormGroup();

        const miseadisposition = service.getMiseadisposition(formGroup) as any;

        expect(miseadisposition).toMatchObject({});
      });

      it('should return IMiseadisposition', () => {
        const formGroup = service.createMiseadispositionFormGroup(sampleWithRequiredData);

        const miseadisposition = service.getMiseadisposition(formGroup) as any;

        expect(miseadisposition).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMiseadisposition should not enable id FormControl', () => {
        const formGroup = service.createMiseadispositionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMiseadisposition should disable id FormControl', () => {
        const formGroup = service.createMiseadispositionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
