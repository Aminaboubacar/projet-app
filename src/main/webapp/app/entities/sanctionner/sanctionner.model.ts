import dayjs from 'dayjs/esm';
import { ISanction } from 'app/entities/sanction/sanction.model';
import { IDemandeDexplication } from 'app/entities/demande-dexplication/demande-dexplication.model';

export interface ISanctionner {
  id: number;
  date?: dayjs.Dayjs | null;
  sanction?: Pick<ISanction, 'id'> | null;
  demandeDexplication?: Pick<IDemandeDexplication, 'id'> | null;
}

export type NewSanctionner = Omit<ISanctionner, 'id'> & { id: null };
