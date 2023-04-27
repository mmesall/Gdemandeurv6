import { IFormation } from 'app/entities/formation/formation.model';
import { IBailleur } from 'app/entities/bailleur/bailleur.model';

export interface IPriseEnCharge {
  id?: number;
  libelle?: string | null;
  montantPC?: number | null;
  formation?: IFormation | null;
  bailleur?: IBailleur | null;
}

export class PriseEnCharge implements IPriseEnCharge {
  constructor(
    public id?: number,
    public libelle?: string | null,
    public montantPC?: number | null,
    public formation?: IFormation | null,
    public bailleur?: IBailleur | null
  ) {}
}

export function getPriseEnChargeIdentifier(priseEnCharge: IPriseEnCharge): number | undefined {
  return priseEnCharge.id;
}
