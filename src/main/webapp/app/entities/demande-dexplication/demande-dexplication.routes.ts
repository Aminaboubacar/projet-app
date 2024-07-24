import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { DemandeDexplicationComponent } from './list/demande-dexplication.component';
import { DemandeDexplicationDetailComponent } from './detail/demande-dexplication-detail.component';
import { DemandeDexplicationUpdateComponent } from './update/demande-dexplication-update.component';
import DemandeDexplicationResolve from './route/demande-dexplication-routing-resolve.service';

const demandeDexplicationRoute: Routes = [
  {
    path: '',
    component: DemandeDexplicationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DemandeDexplicationDetailComponent,
    resolve: {
      demandeDexplication: DemandeDexplicationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DemandeDexplicationUpdateComponent,
    resolve: {
      demandeDexplication: DemandeDexplicationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DemandeDexplicationUpdateComponent,
    resolve: {
      demandeDexplication: DemandeDexplicationResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default demandeDexplicationRoute;
