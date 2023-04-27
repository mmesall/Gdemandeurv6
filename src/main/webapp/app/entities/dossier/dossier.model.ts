import dayjs from 'dayjs/esm';
import { IEleve } from 'app/entities/eleve/eleve.model';
import { IEtudiant } from 'app/entities/etudiant/etudiant.model';
import { IProfessionnel } from 'app/entities/professionnel/professionnel.model';
import { IDemandeur } from 'app/entities/demandeur/demandeur.model';
import { NomRegion } from 'app/entities/enumerations/nom-region.model';
import { NomDepartement } from 'app/entities/enumerations/nom-departement.model';
import { TypePiece } from 'app/entities/enumerations/type-piece.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';
import { NomFiliere } from 'app/entities/enumerations/nom-filiere.model';
import { NomSerie } from 'app/entities/enumerations/nom-serie.model';
import { DiplomeRequis } from 'app/entities/enumerations/diplome-requis.model';

export interface IDossier {
  id?: number;
  numDossier?: string | null;
  dateNaiss?: dayjs.Dayjs | null;
  prenom?: string;
  nom?: string;
  nomUtilisateur?: string | null;
  regionNaiss?: NomRegion | null;
  departementNaiss?: NomDepartement | null;
  lieuNaiss?: string | null;
  typePiece?: TypePiece | null;
  numeroPiece?: number | null;
  sexe?: Sexe | null;
  regionResidence?: NomRegion | null;
  depResidence?: NomDepartement | null;
  adresseResidence?: string | null;
  telephone1?: string | null;
  telephone2?: string | null;
  email?: string | null;
  niveauFormation?: NiveauEtude | null;
  specialite1?: NomFiliere | null;
  specialite2?: NomSerie | null;
  diplomeRequis?: DiplomeRequis | null;
  cvContentType?: string | null;
  cv?: string | null;
  lettreMotivation?: string | null;
  profession?: string | null;
  eleve?: IEleve | null;
  etudiant?: IEtudiant | null;
  professionnel?: IProfessionnel | null;
  demandeur?: IDemandeur | null;
}

export class Dossier implements IDossier {
  constructor(
    public id?: number,
    public numDossier?: string | null,
    public dateNaiss?: dayjs.Dayjs | null,
    public prenom?: string,
    public nom?: string,
    public nomUtilisateur?: string | null,
    public regionNaiss?: NomRegion | null,
    public departementNaiss?: NomDepartement | null,
    public lieuNaiss?: string | null,
    public typePiece?: TypePiece | null,
    public numeroPiece?: number | null,
    public sexe?: Sexe | null,
    public regionResidence?: NomRegion | null,
    public depResidence?: NomDepartement | null,
    public adresseResidence?: string | null,
    public telephone1?: string | null,
    public telephone2?: string | null,
    public email?: string | null,
    public niveauFormation?: NiveauEtude | null,
    public specialite1?: NomFiliere | null,
    public specialite2?: NomSerie | null,
    public diplomeRequis?: DiplomeRequis | null,
    public cvContentType?: string | null,
    public cv?: string | null,
    public lettreMotivation?: string | null,
    public profession?: string | null,
    public eleve?: IEleve | null,
    public etudiant?: IEtudiant | null,
    public professionnel?: IProfessionnel | null,
    public demandeur?: IDemandeur | null
  ) {}
}

export function getDossierIdentifier(dossier: IDossier): number | undefined {
  return dossier.id;
}
