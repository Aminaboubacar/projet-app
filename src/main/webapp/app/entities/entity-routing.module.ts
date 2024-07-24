import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'agent',
        data: { pageTitle: 'Agents' },
        loadChildren: () => import('./agent/agent.routes'),
      },
      {
        path: 'degre',
        data: { pageTitle: 'Degres' },
        loadChildren: () => import('./degre/degre.routes'),
      },
      {
        path: 'sanction',
        data: { pageTitle: 'Sanctions' },
        loadChildren: () => import('./sanction/sanction.routes'),
      },
      {
        path: 'demande-dexplication',
        data: { pageTitle: 'DemandeDexplications' },
        loadChildren: () => import('./demande-dexplication/demande-dexplication.routes'),
      },
      {
        path: 'poste',
        data: { pageTitle: 'Postes' },
        loadChildren: () => import('./poste/poste.routes'),
      },
      {
        path: 'miseadisposition',
        data: { pageTitle: 'Miseadispositions' },
        loadChildren: () => import('./miseadisposition/miseadisposition.routes'),
      },
      {
        path: 'motif-disponibilite',
        data: { pageTitle: 'MotifDisponibilites' },
        loadChildren: () => import('./motif-disponibilite/motif-disponibilite.routes'),
      },
      {
        path: 'disponibilite',
        data: { pageTitle: 'Disponibilites' },
        loadChildren: () => import('./disponibilite/disponibilite.routes'),
      },
      {
        path: 'sanctionner',
        data: { pageTitle: 'Sanctionners' },
        loadChildren: () => import('./sanctionner/sanctionner.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
