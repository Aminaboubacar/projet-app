import { IDegre, NewDegre } from './degre.model';

export const sampleWithRequiredData: IDegre = {
  id: 11725,
  code: 'miam',
  libelle: 'depuis',
};

export const sampleWithPartialData: IDegre = {
  id: 7396,
  code: 'membre à vie lorsque',
  libelle: 'sus',
};

export const sampleWithFullData: IDegre = {
  id: 30694,
  code: 'tsoin-tsoin lors de',
  libelle: 'demain trop trop',
};

export const sampleWithNewData: NewDegre = {
  code: 'rectorat énorme dans la mesure où',
  libelle: 'monter',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
