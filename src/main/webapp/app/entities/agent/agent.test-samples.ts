import dayjs from 'dayjs/esm';

import { IAgent, NewAgent } from './agent.model';

export const sampleWithRequiredData: IAgent = {
  id: 5930,
  matricule: 'gagner sauf',
  nom: 'de sorte que',
  prenom: 'à travers broum toc',
};

export const sampleWithPartialData: IAgent = {
  id: 1809,
  matricule: 'fonder',
  nom: 'sombre aux environs de entre-temps',
  prenom: 'agiter ding derechef',
  lieuNaissance: 'parlementaire par suite de vu que',
  nationalite: 'ici guider',
  adresse: 'terne',
  causeDece: 'tsoin-tsoin',
};

export const sampleWithFullData: IAgent = {
  id: 17749,
  matricule: 'assurément',
  nom: 'probablement quasi',
  prenom: 'camarade',
  dateNaissance: dayjs('2024-03-19T18:52'),
  lieuNaissance: 'à condition que probablement',
  nationalite: 'au moyen de chut',
  telephone: '0501479407',
  adresse: 'de sorte que',
  dateDece: dayjs('2024-03-20T02:20'),
  causeDece: 'apparemment',
};

export const sampleWithNewData: NewAgent = {
  matricule: 'personnel céans main-d’œuvre',
  nom: 'demain après que',
  prenom: 'moins membre à vie recta',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
