import dayjs from 'dayjs/esm';
import { IEleve } from 'app/entities/eleve/eleve.model';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { IFormationInitiale } from 'app/entities/formation-initiale/formation-initiale.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { Resultat } from 'app/entities/enumerations/resultat.model';

export interface ICandidatureE {
  id?: number;
  offreFormation?: NomFiliere | null;
  dateDebutOffre?: dayjs.Dayjs | null;
  dateFinOffre?: dayjs.Dayjs | null;
  dateDepot?: dayjs.Dayjs | null;
  resultat?: Resultat | null;
  eleve?: IEleve | null;
  etudiant?: IEtudiant | null;
  formationInitiale?: IFormationInitiale | null;
  etablissement?: IEtablissement | null;
}

export class CandidatureE implements ICandidatureE {
  constructor(
    public id?: number,
    public offreFormation?: NomFiliere | null,
    public dateDebutOffre?: dayjs.Dayjs | null,
    public dateFinOffre?: dayjs.Dayjs | null,
    public dateDepot?: dayjs.Dayjs | null,
    public resultat?: Resultat | null,
    public eleve?: IEleve | null,
    public etudiant?: IEtudiant | null,
    public formationInitiale?: IFormationInitiale | null,
    public etablissement?: IEtablissement | null
  ) {}
}

export function getCandidatureEIdentifier(candidatureE: ICandidatureE): number | undefined {
  return candidatureE.id;
}
