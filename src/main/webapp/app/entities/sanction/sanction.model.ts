import { IDegre } from 'app/entities/degre/degre.model';

export interface ISanction {
  id: number;
  code?: string | null;
  libelle?: string | null;
  degre?: IDegre | null;
}

export type NewSanction = Omit<ISanction, 'id'> & { id: null };
