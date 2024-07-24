import dayjs from 'dayjs/esm';

import { IMiseadisposition, NewMiseadisposition } from './miseadisposition.model';

export const sampleWithRequiredData: IMiseadisposition = {
  id: 30293,
  code: 'au cas où',
  organisme: 'mairie atchoum',
};

export const sampleWithPartialData: IMiseadisposition = {
  id: 3308,
  code: 'lectorat sincère patientèle',
  organisme: 'souvent chanter',
  dateDebut: dayjs('2024-03-19T14:35'),
  dateFin: dayjs('2024-03-19T18:47'),
  dateRetour: dayjs('2024-03-20T04:15'),
};

export const sampleWithFullData: IMiseadisposition = {
  id: 23034,
  code: 'ruiner rectorat relire',
  organisme: 'tsoin-tsoin',
  dateDebut: dayjs('2024-03-19T15:51'),
  dateFin: dayjs('2024-03-19T14:23'),
  sensMouvement: 'délégation',
  dateRetour: dayjs('2024-03-19T17:04'),
};

export const sampleWithNewData: NewMiseadisposition = {
  code: 'lectorat',
  organisme: 'contre sur quant à',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
