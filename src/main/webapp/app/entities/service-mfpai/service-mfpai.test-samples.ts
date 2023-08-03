import { IServiceMFPAI, NewServiceMFPAI } from './service-mfpai.model';

export const sampleWithRequiredData: IServiceMFPAI = {
  id: 10187,
  chefService: 'à',
};

export const sampleWithPartialData: IServiceMFPAI = {
  id: 3367,
  imageService: '../fake-data/blob/hipster.png',
  imageServiceContentType: 'unknown',
  chefService: 'café',
};

export const sampleWithFullData: IServiceMFPAI = {
  id: 26012,
  imageService: '../fake-data/blob/hipster.png',
  imageServiceContentType: 'unknown',
  nomService: 'que',
  chefService: 'Directeur sed',
  description: '../fake-data/blob/hipster.txt',
};

export const sampleWithNewData: NewServiceMFPAI = {
  chefService: 'corporis Ergonomique',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
