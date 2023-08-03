import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'etablissement',
        data: { pageTitle: 'Etablissements' },
        loadChildren: () => import('./etablissement/etablissement.routes'),
      },
      {
        path: 'formation',
        data: { pageTitle: 'Formations' },
        loadChildren: () => import('./formation/formation.routes'),
      },
      {
        path: 'formation-initiale',
        data: { pageTitle: 'FormationInitiales' },
        loadChildren: () => import('./formation-initiale/formation-initiale.routes'),
      },
      {
        path: 'formation-continue',
        data: { pageTitle: 'FormationContinues' },
        loadChildren: () => import('./formation-continue/formation-continue.routes'),
      },
      {
        path: 'concours',
        data: { pageTitle: 'Concours' },
        loadChildren: () => import('./concours/concours.routes'),
      },
      {
        path: 'prise-en-charge',
        data: { pageTitle: 'PriseEnCharges' },
        loadChildren: () => import('./prise-en-charge/prise-en-charge.routes'),
      },
      {
        path: 'bailleur',
        data: { pageTitle: 'Bailleurs' },
        loadChildren: () => import('./bailleur/bailleur.routes'),
      },
      {
        path: 'dossier',
        data: { pageTitle: 'Dossiers' },
        loadChildren: () => import('./dossier/dossier.routes'),
      },
      {
        path: 'diplome',
        data: { pageTitle: 'Diplomes' },
        loadChildren: () => import('./diplome/diplome.routes'),
      },
      {
        path: 'experience',
        data: { pageTitle: 'Experiences' },
        loadChildren: () => import('./experience/experience.routes'),
      },
      {
        path: 'demandeur',
        data: { pageTitle: 'Demandeurs' },
        loadChildren: () => import('./demandeur/demandeur.routes'),
      },
      {
        path: 'eleve',
        data: { pageTitle: 'Eleves' },
        loadChildren: () => import('./eleve/eleve.routes'),
      },
      {
        path: 'etudiant',
        data: { pageTitle: 'Etudiants' },
        loadChildren: () => import('./etudiant/etudiant.routes'),
      },
      {
        path: 'professionnel',
        data: { pageTitle: 'Professionnels' },
        loadChildren: () => import('./professionnel/professionnel.routes'),
      },
      {
        path: 'agent',
        data: { pageTitle: 'Agents' },
        loadChildren: () => import('./agent/agent.routes'),
      },
      {
        path: 'service-mfpai',
        data: { pageTitle: 'ServiceMFPAIS' },
        loadChildren: () => import('./service-mfpai/service-mfpai.routes'),
      },
      {
        path: 'candidature-e',
        data: { pageTitle: 'CandidatureES' },
        loadChildren: () => import('./candidature-e/candidature-e.routes'),
      },
      {
        path: 'candidature-p',
        data: { pageTitle: 'CandidaturePS' },
        loadChildren: () => import('./candidature-p/candidature-p.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
