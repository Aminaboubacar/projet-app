import dayjs from 'dayjs/esm';

import { IDemandeDexplication, NewDemandeDexplication } from './demande-dexplication.model';

export const sampleWithRequiredData: IDemandeDexplication = {
  id: 24113,
  code: 'en plus de raide corps enseignant',
  objet: "à l'entour de",
};

export const sampleWithPartialData: IDemandeDexplication = {
  id: 5187,
  code: 'lunatique',
  objet: 'fourbe trop de peur que',
  appDg: 'pourvu que toc-toc en vérité',
};

export const sampleWithFullData: IDemandeDexplication = {
  id: 21739,
  code: 'boum',
  date: dayjs('2024-03-20T09:59'),
  objet: 'là-haut également sans',
  appDg: 'cocorico si bien que',
  datappDg: dayjs('2024-03-20T01:08'),
};

export const sampleWithNewData: NewDemandeDexplication = {
  code: 'rectangulaire d’autant que',
  objet: 'hormis debout',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
