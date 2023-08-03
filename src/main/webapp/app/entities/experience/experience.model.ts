import dayjs from 'dayjs/esm';
import { IEleve } from 'app/entities/eleve/eleve.model';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { IProfessionnel } from 'app/entities/professionnel/professionnel.model';
import { IDemandeur } from 'app/entities/demandeur/demandeur.model';

export interface IExperience {
  id: number;
  dateDebut?: dayjs.Dayjs | null;
  dateFin?: dayjs.Dayjs | null;
  nomEntreprise?: string | null;
  posteOccupe?: string | null;
  mission?: string | null;
  eleve?: Pick<IEleve, 'id' | 'niveauEtude'> | null;
  etudiant?: Pick<IEtudiant, 'id' | 'carteEtudiant'> | null;
  professionnel?: Pick<IProfessionnel, 'id' | 'profession'> | null;
  demandeur?: Pick<IDemandeur, 'id' | 'profil'> | null;
}

export type NewExperience = Omit<IExperience, 'id'> & { id: null };
