import { IFormation } from 'app/entities/formation/formation.model';
import { NomEtablissement } from 'app/entities/enumerations/nom-etablissement.model';
import { NomRegion } from 'app/entities/enumerations/nom-region.model';
import { NomDepartement } from 'app/entities/enumerations/nom-departement.model';
import { TypeEtablissement } from 'app/entities/enumerations/type-etablissement.model';
import { StatutEtab } from 'app/entities/enumerations/statut-etab.model';
import { CFP } from 'app/entities/enumerations/cfp.model';
import { LYCEE } from 'app/entities/enumerations/lycee.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { NomSerie } from 'app/entities/enumerations/nom-serie.model';

export interface IEtablissement {
  id?: number;
  nomEtablissement?: NomEtablissement | null;
  photoContentType?: string | null;
  photo?: string | null;
  region?: NomRegion;
  departement?: NomDepartement;
  email?: string | null;
  telephone?: number | null;
  typeEtablissement?: TypeEtablissement | null;
  statut?: StatutEtab;
  autreRegion?: string | null;
  autreDepartement?: string | null;
  cfp?: CFP | null;
  lycee?: LYCEE | null;
  filiere?: NomFiliere | null;
  serie?: NomSerie | null;
  autreFiliere?: string | null;
  autreSerie?: string | null;
  autreNomEtablissement?: string | null;
  formations?: IFormation[];
}

export class Etablissement implements IEtablissement {
  constructor(
    public id?: number,
    public nomEtablissement?: NomEtablissement | null,
    public photoContentType?: string | null,
    public photo?: string | null,
    public region?: NomRegion,
    public departement?: NomDepartement,
    public email?: string | null,
    public telephone?: number | null,
    public typeEtablissement?: TypeEtablissement | null,
    public statut?: StatutEtab,
    public autreRegion?: string | null,
    public autreDepartement?: string | null,
    public cfp?: CFP | null,
    public lycee?: LYCEE | null,
    public filiere?: NomFiliere | null,
    public serie?: NomSerie | null,
    public autreFiliere?: string | null,
    public autreSerie?: string | null,
    public autreNomEtablissement?: string | null,
    public formations?: IFormation[]
  ) {}
}

export function getEtablissementIdentifier(etablissement: IEtablissement): number | undefined {
  return etablissement.id;
}
