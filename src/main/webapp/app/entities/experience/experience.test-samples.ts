import dayjs from 'dayjs/esm';

import { IExperience, NewExperience } from './experience.model';

export const sampleWithRequiredData: IExperience = {
  id: 7546,
  dateDebut: dayjs('2023-03-27'),
  dateFin: dayjs('2023-03-28'),
  nomEntreprise: 'excuser Gocycle bisque',
  posteOccupe: 'Berlines dépôt',
};

export const sampleWithPartialData: IExperience = {
  id: 28106,
  dateDebut: dayjs('2023-03-27'),
  dateFin: dayjs('2023-03-27'),
  nomEntreprise: 'membre mal sapiente',
  posteOccupe: 'Berlines Recyclé collègue',
  mission: '../fake-data/blob/hipster.txt',
};

export const sampleWithFullData: IExperience = {
  id: 28952,
  dateDebut: dayjs('2023-03-28'),
  dateFin: dayjs('2023-03-28'),
  nomEntreprise: 'Triplette songer géométrique',
  posteOccupe: 'smack',
  mission: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewExperience = {
  dateDebut: dayjs('2023-03-27'),
  dateFin: dayjs('2023-03-27'),
  nomEntreprise: 'Country',
  posteOccupe: 'Architecte Fait Caoutchouc',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
