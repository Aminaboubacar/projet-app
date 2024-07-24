import dayjs from 'dayjs/esm';

import { IDisponibilite, NewDisponibilite } from './disponibilite.model';

export const sampleWithRequiredData: IDisponibilite = {
  id: 7621,
  code: 'désagréable de façon à ce que imposer',
};

export const sampleWithPartialData: IDisponibilite = {
  id: 23956,
  code: 'ouah paraître pourvu que',
  dateFin: dayjs('2024-03-19T18:59'),
  dateRetour: dayjs('2024-03-19T20:39'),
};

export const sampleWithFullData: IDisponibilite = {
  id: 3319,
  code: 'sitôt que gens atchoum',
  dateDebut: dayjs('2024-03-19T15:27'),
  dateFin: dayjs('2024-03-20T00:44'),
  dateRetour: dayjs('2024-03-20T04:48'),
};

export const sampleWithNewData: NewDisponibilite = {
  code: 'cyan solitaire peu',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
