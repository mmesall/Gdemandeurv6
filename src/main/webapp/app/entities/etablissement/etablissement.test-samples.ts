import { NomEtablissement } from 'app/entities/enumerations/nom-etablissement.model';
import { NomRegion } from 'app/entities/enumerations/nom-region.model';
import { NomDepartement } from 'app/entities/enumerations/nom-departement.model';
import { TypeEtablissement } from 'app/entities/enumerations/type-etablissement.model';
import { StatutEtab } from 'app/entities/enumerations/statut-etab.model';
import { CFP } from 'app/entities/enumerations/cfp.model';
import { LYCEE } from 'app/entities/enumerations/lycee.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { NomSerie } from 'app/entities/enumerations/nom-serie.model';

import { IEtablissement, NewEtablissement } from './etablissement.model';

export const sampleWithRequiredData: IEtablissement = {
  id: 14491,
  region: 'KAOLACK',
  departement: 'DIOURBEL',
  statut: 'PUBLIC',
};

export const sampleWithPartialData: IEtablissement = {
  id: 17280,
  nomEtablissement: 'CFP_OUROSSOGUI',
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  region: 'MATAM',
  departement: 'NIORO',
  telephone: 30978,
  statut: 'PRIVE',
  cfp: 'CFP_SOKONE',
  lycee: 'LETFP_TAMBA',
  filiere: 'MECANIQUE',
  serie: 'STEG',
  autreFiliere: 'bronze des',
};

export const sampleWithFullData: IEtablissement = {
  id: 6283,
  nomEtablissement: 'CFPJ_YMCA',
  photo: '../fake-data/blob/hipster.png',
  photoContentType: 'unknown',
  region: 'KOLDA',
  departement: 'KOLDA',
  email: 'Lauriane18@yahoo.fr',
  telephone: 17317,
  typeEtablissement: 'CFP',
  statut: 'PRIVE',
  autreRegion: 'Femme plouf Femme',
  autreDepartement: 'que suédoise',
  cfp: 'CFP_SOKONE',
  lycee: 'LT_FATICK',
  filiere: 'ELEVAGE',
  serie: 'STIDD_M',
  autreFiliere: 'Nord Designer Vélocar',
  autreSerie: 'Ouest',
  autreNomEtablissement: 'paiement',
};

export const sampleWithNewData: NewEtablissement = {
  region: 'TAMBACOUNDA',
  departement: 'MATAM',
  statut: 'PUBLIC',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
