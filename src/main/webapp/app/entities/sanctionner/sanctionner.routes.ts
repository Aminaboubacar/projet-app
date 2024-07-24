import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SanctionnerComponent } from './list/sanctionner.component';
import { SanctionnerDetailComponent } from './detail/sanctionner-detail.component';
import { SanctionnerUpdateComponent } from './update/sanctionner-update.component';
import SanctionnerResolve from './route/sanctionner-routing-resolve.service';

const sanctionnerRoute: Routes = [
  {
    path: '',
    component: SanctionnerComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SanctionnerDetailComponent,
    resolve: {
      sanctionner: SanctionnerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SanctionnerUpdateComponent,
    resolve: {
      sanctionner: SanctionnerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SanctionnerUpdateComponent,
    resolve: {
      sanctionner: SanctionnerResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default sanctionnerRoute;
