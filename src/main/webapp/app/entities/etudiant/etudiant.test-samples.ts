import dayjs from 'dayjs/esm';

import { Sexe } from 'app/entities/enumerations/sexe.model';
import { NomRegion } from 'app/entities/enumerations/nom-region.model';
import { NomDepartement } from 'app/entities/enumerations/nom-departement.model';

import { IEtudiant, NewEtudiant } from './etudiant.model';

export const sampleWithRequiredData: IEtudiant = {
  id: 6421,
  carteEtudiant: 'Soul insuffisamment Est',
  nom: 'Essence',
  prenom: 'cuivre Technicien',
  cni: 30047,
};

export const sampleWithPartialData: IEtudiant = {
  id: 28920,
  carteEtudiant: 'Est Pratique Fait',
  nom: 'est hi in',
  prenom: 'Artisanal Frites serviable',
  lieuNaiss: 'Superviseur',
  departResidence: 'SEDHIOU',
  cni: 29735,
};

export const sampleWithFullData: IEtudiant = {
  id: 26632,
  carteEtudiant: 'Électrique Variété',
  nom: 'Homme tourterelle',
  prenom: 'employer lunaire Jeux',
  dateNaiss: dayjs('2023-03-27'),
  lieuNaiss: 'Vélo Femme',
  sexe: 'HOMME',
  telephone: 10944,
  adressePhysique: 'Films',
  regionResidence: 'KOLDA',
  departResidence: 'MBOUR',
  email: 'Sebastien33@hotmail.fr',
  cni: 18104,
};

export const sampleWithNewData: NewEtudiant = {
  carteEtudiant: 'Ordinateurs',
  nom: 'Béton Dahomey',
  prenom: 'Musique Est Raffiné',
  cni: 29999,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
