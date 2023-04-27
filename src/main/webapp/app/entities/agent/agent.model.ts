import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IServiceMFPAI } from 'app/entities/service-mfpai/service-mfpai.model';
import { Sexe } from 'app/entities/enumerations/sexe.model';

export interface IAgent {
  id?: number;
  matricule?: string;
  nom?: string | null;
  prenom?: string | null;
  dateNaiss?: dayjs.Dayjs | null;
  lieuNaiss?: string | null;
  sexe?: Sexe | null;
  telephone?: number | null;
  email?: string | null;
  user?: IUser | null;
  serviceMFPAI?: IServiceMFPAI | null;
}

export class Agent implements IAgent {
  constructor(
    public id?: number,
    public matricule?: string,
    public nom?: string | null,
    public prenom?: string | null,
    public dateNaiss?: dayjs.Dayjs | null,
    public lieuNaiss?: string | null,
    public sexe?: Sexe | null,
    public telephone?: number | null,
    public email?: string | null,
    public user?: IUser | null,
    public serviceMFPAI?: IServiceMFPAI | null
  ) {}
}

export function getAgentIdentifier(agent: IAgent): number | undefined {
  return agent.id;
}
