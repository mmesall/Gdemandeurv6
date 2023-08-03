import dayjs from 'dayjs/esm';

import { NomEtablissement } from 'app/entities/enumerations/nom-etablissement.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';

import { IConcours, NewConcours } from './concours.model';

export const sampleWithRequiredData: IConcours = {
  id: 30563,
};

export const sampleWithPartialData: IConcours = {
  id: 699,
  nomConcours: 'de Berlines jusque',
  nomEtablissement: 'CFP_BAKEL2',
  niveauEtude: 'BT1',
  dateCloture: dayjs('2023-03-27'),
};

export const sampleWithFullData: IConcours = {
  id: 13415,
  nomConcours: 'paiement redouter Films',
  nomEtablissement: 'CFP_SEDHIOU',
  niveauEtude: 'BT',
  dateOuverture: dayjs('2023-03-28'),
  dateCloture: dayjs('2023-03-27'),
  dateConcours: dayjs('2023-03-28'),
  affiche: '../fake-data/blob/hipster.png',
  afficheContentType: 'unknown',
};

export const sampleWithNewData: NewConcours = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
