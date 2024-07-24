import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SanctionComponent } from './list/sanction.component';
import { SanctionDetailComponent } from './detail/sanction-detail.component';
import { SanctionUpdateComponent } from './update/sanction-update.component';
import SanctionResolve from './route/sanction-routing-resolve.service';

const sanctionRoute: Routes = [
  {
    path: '',
    component: SanctionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SanctionDetailComponent,
    resolve: {
      sanction: SanctionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SanctionUpdateComponent,
    resolve: {
      sanction: SanctionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SanctionUpdateComponent,
    resolve: {
      sanction: SanctionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default sanctionRoute;
