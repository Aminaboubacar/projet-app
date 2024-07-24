import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DegreComponent } from './list/degre.component';
import { DegreDetailComponent } from './detail/degre-detail.component';
import { DegreUpdateComponent } from './update/degre-update.component';
import DegreResolve from './route/degre-routing-resolve.service';

const degreRoute: Routes = [
  {
    path: '',
    component: DegreComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DegreDetailComponent,
    resolve: {
      degre: DegreResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DegreUpdateComponent,
    resolve: {
      degre: DegreResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DegreUpdateComponent,
    resolve: {
      degre: DegreResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default degreRoute;
