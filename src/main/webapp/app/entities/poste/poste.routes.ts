import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { PosteComponent } from './list/poste.component';
import { PosteDetailComponent } from './detail/poste-detail.component';
import { PosteUpdateComponent } from './update/poste-update.component';
import PosteResolve from './route/poste-routing-resolve.service';

const posteRoute: Routes = [
  {
    path: '',
    component: PosteComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PosteDetailComponent,
    resolve: {
      poste: PosteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PosteUpdateComponent,
    resolve: {
      poste: PosteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PosteUpdateComponent,
    resolve: {
      poste: PosteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default posteRoute;
