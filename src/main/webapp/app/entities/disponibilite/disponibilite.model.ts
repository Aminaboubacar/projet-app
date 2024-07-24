import dayjs from 'dayjs/esm';
import { IMotifDisponibilite } from 'app/entities/motif-disponibilite/motif-disponibilite.model';
import { IAgent } from 'app/entities/agent/agent.model';

export interface IDisponibilite {
  id: number;
  code?: string | null;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  dateRetour?: dayjs.Dayjs | null;
  motifDisponibilite?: Pick<IMotifDisponibilite, 'id'> | null;
  agent?: Pick<IAgent, 'id'> | null;
}

export type NewDisponibilite = Omit<IDisponibilite, 'id'> & { id: null };
