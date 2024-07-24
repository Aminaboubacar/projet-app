import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAgent, NewAgent } from '../agent.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAgent for edit and NewAgentFormGroupInput for create.
 */
type AgentFormGroupInput = IAgent | PartialWithRequiredKeyOf<NewAgent>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAgent | NewAgent> = Omit<T, 'dateNaissance' | 'dateDece'> & {
  dateNaissance?: string | null;
  dateDece?: string | null;
};

type AgentFormRawValue = FormValueOf<IAgent>;

type NewAgentFormRawValue = FormValueOf<NewAgent>;

type AgentFormDefaults = Pick<NewAgent, 'id' | 'dateNaissance' | 'dateDece'>;

type AgentFormGroupContent = {
  id: FormControl<AgentFormRawValue['id'] | NewAgent['id']>;
  matricule: FormControl<AgentFormRawValue['matricule']>;
  nom: FormControl<AgentFormRawValue['nom']>;
  prenom: FormControl<AgentFormRawValue['prenom']>;
  dateNaissance: FormControl<AgentFormRawValue['dateNaissance']>;
  lieuNaissance: FormControl<AgentFormRawValue['lieuNaissance']>;
  nationalite: FormControl<AgentFormRawValue['nationalite']>;
  telephone: FormControl<AgentFormRawValue['telephone']>;
  adresse: FormControl<AgentFormRawValue['adresse']>;
  dateDece: FormControl<AgentFormRawValue['dateDece']>;
  causeDece: FormControl<AgentFormRawValue['causeDece']>;
  poste: FormControl<AgentFormRawValue['poste']>;
};

export type AgentFormGroup = FormGroup<AgentFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AgentFormService {
  createAgentFormGroup(agent: AgentFormGroupInput = { id: null }): AgentFormGroup {
    const agentRawValue = this.convertAgentToAgentRawValue({
      ...this.getFormDefaults(),
      ...agent,
    });
    return new FormGroup<AgentFormGroupContent>({
      id: new FormControl(
        { value: agentRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      matricule: new FormControl(agentRawValue.matricule, {
        validators: [Validators.required],
      }),
      nom: new FormControl(agentRawValue.nom, {
        validators: [Validators.required],
      }),
      prenom: new FormControl(agentRawValue.prenom, {
        validators: [Validators.required],
      }),
      dateNaissance: new FormControl(agentRawValue.dateNaissance),
      lieuNaissance: new FormControl(agentRawValue.lieuNaissance),
      nationalite: new FormControl(agentRawValue.nationalite),
      telephone: new FormControl(agentRawValue.telephone),
      adresse: new FormControl(agentRawValue.adresse),
      dateDece: new FormControl(agentRawValue.dateDece),
      causeDece: new FormControl(agentRawValue.causeDece),
      poste: new FormControl(agentRawValue.poste),
    });
  }

  getAgent(form: AgentFormGroup): IAgent | NewAgent {
    return this.convertAgentRawValueToAgent(form.getRawValue() as AgentFormRawValue | NewAgentFormRawValue);
  }

  resetForm(form: AgentFormGroup, agent: AgentFormGroupInput): void {
    const agentRawValue = this.convertAgentToAgentRawValue({ ...this.getFormDefaults(), ...agent });
    form.reset(
      {
        ...agentRawValue,
        id: { value: agentRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AgentFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateNaissance: currentTime,
      dateDece: currentTime,
    };
  }

  private convertAgentRawValueToAgent(rawAgent: AgentFormRawValue | NewAgentFormRawValue): IAgent | NewAgent {
    return {
      ...rawAgent,
      dateNaissance: dayjs(rawAgent.dateNaissance, DATE_TIME_FORMAT),
      dateDece: dayjs(rawAgent.dateDece, DATE_TIME_FORMAT),
    };
  }

  private convertAgentToAgentRawValue(
    agent: IAgent | (Partial<NewAgent> & AgentFormDefaults),
  ): AgentFormRawValue | PartialWithRequiredKeyOf<NewAgentFormRawValue> {
    return {
      ...agent,
      dateNaissance: agent.dateNaissance ? agent.dateNaissance.format(DATE_TIME_FORMAT) : undefined,
      dateDece: agent.dateDece ? agent.dateDece.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
