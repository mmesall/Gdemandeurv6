import { IEleve } from 'app/entities/eleve/eleve.model';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { IProfessionnel } from 'app/entities/professionnel/professionnel.model';
import { IDemandeur } from 'app/entities/demandeur/demandeur.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';
import { Mention } from 'app/entities/enumerations/mention.model';

export interface IDiplome {
  id?: number;
  intitule?: string | null;
  domaine?: NomFiliere;
  niveau?: NiveauEtude | null;
  mention?: Mention | null;
  anneeObtention?: string | null;
  etablissement?: string | null;
  documentContentType?: string;
  document?: string;
  eleve?: IEleve | null;
  etudiant?: IEtudiant | null;
  professionnel?: IProfessionnel | null;
  demandeur?: IDemandeur | null;
}

export class Diplome implements IDiplome {
  constructor(
    public id?: number,
    public intitule?: string | null,
    public domaine?: NomFiliere,
    public niveau?: NiveauEtude | null,
    public mention?: Mention | null,
    public anneeObtention?: string | null,
    public etablissement?: string | null,
    public documentContentType?: string,
    public document?: string,
    public eleve?: IEleve | null,
    public etudiant?: IEtudiant | null,
    public professionnel?: IProfessionnel | null,
    public demandeur?: IDemandeur | null
  ) {}
}

export function getDiplomeIdentifier(diplome: IDiplome): number | undefined {
  return diplome.id;
}
