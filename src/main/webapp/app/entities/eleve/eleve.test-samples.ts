import dayjs from 'dayjs/esm';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { NomRegion } from 'app/entities/enumerations/nom-region.model';
import { NomDepartement } from 'app/entities/enumerations/nom-departement.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';

import { IEleve, NewEleve } from './eleve.model';

export const sampleWithRequiredData: IEleve = {
  id: 30367,
  nom: 'monospaces Kazakhstan',
  prenom: 'Sommerard',
  niveauEtude: 'LICEMCE3',
};

export const sampleWithPartialData: IEleve = {
  id: 7892,
  nom: 'Avenue Auvergne retrait',
  prenom: 'Marshall Chapeau',
  dateNaiss: dayjs('2023-03-27'),
  telephone: 17118,
  niveauEtude: 'BEP',
};

export const sampleWithFullData: IEleve = {
  id: 24114,
  nom: 'environs',
  prenom: 'cacao',
  dateNaiss: dayjs('2023-03-27'),
  lieuNaiss: 'secours Est',
  sexe: 'FEMME',
  telephone: 18555,
  adressePhysique: 'Femme Poulet',
  regionResidence: 'DIOURBEL',
  departResidence: 'GUINGUINEO',
  niveauEtude: 'LICEMCE3',
  cni: 25415,
};

export const sampleWithNewData: NewEleve = {
  nom: 'Fromage Automobile Hip',
  prenom: 'Raffiné dépôt au',
  niveauEtude: 'CQP',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
