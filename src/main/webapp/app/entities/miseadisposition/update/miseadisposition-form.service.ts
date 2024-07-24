import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMiseadisposition, NewMiseadisposition } from '../miseadisposition.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMiseadisposition for edit and NewMiseadispositionFormGroupInput for create.
 */
type MiseadispositionFormGroupInput = IMiseadisposition | PartialWithRequiredKeyOf<NewMiseadisposition>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IMiseadisposition | NewMiseadisposition> = Omit<T, 'dateDebut' | 'dateFin' | 'dateRetour'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
  dateRetour?: string | null;
};

type MiseadispositionFormRawValue = FormValueOf<IMiseadisposition>;

type NewMiseadispositionFormRawValue = FormValueOf<NewMiseadisposition>;

type MiseadispositionFormDefaults = Pick<NewMiseadisposition, 'id' | 'dateDebut' | 'dateFin' | 'dateRetour'>;

type MiseadispositionFormGroupContent = {
  id: FormControl<MiseadispositionFormRawValue['id'] | NewMiseadisposition['id']>;
  code: FormControl<MiseadispositionFormRawValue['code']>;
  organisme: FormControl<MiseadispositionFormRawValue['organisme']>;
  dateDebut: FormControl<MiseadispositionFormRawValue['dateDebut']>;
  dateFin: FormControl<MiseadispositionFormRawValue['dateFin']>;
  sensMouvement: FormControl<MiseadispositionFormRawValue['sensMouvement']>;
  dateRetour: FormControl<MiseadispositionFormRawValue['dateRetour']>;
  agent: FormControl<MiseadispositionFormRawValue['agent']>;
};

export type MiseadispositionFormGroup = FormGroup<MiseadispositionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MiseadispositionFormService {
  createMiseadispositionFormGroup(miseadisposition: MiseadispositionFormGroupInput = { id: null }): MiseadispositionFormGroup {
    const miseadispositionRawValue = this.convertMiseadispositionToMiseadispositionRawValue({
      ...this.getFormDefaults(),
      ...miseadisposition,
    });
    return new FormGroup<MiseadispositionFormGroupContent>({
      id: new FormControl(
        { value: miseadispositionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(miseadispositionRawValue.code, {
        validators: [Validators.required],
      }),
      organisme: new FormControl(miseadispositionRawValue.organisme, {
        validators: [Validators.required],
      }),
      dateDebut: new FormControl(miseadispositionRawValue.dateDebut),
      dateFin: new FormControl(miseadispositionRawValue.dateFin),
      sensMouvement: new FormControl(miseadispositionRawValue.sensMouvement),
      dateRetour: new FormControl(miseadispositionRawValue.dateRetour),
      agent: new FormControl(miseadispositionRawValue.agent),
    });
  }

  getMiseadisposition(form: MiseadispositionFormGroup): IMiseadisposition | NewMiseadisposition {
    return this.convertMiseadispositionRawValueToMiseadisposition(
      form.getRawValue() as MiseadispositionFormRawValue | NewMiseadispositionFormRawValue,
    );
  }

  resetForm(form: MiseadispositionFormGroup, miseadisposition: MiseadispositionFormGroupInput): void {
    const miseadispositionRawValue = this.convertMiseadispositionToMiseadispositionRawValue({
      ...this.getFormDefaults(),
      ...miseadisposition,
    });
    form.reset(
      {
        ...miseadispositionRawValue,
        id: { value: miseadispositionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MiseadispositionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateDebut: currentTime,
      dateFin: currentTime,
      dateRetour: currentTime,
    };
  }

  private convertMiseadispositionRawValueToMiseadisposition(
    rawMiseadisposition: MiseadispositionFormRawValue | NewMiseadispositionFormRawValue,
  ): IMiseadisposition | NewMiseadisposition {
    return {
      ...rawMiseadisposition,
      dateDebut: dayjs(rawMiseadisposition.dateDebut, DATE_TIME_FORMAT),
      dateFin: dayjs(rawMiseadisposition.dateFin, DATE_TIME_FORMAT),
      dateRetour: dayjs(rawMiseadisposition.dateRetour, DATE_TIME_FORMAT),
    };
  }

  private convertMiseadispositionToMiseadispositionRawValue(
    miseadisposition: IMiseadisposition | (Partial<NewMiseadisposition> & MiseadispositionFormDefaults),
  ): MiseadispositionFormRawValue | PartialWithRequiredKeyOf<NewMiseadispositionFormRawValue> {
    return {
      ...miseadisposition,
      dateDebut: miseadisposition.dateDebut ? miseadisposition.dateDebut.format(DATE_TIME_FORMAT) : undefined,
      dateFin: miseadisposition.dateFin ? miseadisposition.dateFin.format(DATE_TIME_FORMAT) : undefined,
      dateRetour: miseadisposition.dateRetour ? miseadisposition.dateRetour.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
