import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DisponibiliteComponent } from './list/disponibilite.component';
import { DisponibiliteDetailComponent } from './detail/disponibilite-detail.component';
import { DisponibiliteUpdateComponent } from './update/disponibilite-update.component';
import DisponibiliteResolve from './route/disponibilite-routing-resolve.service';

const disponibiliteRoute: Routes = [
  {
    path: '',
    component: DisponibiliteComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DisponibiliteDetailComponent,
    resolve: {
      disponibilite: DisponibiliteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DisponibiliteUpdateComponent,
    resolve: {
      disponibilite: DisponibiliteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DisponibiliteUpdateComponent,
    resolve: {
      disponibilite: DisponibiliteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default disponibiliteRoute;
