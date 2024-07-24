export interface IPoste {
  id: number;
  code?: string | null;
  libelle?: string | null;
}

export type NewPoste = Omit<IPoste, 'id'> & { id: null };
