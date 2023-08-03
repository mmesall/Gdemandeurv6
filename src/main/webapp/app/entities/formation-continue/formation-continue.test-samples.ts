import { Admission } from 'app/entities/enumerations/admission.model';
import { DiplomeRequis } from 'app/entities/enumerations/diplome-requis.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { NomSerie } from 'app/entities/enumerations/nom-serie.model';
import { CFP } from 'app/entities/enumerations/cfp.model';
import { LYCEE } from 'app/entities/enumerations/lycee.model';
import { DiplomeObtenu } from 'app/entities/enumerations/diplome-obtenu.model';

import { IFormationContinue, NewFormationContinue } from './formation-continue.model';

export const sampleWithRequiredData: IFormationContinue = {
  id: 21903,
};

export const sampleWithPartialData: IFormationContinue = {
  id: 11900,
  nomFormationC: 'gestionnaire Homme magenta',
  lycee: 'LTAP_SAINT_LOUIS',
  coutFormation: 22251,
  detailPC: '../fake-data/blob/hipster.txt',
  nomDiplome: 'AUTRES',
  autreDiplome: 'Homme',
};

export const sampleWithFullData: IFormationContinue = {
  id: 24644,
  nomFormationC: 'Doux puisque',
  duree: 'facture',
  admission: 'PC',
  diplomeRequis: 'ATTESTATION',
  niveauEtude: 'BTI',
  filiere: 'PECHE',
  serie: 'STIDD_M',
  cfp: 'CFP_MATAM',
  lycee: 'LTSLL_DAKAR',
  ficheFormation: '../fake-data/blob/hipster.png',
  ficheFormationContentType: 'unknown',
  libellePC: 'Nord vert',
  montantPriseEnCharge: 18627,
  coutFormation: 8185,
  detailPC: '../fake-data/blob/hipster.txt',
  nomDiplome: 'CQP',
  autreDiplome: 'pourpre bof Mauritanie',
  nomDebouche: 'blanditiis',
};

export const sampleWithNewData: NewFormationContinue = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
