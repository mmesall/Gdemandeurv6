import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';
import { Mention } from 'app/entities/enumerations/mention.model';

import { IDiplome, NewDiplome } from './diplome.model';

export const sampleWithRequiredData: IDiplome = {
  id: 1025,
  domaine: 'AGRI_ELEVAGE',
  document: '../fake-data/blob/hipster.png',
  documentContentType: 'unknown',
};

export const sampleWithPartialData: IDiplome = {
  id: 31064,
  intitule: 'Électrique minuscule',
  domaine: 'MECANIQUE',
  mention: 'TRES_BIEN',
  anneeObtention: 'canadien argent facture',
  etablissement: 'crever Berlines',
  document: '../fake-data/blob/hipster.png',
  documentContentType: 'unknown',
};

export const sampleWithFullData: IDiplome = {
  id: 7198,
  intitule: 'vert',
  domaine: 'ARTISANAT',
  niveau: 'BEP1',
  mention: 'BIEN',
  anneeObtention: 'Fatbike paiement',
  etablissement: 'Rock Enfants miaou',
  document: '../fake-data/blob/hipster.png',
  documentContentType: 'unknown',
};

export const sampleWithNewData: NewDiplome = {
  domaine: 'ENVIRONNEMENT',
  document: '../fake-data/blob/hipster.png',
  documentContentType: 'unknown',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
