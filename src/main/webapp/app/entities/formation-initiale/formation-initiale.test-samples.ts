import dayjs from 'dayjs/esm';

import { Admission } from 'app/entities/enumerations/admission.model';
import { DiplomeRequis } from 'app/entities/enumerations/diplome-requis.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { NomSerie } from 'app/entities/enumerations/nom-serie.model';
import { CFP } from 'app/entities/enumerations/cfp.model';
import { LYCEE } from 'app/entities/enumerations/lycee.model';
import { DiplomeObtenu } from 'app/entities/enumerations/diplome-obtenu.model';

import { IFormationInitiale, NewFormationInitiale } from './formation-initiale.model';

export const sampleWithRequiredData: IFormationInitiale = {
  id: 21231,
};

export const sampleWithPartialData: IFormationInitiale = {
  id: 4102,
  duree: 'cuisine lankaise',
  niveauEtude: 'TERMINAL',
  ficheFormation: '../fake-data/blob/hipster.png',
  ficheFormationContentType: 'unknown',
  cfp: 'CFP_GOUDIRY',
  lycee: 'LTCAN_KAOLACK',
  nomConcours: 'Poulet',
  dateCloture: dayjs('2023-03-28'),
  dateConcours: dayjs('2023-03-28'),
  nomDiplome: 'BEP',
  nomDebouche: 'Homme',
};

export const sampleWithFullData: IFormationInitiale = {
  id: 18099,
  nomFormationI: 'Dollar suisse Manager',
  duree: 'Automobile',
  admission: 'PC',
  diplomeRequis: 'LICENCE',
  niveauEtude: 'BT3',
  ficheFormation: '../fake-data/blob/hipster.png',
  ficheFormationContentType: 'unknown',
  filiere: 'RESTAURATION_ET_HOTELLORIE',
  serie: 'S4',
  cfp: 'CFP_SEDHIOU',
  lycee: 'LTIM_KEDOUGOU',
  nomConcours: 'argent large mélanger',
  dateOuverture: dayjs('2023-03-27'),
  dateCloture: dayjs('2023-03-28'),
  dateConcours: dayjs('2023-03-27'),
  nomDiplome: 'CQP',
  nomDebouche: 'après Génial',
};

export const sampleWithNewData: NewFormationInitiale = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
