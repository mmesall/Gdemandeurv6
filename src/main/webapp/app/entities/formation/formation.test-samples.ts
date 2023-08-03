import { TypeFormation } from 'app/entities/enumerations/type-formation.model';
import { Admission } from 'app/entities/enumerations/admission.model';
import { DiplomeRequis } from 'app/entities/enumerations/diplome-requis.model';

import { IFormation, NewFormation } from './formation.model';

export const sampleWithRequiredData: IFormation = {
  id: 9806,
};

export const sampleWithPartialData: IFormation = {
  id: 14018,
  nomFormation: 'Congelé Peso',
  typeFormation: 'INITIALE',
  admission: 'CONCOURS',
  diplomeRequis: 'AUTRES',
  ficheFormation: '../fake-data/blob/hipster.png',
  ficheFormationContentType: 'unknown',
};

export const sampleWithFullData: IFormation = {
  id: 6686,
  nomFormation: 'Metal',
  imageFormation: '../fake-data/blob/hipster.png',
  imageFormationContentType: 'unknown',
  typeFormation: 'CONTINUE',
  duree: 'tâcher',
  admission: 'PC',
  diplomeRequis: 'MASTER',
  ficheFormation: '../fake-data/blob/hipster.png',
  ficheFormationContentType: 'unknown',
};

export const sampleWithNewData: NewFormation = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
