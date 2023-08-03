import { IFormation } from 'app/entities/formation/formation.model';
import { IBailleur } from 'app/entities/bailleur/bailleur.model';

export interface IPriseEnCharge {
  id: number;
  libelle?: string | null;
  montantPC?: number | null;
  formation?: Pick<IFormation, 'id'> | null;
  bailleur?: Pick<IBailleur, 'id' | 'nomBailleur'> | null;
}

export type NewPriseEnCharge = Omit<IPriseEnCharge, 'id'> & { id: null };
