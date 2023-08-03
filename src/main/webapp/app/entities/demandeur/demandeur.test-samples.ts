import dayjs from 'dayjs/esm';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { Profil } from 'app/entities/enumerations/profil.model';

import { IDemandeur, NewDemandeur } from './demandeur.model';

export const sampleWithRequiredData: IDemandeur = {
  id: 24285,
};

export const sampleWithPartialData: IDemandeur = {
  id: 4387,
  lieuNaiss: 'badaboum Beauté',
  telephone: 1329,
};

export const sampleWithFullData: IDemandeur = {
  id: 12565,
  nom: 'Cruiser Brésil Designer',
  prenom: 'sale',
  dateNaiss: dayjs('2023-03-27'),
  lieuNaiss: 'groin',
  sexe: 'HOMME',
  telephone: 28209,
  email: 'Jerome_Vasseur@gmail.com',
  profil: 'ETUDIANT',
};

export const sampleWithNewData: NewDemandeur = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
