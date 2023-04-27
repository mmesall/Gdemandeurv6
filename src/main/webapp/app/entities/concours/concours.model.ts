import dayjs from 'dayjs/esm';
import { IFormation } from 'app/entities/formation/formation.model';
import { NomEtablissement } from 'app/entities/enumerations/nom-etablissement.model';
import { NiveauEtude } from 'app/entities/enumerations/niveau-etude.model';

export interface IConcours {
  id?: number;
  nomConcours?: string | null;
  nomEtablissement?: NomEtablissement | null;
  niveauEtude?: NiveauEtude | null;
  dateOuverture?: dayjs.Dayjs | null;
  dateCloture?: dayjs.Dayjs | null;
  dateConcours?: dayjs.Dayjs | null;
  afficheContentType?: string | null;
  affiche?: string | null;
  formation?: IFormation | null;
}

export class Concours implements IConcours {
  constructor(
    public id?: number,
    public nomConcours?: string | null,
    public nomEtablissement?: NomEtablissement | null,
    public niveauEtude?: NiveauEtude | null,
    public dateOuverture?: dayjs.Dayjs | null,
    public dateCloture?: dayjs.Dayjs | null,
    public dateConcours?: dayjs.Dayjs | null,
    public afficheContentType?: string | null,
    public affiche?: string | null,
    public formation?: IFormation | null
  ) {}
}

export function getConcoursIdentifier(concours: IConcours): number | undefined {
  return concours.id;
}
