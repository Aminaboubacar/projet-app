import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDisponibilite, NewDisponibilite } from '../disponibilite.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDisponibilite for edit and NewDisponibiliteFormGroupInput for create.
 */
type DisponibiliteFormGroupInput = IDisponibilite | PartialWithRequiredKeyOf<NewDisponibilite>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IDisponibilite | NewDisponibilite> = Omit<T, 'dateDebut' | 'dateFin' | 'dateRetour'> & {
  dateDebut?: string | null;
  dateFin?: string | null;
  dateRetour?: string | null;
};

type DisponibiliteFormRawValue = FormValueOf<IDisponibilite>;

type NewDisponibiliteFormRawValue = FormValueOf<NewDisponibilite>;

type DisponibiliteFormDefaults = Pick<NewDisponibilite, 'id' | 'dateDebut' | 'dateFin' | 'dateRetour'>;

type DisponibiliteFormGroupContent = {
  id: FormControl<DisponibiliteFormRawValue['id'] | NewDisponibilite['id']>;
  code: FormControl<DisponibiliteFormRawValue['code']>;
  dateDebut: FormControl<DisponibiliteFormRawValue['dateDebut']>;
  dateFin: FormControl<DisponibiliteFormRawValue['dateFin']>;
  dateRetour: FormControl<DisponibiliteFormRawValue['dateRetour']>;
  motifDisponibilite: FormControl<DisponibiliteFormRawValue['motifDisponibilite']>;
  agent: FormControl<DisponibiliteFormRawValue['agent']>;
};

export type DisponibiliteFormGroup = FormGroup<DisponibiliteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DisponibiliteFormService {
  createDisponibiliteFormGroup(disponibilite: DisponibiliteFormGroupInput = { id: null }): DisponibiliteFormGroup {
    const disponibiliteRawValue = this.convertDisponibiliteToDisponibiliteRawValue({
      ...this.getFormDefaults(),
      ...disponibilite,
    });
    return new FormGroup<DisponibiliteFormGroupContent>({
      id: new FormControl(
        { value: disponibiliteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(disponibiliteRawValue.code, {
        validators: [Validators.required],
      }),
      dateDebut: new FormControl(disponibiliteRawValue.dateDebut),
      dateFin: new FormControl(disponibiliteRawValue.dateFin),
      dateRetour: new FormControl(disponibiliteRawValue.dateRetour),
      motifDisponibilite: new FormControl(disponibiliteRawValue.motifDisponibilite),
      agent: new FormControl(disponibiliteRawValue.agent),
    });
  }

  getDisponibilite(form: DisponibiliteFormGroup): IDisponibilite | NewDisponibilite {
    return this.convertDisponibiliteRawValueToDisponibilite(form.getRawValue() as DisponibiliteFormRawValue | NewDisponibiliteFormRawValue);
  }

  resetForm(form: DisponibiliteFormGroup, disponibilite: DisponibiliteFormGroupInput): void {
    const disponibiliteRawValue = this.convertDisponibiliteToDisponibiliteRawValue({ ...this.getFormDefaults(), ...disponibilite });
    form.reset(
      {
        ...disponibiliteRawValue,
        id: { value: disponibiliteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DisponibiliteFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateDebut: currentTime,
      dateFin: currentTime,
      dateRetour: currentTime,
    };
  }

  private convertDisponibiliteRawValueToDisponibilite(
    rawDisponibilite: DisponibiliteFormRawValue | NewDisponibiliteFormRawValue,
  ): IDisponibilite | NewDisponibilite {
    return {
      ...rawDisponibilite,
      dateDebut: dayjs(rawDisponibilite.dateDebut, DATE_TIME_FORMAT),
      dateFin: dayjs(rawDisponibilite.dateFin, DATE_TIME_FORMAT),
      dateRetour: dayjs(rawDisponibilite.dateRetour, DATE_TIME_FORMAT),
    };
  }

  private convertDisponibiliteToDisponibiliteRawValue(
    disponibilite: IDisponibilite | (Partial<NewDisponibilite> & DisponibiliteFormDefaults),
  ): DisponibiliteFormRawValue | PartialWithRequiredKeyOf<NewDisponibiliteFormRawValue> {
    return {
      ...disponibilite,
      dateDebut: disponibilite.dateDebut ? disponibilite.dateDebut.format(DATE_TIME_FORMAT) : undefined,
      dateFin: disponibilite.dateFin ? disponibilite.dateFin.format(DATE_TIME_FORMAT) : undefined,
      dateRetour: disponibilite.dateRetour ? disponibilite.dateRetour.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
