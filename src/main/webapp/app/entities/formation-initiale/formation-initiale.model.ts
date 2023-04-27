import dayjs from 'dayjs/esm';
import { IFormation } from 'app/entities/formation/formation.model';
import { Admission } from 'app/entities/enumerations/admission.model';
import { DiplomeRequis } from 'app/entities/enumerations/diplome-requis.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { NomSerie } from 'app/entities/enumerations/nom-serie.model';
import { CFP } from 'app/entities/enumerations/cfp.model';
import { LYCEE } from 'app/entities/enumerations/lycee.model';
import { DiplomeObtenu } from 'app/entities/enumerations/diplome-obtenu.model';

export interface IFormationInitiale {
  id?: number;
  nomFormationI?: string | null;
  duree?: string | null;
  admission?: Admission | null;
  diplomeRequis?: DiplomeRequis | null;
  niveauEtude?: NiveauEtude | null;
  ficheFormationContentType?: string | null;
  ficheFormation?: string | null;
  filiere?: NomFiliere | null;
  serie?: NomSerie | null;
  cfp?: CFP | null;
  lycee?: LYCEE | null;
  nomConcours?: string | null;
  dateOuverture?: dayjs.Dayjs | null;
  dateCloture?: dayjs.Dayjs | null;
  dateConcours?: dayjs.Dayjs | null;
  nomDiplome?: DiplomeObtenu | null;
  nomDebouche?: string | null;
  formation?: IFormation | null;
}

export class FormationInitiale implements IFormationInitiale {
  constructor(
    public id?: number,
    public nomFormationI?: string | null,
    public duree?: string | null,
    public admission?: Admission | null,
    public diplomeRequis?: DiplomeRequis | null,
    public niveauEtude?: NiveauEtude | null,
    public ficheFormationContentType?: string | null,
    public ficheFormation?: string | null,
    public filiere?: NomFiliere | null,
    public serie?: NomSerie | null,
    public cfp?: CFP | null,
    public lycee?: LYCEE | null,
    public nomConcours?: string | null,
    public dateOuverture?: dayjs.Dayjs | null,
    public dateCloture?: dayjs.Dayjs | null,
    public dateConcours?: dayjs.Dayjs | null,
    public nomDiplome?: DiplomeObtenu | null,
    public nomDebouche?: string | null,
    public formation?: IFormation | null
  ) {}
}

export function getFormationInitialeIdentifier(formationInitiale: IFormationInitiale): number | undefined {
  return formationInitiale.id;
}
