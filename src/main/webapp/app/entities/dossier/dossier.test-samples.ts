import dayjs from 'dayjs/esm';

import { NomRegion } from 'app/entities/enumerations/nom-region.model';
import { NomDepartement } from 'app/entities/enumerations/nom-departement.model';
import { TypePiece } from 'app/entities/enumerations/type-piece.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { NIVEAUCOMP } from 'app/entities/enumerations/niveaucomp.model';

import { IDossier, NewDossier } from './dossier.model';

export const sampleWithRequiredData: IDossier = {
  id: 12509,
  prenom: 'Country Berlines mature',
  nom: 'Country Femme Ouest',
  posteOccupe: 'Dollar Developpeur',
  dateDebut: dayjs('2023-03-28'),
  dateFin: dayjs('2023-03-27'),
  nomEntreprise: 'recta brûlé lors',
};

export const sampleWithPartialData: IDossier = {
  id: 27980,
  numDossier: 'Voiture',
  prenom: 'Femme Gants circulaire',
  nom: 'cœruleum',
  dateNaiss: dayjs('2023-03-27'),
  lieuNaiss: 'Technicien Folk',
  departementNaiss: 'ZICHUINCHOR',
  numeroPiece: 18446,
  sexe: 'FEMME',
  regionResidence: 'AUTRE',
  adresseResidence: 'Ouest',
  telephone2: 'que',
  email: 'Honore.Clement@hotmail.fr',
  intituleDiplome: 'Architecte Berlines accusantium',
  cv: '../fake-data/blob/hipster.png',
  cvContentType: 'unknown',
  autreSpecialite: 'Latine cacao Femme',
  niveauCompetence: 'DEBUTANT',
  intituleExperience: 'Poulet virer',
  posteOccupe: 'Essence avocat',
  dateDebut: dayjs('2023-03-27'),
  dateFin: dayjs('2023-03-28'),
  nomEntreprise: 'excepturi Administrateur',
  mission: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IDossier = {
  id: 25704,
  numDossier: 'cramoisi Essence',
  prenom: 'vérité nobis',
  nom: 'Sud voir',
  nomUtilisateur: 'foule Chèque retrait',
  dateNaiss: dayjs('2023-03-27'),
  lieuNaiss: 'Métal Sud magenta',
  regionNaiss: 'AUTRE',
  departementNaiss: 'THIES',
  typePiece: 'PASSEPORT',
  numeroPiece: 29499,
  sexe: 'FEMME',
  regionResidence: 'THIES',
  depResidence: 'GUEDIAWAYE',
  adresseResidence: 'Limousin',
  telephone1: 'Homme tandis facture',
  telephone2: 'Ethereum',
  email: 'Philippe_Legall92@yahoo.fr',
  niveauFormation: 'LICEMCE3',
  specialite: 'INFORMATIQUE_ET_NUMERIQUE',
  intituleDiplome: 'Analyste',
  diplome: '../fake-data/blob/hipster.png',
  diplomeContentType: 'unknown',
  anneeObtention: dayjs('2023-03-28'),
  lieuObtention: 'Est hirsute dépôt',
  cv: '../fake-data/blob/hipster.png',
  cvContentType: 'unknown',
  lettreMotivation: '../fake-data/blob/hipster.png',
  lettreMotivationContentType: 'unknown',
  profession: 'Jeux crac',
  autreSpecialite: 'de trop',
  nomCompetence: 'prononcer',
  niveauCompetence: 'DEBUTANT',
  intituleExperience: 'pendant Leu',
  posteOccupe: 'Essence',
  dateDebut: dayjs('2023-03-28'),
  dateFin: dayjs('2023-03-27'),
  nomEntreprise: 'Ingenieur que',
  mission: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewDossier = {
  prenom: 'Poulet',
  nom: 'Artisanal Caoutchouc',
  posteOccupe: 'immobilier blanditiis',
  dateDebut: dayjs('2023-03-27'),
  dateFin: dayjs('2023-03-27'),
  nomEntreprise: 'Auvergne',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
