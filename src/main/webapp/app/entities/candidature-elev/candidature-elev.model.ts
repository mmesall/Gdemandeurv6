import dayjs from 'dayjs/esm';
import { IEleve } from 'app/entities/eleve/eleve.model';
import { IFormationInitiale } from 'app/entities/formation-initiale/formation-initiale.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { Resultat } from 'app/entities/enumerations/resultat.model';

export interface ICandidatureElev {
  id?: number;
  offreFormation?: NomFiliere | null;
  dateDebutOffre?: dayjs.Dayjs | null;
  dateFinOffre?: dayjs.Dayjs | null;
  dateDepot?: dayjs.Dayjs | null;
  resultat?: Resultat | null;
  eleve?: IEleve | null;
  formationInitiale?: IFormationInitiale | null;
}

export class CandidatureElev implements ICandidatureElev {
  constructor(
    public id?: number,
    public offreFormation?: NomFiliere | null,
    public dateDebutOffre?: dayjs.Dayjs | null,
    public dateFinOffre?: dayjs.Dayjs | null,
    public dateDepot?: dayjs.Dayjs | null,
    public resultat?: Resultat | null,
    public eleve?: IEleve | null,
    public formationInitiale?: IFormationInitiale | null
  ) {}
}

export function getCandidatureElevIdentifier(candidatureElev: ICandidatureElev): number | undefined {
  return candidatureElev.id;
}
