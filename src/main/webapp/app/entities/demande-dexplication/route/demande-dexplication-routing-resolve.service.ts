import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDemandeDexplication } from '../demande-dexplication.model';
import { DemandeDexplicationService } from '../service/demande-dexplication.service';

export const demandeDexplicationResolve = (route: ActivatedRouteSnapshot): Observable<null | IDemandeDexplication> => {
  const id = route.params['id'];
  if (id) {
    return inject(DemandeDexplicationService)
      .find(id)
      .pipe(
        mergeMap((demandeDexplication: HttpResponse<IDemandeDexplication>) => {
          if (demandeDexplication.body) {
            return of(demandeDexplication.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default demandeDexplicationResolve;
