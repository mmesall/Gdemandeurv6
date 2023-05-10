import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IDiplome } from 'app/entities/diplome/diplome.model';
import { IExperience } from 'app/entities/experience/experience.model';
import { ICandidatureP } from 'app/entities/candidature-p/candidature-p.model';
import { IDossier } from 'app/entities/dossier/dossier.model';
import { IDemandeur } from 'app/entities/demandeur/demandeur.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { NomRegion } from 'app/entities/enumerations/nom-region.model';
import { NomDepartement } from 'app/entities/enumerations/nom-departement.model';

export interface IProfessionnel {
  id?: number;
  profession?: string;
  nom?: string;
  prenom?: string;
  dateNaiss?: dayjs.Dayjs | null;
  lieuNaiss?: string | null;
  sexe?: Sexe | null;
  telephone?: number | null;
  adressePhysique?: string | null;
  regionResidence?: NomRegion | null;
  departResidence?: NomDepartement | null;
  email?: string | null;
  cni?: number;
  user?: IUser | null;
  diplomes?: IDiplome[] | null;
  experiences?: IExperience[] | null;
  candidaturePS?: ICandidatureP[] | null;
  dossier?: IDossier | null;
  demandeur?: IDemandeur | null;
}

export class Professionnel implements IProfessionnel {
  constructor(
    public id?: number,
    public profession?: string,
    public nom?: string,
    public prenom?: string,
    public dateNaiss?: dayjs.Dayjs | null,
    public lieuNaiss?: string | null,
    public sexe?: Sexe | null,
    public telephone?: number | null,
    public adressePhysique?: string | null,
    public regionResidence?: NomRegion | null,
    public departResidence?: NomDepartement | null,
    public email?: string | null,
    public cni?: number,
    public user?: IUser | null,
    public diplomes?: IDiplome[] | null,
    public experiences?: IExperience[] | null,
    public candidaturePS?: ICandidatureP[] | null,
    public dossier?: IDossier | null,
    public demandeur?: IDemandeur | null
  ) {}
}

export function getProfessionnelIdentifier(professionnel: IProfessionnel): number | undefined {
  return professionnel.id;
}
