import { IDemandeur } from 'app/entities/demandeur/demandeur.model';
import { IEleve } from 'app/entities/eleve/eleve.model';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { IProfessionnel } from 'app/entities/professionnel/professionnel.model';
import { IEtablissement } from 'app/entities/etablissement/etablissement.model';
import { IPriseEnCharge } from 'app/entities/prise-en-charge/prise-en-charge.model';
import { TypeFormation } from 'app/entities/enumerations/type-formation.model';
import { Admission } from 'app/entities/enumerations/admission.model';
import { DiplomeRequis } from 'app/entities/enumerations/diplome-requis.model';

export interface IFormation {
  id?: number;
  nomFormation?: string | null;
  imageFormationContentType?: string | null;
  imageFormation?: string | null;
  typeFormation?: TypeFormation | null;
  duree?: string | null;
  admission?: Admission | null;
  diplomeRequis?: DiplomeRequis | null;
  ficheFormationContentType?: string | null;
  ficheFormation?: string | null;
  demandeurs?: IDemandeur[] | null;
  eleves?: IEleve[] | null;
  etudiants?: IEtudiant[] | null;
  professionnels?: IProfessionnel[] | null;
  etablissements?: IEtablissement[];
  priseEnCharge?: IPriseEnCharge | null;
}

export class Formation implements IFormation {
  constructor(
    public id?: number,
    public nomFormation?: string | null,
    public imageFormationContentType?: string | null,
    public imageFormation?: string | null,
    public typeFormation?: TypeFormation | null,
    public duree?: string | null,
    public admission?: Admission | null,
    public diplomeRequis?: DiplomeRequis | null,
    public ficheFormationContentType?: string | null,
    public ficheFormation?: string | null,
    public demandeurs?: IDemandeur[] | null,
    public eleves?: IEleve[] | null,
    public etudiants?: IEtudiant[] | null,
    public professionnels?: IProfessionnel[] | null,
    public etablissements?: IEtablissement[],
    public priseEnCharge?: IPriseEnCharge | null
  ) {}
}

export function getFormationIdentifier(formation: IFormation): number | undefined {
  return formation.id;
}
