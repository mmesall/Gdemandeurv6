import dayjs from 'dayjs/esm';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { IFormationInitiale } from 'app/entities/formation-initiale/formation-initiale.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { Resultat } from 'app/entities/enumerations/resultat.model';

export interface ICandidatureEtudiant {
  id?: number;
  offreFormation?: NomFiliere | null;
  dateDebutOffre?: dayjs.Dayjs | null;
  dateFinOffre?: dayjs.Dayjs | null;
  dateDepot?: dayjs.Dayjs | null;
  resultat?: Resultat | null;
  etudiant?: IEtudiant | null;
  formationInitiale?: IFormationInitiale | null;
}

export class CandidatureEtudiant implements ICandidatureEtudiant {
  constructor(
    public id?: number,
    public offreFormation?: NomFiliere | null,
    public dateDebutOffre?: dayjs.Dayjs | null,
    public dateFinOffre?: dayjs.Dayjs | null,
    public dateDepot?: dayjs.Dayjs | null,
    public resultat?: Resultat | null,
    public etudiant?: IEtudiant | null,
    public formationInitiale?: IFormationInitiale | null
  ) {}
}

export function getCandidatureEtudiantIdentifier(candidatureEtudiant: ICandidatureEtudiant): number | undefined {
  return candidatureEtudiant.id;
}
