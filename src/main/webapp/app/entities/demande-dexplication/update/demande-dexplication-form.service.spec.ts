import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../demande-dexplication.test-samples';

import { DemandeDexplicationFormService } from './demande-dexplication-form.service';

describe('DemandeDexplication Form Service', () => {
  let service: DemandeDexplicationFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DemandeDexplicationFormService);
  });

  describe('Service methods', () => {
    describe('createDemandeDexplicationFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createDemandeDexplicationFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            date: expect.any(Object),
            objet: expect.any(Object),
            appDg: expect.any(Object),
            datappDg: expect.any(Object),
            agent: expect.any(Object),
          }),
        );
      });

      it('passing IDemandeDexplication should create a new form with FormGroup', () => {
        const formGroup = service.createDemandeDexplicationFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            code: expect.any(Object),
            date: expect.any(Object),
            objet: expect.any(Object),
            appDg: expect.any(Object),
            datappDg: expect.any(Object),
            agent: expect.any(Object),
          }),
        );
      });
    });

    describe('getDemandeDexplication', () => {
      it('should return NewDemandeDexplication for default DemandeDexplication initial value', () => {
        const formGroup = service.createDemandeDexplicationFormGroup(sampleWithNewData);

        const demandeDexplication = service.getDemandeDexplication(formGroup) as any;

        expect(demandeDexplication).toMatchObject(sampleWithNewData);
      });

      it('should return NewDemandeDexplication for empty DemandeDexplication initial value', () => {
        const formGroup = service.createDemandeDexplicationFormGroup();

        const demandeDexplication = service.getDemandeDexplication(formGroup) as any;

        expect(demandeDexplication).toMatchObject({});
      });

      it('should return IDemandeDexplication', () => {
        const formGroup = service.createDemandeDexplicationFormGroup(sampleWithRequiredData);

        const demandeDexplication = service.getDemandeDexplication(formGroup) as any;

        expect(demandeDexplication).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IDemandeDexplication should not enable id FormControl', () => {
        const formGroup = service.createDemandeDexplicationFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewDemandeDexplication should disable id FormControl', () => {
        const formGroup = service.createDemandeDexplicationFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
