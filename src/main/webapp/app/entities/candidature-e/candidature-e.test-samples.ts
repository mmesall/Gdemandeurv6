import dayjs from 'dayjs/esm';

import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { Resultat } from 'app/entities/enumerations/resultat.model';

import { ICandidatureE, NewCandidatureE } from './candidature-e.model';

export const sampleWithRequiredData: ICandidatureE = {
  id: 1342,
};

export const sampleWithPartialData: ICandidatureE = {
  id: 17263,
  dateFinOffre: dayjs('2023-05-10'),
  dateDepot: dayjs('2023-05-09'),
  resultat: 'REJETE',
};

export const sampleWithFullData: ICandidatureE = {
  id: 1751,
  offreFormation: 'AGROALIMENTAIRE',
  dateDebutOffre: dayjs('2023-05-09'),
  dateFinOffre: dayjs('2023-05-10'),
  dateDepot: dayjs('2023-05-10'),
  resultat: 'APPROUVE',
};

export const sampleWithNewData: NewCandidatureE = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
