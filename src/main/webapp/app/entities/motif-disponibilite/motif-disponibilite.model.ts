export interface IMotifDisponibilite {
  id: number;
  code?: string | null;
  libelle?: string | null;
}

export type NewMotifDisponibilite = Omit<IMotifDisponibilite, 'id'> & { id: null };
