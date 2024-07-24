import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IDegre, NewDegre } from '../degre.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IDegre for edit and NewDegreFormGroupInput for create.
 */
type DegreFormGroupInput = IDegre | PartialWithRequiredKeyOf<NewDegre>;

type DegreFormDefaults = Pick<NewDegre, 'id'>;

type DegreFormGroupContent = {
  id: FormControl<IDegre['id'] | NewDegre['id']>;
  code: FormControl<IDegre['code']>;
  libelle: FormControl<IDegre['libelle']>;
};

export type DegreFormGroup = FormGroup<DegreFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class DegreFormService {
  createDegreFormGroup(degre: DegreFormGroupInput = { id: null }): DegreFormGroup {
    const degreRawValue = {
      ...this.getFormDefaults(),
      ...degre,
    };
    return new FormGroup<DegreFormGroupContent>({
      id: new FormControl(
        { value: degreRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      code: new FormControl(degreRawValue.code, {
        validators: [Validators.required],
      }),
      libelle: new FormControl(degreRawValue.libelle, {
        validators: [Validators.required],
      }),
    });
  }

  getDegre(form: DegreFormGroup): IDegre | NewDegre {
    return form.getRawValue() as IDegre | NewDegre;
  }

  resetForm(form: DegreFormGroup, degre: DegreFormGroupInput): void {
    const degreRawValue = { ...this.getFormDefaults(), ...degre };
    form.reset(
      {
        ...degreRawValue,
        id: { value: degreRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): DegreFormDefaults {
    return {
      id: null,
    };
  }
}
