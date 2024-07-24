import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../disponibilite.test-samples';

import { DisponibiliteFormService } from './disponibilite-form.service';

describe('Disponibilite Form Service', () => {
  let service: DisponibiliteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DisponibiliteFormService);
  });

  describe('Service methods', () => {
    describe('createDisponibiliteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDisponibiliteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            dateRetour: expect.any(Object),
            motifDisponibilite: expect.any(Object),
            agent: expect.any(Object),
          }),
        );
      });

      it('passing IDisponibilite should create a new form with FormGroup', () => {
        const formGroup = service.createDisponibiliteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            dateDebut: expect.any(Object),
            dateFin: expect.any(Object),
            dateRetour: expect.any(Object),
            motifDisponibilite: expect.any(Object),
            agent: expect.any(Object),
          }),
        );
      });
    });

    describe('getDisponibilite', () => {
      it('should return NewDisponibilite for default Disponibilite initial value', () => {
        const formGroup = service.createDisponibiliteFormGroup(sampleWithNewData);

        const disponibilite = service.getDisponibilite(formGroup) as any;

        expect(disponibilite).toMatchObject(sampleWithNewData);
      });

      it('should return NewDisponibilite for empty Disponibilite initial value', () => {
        const formGroup = service.createDisponibiliteFormGroup();

        const disponibilite = service.getDisponibilite(formGroup) as any;

        expect(disponibilite).toMatchObject({});
      });

      it('should return IDisponibilite', () => {
        const formGroup = service.createDisponibiliteFormGroup(sampleWithRequiredData);

        const disponibilite = service.getDisponibilite(formGroup) as any;

        expect(disponibilite).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDisponibilite should not enable id FormControl', () => {
        const formGroup = service.createDisponibiliteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDisponibilite should disable id FormControl', () => {
        const formGroup = service.createDisponibiliteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
