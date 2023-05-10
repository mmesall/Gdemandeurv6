import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'etablissement',
        data: { pageTitle: 'Etablissements' },
        loadChildren: () => import('./etablissement/etablissement.module').then(m => m.EtablissementModule),
      },
      {
        path: 'formation',
        data: { pageTitle: 'Formations' },
        loadChildren: () => import('./formation/formation.module').then(m => m.FormationModule),
      },
      {
        path: 'formation-initiale',
        data: { pageTitle: 'FormationInitiales' },
        loadChildren: () => import('./formation-initiale/formation-initiale.module').then(m => m.FormationInitialeModule),
      },
      {
        path: 'formation-continue',
        data: { pageTitle: 'FormationContinues' },
        loadChildren: () => import('./formation-continue/formation-continue.module').then(m => m.FormationContinueModule),
      },
      {
        path: 'concours',
        data: { pageTitle: 'Concours' },
        loadChildren: () => import('./concours/concours.module').then(m => m.ConcoursModule),
      },
      {
        path: 'prise-en-charge',
        data: { pageTitle: 'PriseEnCharges' },
        loadChildren: () => import('./prise-en-charge/prise-en-charge.module').then(m => m.PriseEnChargeModule),
      },
      {
        path: 'bailleur',
        data: { pageTitle: 'Bailleurs' },
        loadChildren: () => import('./bailleur/bailleur.module').then(m => m.BailleurModule),
      },
      {
        path: 'dossier',
        data: { pageTitle: 'Dossiers' },
        loadChildren: () => import('./dossier/dossier.module').then(m => m.DossierModule),
      },
      {
        path: 'diplome',
        data: { pageTitle: 'Diplomes' },
        loadChildren: () => import('./diplome/diplome.module').then(m => m.DiplomeModule),
      },
      {
        path: 'experience',
        data: { pageTitle: 'Experiences' },
        loadChildren: () => import('./experience/experience.module').then(m => m.ExperienceModule),
      },
      {
        path: 'candidature',
        data: { pageTitle: 'Candidatures' },
        loadChildren: () => import('./candidature/candidature.module').then(m => m.CandidatureModule),
      },
      {
        path: 'demandeur',
        data: { pageTitle: 'Demandeurs' },
        loadChildren: () => import('./demandeur/demandeur.module').then(m => m.DemandeurModule),
      },
      {
        path: 'eleve',
        data: { pageTitle: 'Eleves' },
        loadChildren: () => import('./eleve/eleve.module').then(m => m.EleveModule),
      },
      {
        path: 'etudiant',
        data: { pageTitle: 'Etudiants' },
        loadChildren: () => import('./etudiant/etudiant.module').then(m => m.EtudiantModule),
      },
      {
        path: 'professionnel',
        data: { pageTitle: 'Professionnels' },
        loadChildren: () => import('./professionnel/professionnel.module').then(m => m.ProfessionnelModule),
      },
      {
        path: 'agent',
        data: { pageTitle: 'Agents' },
        loadChildren: () => import('./agent/agent.module').then(m => m.AgentModule),
      },
      {
        path: 'service-mfpai',
        data: { pageTitle: 'ServiceMFPAIS' },
        loadChildren: () => import('./service-mfpai/service-mfpai.module').then(m => m.ServiceMFPAIModule),
      },
      {
        path: 'candidature-elev',
        data: { pageTitle: 'CandidatureElevs' },
        loadChildren: () => import('./candidature-elev/candidature-elev.module').then(m => m.CandidatureElevModule),
      },
      {
        path: 'candidature-etudiant',
        data: { pageTitle: 'CandidatureEtudiants' },
        loadChildren: () => import('./candidature-etudiant/candidature-etudiant.module').then(m => m.CandidatureEtudiantModule),
      },
      {
        path: 'candidature-prof',
        data: { pageTitle: 'CandidatureProfs' },
        loadChildren: () => import('./candidature-prof/candidature-prof.module').then(m => m.CandidatureProfModule),
      },
      {
        path: 'candidature-e',
        data: { pageTitle: 'CandidatureES' },
        loadChildren: () => import('./candidature-e/candidature-e.module').then(m => m.CandidatureEModule),
      },
      {
        path: 'candidature-p',
        data: { pageTitle: 'CandidaturePS' },
        loadChildren: () => import('./candidature-p/candidature-p.module').then(m => m.CandidaturePModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
