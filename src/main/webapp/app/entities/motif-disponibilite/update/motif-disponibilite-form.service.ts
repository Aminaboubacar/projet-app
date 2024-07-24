import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IMotifDisponibilite, NewMotifDisponibilite } from '../motif-disponibilite.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IMotifDisponibilite for edit and NewMotifDisponibiliteFormGroupInput for create.
 */
type MotifDisponibiliteFormGroupInput = IMotifDisponibilite | PartialWithRequiredKeyOf<NewMotifDisponibilite>;

type MotifDisponibiliteFormDefaults = Pick<NewMotifDisponibilite, 'id'>;

type MotifDisponibiliteFormGroupContent = {
  id: FormControl<IMotifDisponibilite['id'] | NewMotifDisponibilite['id']>;
  code: FormControl<IMotifDisponibilite['code']>;
  libelle: FormControl<IMotifDisponibilite['libelle']>;
};

export type MotifDisponibiliteFormGroup = FormGroup<MotifDisponibiliteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class MotifDisponibiliteFormService {
  createMotifDisponibiliteFormGroup(motifDisponibilite: MotifDisponibiliteFormGroupInput = { id: null }): MotifDisponibiliteFormGroup {
    const motifDisponibiliteRawValue = {
      ...this.getFormDefaults(),
      ...motifDisponibilite,
    };
    return new FormGroup<MotifDisponibiliteFormGroupContent>({
      id: new FormControl(
        { value: motifDisponibiliteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(motifDisponibiliteRawValue.code, {
        validators: [Validators.required],
      }),
      libelle: new FormControl(motifDisponibiliteRawValue.libelle, {
        validators: [Validators.required],
      }),
    });
  }

  getMotifDisponibilite(form: MotifDisponibiliteFormGroup): IMotifDisponibilite | NewMotifDisponibilite {
    return form.getRawValue() as IMotifDisponibilite | NewMotifDisponibilite;
  }

  resetForm(form: MotifDisponibiliteFormGroup, motifDisponibilite: MotifDisponibiliteFormGroupInput): void {
    const motifDisponibiliteRawValue = { ...this.getFormDefaults(), ...motifDisponibilite };
    form.reset(
      {
        ...motifDisponibiliteRawValue,
        id: { value: motifDisponibiliteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): MotifDisponibiliteFormDefaults {
    return {
      id: null,
    };
  }
}
