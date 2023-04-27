import { IFormation } from 'app/entities/formation/formation.model';
import { Admission } from 'app/entities/enumerations/admission.model';
import { DiplomeRequis } from 'app/entities/enumerations/diplome-requis.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { NomSerie } from 'app/entities/enumerations/nom-serie.model';
import { CFP } from 'app/entities/enumerations/cfp.model';
import { LYCEE } from 'app/entities/enumerations/lycee.model';
import { DiplomeObtenu } from 'app/entities/enumerations/diplome-obtenu.model';

export interface IFormationContinue {
  id?: number;
  nomFormationC?: string | null;
  duree?: string | null;
  admission?: Admission | null;
  diplomeRequis?: DiplomeRequis | null;
  niveauEtude?: NiveauEtude | null;
  filiere?: NomFiliere | null;
  serie?: NomSerie | null;
  cfp?: CFP | null;
  lycee?: LYCEE | null;
  ficheFormationContentType?: string | null;
  ficheFormation?: string | null;
  libellePC?: string | null;
  montantPriseEnCharge?: number | null;
  coutFormation?: number | null;
  detailPC?: string | null;
  nomDiplome?: DiplomeObtenu | null;
  autreDiplome?: string | null;
  nomDebouche?: string | null;
  formation?: IFormation | null;
}

export class FormationContinue implements IFormationContinue {
  constructor(
    public id?: number,
    public nomFormationC?: string | null,
    public duree?: string | null,
    public admission?: Admission | null,
    public diplomeRequis?: DiplomeRequis | null,
    public niveauEtude?: NiveauEtude | null,
    public filiere?: NomFiliere | null,
    public serie?: NomSerie | null,
    public cfp?: CFP | null,
    public lycee?: LYCEE | null,
    public ficheFormationContentType?: string | null,
    public ficheFormation?: string | null,
    public libellePC?: string | null,
    public montantPriseEnCharge?: number | null,
    public coutFormation?: number | null,
    public detailPC?: string | null,
    public nomDiplome?: DiplomeObtenu | null,
    public autreDiplome?: string | null,
    public nomDebouche?: string | null,
    public formation?: IFormation | null
  ) {}
}

export function getFormationContinueIdentifier(formationContinue: IFormationContinue): number | undefined {
  return formationContinue.id;
}
