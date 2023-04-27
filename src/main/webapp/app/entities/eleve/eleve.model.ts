import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IDiplome } from 'app/entities/diplome/diplome.model';
import { IExperience } from 'app/entities/experience/experience.model';
import { ICandidatureElev } from 'app/entities/candidature-elev/candidature-elev.model';
import { IDossier } from 'app/entities/dossier/dossier.model';
import { IDemandeur } from 'app/entities/demandeur/demandeur.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { NomRegion } from 'app/entities/enumerations/nom-region.model';
import { NomDepartement } from 'app/entities/enumerations/nom-departement.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';

export interface IEleve {
  id?: number;
  nom?: string;
  prenom?: string;
  dateNaiss?: dayjs.Dayjs | null;
  lieuNaiss?: string | null;
  sexe?: Sexe | null;
  telephone?: number | null;
  adressePhysique?: string | null;
  regionResidence?: NomRegion | null;
  departResidence?: NomDepartement | null;
  niveauEtude?: NiveauEtude;
  cni?: number | null;
  user?: IUser | null;
  diplomes?: IDiplome[] | null;
  experiences?: IExperience[] | null;
  candidatureElevs?: ICandidatureElev[] | null;
  dossier?: IDossier | null;
  demandeur?: IDemandeur | null;
}

export class Eleve implements IEleve {
  constructor(
    public id?: number,
    public nom?: string,
    public prenom?: string,
    public dateNaiss?: dayjs.Dayjs | null,
    public lieuNaiss?: string | null,
    public sexe?: Sexe | null,
    public telephone?: number | null,
    public adressePhysique?: string | null,
    public regionResidence?: NomRegion | null,
    public departResidence?: NomDepartement | null,
    public niveauEtude?: NiveauEtude,
    public cni?: number | null,
    public user?: IUser | null,
    public diplomes?: IDiplome[] | null,
    public experiences?: IExperience[] | null,
    public candidatureElevs?: ICandidatureElev[] | null,
    public dossier?: IDossier | null,
    public demandeur?: IDemandeur | null
  ) {}
}

export function getEleveIdentifier(eleve: IEleve): number | undefined {
  return eleve.id;
}
