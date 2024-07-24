import dayjs from 'dayjs/esm';

import { ISanctionner, NewSanctionner } from './sanctionner.model';

export const sampleWithRequiredData: ISanctionner = {
  id: 14936,
};

export const sampleWithPartialData: ISanctionner = {
  id: 18044,
  date: dayjs('2024-03-19T13:23'),
};

export const sampleWithFullData: ISanctionner = {
  id: 9848,
  date: dayjs('2024-03-19T13:33'),
};

export const sampleWithNewData: NewSanctionner = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
