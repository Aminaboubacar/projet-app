import { IMotifDisponibilite, NewMotifDisponibilite } from './motif-disponibilite.model';

export const sampleWithRequiredData: IMotifDisponibilite = {
  id: 5218,
  code: 'de peur de',
  libelle: 'ici bof mal',
};

export const sampleWithPartialData: IMotifDisponibilite = {
  id: 29635,
  code: 'ah réciter',
  libelle: 'cocorico',
};

export const sampleWithFullData: IMotifDisponibilite = {
  id: 9459,
  code: 'brave groin groin afin que',
  libelle: 'effondrer débile aux alentours de',
};

export const sampleWithNewData: NewMotifDisponibilite = {
  code: 'condamner fourbe',
  libelle: 'à partir de',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
