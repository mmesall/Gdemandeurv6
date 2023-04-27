import { IAgent } from 'app/entities/agent/agent.model';

export interface IServiceMFPAI {
  id?: number;
  imageServiceContentType?: string | null;
  imageService?: string | null;
  nomService?: string | null;
  chefService?: string;
  description?: string | null;
  agent?: IAgent | null;
}

export class ServiceMFPAI implements IServiceMFPAI {
  constructor(
    public id?: number,
    public imageServiceContentType?: string | null,
    public imageService?: string | null,
    public nomService?: string | null,
    public chefService?: string,
    public description?: string | null,
    public agent?: IAgent | null
  ) {}
}

export function getServiceMFPAIIdentifier(serviceMFPAI: IServiceMFPAI): number | undefined {
  return serviceMFPAI.id;
}
