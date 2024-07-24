import { ISanction, NewSanction } from './sanction.model';

export const sampleWithRequiredData: ISanction = {
  id: 26976,
  code: 'cadre groin groin vis-à-vie de',
  libelle: 'plutôt par rapport à',
};

export const sampleWithPartialData: ISanction = {
  id: 6998,
  code: 'probablement',
  libelle: 'électorat',
};

export const sampleWithFullData: ISanction = {
  id: 21283,
  code: 'sauf à',
  libelle: 'au moyen de population du Québec',
};

export const sampleWithNewData: NewSanction = {
  code: 'tsoin-tsoin sans',
  libelle: 'guide au défaut de fumer',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
