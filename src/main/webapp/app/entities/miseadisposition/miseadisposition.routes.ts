import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { MiseadispositionComponent } from './list/miseadisposition.component';
import { MiseadispositionDetailComponent } from './detail/miseadisposition-detail.component';
import { MiseadispositionUpdateComponent } from './update/miseadisposition-update.component';
import MiseadispositionResolve from './route/miseadisposition-routing-resolve.service';

const miseadispositionRoute: Routes = [
  {
    path: '',
    component: MiseadispositionComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MiseadispositionDetailComponent,
    resolve: {
      miseadisposition: MiseadispositionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MiseadispositionUpdateComponent,
    resolve: {
      miseadisposition: MiseadispositionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MiseadispositionUpdateComponent,
    resolve: {
      miseadisposition: MiseadispositionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default miseadispositionRoute;
