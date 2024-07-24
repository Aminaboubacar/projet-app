import dayjs from 'dayjs/esm';
import { IAgent } from 'app/entities/agent/agent.model';

export interface IDemandeDexplication {
  id: number;
  code?: string | null;
  date?: dayjs.Dayjs | null;
  objet?: string | null;
  appDg?: string | null;
  datappDg?: dayjs.Dayjs | null;
  agent?: Pick<IAgent, 'id'> | null;
}

export type NewDemandeDexplication = Omit<IDemandeDexplication, 'id'> & { id: null };
