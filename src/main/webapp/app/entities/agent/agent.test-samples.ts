import dayjs from 'dayjs/esm';

import { Sexe } from 'app/entities/enumerations/sexe.model';

import { IAgent, NewAgent } from './agent.model';

export const sampleWithRequiredData: IAgent = {
  id: 17350,
  matricule: 'Granit',
};

export const sampleWithPartialData: IAgent = {
  id: 9018,
  matricule: 'monétaire de mairie',
  lieuNaiss: 'Plastique neutre',
  sexe: 'HOMME',
  telephone: 13753,
};

export const sampleWithFullData: IAgent = {
  id: 2822,
  matricule: 'Maroc Bretagne',
  nomAgent: 'pschitt',
  prenom: 'Soul tourterelle',
  dateNaiss: dayjs('2023-03-28'),
  lieuNaiss: 'Prêt Epargne',
  sexe: 'HOMME',
  telephone: 19195,
  email: 'Anthelmette.Fontaine99@hotmail.fr',
};

export const sampleWithNewData: NewAgent = {
  matricule: 'Prêt prasin Tall',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
