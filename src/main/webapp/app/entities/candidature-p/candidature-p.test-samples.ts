import dayjs from 'dayjs/esm';

import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { Resultat } from 'app/entities/enumerations/resultat.model';

import { ICandidatureP, NewCandidatureP } from './candidature-p.model';

export const sampleWithRequiredData: ICandidatureP = {
  id: 18469,
};

export const sampleWithPartialData: ICandidatureP = {
  id: 5811,
  dateDebutOffre: dayjs('2023-05-10'),
  dateDepot: dayjs('2023-05-09'),
  resultat: 'APPROUVE',
};

export const sampleWithFullData: ICandidatureP = {
  id: 16980,
  offreFormation: 'ELEVAGE',
  dateDebutOffre: dayjs('2023-05-09'),
  dateFinOffre: dayjs('2023-05-09'),
  dateDepot: dayjs('2023-05-10'),
  resultat: 'REJETE',
};

export const sampleWithNewData: NewCandidatureP = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
