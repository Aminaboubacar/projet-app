import { IPoste, NewPoste } from './poste.model';

export const sampleWithRequiredData: IPoste = {
  id: 19901,
  code: 'complètement après sage',
  libelle: 'au-devant cot cot',
};

export const sampleWithPartialData: IPoste = {
  id: 703,
  code: 'à moins de',
  libelle: 'abattre',
};

export const sampleWithFullData: IPoste = {
  id: 19832,
  code: 'candide',
  libelle: 'pour que sauf',
};

export const sampleWithNewData: NewPoste = {
  code: 'marron combien',
  libelle: "chef de cuisine d'après",
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
