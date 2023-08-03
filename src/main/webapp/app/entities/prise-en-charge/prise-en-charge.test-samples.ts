import { IPriseEnCharge, NewPriseEnCharge } from './prise-en-charge.model';

export const sampleWithRequiredData: IPriseEnCharge = {
  id: 12674,
};

export const sampleWithPartialData: IPriseEnCharge = {
  id: 4497,
  libelle: 'là',
  montantPC: 20744,
};

export const sampleWithFullData: IPriseEnCharge = {
  id: 16686,
  libelle: 'que',
  montantPC: 2232,
};

export const sampleWithNewData: NewPriseEnCharge = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
