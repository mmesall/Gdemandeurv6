import dayjs from 'dayjs/esm';
import { IFormationInitiale } from 'app/entities/formation-initiale/formation-initiale.model';
import { IFormationContinue } from 'app/entities/formation-continue/formation-continue.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { Resultat } from 'app/entities/enumerations/resultat.model';

export interface ICandidature {
  id?: number;
  offreFormation?: NomFiliere | null;
  dateDebutOffre?: dayjs.Dayjs | null;
  dateFinOffre?: dayjs.Dayjs | null;
  dateDepot?: dayjs.Dayjs | null;
  resultat?: Resultat | null;
  formationInitiale?: IFormationInitiale | null;
  formationContinue?: IFormationContinue | null;
}

export class Candidature implements ICandidature {
  constructor(
    public id?: number,
    public offreFormation?: NomFiliere | null,
    public dateDebutOffre?: dayjs.Dayjs | null,
    public dateFinOffre?: dayjs.Dayjs | null,
    public dateDepot?: dayjs.Dayjs | null,
    public resultat?: Resultat | null,
    public formationInitiale?: IFormationInitiale | null,
    public formationContinue?: IFormationContinue | null
  ) {}
}

export function getCandidatureIdentifier(candidature: ICandidature): number | undefined {
  return candidature.id;
}
