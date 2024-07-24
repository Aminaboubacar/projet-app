import dayjs from 'dayjs/esm';
import { IAgent } from 'app/entities/agent/agent.model';

export interface IMiseadisposition {
  id: number;
  code?: string | null;
  organisme?: string | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  sensMouvement?: string | null;
  dateRetour?: dayjs.Dayjs | null;
  agent?: Pick<IAgent, 'id'> | null;
}

export type NewMiseadisposition = Omit<IMiseadisposition, 'id'> & { id: null };
