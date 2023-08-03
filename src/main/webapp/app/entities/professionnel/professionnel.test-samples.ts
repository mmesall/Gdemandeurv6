import dayjs from 'dayjs/esm';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { NomRegion } from 'app/entities/enumerations/nom-region.model';
import { NomDepartement } from 'app/entities/enumerations/nom-departement.model';

import { IProfessionnel, NewProfessionnel } from './professionnel.model';

export const sampleWithRequiredData: IProfessionnel = {
  id: 18009,
  profession: 'Caoutchouc de',
  nom: 'Costa',
  prenom: 'sus commis',
  cni: 25998,
};

export const sampleWithPartialData: IProfessionnel = {
  id: 19183,
  profession: 'Femme Pratique de',
  nom: 'soufre',
  prenom: 'hé retrait',
  dateNaiss: dayjs('2023-03-28'),
  telephone: 3505,
  adressePhysique: 'Sol basané y',
  regionResidence: 'KEDOUGOU',
  cni: 24774,
};

export const sampleWithFullData: IProfessionnel = {
  id: 11635,
  profession: 'Analyste Quai',
  nom: 'Éthiopie Account',
  prenom: 'Nord lilas Rue',
  dateNaiss: dayjs('2023-03-27'),
  lieuNaiss: 'Acier Dalasi',
  sexe: 'FEMME',
  telephone: 31340,
  adressePhysique: 'juriste',
  regionResidence: 'KAFFRINE',
  departResidence: 'KAOLOACK',
  email: 'Laurene.Guillot26@gmail.com',
  cni: 30542,
};

export const sampleWithNewData: NewProfessionnel = {
  profession: 'secouriste',
  nom: 'Joubert par considérable',
  prenom: 'Specialiste',
  cni: 6302,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
