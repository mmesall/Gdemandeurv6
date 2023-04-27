import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AgentService } from '../service/agent.service';
import { IAgent, Agent } from '../agent.model';

import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IServiceMFPAI } from 'app/entities/service-mfpai/service-mfpai.model';
import { ServiceMFPAIService } from 'app/entities/service-mfpai/service/service-mfpai.service';

import { AgentUpdateComponent } from './agent-update.component';

describe('Agent Management Update Component', () => {
  let comp: AgentUpdateComponent;
  let fixture: ComponentFixture<AgentUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let agentService: AgentService;
  let userService: UserService;
  let serviceMFPAIService: ServiceMFPAIService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AgentUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AgentUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AgentUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    agentService = TestBed.inject(AgentService);
    userService = TestBed.inject(UserService);
    serviceMFPAIService = TestBed.inject(ServiceMFPAIService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call User query and add missing value', () => {
      const agent: IAgent = { id: 456 };
      const user: IUser = { id: 63018 };
      agent.user = user;

      const userCollection: IUser[] = [{ id: 8920 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ agent });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(userCollection, ...additionalUsers);
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should call serviceMFPAI query and add missing value', () => {
      const agent: IAgent = { id: 456 };
      const serviceMFPAI: IServiceMFPAI = { id: 66173 };
      agent.serviceMFPAI = serviceMFPAI;

      const serviceMFPAICollection: IServiceMFPAI[] = [{ id: 41380 }];
      jest.spyOn(serviceMFPAIService, 'query').mockReturnValue(of(new HttpResponse({ body: serviceMFPAICollection })));
      const expectedCollection: IServiceMFPAI[] = [serviceMFPAI, ...serviceMFPAICollection];
      jest.spyOn(serviceMFPAIService, 'addServiceMFPAIToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ agent });
      comp.ngOnInit();

      expect(serviceMFPAIService.query).toHaveBeenCalled();
      expect(serviceMFPAIService.addServiceMFPAIToCollectionIfMissing).toHaveBeenCalledWith(serviceMFPAICollection, serviceMFPAI);
      expect(comp.serviceMFPAISCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const agent: IAgent = { id: 456 };
      const user: IUser = { id: 92112 };
      agent.user = user;
      const serviceMFPAI: IServiceMFPAI = { id: 92961 };
      agent.serviceMFPAI = serviceMFPAI;

      activatedRoute.data = of({ agent });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(agent));
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.serviceMFPAISCollection).toContain(serviceMFPAI);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Agent>>();
      const agent = { id: 123 };
      jest.spyOn(agentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agent }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(agentService.update).toHaveBeenCalledWith(agent);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Agent>>();
      const agent = new Agent();
      jest.spyOn(agentService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: agent }));
      saveSubject.complete();

      // THEN
      expect(agentService.create).toHaveBeenCalledWith(agent);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Agent>>();
      const agent = { id: 123 };
      jest.spyOn(agentService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ agent });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(agentService.update).toHaveBeenCalledWith(agent);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackUserById', () => {
      it('Should return tracked User primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackUserById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });

    describe('trackServiceMFPAIById', () => {
      it('Should return tracked ServiceMFPAI primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackServiceMFPAIById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
