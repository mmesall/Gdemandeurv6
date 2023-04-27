import dayjs from 'dayjs/esm';
import { IEleve } from 'app/entities/eleve/eleve.model';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { IProfessionnel } from 'app/entities/professionnel/professionnel.model';
import { IDemandeur } from 'app/entities/demandeur/demandeur.model';

export interface IExperience {
  id?: number;
  dateDebut?: dayjs.Dayjs;
  dateFin?: dayjs.Dayjs;
  nomEntreprise?: string;
  posteOccupe?: string;
  mission?: string | null;
  eleve?: IEleve | null;
  etudiant?: IEtudiant | null;
  professionnel?: IProfessionnel | null;
  demandeur?: IDemandeur | null;
}

export class Experience implements IExperience {
  constructor(
    public id?: number,
    public dateDebut?: dayjs.Dayjs,
    public dateFin?: dayjs.Dayjs,
    public nomEntreprise?: string,
    public posteOccupe?: string,
    public mission?: string | null,
    public eleve?: IEleve | null,
    public etudiant?: IEtudiant | null,
    public professionnel?: IProfessionnel | null,
    public demandeur?: IDemandeur | null
  ) {}
}

export function getExperienceIdentifier(experience: IExperience): number | undefined {
  return experience.id;
}
