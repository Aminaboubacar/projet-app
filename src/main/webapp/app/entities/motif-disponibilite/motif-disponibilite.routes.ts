import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MotifDisponibiliteComponent } from './list/motif-disponibilite.component';
import { MotifDisponibiliteDetailComponent } from './detail/motif-disponibilite-detail.component';
import { MotifDisponibiliteUpdateComponent } from './update/motif-disponibilite-update.component';
import MotifDisponibiliteResolve from './route/motif-disponibilite-routing-resolve.service';

const motifDisponibiliteRoute: Routes = [
  {
    path: '',
    component: MotifDisponibiliteComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MotifDisponibiliteDetailComponent,
    resolve: {
      motifDisponibilite: MotifDisponibiliteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MotifDisponibiliteUpdateComponent,
    resolve: {
      motifDisponibilite: MotifDisponibiliteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MotifDisponibiliteUpdateComponent,
    resolve: {
      motifDisponibilite: MotifDisponibiliteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default motifDisponibiliteRoute;
