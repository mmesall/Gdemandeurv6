import dayjs from 'dayjs/esm';
import { IProfessionnel } from 'app/entities/professionnel/professionnel.model';
import { IFormationContinue } from 'app/entities/formation-continue/formation-continue.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { Resultat } from 'app/entities/enumerations/resultat.model';

export interface ICandidatureP {
  id?: number;
  offreFormation?: NomFiliere | null;
  dateDebutOffre?: dayjs.Dayjs | null;
  dateFinOffre?: dayjs.Dayjs | null;
  dateDepot?: dayjs.Dayjs | null;
  resultat?: Resultat | null;
  professionnel?: IProfessionnel | null;
  formationContinue?: IFormationContinue | null;
  etablissement?: IEtablissement | null;
}

export class CandidatureP implements ICandidatureP {
  constructor(
    public id?: number,
    public offreFormation?: NomFiliere | null,
    public dateDebutOffre?: dayjs.Dayjs | null,
    public dateFinOffre?: dayjs.Dayjs | null,
    public dateDepot?: dayjs.Dayjs | null,
    public resultat?: Resultat | null,
    public professionnel?: IProfessionnel | null,
    public formationContinue?: IFormationContinue | null,
    public etablissement?: IEtablissement | null
  ) {}
}

export function getCandidaturePIdentifier(candidatureP: ICandidatureP): number | undefined {
  return candidatureP.id;
}
