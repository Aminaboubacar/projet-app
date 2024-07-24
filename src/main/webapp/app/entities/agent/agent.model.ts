import dayjs from 'dayjs/esm';
import { IPoste } from 'app/entities/poste/poste.model';

export interface IAgent {
  id: number;
  matricule?: string | null;
  nom?: string | null;
  prenom?: string | null;
  dateNaissance?: dayjs.Dayjs | null;
  lieuNaissance?: string | null;
  nationalite?: string | null;
  telephone?: string | null;
  adresse?: string | null;
  dateDece?: dayjs.Dayjs | null;
  causeDece?: string | null;
  poste?: IPoste | null;
  //*poste?: Pick<IPoste, 'id'> | null;*//
}

export type NewAgent = Omit<IAgent, 'id'> & { id: null };
