import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISanctionner, NewSanctionner } from '../sanctionner.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISanctionner for edit and NewSanctionnerFormGroupInput for create.
 */
type SanctionnerFormGroupInput = ISanctionner | PartialWithRequiredKeyOf<NewSanctionner>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISanctionner | NewSanctionner> = Omit<T, 'date'> & {
  date?: string | null;
};

type SanctionnerFormRawValue = FormValueOf<ISanctionner>;

type NewSanctionnerFormRawValue = FormValueOf<NewSanctionner>;

type SanctionnerFormDefaults = Pick<NewSanctionner, 'id' | 'date'>;

type SanctionnerFormGroupContent = {
  id: FormControl<SanctionnerFormRawValue['id'] | NewSanctionner['id']>;
  date: FormControl<SanctionnerFormRawValue['date']>;
  sanction: FormControl<SanctionnerFormRawValue['sanction']>;
  demandeDexplication: FormControl<SanctionnerFormRawValue['demandeDexplication']>;
};

export type SanctionnerFormGroup = FormGroup<SanctionnerFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SanctionnerFormService {
  createSanctionnerFormGroup(sanctionner: SanctionnerFormGroupInput = { id: null }): SanctionnerFormGroup {
    const sanctionnerRawValue = this.convertSanctionnerToSanctionnerRawValue({
      ...this.getFormDefaults(),
      ...sanctionner,
    });
    return new FormGroup<SanctionnerFormGroupContent>({
      id: new FormControl(
        { value: sanctionnerRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      date: new FormControl(sanctionnerRawValue.date),
      sanction: new FormControl(sanctionnerRawValue.sanction),
      demandeDexplication: new FormControl(sanctionnerRawValue.demandeDexplication),
    });
  }

  getSanctionner(form: SanctionnerFormGroup): ISanctionner | NewSanctionner {
    return this.convertSanctionnerRawValueToSanctionner(form.getRawValue() as SanctionnerFormRawValue | NewSanctionnerFormRawValue);
  }

  resetForm(form: SanctionnerFormGroup, sanctionner: SanctionnerFormGroupInput): void {
    const sanctionnerRawValue = this.convertSanctionnerToSanctionnerRawValue({ ...this.getFormDefaults(), ...sanctionner });
    form.reset(
      {
        ...sanctionnerRawValue,
        id: { value: sanctionnerRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SanctionnerFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      date: currentTime,
    };
  }

  private convertSanctionnerRawValueToSanctionner(
    rawSanctionner: SanctionnerFormRawValue | NewSanctionnerFormRawValue,
  ): ISanctionner | NewSanctionner {
    return {
      ...rawSanctionner,
      date: dayjs(rawSanctionner.date, DATE_TIME_FORMAT),
    };
  }

  private convertSanctionnerToSanctionnerRawValue(
    sanctionner: ISanctionner | (Partial<NewSanctionner> & SanctionnerFormDefaults),
  ): SanctionnerFormRawValue | PartialWithRequiredKeyOf<NewSanctionnerFormRawValue> {
    return {
      ...sanctionner,
      date: sanctionner.date ? sanctionner.date.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
