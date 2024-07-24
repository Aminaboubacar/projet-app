export interface IDegre {
  id: number;
  code?: string | null;
  libelle?: string | null;
}

export type NewDegre = Omit<IDegre, 'id'> & { id: null };
