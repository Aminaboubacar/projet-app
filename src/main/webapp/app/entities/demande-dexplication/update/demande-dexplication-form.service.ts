import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDemandeDexplication, NewDemandeDexplication } from '../demande-dexplication.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDemandeDexplication for edit and NewDemandeDexplicationFormGroupInput for create.
 */
type DemandeDexplicationFormGroupInput = IDemandeDexplication | PartialWithRequiredKeyOf<NewDemandeDexplication>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDemandeDexplication | NewDemandeDexplication> = Omit<T, 'date' | 'datappDg'> & {
  date?: string | null;
  datappDg?: string | null;
};

type DemandeDexplicationFormRawValue = FormValueOf<IDemandeDexplication>;

type NewDemandeDexplicationFormRawValue = FormValueOf<NewDemandeDexplication>;

type DemandeDexplicationFormDefaults = Pick<NewDemandeDexplication, 'id' | 'date' | 'datappDg'>;

type DemandeDexplicationFormGroupContent = {
  id: FormControl<DemandeDexplicationFormRawValue['id'] | NewDemandeDexplication['id']>;
  code: FormControl<DemandeDexplicationFormRawValue['code']>;
  date: FormControl<DemandeDexplicationFormRawValue['date']>;
  objet: FormControl<DemandeDexplicationFormRawValue['objet']>;
  appDg: FormControl<DemandeDexplicationFormRawValue['appDg']>;
  datappDg: FormControl<DemandeDexplicationFormRawValue['datappDg']>;
  agent: FormControl<DemandeDexplicationFormRawValue['agent']>;
};

export type DemandeDexplicationFormGroup = FormGroup<DemandeDexplicationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DemandeDexplicationFormService {
  createDemandeDexplicationFormGroup(demandeDexplication: DemandeDexplicationFormGroupInput = { id: null }): DemandeDexplicationFormGroup {
    const demandeDexplicationRawValue = this.convertDemandeDexplicationToDemandeDexplicationRawValue({
      ...this.getFormDefaults(),
      ...demandeDexplication,
    });
    return new FormGroup<DemandeDexplicationFormGroupContent>({
      id: new FormControl(
        { value: demandeDexplicationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(demandeDexplicationRawValue.code, {
        validators: [Validators.required],
      }),
      date: new FormControl(demandeDexplicationRawValue.date),
      objet: new FormControl(demandeDexplicationRawValue.objet, {
        validators: [Validators.required],
      }),
      appDg: new FormControl(demandeDexplicationRawValue.appDg),
      datappDg: new FormControl(demandeDexplicationRawValue.datappDg),
      agent: new FormControl(demandeDexplicationRawValue.agent),
    });
  }

  getDemandeDexplication(form: DemandeDexplicationFormGroup): IDemandeDexplication | NewDemandeDexplication {
    return this.convertDemandeDexplicationRawValueToDemandeDexplication(
      form.getRawValue() as DemandeDexplicationFormRawValue | NewDemandeDexplicationFormRawValue,
    );
  }

  resetForm(form: DemandeDexplicationFormGroup, demandeDexplication: DemandeDexplicationFormGroupInput): void {
    const demandeDexplicationRawValue = this.convertDemandeDexplicationToDemandeDexplicationRawValue({
      ...this.getFormDefaults(),
      ...demandeDexplication,
    });
    form.reset(
      {
        ...demandeDexplicationRawValue,
        id: { value: demandeDexplicationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DemandeDexplicationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
      datappDg: currentTime,
    };
  }

  private convertDemandeDexplicationRawValueToDemandeDexplication(
    rawDemandeDexplication: DemandeDexplicationFormRawValue | NewDemandeDexplicationFormRawValue,
  ): IDemandeDexplication | NewDemandeDexplication {
    return {
      ...rawDemandeDexplication,
      date: dayjs(rawDemandeDexplication.date, DATE_TIME_FORMAT),
      datappDg: dayjs(rawDemandeDexplication.datappDg, DATE_TIME_FORMAT),
    };
  }

  private convertDemandeDexplicationToDemandeDexplicationRawValue(
    demandeDexplication: IDemandeDexplication | (Partial<NewDemandeDexplication> & DemandeDexplicationFormDefaults),
  ): DemandeDexplicationFormRawValue | PartialWithRequiredKeyOf<NewDemandeDexplicationFormRawValue> {
    return {
      ...demandeDexplication,
      date: demandeDexplication.date ? demandeDexplication.date.format(DATE_TIME_FORMAT) : undefined,
      datappDg: demandeDexplication.datappDg ? demandeDexplication.datappDg.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
