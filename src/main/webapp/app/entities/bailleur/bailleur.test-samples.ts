import { IBailleur, NewBailleur } from './bailleur.model';

export const sampleWithRequiredData: IBailleur = {
  id: 13269,
  nomBailleur: 'Poulet Lepic',
};

export const sampleWithPartialData: IBailleur = {
  id: 21682,
  nomBailleur: 'paf paf Poulet',
  budgetPrevu: 9721,
  budgetRestant: 30286,
};

export const sampleWithFullData: IBailleur = {
  id: 5839,
  nomBailleur: 'paiement Granit',
  budgetPrevu: 15864,
  budgetDepense: 10305,
  budgetRestant: 40,
  nbrePC: 25506,
};

export const sampleWithNewData: NewBailleur = {
  nomBailleur: 'durant',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
